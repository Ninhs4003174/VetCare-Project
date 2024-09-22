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
            // Get a connection to the database
            Connection connection = source.getConnection();
             // Prepare the SQL query to retrieve all appointments
            PreparedStatement stm = connection.prepareStatement("SELECT * FROM appointments;");
            List<Appointment> appointments = new ArrayList<>();
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                // Create an Appointment object and add it to the list
                Appointment appt = new Appointment(rs.getLong(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), null);
                appointments.add(appt);
            }
            connection.close();
            return appointments;
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving appointments", e);
        }
    }
 // finding appointments by vet's name
    @Override
    public List<Appointment> findByVetName(String vetName) {
        try {
            Connection connection = source.getConnection();
            PreparedStatement stm = connection.prepareStatement("SELECT * FROM appointments WHERE vet_name = ?;");
            stm.setString(1, vetName);
            ResultSet rs = stm.executeQuery();
            List<Appointment> appointments = new ArrayList<>();
            while (rs.next()) {
                Appointment appt = new Appointment(rs.getLong(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), null);
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
                Appointment appt = new Appointment(rs.getLong(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), user);
                appointments.add(appt);
            }
            connection.close();
            return appointments;
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving appointments for user", e);
        }
    }

    //  save method 
    @Override
    public void save(Appointment appointment) {
        try {
            if (appointment.getUser() == null || appointment.getUser().getId() == null) {
                throw new IllegalArgumentException("Appointment must have a valid user.");
            }

            Connection connection = source.getConnection();
            if (appointment.getId() == null) {
                PreparedStatement insertStm = connection.prepareStatement(
                    "INSERT INTO appointments (pet_name, vet_name, date, time, status, user_id) VALUES (?, ?, ?, ?, ?, ?)"
                );
                insertStm.setString(1, appointment.getPetName());
                insertStm.setString(2, appointment.getVetName());
                insertStm.setString(3, appointment.getDate());
                insertStm.setString(4, appointment.getTime());
                insertStm.setString(5, appointment.getStatus());
                insertStm.setLong(6, appointment.getUser().getId());
                insertStm.executeUpdate();
            } else {
                PreparedStatement updateStm = connection.prepareStatement(
                    "UPDATE appointments SET pet_name = ?, vet_name = ?, date = ?, time = ?, status = ?, user_id = ? WHERE id = ?"
                );
                updateStm.setString(1, appointment.getPetName());
                updateStm.setString(2, appointment.getVetName());
                updateStm.setString(3, appointment.getDate());
                updateStm.setString(4, appointment.getTime());
                updateStm.setString(5, appointment.getStatus());
                updateStm.setLong(6, appointment.getUser().getId());
                updateStm.setLong(7, appointment.getId());
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
   // Method to find an appointment by its ID(for editing)
    @Override
    public Appointment findById(Long id) {
        try {
            Connection connection = source.getConnection();
            PreparedStatement stm = connection.prepareStatement("SELECT * FROM appointments WHERE id = ?");
            stm.setLong(1, id);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                Appointment appointment = new Appointment(rs.getLong(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), null);
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
