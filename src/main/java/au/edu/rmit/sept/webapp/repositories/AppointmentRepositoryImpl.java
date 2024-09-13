package au.edu.rmit.sept.webapp.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.stereotype.Repository;

import au.edu.rmit.sept.webapp.models.Appointment;

@Repository
public class AppointmentRepositoryImpl implements AppointmentRepository {

    private final DataSource source;

    public AppointmentRepositoryImpl(DataSource source) {
        this.source = source;
    }

    @Override
    public List<Appointment> findAll() {
        try {
            Connection connection = source.getConnection();
            PreparedStatement stm = connection.prepareStatement("SELECT * FROM appointments;");
            List<Appointment> appointments = new ArrayList<>();
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                Appointment appt = new Appointment(rs.getLong(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6));
                appointments.add(appt);
            }
            connection.close();
            return appointments;
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving appointments", e);
        }
    }

    @Override
    public List<Appointment> findByVetName(String vetName) {
        try {
            Connection connection = source.getConnection();
            PreparedStatement stm = connection.prepareStatement("SELECT * FROM appointments WHERE vet_name = ?;");
            stm.setString(1, vetName);
            ResultSet rs = stm.executeQuery();
            List<Appointment> appointments = new ArrayList<>();
            while (rs.next()) {
                Appointment appt = new Appointment(rs.getLong(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6));
                appointments.add(appt);
            }
            connection.close();
            return appointments;
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving vet appointments", e);
        }
    }
    public void save(Appointment appointment) {
                try {
                    Connection connection = this.source.getConnection();
                    PreparedStatement stm = connection.prepareStatement(
                        "INSERT INTO appointments (pet_name, vet_name, date, time, status) VALUES (?, ?, ?, ?, ?)"
                    );
                    stm.setString(1, appointment.getPetName());
                    stm.setString(2, appointment.getVetName());
                    stm.setString(3, appointment.getDate());
                    stm.setString(4, appointment.getTime());
                    stm.setString(5, appointment.getStatus());
                    stm.executeUpdate();
                    connection.close();
                } catch (SQLException e) {
                    throw new RuntimeException("Error saving appointment", e);
                }
            }
        

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
        }
        


