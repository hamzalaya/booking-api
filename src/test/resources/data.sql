-- ==========================================
-- Users
-- ==========================================
INSERT INTO users (id, email, username, first_name, last_name, password, confirmed, enabled, locked, expired, deleted,
                   created_at, updated_at)
VALUES (1, 'john.doe@example.com', 'john.doe@example.com', 'Alice', 'Smith',
        '$2a$10$8p4KKaL8jOqsX31anl/KMexX/s2G.KUgRWHpmKLZ3TcJAeCDv30kq', true, true, false, false,
        false, NOW(), NOW()),
       (2, 'bob@example.com', 'bob@example.com', 'Bob', 'Johnson',
        '$2a$10$8p4KKaL8jOqsX31anl/KMexX/s2G.KUgRWHpmKLZ3TcJAeCDv30kq', true, true, false, false,
        false, NOW(), NOW()),
       (3, 'charlie@example.com', 'charlie@example.com', 'Charlie', 'Brown',
        '$2a$10$8p4KKaL8jOqsX31anl/KMexX/s2G.KUgRWHpmKLZ3TcJAeCDv30kq', true, true, false,
        false, false, NOW(), NOW());

-- ==========================================
-- Properties
-- ==========================================
INSERT INTO properties (id, name, description, address, city, country, created_at, updated_at, owner_id)
VALUES (1, 'Sunny Apartment', 'A bright apartment in the city center', '123 Main St', 'Paris', 'France', NOW(), NOW(),
        1),
       (2, 'Cozy Cottage', 'A quiet cottage near the lake', '45 Lakeview Rd', 'Annecy', 'France', NOW(), NOW(), 2),
       (3, 'Beach House', 'A spacious house with sea view', '77 Ocean Blvd', 'Nice', 'France', NOW(), NOW(), 3);

-- ==========================================
-- Example Bookings (Optional)
-- ==========================================
INSERT INTO bookings (id, property_id, guest_id, start_date, end_date, status, deleted, created_at, updated_at)
VALUES (1, 2, 1, '2025-11-10', '2025-11-15', 'ACTIVE', false, NOW(), NOW()),
       (2, 2, 2, '2025-11-20', '2025-11-22', 'ACTIVE', false, NOW(), NOW());

-- ==========================================
-- Example Blocks (Optional)
-- ==========================================
INSERT INTO blocks (id, property_id, start_date, end_date, status, created_at, updated_at)
VALUES (1, 2, '2025-12-01', '2025-12-05', 'ACTIVE', NOW(), NOW()),
       (2, 3, '2025-11-25', '2025-11-28', 'ACTIVE', NOW(), NOW());
