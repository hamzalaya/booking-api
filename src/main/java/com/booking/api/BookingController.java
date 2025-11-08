package com.booking.api;

import com.booking.dto.BookingDto;
import com.booking.services.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.booking.holders.ApiPaths.BOOKINGS;

@RestController
@RequestMapping(BOOKINGS)
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    // Create a new booking
    @PostMapping
    public ResponseEntity<BookingDto> createBooking(@RequestBody BookingDto bookingDto) {
        BookingDto createdBooking = bookingService.createBooking(bookingDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBooking);
    }

    // Get booking by ID
    @GetMapping("/{id}")
    public ResponseEntity<BookingDto> getBooking(@PathVariable Long id) {
        BookingDto booking = bookingService.findById(id);
        return ResponseEntity.ok(booking);
    }

    // Update booking
    @PutMapping("/{id}")
    public ResponseEntity<BookingDto> updateBooking(@PathVariable Long id, @RequestBody BookingDto bookingDto) {
        BookingDto updatedBooking = bookingService.updateBooking(id, bookingDto);
        return ResponseEntity.ok(updatedBooking);
    }

    // Cancel booking
    @PatchMapping("/{id}/cancel")
    public ResponseEntity<BookingDto> cancelBooking(@PathVariable Long id) {
        BookingDto canceledBooking = bookingService.cancelBooking(id);
        return ResponseEntity.ok(canceledBooking);
    }

    // Rebook a canceled booking
    @PatchMapping("/{id}/rebook")
    public ResponseEntity<BookingDto> rebookBooking(@PathVariable Long id) {
        BookingDto rebookedBooking = bookingService.rebookCanceledBooking(id);
        return ResponseEntity.ok(rebookedBooking);
    }

    // Delete booking
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBooking(@PathVariable Long id) {
        bookingService.deleteBooking(id);
        return ResponseEntity.noContent().build();
    }
}

