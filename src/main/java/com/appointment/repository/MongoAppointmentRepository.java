package com.appointment.repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.appointment.model.Appointment;
import com.appointment.model.AppointmentStatus;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class MongoAppointmentRepository implements AppointmentRepository {

    private final MongoCollection<Document> collection;

    public MongoAppointmentRepository(String uri) {
        MongoClient client = MongoClients.create(uri);
        MongoDatabase db = client.getDatabase("appointment_test");
        this.collection = db.getCollection("appointments");
    }

    @Override
    public Appointment save(Appointment appointment) {
        Document doc = new Document()
            .append("clientName", appointment.getCustomerName())
            .append("dateTime", appointment.getAppointmentDate().toString())
            .append("service", appointment.getServiceType())
            .append("status", appointment.getStatus().toString());

        collection.insertOne(doc);
        String id = doc.getObjectId("_id").toString();
        return new Appointment(id,
                               appointment.getCustomerName(),
                               appointment.getAppointmentDate(),
                               appointment.getServiceType(),
                               appointment.getStatus());
    }

    @Override
    public Appointment findById(String id) {
        Document doc = collection.find(new Document("_id", new ObjectId(id))).first();
        if (doc == null) return null;
        return new Appointment(
            doc.getObjectId("_id").toString(),
            doc.getString("clientName"),
            LocalDateTime.parse(doc.getString("dateTime")),
            doc.getString("service"),
            AppointmentStatus.valueOf(doc.getString("status"))
        );
    }

    @Override
    public List<Appointment> findAll() {
        List<Appointment> list = new ArrayList<>();
        for (Document doc : collection.find()) {
            list.add(new Appointment(
                doc.getObjectId("_id").toString(),
                doc.getString("clientName"),
                LocalDateTime.parse(doc.getString("dateTime")),
                doc.getString("service"),
                AppointmentStatus.valueOf(doc.getString("status"))
            ));
        }
        return list;
    }

    @Override
    public void update(Appointment appointment) {
        Document doc = new Document()
            .append("clientName", appointment.getCustomerName())
            .append("dateTime", appointment.getAppointmentDate().toString())
            .append("service", appointment.getServiceType())
            .append("status", appointment.getStatus().toString());
        collection.replaceOne(new Document("_id", new ObjectId(appointment.getId())), doc);
    }

    @Override
    public void deleteById(String id) {
        collection.deleteOne(new Document("_id", new ObjectId(id)));
    }
}