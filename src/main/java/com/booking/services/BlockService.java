package com.booking.services;

import com.booking.dto.BlockDto;
import com.booking.entities.Block;
import com.booking.entities.Property;
import com.booking.enums.BookingStatus;
import com.booking.exceptions.BookingException;
import com.booking.mapper.BlockMapper;
import com.booking.repositories.BlockRepository;
import com.booking.repositories.PropertyAvailabilityRepository;
import com.booking.security.permissions.PermissionsService;
import com.booking.validation.DateValidation;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

import static com.booking.exceptions.error.ExceptionCode.API_BAD_REQUEST;
import static com.booking.exceptions.error.ExceptionCode.API_NOT_FOUND;


@Service
@RequiredArgsConstructor
public class BlockService {

    private final BlockRepository blockRepository;
    private final PropertyService propertyService;
    private final PropertyAvailabilityRepository propertyAvailabilityRepository;

    @Transactional
    public BlockDto createBlock(BlockDto blockDto) {
        LocalDate startDate = blockDto.getStartDate();
        LocalDate endDate = blockDto.getEndDate();
        Long propertyId = blockDto.getPropertyId();
        DateValidation.validateDates(startDate, endDate, "block.date.invalide");
        Property property = propertyService.findAndLockPropertyById(propertyId);
        PermissionsService.canManageProperty(property);
        if (hasOverlaps(propertyId, startDate, endDate)) {
            throw new BookingException(API_BAD_REQUEST, "block.not.possible");
        }
        Block block = new Block();
        block.setProperty(property);
        block.setStartDate(startDate);
        block.setEndDate(endDate);
        block.setStatus(BookingStatus.ACTIVE);
        //if we have a heavy process
        return BlockMapper.toDto(blockRepository.save(block));
    }

    public BlockDto findById(Long id) {
        return BlockMapper.toDto(findEntityById(id));
    }

    @Transactional
    public BlockDto updateBlock(Long id, BlockDto blockDto) {
        Block block = findEntityById(id);
        PermissionsService.canManageBlock(block);
        LocalDate newStart = blockDto.getStartDate();
        LocalDate newEnd = blockDto.getEndDate();
        DateValidation.validateDates(newStart, newEnd, "block.date.invalide");

        Property property = propertyService.findAndLockPropertyById(block.getProperty().getId());

        // Ignore its own range when checking overlap
        boolean hasConflict = propertyAvailabilityRepository.hasBlockOrBookingExcludingBlockId(
                property.getId(),
                newStart,
                newEnd,
                block.getId()
        );
        if (hasConflict) {
            throw new BookingException(API_BAD_REQUEST, "block.not.possible");
        }

        block.setStartDate(newStart);
        block.setEndDate(newEnd);
        block.setStatus(BookingStatus.ACTIVE);

        return BlockMapper.toDto(blockRepository.save(block));
    }

    @Transactional
    public BlockDto cancelBlock(Long id) {
        Block block = findEntityById(id);
        PermissionsService.canManageBlock(block);
        if (block.getStatus() == BookingStatus.CANCELED) {
            throw new BookingException(API_BAD_REQUEST, "block.already.canceled");
        }
        if (block.getEndDate().isBefore(LocalDate.now())) {
            throw new BookingException(API_BAD_REQUEST, "block.already.passed");
        }

        block.setStatus(BookingStatus.CANCELED);
        return BlockMapper.toDto(blockRepository.save(block));
    }

    @Transactional
    public void deleteBlock(Long id) {
        Block block = findEntityById(id);
        PermissionsService.canManageBlock(block);
        blockRepository.delete(block);
    }

    private boolean hasOverlaps(Long propertyId, LocalDate startDate, LocalDate endDate) {
        return propertyAvailabilityRepository.hasBookingOrBlock(propertyId, startDate, endDate);
    }

    private Block findEntityById(Long id) {
        return blockRepository.findById(id)
                .orElseThrow(BookingException.of(API_NOT_FOUND, "block.not.found"));
    }

}