## Appointment Booking Manager

[![Java CI with Maven](https://github.com/hrishikesh3001/appointment-booking-manager/actions/workflows/maven.yml/badge.svg)](https://github.com/hrishikesh3001/appointment-booking-manager/actions/workflows/maven.yml)
[![Coverage Status](https://coveralls.io/repos/github/hrishikesh3001/appointment-booking-manager/badge.svg?branch=main)](https://coveralls.io/github/hrishikesh3001/appointment-booking-manager?branch=main)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=hrishikesh3001_appointment-booking-manager&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=hrishikesh3001_appointment-booking-manager)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=hrishikesh3001_appointment-booking-manager&metric=coverage)](https://sonarcloud.io/summary/new_code?id=hrishikesh3001_appointment-booking-manager)

Desktop application for managing appointments, developed following **Test-Driven Development (TDD)** principles with a strong focus on code quality, test coverage, and continuous integration.

---

## Technologies

- Java 17  
- Maven  
- Swing (desktop UI)  
- JUnit 5 (unit testing)  
- Testcontainers + MongoDB (integration testing)  
- Mockito  
- JaCoCo (code coverage)  
- GitHub Actions (CI)  
- SonarCloud (static analysis & quality gates)  
- Coveralls (coverage reporting)

> **Note**  
> The application uses an **in-memory repository** for normal execution and unit testing.  
> `MongoAppointmentRepository` represents infrastructure code and is tested **only via integration tests** using Testcontainers.

---

## Build & Run

Build and run unit tests:

```bash
mvn clean test
```
Run the application:

```bash
mvn -DskipTests package
mvn exec:java
```
Or run com.appointment.App directly from the IDE.


## Tests
### Unit Tests:

```bash
mvn clean test
```

### Integration Tests (MongoDB via Testcontainers):
Requires Docker Desktop running.

```bash
mvn clean verify
```
This executes:

All unit tests

Integration tests for MongoAppointmentRepository

## Code Coverage

### JaCoCo

Generate coverage report:

```bash
mvn clean test
```
Report location:

```bash
target/site/jacoco/index.html
```

## Mutation Testing (PIT)
Mutation testing is used to evaluate the effectiveness of the test suite, not just
statement execution.

The project uses PIT (Pitest) to ensure tests detect faulty logic introduced by mutations.

Run mutation tests:

```bash
mvn org.pitest:pitest-maven:mutationCoverage
```
Report location:

```bash
target/pit-reports/index.html
```

### Status
Mutation coverage: 100% (core classes)

Test strength: 100%

## Continuous Integration & Quality
The project uses GitHub Actions to automatically:

Build the project

Run unit and integration tests

Generate JaCoCo reports

Upload coverage to Coveralls

Analyze code quality with SonarCloud

## Quality Gates Enforced
No blocker issues

High maintainability rating

Reliable and consistent coverage reporting

## Project Status
CI pipeline: ✅ Passing

Coveralls coverage: ✅ 100% (core logic)

SonarCloud quality gate: ✅ Passed

Static analysis issues: ✅ Resolved