package com.booking.repositories;

import com.booking.dto.PropertyDto;
import com.booking.entities.Property;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface PropertyRepository extends JpaRepository<Property, Long> {

    @Query("""
                SELECT new com.booking.dto.PropertyDto(p.id, p.name, p.city, p.country)
                FROM Property p
                WHERE p.id NOT IN (
                    SELECT b.property.id FROM Booking b
                    WHERE b.deleted =false 
                    AND b.status <> 'CANCELED'
                      AND b.startDate < :endDate
                      AND b.endDate > :startDate
                )
                AND p.id NOT IN (
                    SELECT bl.property.id FROM Block bl
                    WHERE bl.startDate < :endDate
                      AND bl.endDate > :startDate
                )
            """)
    Page<PropertyDto> findAvailableProperties(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            Pageable pageable
    );
}

