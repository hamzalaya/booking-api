package com.booking.data;

import com.booking.entities.Block;
import com.booking.entities.Booking;
import com.booking.entities.Property;
import com.booking.entities.User;
import com.booking.enums.BookingStatus;
import com.booking.repositories.BlockRepository;
import com.booking.repositories.BookingRepository;
import com.booking.repositories.PropertyRepository;
import com.booking.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Configuration
public class LoadTestData {

    private static final int USER_COUNT = 2000;
    private static final int PROPERTY_COUNT = 1000;
    private static final int BOOKING_COUNT = 1_000_000;
    private static final int BATCH_SIZE = 1000;

    @Bean
    CommandLineRunner generateTestData(
            UserRepository userRepository,
            PropertyRepository propertyRepository,
            BlockRepository blockRepository,
            BookingRepository bookingRepository) {

        return args -> {
            long start = System.currentTimeMillis();
            Random random = new Random();

            // --- 1️⃣ Create users if empty
            if (userRepository.count() == 0) {
                System.out.println("Generating " + USER_COUNT + " users...");
                User user = new User();
                user.setFirstName("John");
                user.setLastName("Doe ");
                user.setPassword("$2a$10$8p4KKaL8jOqsX31anl/KMexX/s2G.KUgRWHpmKLZ3TcJAeCDv30kq");
                user.setEmail("john.doe@example.com");
                user.setUsername("john.doe@example.com");
                user.setEnabled(true);
                user.setConfirmed(true);

                List<User> users = new ArrayList<>();
                users.add(user);
                for (int i = 1; i <= USER_COUNT; i++) {
                    User u = new User();
                    u.setFirstName("First name  " + i);
                    u.setLastName("Last name " + i);
                    u.setEmail("user" + i + "@example.com");
                    u.setUsername("user" + i + "@example.com");
                    u.setEnabled(true);
                    u.setConfirmed(true);
                    users.add(u);

                    if (i % BATCH_SIZE == 0) {
                        userRepository.saveAll(users);
                        userRepository.flush();
                        users.clear();
                    }
                }
                if (!users.isEmpty()) userRepository.saveAll(users);
                System.out.println("Users created ✅");
            }
            var users = userRepository.findAll();

            // --- 2️⃣ Create properties if empty
            if (propertyRepository.count() == 0) {
                System.out.println("Generating " + PROPERTY_COUNT + " properties...");
                List<Property> properties = new ArrayList<>();
                for (int i = 1; i <= PROPERTY_COUNT; i++) {
                    Property p = new Property();
                    p.setName("Property " + i);
                    p.setDescription("Test property number " + i);
                    p.setAddress("Address " + i);
                    p.setCity("City " + (i % 100));
                    p.setCountry("Morocco");
                    p.setOwner(users.get(random.nextInt(users.size())));
                    properties.add(p);

                    if (i % BATCH_SIZE == 0) {
                        propertyRepository.saveAll(properties);
                        propertyRepository.flush();
                        properties.clear();
                    }
                }
                if (!properties.isEmpty()) propertyRepository.saveAll(properties);
                System.out.println("Properties created ✅");
            }
            var properties = propertyRepository.findAll();

            // 4️⃣ Create 5000 blocks
            if (blockRepository.count() == 0) {
                System.out.println("Creating blocks...");
                for (int i = 0; i < 5000; i++) {
                    Block block = new Block();
                    block.setProperty(properties.get(random.nextInt(properties.size())));
                    block.setStartDate(LocalDate.now().plusDays(random.nextInt(365)));
                    block.setEndDate(block.getStartDate().plusDays(random.nextInt(5) + 1));
                    block.setReason("Maintenance period " + i);
                    blockRepository.save(block);

                    if (i % 500 == 0) {
                        blockRepository.flush();
                    }
                }
                blockRepository.flush();
            }

            // --- 3️⃣ Generate bookings
            System.out.println("Generating " + BOOKING_COUNT + " bookings...");
            List<Booking> bookings = new ArrayList<>(BATCH_SIZE);

            if (bookingRepository.count() == 0) {
                for (int i = 1; i <= BOOKING_COUNT; i++) {
                    Booking booking = new Booking();
                    booking.setGuest(users.get(random.nextInt(users.size())));
                    booking.setProperty(properties.get(random.nextInt(properties.size())));
                    booking.setStartDate(LocalDate.now().plusDays(random.nextInt(365)));
                    booking.setEndDate(booking.getStartDate().plusDays(random.nextInt(10) + 1));
                    booking.setStatus(BookingStatus.ACTIVE);
                    bookings.add(booking);

                    if (i % BATCH_SIZE == 0) {
                        bookingRepository.saveAll(bookings);
                        bookingRepository.flush();
                        bookings.clear();
                        System.out.printf("Inserted %d/%d bookings...\n", i, BOOKING_COUNT);
                    }
                }
                if (!bookings.isEmpty()) bookingRepository.saveAll(bookings);

                System.out.println("✅ 1M bookings created in " +
                                   (System.currentTimeMillis() - start) / 1000 + "s");
            }
        };
    }
}


