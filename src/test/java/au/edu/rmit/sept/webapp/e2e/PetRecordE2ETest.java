// package au.edu.rmit.sept.webapp.e2e;

// import org.junit.jupiter.api.AfterEach;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.openqa.selenium.By;
// import org.openqa.selenium.WebDriver;
// import org.openqa.selenium.WebElement;
// import org.openqa.selenium.chrome.ChromeDriver;
// import org.openqa.selenium.chrome.ChromeOptions;

// import java.util.concurrent.TimeUnit;

// import static org.junit.jupiter.api.Assertions.assertTrue;

// public class PetRecordE2ETest {

// private WebDriver driver;

// @BeforeEach
// public void setUp() {
// // Set ChromeDriver path dynamically or hardcode it if necessary
// System.setProperty("webdriver.chrome.driver", "path/to/chromedriver");

// // Configure ChromeOptions for headless mode (if necessary)
// ChromeOptions options = new ChromeOptions();
// options.addArguments("--headless"); // Run in headless mode if you want the
// browser not to open
// options.addArguments("--disable-gpu");

// // Initialize ChromeDriver
// driver = new ChromeDriver(options);

// // Set implicit wait
// driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
// }

// @Test
// public void testViewPetRecords() {
// // Step 1: Go to the login page
// driver.get("http://localhost:8080/login-client");

// // Step 2: Log in as 'vetuser'
// WebElement usernameInput = driver.findElement(By.id("username"));
// WebElement passwordInput = driver.findElement(By.id("password"));
// WebElement loginButton = driver.findElement(By.tagName("button"));

// // Input credentials
// usernameInput.sendKeys("vetuser");
// passwordInput.sendKeys("vet123");
// loginButton.click();

// // Step 3: Navigate to pet records after login
// driver.get("http://localhost:8080/records");

// // Step 4: Assert that the records page is loaded
// assertTrue(driver.getTitle().contains("Pet Records"));

// // Check if the record table exists (an element in the records page)
// WebElement recordsTable = driver.findElement(By.tagName("table"));
// assertTrue(recordsTable.isDisplayed());
// }

// @AfterEach
// public void tearDown() {
// if (driver != null) {
// driver.quit();
// }
// }
// }
