package com.booking.services;

import com.booking.aspect.Timed;
import com.booking.dto.BookingDto;
import com.booking.entities.Booking;
import com.booking.entities.Property;
import com.booking.entities.User;
import com.booking.enums.BookingStatus;
import com.booking.exceptions.BookingException;
import com.booking.mapper.BookingMapper;
import com.booking.repositories.BookingRepository;
import com.booking.repositories.PropertyAvailabilityRepository;
import com.booking.repositories.UserRepository;
import com.booking.security.permissions.PermissionsService;
import com.booking.utils.UserContext;
import com.booking.validation.DateValidation;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

import static com.booking.exceptions.error.ExceptionCode.*;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final PropertyService propertyService;
    private final UserRepository userRepository;
    private final PropertyAvailabilityRepository propertyAvailabilityRepository;


    @Transactional
    @Timed
    public BookingDto createBooking(BookingDto bookingDto) {
        LocalDate startDate = bookingDto.getStartDate();
        LocalDate endDate = bookingDto.getEndDate();
        Long propertyId = bookingDto.getPropertyId();

        DateValidation.validateDates(startDate, endDate, "booking.date.invalide");
        Property property = propertyService.findAndLockPropertyById(propertyId);
        if (hasOverlaps(propertyId, startDate, endDate)) {
            throw new BookingException(API_BAD_REQUEST, "property.not.available");
        }

        User user = userRepository.findByUsername(UserContext.userName())
                .orElseThrow(() -> new BookingException(API_INTERNAL_SERVER_ERROR, "internal.server.error"));

        Booking booking = new Booking();
        booking.setProperty(property);
        booking.setGuest(user);
        booking.setStartDate(startDate);
        booking.setEndDate(endDate);
        booking.setTotalPrice(bookingDto.getTotalPrice());
        booking.setStatus(BookingStatus.ACTIVE);

        return BookingMapper.toDto(bookingRepository.save(booking));
    }

    public BookingDto findById(Long id) {
        return BookingMapper.toDto(findEntityById(id));
    }

    @Transactional
    public BookingDto updateBooking(Long id, BookingDto bookingDto) {
        Booking booking = findEntityById(id);
        PermissionsService.canManageBooking(booking);
        if (booking.getStatus() == BookingStatus.CANCELED) {
            throw new BookingException(API_BAD_REQUEST, "booking.already.canceled");
        }

        LocalDate newStart = bookingDto.getStartDate();
        LocalDate newEnd = bookingDto.getEndDate();
        DateValidation.validateDates(newStart, newEnd, "booking.date.invalide");
        Property property = propertyService.findAndLockPropertyById(booking.getProperty().getId());
        // Ignore its own range when checking overlap
        boolean hasConflict = propertyAvailabilityRepository.hasBookingOrBlockExcluding(
                booking.getProperty().getId(),
                newStart,
                newEnd,
                booking.getId()
        );
        if (hasConflict) {
            throw new BookingException(API_BAD_REQUEST, "property.not.available");
        }

        booking.setStartDate(newStart);
        booking.setEndDate(newEnd);
        booking.setStatus(BookingStatus.ACTIVE);
        booking.setTotalPrice(bookingDto.getTotalPrice());

        return BookingMapper.toDto(bookingRepository.save(booking));
    }

    @Transactional
    public BookingDto cancelBooking(Long id) {
        Booking booking = findEntityById(id);
        PermissionsService.canManageBooking(booking);
        if (booking.getStatus() == BookingStatus.CANCELED) {
            throw new BookingException(API_BAD_REQUEST, "booking.already.canceled");
        }
        if (booking.getEndDate().isBefore(LocalDate.now())) {
            throw new BookingException(API_BAD_REQUEST, "booking.already.passed");
        }
        booking.setStatus(BookingStatus.CANCELED);
        return BookingMapper.toDto(bookingRepository.save(booking));
    }

    @Transactional
    public BookingDto rebookCanceledBooking(Long id) {
        Booking booking = findEntityById(id);
        PermissionsService.canManageBooking(booking);
        if (booking.getStatus() != BookingStatus.CANCELED) {
            throw new BookingException(API_BAD_REQUEST, "booking.cannot.be.rebooked");
        }

        if (booking.getStartDate().isBefore(LocalDate.now())) {
            throw new BookingException(API_BAD_REQUEST, "booking.already.passed");
        }
        LocalDate start = booking.getStartDate();
        LocalDate end = booking.getEndDate();
        Long propertyId = booking.getProperty().getId();
        Property property = propertyService.findAndLockPropertyById(propertyId);
        if (hasOverlaps(propertyId, start, end)) {
            throw new BookingException(API_BAD_REQUEST, "property.not.available");
        }

        booking.setStatus(BookingStatus.ACTIVE);
        return BookingMapper.toDto(bookingRepository.save(booking));
    }


    @Transactional
    public void deleteBooking(Long id) {
        Booking booking = findEntityById(id);
        PermissionsService.canManageBooking(booking);
        bookingRepository.delete(booking);
    }

    private boolean hasOverlaps(Long propertyId, LocalDate startDate, LocalDate endDate) {
        return propertyAvailabilityRepository.hasBookingOrBlock(propertyId, startDate, endDate);
    }


    private Booking findEntityById(Long id) {
        return bookingRepository.findById(id).orElseThrow(BookingException.of(API_NOT_FOUND, "booking.not.found"));
    }


}
