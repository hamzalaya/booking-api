package com.booking.api;

import com.booking.dto.BookingDto;
import com.booking.dto.JwtAccessToken;
import com.booking.dto.LoginDto;
import com.booking.holders.ApiPaths;
import com.booking.services.BookingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.concurrent.CompletableFuture;

import static com.booking.holders.ApiPaths.BOOKINGS;
import static com.booking.holders.ApiPaths.PROPERTIES;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
class BookingControllerTest {


    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    BookingService bookingService;
    private String jwtToken;
    static Long createdBookingId;

    static BookingDto createSampleDto() {
        return BookingDto.builder()
                .propertyId(1L)
                .guestId(200L)
                .startDate(LocalDate.of(2025, 11, 10))
                .endDate(LocalDate.of(2025, 11, 15))
                .totalPrice(BigDecimal.valueOf(500.0))
                .build();
    }

    @BeforeAll
    void authenticate() throws Exception {
        LoginDto login = new LoginDto();
        login.setUsername("john.doe@example.com");  // ← Use valid test user
        login.setPassword("P@ssw0rd123");

        String response = mockMvc.perform(post(ApiPaths.V1 + ApiPaths.AUTH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(login)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JwtAccessToken token = objectMapper.readValue(response, JwtAccessToken.class);
        this.jwtToken = token.getAccessToken();

        assertThat(jwtToken).isNotNull();

        //Create booking
        BookingDto dto = createSampleDto();

        response = mockMvc.perform(post(BOOKINGS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.propertyId").value(1))
                .andReturn()
                .getResponse()
                .getContentAsString();

        BookingDto created = objectMapper.readValue(response, BookingDto.class);
        assertThat(created.getId()).isNotNull();
        createdBookingId = created.getId();
    }

    @Test
    @Order(1)
    void shouldCreateBooking_propertyNotAvailable() throws Exception {
        BookingDto dto = createSampleDto();

        mockMvc.perform(post(BOOKINGS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("property.not.available"));

    }

    @Test
    @Order(1)
    void shouldCreateBooking() throws Exception {
        BookingDto dto = createSampleDto();
        dto.setStartDate(LocalDate.of(2025, 12, 25));
        dto.setEndDate(LocalDate.of(2025, 12, 26));

        mockMvc.perform(post(BOOKINGS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.propertyId").value(1));

    }

    @Test
    @Order(2)
    void shouldGetBookingById() throws Exception {
        mockMvc.perform(get(BOOKINGS + "/{id}", createdBookingId)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(createdBookingId))
                .andExpect(jsonPath("$.propertyId").value(1));
    }

    @Test
    @Order(3)
    void shouldUpdateBooking() throws Exception {
        BookingDto updateDto = createSampleDto();
        updateDto.setTotalPrice(BigDecimal.valueOf(750.0));
        updateDto.setStartDate(LocalDate.of(2025, 12, 10));
        updateDto.setEndDate(LocalDate.of(2025, 12, 15));

        mockMvc.perform(put(BOOKINGS + "/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalPrice").value(750.0));
    }

    @Test
    @Order(4)
    void shouldCancelBooking() throws Exception {
        mockMvc.perform(patch(BOOKINGS + "/{id}/cancel", createdBookingId)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CANCELED"));
    }

    @Test
    @Order(5)
    void shouldRebookCanceledBooking() throws Exception {
        mockMvc.perform(patch(BOOKINGS + "/{id}/rebook", createdBookingId)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("ACTIVE"));
    }

    @Test
    @Order(6)
    void shouldDeleteBooking() throws Exception {
        mockMvc.perform(delete(BOOKINGS + "/{id}", 1)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken))
                .andExpect(status().isNoContent());

        mockMvc.perform(get(BOOKINGS + "/{id}", 1)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                )
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnNotFoundForInvalidId() throws Exception {
        mockMvc.perform(get(BOOKINGS + "/{id}", 9999L)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                )
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnBadRequestForInvalidDates() throws Exception {
        BookingDto invalid = createSampleDto();
        invalid.setStartDate(LocalDate.of(2025, 11, 20));
        invalid.setEndDate(LocalDate.of(2025, 11, 10)); // end < start

        mockMvc.perform(post(BOOKINGS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(1)
    void shouldCreateBooking_propertyOnHold() throws Exception {

        CompletableFuture<Boolean> lockCheckFuture = CompletableFuture.supplyAsync(() -> {
            try {
                mockMvc.perform(get(PROPERTIES + "/{id}/lock", 3L)
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken))
                        .andExpect(status().isOk())
                        .andReturn();

                return true;
            } catch (Exception e) {
                return false;
            }
        });
        Thread.sleep(1000);

        BookingDto dto = createSampleDto();
        dto.setPropertyId(3L);
        dto.setStartDate(LocalDate.of(2025, 12, 27));
        dto.setEndDate(LocalDate.of(2025, 12, 28));

        mockMvc.perform(post(BOOKINGS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("property.on.hold"));
    }
}