package au.edu.rmit.sept.webapp.repository;

import au.edu.rmit.sept.webapp.model.Appointment;
import au.edu.rmit.sept.webapp.model.User;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class AppointmentRepositoryImpl implements AppointmentRepository {

    private final DataSource source;

    public AppointmentRepositoryImpl(DataSource source) {
        this.source = source;
    }

    @Override
    public List<Appointment> findAll() {
        try (Connection connection = source.getConnection()) {
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
                        findUserById(rs.getLong("user_id"))); // Set the User object
                appointments.add(appt);
            }
            return appointments;
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving appointments", e);
        }
    }

    @Override
    public List<Appointment> findByVetId(Long vetId) {
        try (Connection connection = source.getConnection()) {
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
                        findUserById(rs.getLong("user_id"))); // Set the User object
                appointments.add(appt);
            }
            return appointments;
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving vet appointments", e);
        }
    }

    @Override
    public List<Appointment> findByUser(User user) {
        try (Connection connection = source.getConnection()) {
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
                        user); // Set the User object
                appointments.add(appt);
            }
            return appointments;
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving appointments for user", e);
        }
    }

    @Override
    public void save(Appointment appointment) {
        try (Connection connection = source.getConnection()) {
            if (appointment.getUser() == null || appointment.getUser().getId() == null) {
                throw new IllegalArgumentException("Appointment must have a valid user.");
            }

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
        } catch (SQLException e) {
            throw new RuntimeException("Error saving appointment", e);
        }
    }

    @Override
    public void deleteById(Long id) {
        try (Connection connection = source.getConnection()) {
            PreparedStatement stm = connection.prepareStatement("DELETE FROM appointments WHERE id = ?;");
            stm.setLong(1, id);
            stm.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error canceling appointment", e);
        }
    }

    @Override
    public Optional<Appointment> findById(Long id) {
        try (Connection connection = source.getConnection()) {
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
                        findUserById(rs.getLong("user_id"))); // Set the User object
                return Optional.of(appointment);
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding appointment", e);
        }
    }

    private User findUserById(Long userId) {
        try (Connection connection = source.getConnection()) {
            PreparedStatement stm = connection.prepareStatement("SELECT * FROM vet_users WHERE id = ?");
            stm.setLong(1, userId);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                return new User(
                        rs.getLong("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("email"),
                        rs.getString("phone_number"),
                        rs.getString("address"),
                        rs.getString("role"),
                        rs.getLong("clinic_id"));
            } else {
                throw new RuntimeException("User not found with ID: " + userId);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding user", e);
        }
    }
}