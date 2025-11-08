package com.booking.services;

import com.booking.dto.PropertyDto;
import com.booking.entities.Property;
import com.booking.exceptions.BookingException;
import com.booking.exceptions.error.ExceptionCode;
import com.booking.repositories.PropertyRepository;
import com.booking.validation.DateValidation;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PessimisticLockException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class PropertyService {

    private final EntityManager entityManager;
    private final PropertyRepository propertyRepository;

    /*
    Get available properties
     */
    public Page<PropertyDto> getAvailableProperties(LocalDate startDate, LocalDate endDate, int page, int size) {
        DateValidation.validateDates(startDate, endDate, "booking.date.invalid");
        Pageable pageable = PageRequest.of(page, size);
        return propertyRepository.findAvailableProperties(startDate, endDate, pageable);
    }


    /*
    Find a property by id and lock it
     */
    public Property findAndLockPropertyById(Long propertyId) {
        try {
            Property property = entityManager.find(
                    Property.class,
                    propertyId,
                    LockModeType.PESSIMISTIC_WRITE
            );
            if (property == null) {
                throw new BookingException(ExceptionCode.API_NOT_FOUND, "property.not.found");
            }
            return property;
        } catch (PessimisticLockException ex) {
            throw new BookingException(ExceptionCode.API_NOT_FOUND, "property.on.hold");
        }
    }
}

