package au.edu.rmit.sept.webapp.integration;

import au.edu.rmit.sept.webapp.WebappApplication;
import au.edu.rmit.sept.webapp.model.PetRecord;
import au.edu.rmit.sept.webapp.service.PetRecordService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = WebappApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PetRecordControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PetRecordService petRecordService;

    @Test
    void testGetAllRecords() {
        String url = "http://localhost:" + port + "/records";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testCreateAndFetchPetRecord() {
        // Create new record
        PetRecord newRecord = new PetRecord();
        newRecord.setName("Max");
        petRecordService.save(newRecord);

        // Fetch the record
        PetRecord fetchedRecord = petRecordService.getPetRecordById(newRecord.getId());
        assertNotNull(fetchedRecord);
        assertEquals("Max", fetchedRecord.getName());
    }
}
