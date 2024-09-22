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
        PreparedStatement stm = connection.prepareStatement("SELECT * FROM vetbooking;");
        ResultSet rs = stm.executeQuery();
        List<VetBooking> vets = new ArrayList<>();
        while (rs.next()) {
            VetBooking vet = new VetBooking(rs.getLong(1), rs.getString(2), rs.getString(3), rs.getString(4));
            vets.add(vet);
        }
        connection.close();
        return vets;
    } catch (SQLException e) {
        throw new RuntimeException("Error retrieving vetbooking", e);
    }
}

    

}
