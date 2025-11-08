package com.booking.dto;

import com.booking.enums.BookingStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingDto {
    private Long id;
    @NotNull
    private Long propertyId;
    private Long guestId;
    private BigDecimal totalPrice;
    @NotNull
    private LocalDate startDate;
    @NotNull
    private LocalDate endDate;

    private BookingStatus status;
}
