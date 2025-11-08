package com.booking.dto;

import com.booking.enums.BookingStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class BlockDto {
    private Long id;
    @NotNull
    private Long propertyId;
    private Long guestId;
    @NotNull
    private LocalDate startDate;
    @NotNull
    private LocalDate endDate;
    private BookingStatus status;
}
