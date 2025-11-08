package com.booking.repositories;


import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public class PropertyAvailabilityRepository {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Checks if any booking or block overlaps with the given date range.
     */
    public boolean hasBookingOrBlock(Long propertyId, LocalDate startDate, LocalDate endDate) {
        String sql = """
                SELECT EXISTS (
                    SELECT 1 FROM bookings
                    WHERE property_id = :propertyId
                      AND status <> 'CANCELED'
                      AND start_date < :endDate
                      AND end_date > :startDate
                      AND deleted = false
                    UNION ALL
                    SELECT 1 FROM blocks
                    WHERE property_id = :propertyId
                      AND start_date < :endDate
                      AND end_date > :startDate
                ) AS has_conflict
                """;

        Boolean result = (Boolean) entityManager
                .createNativeQuery(sql)
                .setParameter("propertyId", propertyId)
                .setParameter("startDate", startDate)
                .setParameter("endDate", endDate)
                .getSingleResult();

        return Boolean.TRUE.equals(result);
    }

    /**
     * Same as above, but excludes a specific booking ID (e.g., when updating).
     */
    public boolean hasBookingOrBlockExcluding(Long propertyId, LocalDate startDate, LocalDate endDate, Long excludeBookingId) {
        String sql = """
                SELECT EXISTS (
                    SELECT 1 FROM bookings
                    WHERE property_id = :propertyId
                      AND id <> :excludeId
                      AND status <> 'CANCELED'
                      AND start_date < :endDate
                      AND end_date > :startDate
                      AND deleted = false
                    UNION ALL
                    SELECT 1 FROM blocks
                    WHERE property_id = :propertyId
                      AND start_date < :endDate
                      AND end_date > :startDate
                ) AS has_conflict
                """;

        Boolean result = (Boolean) entityManager
                .createNativeQuery(sql)
                .setParameter("propertyId", propertyId)
                .setParameter("excludeId", excludeBookingId)
                .setParameter("startDate", startDate)
                .setParameter("endDate", endDate)
                .getSingleResult();

        return Boolean.TRUE.equals(result);
    }

    /**
     * Same as above, but excludes a specific block ID.
     */
    public boolean hasBlockOrBookingExcludingBlockId(Long propertyId, LocalDate startDate, LocalDate endDate, Long excludeBlockId) {
        String sql = """
                SELECT EXISTS (
                    SELECT 1 FROM bookings
                    WHERE property_id = :propertyId
                      AND status <> 'CANCELED'
                      AND start_date < :endDate
                      AND end_date > :startDate
                      AND deleted = false
                    UNION ALL
                    SELECT 1 FROM blocks
                    WHERE property_id = :propertyId
                      AND id <> :excludeId
                      AND start_date < :endDate
                      AND end_date > :startDate
                ) AS has_conflict
                """;

        Boolean result = (Boolean) entityManager
                .createNativeQuery(sql)
                .setParameter("propertyId", propertyId)
                .setParameter("excludeId", excludeBlockId)
                .setParameter("startDate", startDate)
                .setParameter("endDate", endDate)
                .getSingleResult();

        return Boolean.TRUE.equals(result);
    }
}
