# Appointment Booking Manager

[![Java CI with Maven](https://github.com/hrishikesh3001/appointment-booking-manager/actions/workflows/maven.yml/badge.svg)](https://github.com/hrishikesh3001/appointment-booking-manager/actions/workflows/maven.yml)
[![Coverage Status](https://coveralls.io/repos/github/hrishikesh3001/appointment-booking-manager/badge.svg?branch=main)](https://coveralls.io/github/hrishikesh3001/appointment-booking-manager?branch=main)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=hrishikesh3001_appointment-booking-manager&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=hrishikesh3001_appointment-booking-manager)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=hrishikesh3001_appointment-booking-manager&metric=coverage)](https://sonarcloud.io/summary/new_code?id=hrishikesh3001_appointment-booking-manager)

Desktop application for managing appointments built with a TDD approach.

## Technologies
- Java 17
- Maven
- Swing GUI
- JUnit 5 (unit tests)
- Testcontainers + MongoDB(integration tests)
- AssertJ-Swing (E2E GUI tests)
- Mockito
- GitHub Actions
- SonarCloud
- Coveralls

> Note: MySQL is in the tech list for completeness — the current project uses an in-memory repository for local runs and Testcontainers + Mongo for integration tests in CI/local integration runs.

---

## Build & run

Build (compile, run unit tests):
```bash
mvn clean test
```

Run the application
```bash
mvn -DskipTests package
mvn exec:java
```
(Or run com.appoitnment.App directly from IDE)

## Tests

Unit tests
```bash
mvn clean test
```

Integration tests (requires Docker Desktop)
```bash
mvn -DskipTests=true -Dit.test=MongoAppointmentRepositoryIT verify
```

Full pipeline (unit + integration)
```bash
mvn clean verify
```

End-to-End (GUI) Tests

GUI tests use AssertJ-Swing and open real windows.
They are not run automatically during mvn verify.

Run manually (recommended)

Open BookingFlowE2E in IDE, Right-click → Run As → JUnit Test

Or run via Maven (local only)
```bash
mvn -DskipTests=true -Dit.test=NONE \
  org.apache.maven.plugins:maven-failsafe-plugin:3.2.3:integration-test \
  -DfailIfNoTests=false \
  -Dtest=BookingFlowE2E
```
