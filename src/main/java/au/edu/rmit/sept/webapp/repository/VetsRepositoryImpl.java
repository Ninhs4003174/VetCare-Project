package au.edu.rmit.sept.webapp.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.stereotype.Repository;

import au.edu.rmit.sept.webapp.model.Vets;

@Repository
public class VetsRepositoryImpl implements VetsRepository {

    private final DataSource source;

    public VetsRepositoryImpl(DataSource source) {
        this.source = source;
    }

    @Override
    public List<Vets> findAll() {
        try {
            Connection connection = source.getConnection();
            PreparedStatement stm = connection.prepareStatement("SELECT * FROM vets;");
            ResultSet rs = stm.executeQuery();
            List<Vets> vets = new ArrayList<>();
            while (rs.next()) {
                Vets vet = new Vets(rs.getLong(1), rs.getString(2), rs.getString(3));
                vets.add(vet);
            }
            connection.close();
            return vets;
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving vets", e);
        }
    }
}
