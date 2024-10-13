package au.edu.rmit.sept.webapp.repository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import au.edu.rmit.sept.webapp.model.VetBooking;

public class VetBookingRepositoryImplTest {

    @Mock
    private DataSource dataSource;

    @Mock
    private Connection connection;

    @Mock
    private PreparedStatement preparedStatement;

    @Mock
    private ResultSet resultSet;

    @InjectMocks
    private VetBookingRepositoryImpl vetBookingRepository;

    @BeforeEach
    public void setUp() throws SQLException {
        MockitoAnnotations.openMocks(this);
        when(dataSource.getConnection()).thenReturn(connection);
    }

    @Test
    public void testFindAll() throws SQLException {
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true, false);
        when(resultSet.getLong("vet_user_id")).thenReturn(1L);
        when(resultSet.getLong("clinic_id")).thenReturn(1L);
        when(resultSet.getString("vet_name")).thenReturn("Dr. Vet");

        List<VetBooking> result = vetBookingRepository.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getVetUserId());
        assertEquals(1L, result.get(0).getClinicId());
        assertEquals("Dr. Vet", result.get(0).getVetName());

        verify(connection, times(1)).close();
    }

    @Test
    public void testSave() throws SQLException {
        VetBooking vetBooking = new VetBooking();
        vetBooking.setVetUserId(1L);
        vetBooking.setClinicId(1L);
        vetBooking.setServiceType("General Checkup");
        vetBooking.setClinicAddress("123 Vet St");
        vetBooking.setPhoneNumber("1234567890");
        vetBooking.setEmail("vet@example.com");

        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);

        vetBookingRepository.save(vetBooking);

        verify(preparedStatement, times(1)).setLong(1, vetBooking.getVetUserId());
        verify(preparedStatement, times(1)).setLong(2, vetBooking.getClinicId());
        verify(preparedStatement, times(1)).setString(3, vetBooking.getServiceType());
        verify(preparedStatement, times(1)).setString(4, vetBooking.getClinicAddress());
        verify(preparedStatement, times(1)).setString(5, vetBooking.getPhoneNumber());
        verify(preparedStatement, times(1)).setString(6, vetBooking.getEmail());
        verify(preparedStatement, times(1)).executeUpdate();
        verify(connection, times(1)).close();
    }
}