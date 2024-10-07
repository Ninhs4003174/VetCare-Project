package au.edu.rmit.sept.webapp.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.stereotype.Repository;

import au.edu.rmit.sept.webapp.model.VetBooking;

@Repository
public class VetBookingRepositoryImpl implements VetBookingRepository {
    private final DataSource source;

    public VetBookingRepositoryImpl(DataSource source) {
        this.source = source;
    }

    @Override
    public List<VetBooking> findAll() {
        try {
            Connection connection = source.getConnection();
            PreparedStatement stm = connection.prepareStatement(
                    "SELECT vu.id AS vet_user_id, vu.clinic_id, vu.username AS vet_name " +
                            "FROM vet_users vu " +
                            "WHERE vu.role = 'VET';");
            ResultSet rs = stm.executeQuery();
            List<VetBooking> vets = new ArrayList<>();
            while (rs.next()) {
                VetBooking vet = new VetBooking();
                vet.setVetUserId(rs.getLong("vet_user_id"));
                vet.setClinicId(rs.getLong("clinic_id"));
                vet.setVetName(rs.getString("vet_name"));
                vets.add(vet);
            }
            connection.close();
            return vets;
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving vet users", e);
        }
    }

    @Override
    public void save(VetBooking vetBooking) {
        try {
            Connection connection = source.getConnection();
            PreparedStatement stm = connection.prepareStatement(
                    "INSERT INTO vetbooking (vet_user_id, clinic_id, service_type, address, phone_number, email) " +
                            "VALUES (?, ?, ?, ?, ?, ?)");
            stm.setLong(1, vetBooking.getVetUserId());
            stm.setLong(2, vetBooking.getClinicId());
            stm.setString(3, vetBooking.getServiceType());
            stm.setString(4, vetBooking.getClinicAddress());
            stm.setString(5, vetBooking.getPhoneNumber());
            stm.setString(6, vetBooking.getEmail());
            stm.executeUpdate();
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException("Error saving vetbooking", e);
        }
    }
}