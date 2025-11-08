package com.booking.api;


import com.booking.dto.PropertyDto;
import com.booking.entities.Property;
import com.booking.services.PropertyService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

import static com.booking.holders.ApiPaths.PROPERTIES;

@RestController
@RequestMapping(PROPERTIES)
@RequiredArgsConstructor
public class PropertyController {

    private final PropertyService propertyService;


    @GetMapping("/available")
    public ResponseEntity<Page<PropertyDto>> getAvailableProperties(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {

        Page<PropertyDto> available = propertyService.getAvailableProperties(startDate, endDate, page, size);
        return ResponseEntity.ok(available);
    }

    @GetMapping("/{id}/lock")
    @Transactional
    /*
    Just created this to simulate lock on property
     */
    public ResponseEntity<String> lockProperty(@PathVariable Long id) {
        Property property = propertyService.findAndLockPropertyById(id);
        // Simulate a long-running transaction (holding the lock)
        try {
            Thread.sleep(20000); // 10 seconds
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return ResponseEntity.ok("Locked property: " + property.getName());
    }
}

