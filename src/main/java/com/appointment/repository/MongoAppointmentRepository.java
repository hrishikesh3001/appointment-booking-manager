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

public class MongoAppointmentRepository implements AppointmentRepository, AutoCloseable {

    private static final String FIELD_ID = "_id";
    private static final String FIELD_CLIENT_NAME = "clientName";
    private static final String FIELD_DATE_TIME = "dateTime";
    private static final String FIELD_SERVICE = "service";
    private static final String FIELD_STATUS = "status";
    
    private final MongoClient client;
    private final MongoCollection<Document> collection;

    public MongoAppointmentRepository(String uri) {
        this.client = MongoClients.create(uri);
        MongoDatabase db = client.getDatabase("appointment_test");
        this.collection = db.getCollection("appointments");
    }

    @Override
    public Appointment save(Appointment appointment) {
        Document doc = new Document()
            .append(FIELD_CLIENT_NAME, appointment.getCustomerName())
            .append(FIELD_DATE_TIME, appointment.getAppointmentDate().toString())
            .append(FIELD_SERVICE, appointment.getServiceType())
            .append(FIELD_STATUS, appointment.getStatus().toString());

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
            doc.getObjectId(FIELD_ID).toString(),
            doc.getString(FIELD_CLIENT_NAME),
            LocalDateTime.parse(doc.getString(FIELD_DATE_TIME)),
            doc.getString(FIELD_SERVICE),
            AppointmentStatus.valueOf(doc.getString(FIELD_STATUS))
        );
    }

    @Override
    public List<Appointment> findAll() {
        List<Appointment> list = new ArrayList<>();
        for (Document doc : collection.find()) {
            list.add(new Appointment(
                doc.getObjectId(FIELD_ID).toString(),
                doc.getString(FIELD_CLIENT_NAME),
                LocalDateTime.parse(doc.getString(FIELD_DATE_TIME)),
                doc.getString(FIELD_SERVICE),
                AppointmentStatus.valueOf(doc.getString(FIELD_STATUS))
            ));
        }
        return list;
    }

    @Override
    public void update(Appointment appointment) {
        Document doc = new Document()
            .append(FIELD_CLIENT_NAME, appointment.getCustomerName())
            .append(FIELD_DATE_TIME, appointment.getAppointmentDate().toString())
            .append(FIELD_SERVICE, appointment.getServiceType())
            .append(FIELD_STATUS, appointment.getStatus().toString());
        collection.replaceOne(new Document(FIELD_ID, new ObjectId(appointment.getId())), doc);
    }

    @Override
    public void deleteById(String id) {
        collection.deleteOne(new Document(FIELD_ID, new ObjectId(id)));
    }
    
    @Override
    public void close() {
    	if (client != null) {
    	client.close();
    	}
    }
}