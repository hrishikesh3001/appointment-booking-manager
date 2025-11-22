package com.appointment.repository;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

import org.bson.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.appointment.model.Appointment;
import com.appointment.model.AppointmentStatus;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

@Testcontainers
class MongoAppointmentRepositoryIT {

    @Container
    static final MongoDBContainer mongoContainer = new MongoDBContainer("mongo:6.0");

    private MongoAppointmentRepository repository;
    private String uri;

    @BeforeEach
    void setUp() {
        uri = mongoContainer.getConnectionString();
        repository = new MongoAppointmentRepository(uri);

        // Clean collection so each test starts from a known state
        try (MongoClient client = MongoClients.create(uri)) {
            MongoDatabase db = client.getDatabase("appointment_test");
            MongoCollection<Document> collection = db.getCollection("appointments");
            collection.deleteMany(new Document());
        }
    }

    @Test
    void testSaveAndFind() {
        LocalDateTime date = LocalDateTime.of(2025, 11, 15, 10, 0);
        Appointment appt = new Appointment(null, "Alice", date, "Haircut", AppointmentStatus.SCHEDULED);

        Appointment saved = repository.save(appt);

        assertThat(saved.getId()).isNotNull();
        assertThat(repository.findById(saved.getId())).isNotNull();
    }

    @Test
    void testFindAllReturnsAllAppointments() {
        LocalDateTime date1 = LocalDateTime.of(2025, 11, 15, 10, 0);
        LocalDateTime date2 = LocalDateTime.of(2025, 11, 16, 14, 0);

        repository.save(new Appointment(null, "Alice", date1, "Haircut", AppointmentStatus.SCHEDULED));
        repository.save(new Appointment(null, "Bob", date2, "Massage", AppointmentStatus.SCHEDULED));

        List<Appointment> all = repository.findAll();

        assertThat(all).hasSize(2);
        assertThat(all)
                .extracting(Appointment::getCustomerName)
                .containsExactlyInAnyOrder("Alice", "Bob");
    }

    @Test
    void testUpdateChangesStoredDocument() {
        LocalDateTime date = LocalDateTime.of(2025, 11, 15, 10, 0);
        Appointment original = new Appointment(null, "Alice", date, "Haircut", AppointmentStatus.SCHEDULED);

        Appointment saved = repository.save(original);

        Appointment updated = new Appointment(
                saved.getId(),
                "Alice",
                date,
                "Color",
                AppointmentStatus.COMPLETED
        );

        repository.update(updated);

        Appointment fromDb = repository.findById(saved.getId());
        assertThat(fromDb).isNotNull();
        assertThat(fromDb.getServiceType()).isEqualTo("Color");
        assertThat(fromDb.getStatus()).isEqualTo(AppointmentStatus.COMPLETED);
    }

    @Test
    void testDeleteByIdRemovesDocument() {
        LocalDateTime date = LocalDateTime.of(2025, 11, 15, 10, 0);
        Appointment saved = repository.save(
                new Appointment(null, "Alice", date, "Haircut", AppointmentStatus.SCHEDULED)
        );

        repository.deleteById(saved.getId());

        assertThat(repository.findById(saved.getId())).isNull();
        assertThat(repository.findAll()).isEmpty();
    }
}
