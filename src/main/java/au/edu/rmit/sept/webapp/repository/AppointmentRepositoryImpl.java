package au.edu.rmit.sept.webapp.repository;

import au.edu.rmit.sept.webapp.model.Appointment;
import au.edu.rmit.sept.webapp.model.User;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class AppointmentRepositoryImpl implements AppointmentRepository {

    private final DataSource source;

    public AppointmentRepositoryImpl(DataSource source) {
        this.source = source;
    }

    // Method to retrieve all appointments from the database
    @Override
    public List<Appointment> findAll() {
        try {
            Connection connection = source.getConnection();
            PreparedStatement stm = connection.prepareStatement("SELECT * FROM appointments;");
            List<Appointment> appointments = new ArrayList<>();
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                Appointment appt = new Appointment(
                        rs.getLong("id"),
                        rs.getLong("pet_id"),
                        rs.getString("pet_name"),
                        rs.getLong("vet_id"),
                        rs.getString("date"),
                        rs.getString("time"),
                        rs.getString("status"),
                        null);
                appointments.add(appt);
            }
            connection.close();
            return appointments;
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving appointments", e);
        }
    }

    // Method to retrieve appointments by vet ID
    @Override
    public List<Appointment> findByVetId(Long vetId) {
        try {
            Connection connection = source.getConnection();
            PreparedStatement stm = connection.prepareStatement("SELECT * FROM appointments WHERE vet_id = ?;");
            stm.setLong(1, vetId);
            ResultSet rs = stm.executeQuery();
            List<Appointment> appointments = new ArrayList<>();
            while (rs.next()) {
                Appointment appt = new Appointment(
                        rs.getLong("id"),
                        rs.getLong("pet_id"),
                        rs.getString("pet_name"),
                        rs.getLong("vet_id"),
                        rs.getString("date"),
                        rs.getString("time"),
                        rs.getString("status"),
                        null);
                appointments.add(appt);
            }
            connection.close();
            return appointments;
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving vet appointments", e);
        }
    }

    // Method to retrieve appointments by user
    @Override
    public List<Appointment> findByUser(User user) {
        try {
            Connection connection = source.getConnection();
            PreparedStatement stm = connection.prepareStatement("SELECT * FROM appointments WHERE user_id = ?;");
            stm.setLong(1, user.getId());
            ResultSet rs = stm.executeQuery();
            List<Appointment> appointments = new ArrayList<>();
            while (rs.next()) {
                Appointment appt = new Appointment(
                        rs.getLong("id"),
                        rs.getLong("pet_id"),
                        rs.getString("pet_name"),
                        rs.getLong("vet_id"),
                        rs.getString("date"),
                        rs.getString("time"),
                        rs.getString("status"),
                        user);
                appointments.add(appt);
            }
            connection.close();
            return appointments;
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving appointments for user", e);
        }
    }

    // Method to save an appointment
    @Override
    public void save(Appointment appointment) {
        try {
            if (appointment.getUser() == null || appointment.getUser().getId() == null) {
                throw new IllegalArgumentException("Appointment must have a valid user.");
            }

            Connection connection = source.getConnection();
            if (appointment.getId() == null) {
                PreparedStatement insertStm = connection.prepareStatement(
                        "INSERT INTO appointments (pet_id, pet_name, vet_id, date, time, status, user_id) VALUES (?, ?, ?, ?, ?, ?, ?)");
                insertStm.setLong(1, appointment.getPetId());
                insertStm.setString(2, appointment.getPetName());
                insertStm.setLong(3, appointment.getVetId());
                insertStm.setString(4, appointment.getDate());
                insertStm.setString(5, appointment.getTime());
                insertStm.setString(6, appointment.getStatus());
                insertStm.setLong(7, appointment.getUser().getId());
                insertStm.executeUpdate();
            } else {
                PreparedStatement updateStm = connection.prepareStatement(
                        "UPDATE appointments SET pet_id = ?, pet_name = ?, vet_id = ?, date = ?, time = ?, status = ?, user_id = ? WHERE id = ?");
                updateStm.setLong(1, appointment.getPetId());
                updateStm.setString(2, appointment.getPetName());
                updateStm.setLong(3, appointment.getVetId());
                updateStm.setString(4, appointment.getDate());
                updateStm.setString(5, appointment.getTime());
                updateStm.setString(6, appointment.getStatus());
                updateStm.setLong(7, appointment.getUser().getId());
                updateStm.setLong(8, appointment.getId());
                updateStm.executeUpdate();
            }
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException("Error saving appointment", e);
        }
    }

    // Method to delete an appointment by its ID
    @Override
    public void deleteById(Long id) {
        try {
            Connection connection = source.getConnection();
            PreparedStatement stm = connection.prepareStatement("DELETE FROM appointments WHERE id = ?;");
            stm.setLong(1, id);
            stm.executeUpdate();
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException("Error canceling appointment", e);
        }
    }

    // Method to find an appointment by its ID (for editing)
    @Override
    public Appointment findById(Long id) {
        try {
            Connection connection = source.getConnection();
            PreparedStatement stm = connection.prepareStatement("SELECT * FROM appointments WHERE id = ?");
            stm.setLong(1, id);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                Appointment appointment = new Appointment(
                        rs.getLong("id"),
                        rs.getLong("pet_id"),
                        rs.getString("pet_name"),
                        rs.getLong("vet_id"),
                        rs.getString("date"),
                        rs.getString("time"),
                        rs.getString("status"),
                        null);
                connection.close();
                return appointment;
            } else {
                connection.close();
                throw new IllegalArgumentException("Invalid appointment ID: " + id);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding appointment", e);
        }
    }
}
