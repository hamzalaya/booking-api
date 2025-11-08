package com.booking.entities;


import com.booking.enums.BookingStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.math.BigDecimal;
import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "bookings", indexes = {@Index(name = "idx_booking_overlap", columnList = "property_id, deleted, status, start_date, end_date")
})
@Data
@SQLDelete(sql = "UPDATE bookings SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
public class Booking extends AbstractEntity {

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Property property;

    @ManyToOne(optional = false)
    private User guest;

    private LocalDate startDate;
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    private BookingStatus status;

    private BigDecimal totalPrice;

    private boolean deleted = false;

}

