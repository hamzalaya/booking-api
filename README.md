## Booking API – Documentation

Booking API

### Overview

The Booking API is REST service designed for managing bookings and blocks on properties
with:

- Overlap detection (bookings & blocks)
- Soft delete support
- Concurrency control via property-level locking
- Good performance: ~1ms response time even with 1+ million records

### Key Features

*bookings* :

- Create booking
- Find booking
- Update booking
- Cancel booking
- Rebook canceled booking
- Delete booking

*bookings* :

- Create block
- Find block
- Update block
- Cancel block
- Delete block

### Validations

To ensure data integrity and logical consistency, the api implements
some validations:

- Start and end dates must be coherent (startDate < endDate)
- Prevent booking past period
- Respect max allowed date (1 years that can be changed in property file)
- Overlapping bookings and blocks are prevented
- Canceling or rebooking respects property availability

### Security

- The api is secured using spring security and jwt
- Manage who can access or modify resource with permissions

### Concurrency

The api manage concurrency mode by locking the property using database lock that is a good choice,
we can use also distributed lock system based on a cache system such as Redis

### Performance

The api use optimised queries, to check overlapping bookings/blocks, I did a test in h2 base with over
1 Millions bookings and 5K blocks, and the execution time of the overlap query is 1ms

### Tests

The api is tested using only integration tests for booking that handle the most used use cases.
Still need unit tests and functional tests using bdd approach based on cucumber

### Architecture & Future Improvements

To keep booking and block optimised, I am thinking of creating a booking archive table that contains
expired bookings, that will serve for history of bookings, and keep my booking table just for live booking
so that checking availability and overlaps will be optimised (the same for blocks) 

