package com.booking.mapper;


import com.booking.dto.BookingDto;
import com.booking.entities.Booking;
import com.booking.entities.Property;
import com.booking.entities.User;

public class BookingMapper {

    private BookingMapper() {
    }

    public static BookingDto toDto(Booking booking) {
        if (booking == null) return null;
        BookingDto dto = new BookingDto();
        dto.setId(booking.getId());
        dto.setPropertyId(
                booking.getProperty() != null ? booking.getProperty().getId() : null
        );
        dto.setGuestId(
                booking.getGuest() != null ? booking.getGuest().getId() : null
        );
        dto.setStartDate(booking.getStartDate());
        dto.setEndDate(booking.getEndDate());
        dto.setTotalPrice(booking.getTotalPrice());
        dto.setStatus(booking.getStatus());
        return dto;
    }


    public static Booking toEntity(BookingDto dto, Property property, User guest) {
        if (dto == null) return null;
        Booking booking = new Booking();
        booking.setId(dto.getId());
        booking.setProperty(property);
        booking.setGuest(guest);
        booking.setStartDate(dto.getStartDate());
        booking.setEndDate(dto.getEndDate());
        return booking;
    }
}

