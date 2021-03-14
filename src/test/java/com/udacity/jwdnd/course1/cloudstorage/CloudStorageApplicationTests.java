package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialsMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credentials;
import com.udacity.jwdnd.course1.cloudstorage.service.EncryptionService;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CloudStorageApplicationTests {
    @Autowired
    private EncryptionService encryptionService;

    @Autowired
    private CredentialsMapper credentialsMapper;

    @LocalServerPort
    private int port;

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeAll
    static void beforeAll() {
        WebDriverManager.chromedriver().setup();
    }

    private void waitAndFindElementById(String elementId, WebDriver driver, WebDriverWait wait) {
        wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.id(elementId))));
    }

    private void executeJsScriptById(String elementId, String script, WebDriver driver) {
        ((JavascriptExecutor) driver).executeScript(script, driver.findElement(By.id(elementId)));
    }

    @BeforeEach
    public void beforeEach() {
        this.driver = new ChromeDriver();
        this.wait = new WebDriverWait(driver, 10);
    }

    @AfterEach
    public void afterEach() {
        if (this.driver != null) {
            driver.quit();
        }
    }

    @Test
    public void getLoginPage() {
        driver.get("http://localhost:" + this.port + "/login");
        Assertions.assertEquals("Login", driver.getTitle());
    }

    //Test home, login and signup authorization without logging in

    //Write a test that verifies that an unauthorized user can only access the login and signup pages.
    @Test
    public void testUnauthorizedUser() {
        driver.get("http://localhost:" + this.port + "/home");
        Assertions.assertNotEquals("Home", driver.getTitle());
        driver.get("http://localhost:" + this.port + "/login");
        Assertions.assertEquals("Login", driver.getTitle());
        driver.get("http://localhost:" + this.port + "/signup");
        Assertions.assertEquals("Sign Up", driver.getTitle());
    }

    //Write a test that signs up a new user, logs in, verifies that the home page is accessible, logs out, and verifies that the home page is no longer accessible.
    @Test
    public void testSignupAuthorizationFlow() {
        String firstName = "John";
        String lastName = "Doe";
        String username = "johndoe";
        String password = "hellojohn123";

        //access sign page
        driver.get("http://localhost:" + this.port + "/signup");
        wait.until(ExpectedConditions.titleContains("Sign Up"));

        waitAndFindElementById("inputFirstName", driver, wait);
        executeJsScriptById("inputFirstName", "arguments[0].click();", driver);
        executeJsScriptById("inputFirstName", "arguments[0].value='" + firstName + "';", driver);

        waitAndFindElementById("inputLastName", driver, wait);
        executeJsScriptById("inputLastName", "arguments[0].click();", driver);
        executeJsScriptById("inputLastName", "arguments[0].value='" + lastName + "';", driver);

        waitAndFindElementById("inputUsername", driver, wait);
        executeJsScriptById("inputUsername", "arguments[0].click();", driver);
        executeJsScriptById("inputUsername", "arguments[0].value='" + username + "';", driver);

        waitAndFindElementById("inputPassword", driver, wait);
        executeJsScriptById("inputPassword", "arguments[0].click();", driver);
        executeJsScriptById("inputPassword", "arguments[0].value='" + password + "';", driver);

        waitAndFindElementById("submitSignUp", driver, wait);
        executeJsScriptById("submitSignUp", "arguments[0].click();", driver);
        Assertions.assertEquals("You successfully signed up! Please continue to the login page.",
                By.id("signUpSuccess").findElement(driver).getText());

        //go to login page and login with the user that have created
        waitAndFindElementById("loginHref", driver, wait);
        executeJsScriptById("loginHref", "arguments[0].click();", driver);

        waitAndFindElementById("inputUsername", driver, wait);
        executeJsScriptById("inputUsername", "arguments[0].click();", driver);
        executeJsScriptById("inputUsername", "arguments[0].value='" + username + "';", driver);

        waitAndFindElementById("inputPassword", driver, wait);
        executeJsScriptById("inputPassword", "arguments[0].click();", driver);
        executeJsScriptById("inputPassword", "arguments[0].value='" + password + "';", driver);

        waitAndFindElementById("submitLogin", driver, wait);
        executeJsScriptById("submitLogin", "arguments[0].click();", driver);

        //go to home after login then logout and go to home page
        Assertions.assertEquals("Home", driver.getTitle());
        waitAndFindElementById("submitLogout", driver, wait);
        executeJsScriptById("submitLogout", "arguments[0].click();", driver);

        driver.get("http://localhost:" + this.port + "/home");
        Assertions.assertNotEquals("Home", driver.getTitle());
    }

    @Test
    public void testNotesCRUD() {
        String firstName = "John";
        String lastName = "Doe";
        String username = "johndoe1";
        String password = "hellojohn123";

        String title = "Hello note";
        String description = "Hello world!";

        String updatedTitle = "Update title";
        String updatedDescription = "Update Hello world!";

        //Signup flow
        driver.get("http://localhost:" + this.port + "/signup");
        wait.until(ExpectedConditions.titleContains("Sign Up"));

        waitAndFindElementById("inputFirstName", driver, wait);
        executeJsScriptById("inputFirstName", "arguments[0].click();", driver);
        executeJsScriptById("inputFirstName", "arguments[0].value='" + firstName + "';", driver);

        waitAndFindElementById("inputLastName", driver, wait);
        executeJsScriptById("inputLastName", "arguments[0].click();", driver);
        executeJsScriptById("inputLastName", "arguments[0].value='" + lastName + "';", driver);

        waitAndFindElementById("inputUsername", driver, wait);
        executeJsScriptById("inputUsername", "arguments[0].click();", driver);
        executeJsScriptById("inputUsername", "arguments[0].value='" + username + "';", driver);

        waitAndFindElementById("inputPassword", driver, wait);
        executeJsScriptById("inputPassword", "arguments[0].click();", driver);
        executeJsScriptById("inputPassword", "arguments[0].value='" + password + "';", driver);

        waitAndFindElementById("submitSignUp", driver, wait);
        executeJsScriptById("submitSignUp", "arguments[0].click();", driver);
        Assertions.assertEquals("You successfully signed up! Please continue to the login page.",
                By.id("signUpSuccess").findElement(driver).getText());

        //Login flow
        waitAndFindElementById("loginHref", driver, wait);
        executeJsScriptById("loginHref", "arguments[0].click();", driver);

        waitAndFindElementById("inputUsername", driver, wait);
        executeJsScriptById("inputUsername", "arguments[0].click();", driver);
        executeJsScriptById("inputUsername", "arguments[0].value='" + username + "';", driver);

        waitAndFindElementById("inputPassword", driver, wait);
        executeJsScriptById("inputPassword", "arguments[0].click();", driver);
        executeJsScriptById("inputPassword", "arguments[0].value='" + password + "';", driver);

        waitAndFindElementById("submitLogin", driver, wait);
        executeJsScriptById("submitLogin", "arguments[0].click();", driver);

        //Check if already in Home page
        Assertions.assertEquals("Home", driver.getTitle());
        waitAndFindElementById("submitLogout", driver, wait);

        /**
         * Test case: Write a test that creates a note, and verifies it is displayed.
         */
        //Click in the field nav-notes-tab
        waitAndFindElementById("nav-notes-tab", driver, wait);
        executeJsScriptById("nav-notes-tab", "arguments[0].click();", driver);

        //Click in the field submit-button
        waitAndFindElementById("submit-button", driver, wait);
        executeJsScriptById("submit-button", "arguments[0].click();", driver);

        //Click and fill field note-title
        waitAndFindElementById("note-title", driver, wait);
        executeJsScriptById("note-title", "arguments[0].click();", driver);
        executeJsScriptById("note-title", "arguments[0].value='" + title + "';", driver);

        //Click and fill field note-description
        waitAndFindElementById("note-description", driver, wait);
        executeJsScriptById("note-description", "arguments[0].click();", driver);
        executeJsScriptById("note-description", "arguments[0].value='" + description + "';", driver);

        //Click in the field //*[@id="noteModal"]/div/div/div[3]/button[2] (Save changes button of modal)
        wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath("//*[@id=\"noteModal\"]/div/div/div[3]/button[2]"))));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", driver.findElement(By.xpath("//*[@id=\"noteModal\"]/div/div/div[3]/button[2]")));

        //Create note success & move to home
        wait.until(ExpectedConditions.titleContains("Result"));
        Assertions.assertEquals("Result", driver.getTitle());
        waitAndFindElementById("homeHref-success", driver, wait);
        executeJsScriptById("homeHref-success", "arguments[0].click();", driver);

        //Click in the field nav-notes-tab
        wait.until(ExpectedConditions.titleContains("Home"));
        Assertions.assertEquals("Home", driver.getTitle());
        waitAndFindElementById("nav-notes-tab", driver, wait);
        executeJsScriptById("nav-notes-tab", "arguments[0].click();", driver);

        //Verify the inclusion of Note title
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//*[@id=\"userTable\"]/tbody/tr/th"))));
        String noteTitle = driver.findElement(By.xpath("//*[@id=\"userTable\"]/tbody/tr/th")).getText();
        Assertions.assertEquals(title, noteTitle);

        //Verify the inclusion of Note description
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//*[@id=\"userTable\"]/tbody/tr/td[2]"))));
        String noteDescription = driver.findElement(By.xpath("//*[@id=\"userTable\"]/tbody/tr/td[2]")).getText();
        Assertions.assertEquals(description, noteDescription);

        /**
         * Test case: Write a test that edits an existing note and verifies that the changes are displayed.
         */
        //Click in the field edit-button
        waitAndFindElementById("edit-button", driver, wait);
        executeJsScriptById("edit-button", "arguments[0].click();", driver);

        //Click and fill field note-title
        waitAndFindElementById("note-title", driver, wait);
        executeJsScriptById("note-title", "arguments[0].click();", driver);
        executeJsScriptById("note-title", "arguments[0].value='" + updatedTitle + "';", driver);

        //Click and fill field note-description
        waitAndFindElementById("note-description", driver, wait);
        executeJsScriptById("note-description", "arguments[0].click();", driver);
        executeJsScriptById("note-description", "arguments[0].value='" + updatedDescription + "';", driver);

        //Click in the field //*[@id="noteModal"]/div/div/div[3]/button[2] (Save changes button of modal)
        wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath("//*[@id=\"noteModal\"]/div/div/div[3]/button[2]"))));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", driver.findElement(By.xpath("//*[@id=\"noteModal\"]/div/div/div[3]/button[2]")));

        //Update note success & move to home
        wait.until(ExpectedConditions.titleContains("Result"));
        Assertions.assertEquals("Result", driver.getTitle());
        waitAndFindElementById("homeHref-success", driver, wait);
        executeJsScriptById("homeHref-success", "arguments[0].click();", driver);

        //Click in the field nav-notes-tab
        wait.until(ExpectedConditions.titleContains("Home"));
        Assertions.assertEquals("Home", driver.getTitle());
        waitAndFindElementById("nav-notes-tab", driver, wait);
        executeJsScriptById("nav-notes-tab", "arguments[0].click();", driver);

        //Verify the inclusion of Note title
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//*[@id=\"userTable\"]/tbody/tr/th"))));
        noteTitle = driver.findElement(By.xpath("//*[@id=\"userTable\"]/tbody/tr/th")).getText();
        Assertions.assertEquals(updatedTitle, noteTitle);

        //Verify the inclusion of Note description
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//*[@id=\"userTable\"]/tbody/tr/td[2]"))));
        noteDescription = driver.findElement(By.xpath("//*[@id=\"userTable\"]/tbody/tr/td[2]")).getText();
        Assertions.assertEquals(updatedDescription, noteDescription);


        /**
         * Test case: Write a test that deletes a note and verifies that the note is no longer displayed.
         */
        //Click in the field delete-button
        waitAndFindElementById("delete-button", driver, wait);
        executeJsScriptById("delete-button", "arguments[0].click();", driver);

        //Delete note success & move to home
        wait.until(ExpectedConditions.titleContains("Result"));
        Assertions.assertEquals("Result", driver.getTitle());
        waitAndFindElementById("homeHref-success", driver, wait);
        executeJsScriptById("homeHref-success", "arguments[0].click();", driver);

        //Click in the field nav-notes-tab
        wait.until(ExpectedConditions.titleContains("Home"));
        Assertions.assertEquals("Home", driver.getTitle());
        waitAndFindElementById("nav-notes-tab", driver, wait);
        executeJsScriptById("nav-notes-tab", "arguments[0].click();", driver);

        // check if title is not present
        Boolean isPresent = driver.findElements(By.id("description-title")).size() > 0;
        Assertions.assertNotEquals(true, isPresent);

        // check if description is not present
        isPresent = driver.findElements(By.id("description-note")).size() > 0;
        Assertions.assertNotEquals(true, isPresent);
    }

    @Test
    public void testCredentialsCRUD() {
        String firstName = "John";
        String lastName = "Doe";
        String username = "johndoe2";
        String password = "hellojohn123";

        String URL = "www.udacity.com";
        String credentialUsername = "random123";
        String credentialPassword = "password123";

        String updatedURL = "www.google.com";
        String updatedUsername = "update" + credentialUsername;
        String updatedPassword = "update" + credentialPassword;

        //Signup flow
        driver.get("http://localhost:" + this.port + "/signup");
        wait.until(ExpectedConditions.titleContains("Sign Up"));

        waitAndFindElementById("inputFirstName", driver, wait);
        executeJsScriptById("inputFirstName", "arguments[0].click();", driver);
        executeJsScriptById("inputFirstName", "arguments[0].value='" + firstName + "';", driver);

        waitAndFindElementById("inputLastName", driver, wait);
        executeJsScriptById("inputLastName", "arguments[0].click();", driver);
        executeJsScriptById("inputLastName", "arguments[0].value='" + lastName + "';", driver);

        waitAndFindElementById("inputUsername", driver, wait);
        executeJsScriptById("inputUsername", "arguments[0].click();", driver);
        executeJsScriptById("inputUsername", "arguments[0].value='" + username + "';", driver);

        waitAndFindElementById("inputPassword", driver, wait);
        executeJsScriptById("inputPassword", "arguments[0].click();", driver);
        executeJsScriptById("inputPassword", "arguments[0].value='" + password + "';", driver);

        waitAndFindElementById("submitSignUp", driver, wait);
        executeJsScriptById("submitSignUp", "arguments[0].click();", driver);
        Assertions.assertEquals("You successfully signed up! Please continue to the login page.",
                By.id("signUpSuccess").findElement(driver).getText());

        //Login flow
        waitAndFindElementById("loginHref", driver, wait);
        executeJsScriptById("loginHref", "arguments[0].click();", driver);

        waitAndFindElementById("inputUsername", driver, wait);
        executeJsScriptById("inputUsername", "arguments[0].click();", driver);
        executeJsScriptById("inputUsername", "arguments[0].value='" + username + "';", driver);

        waitAndFindElementById("inputPassword", driver, wait);
        executeJsScriptById("inputPassword", "arguments[0].click();", driver);
        executeJsScriptById("inputPassword", "arguments[0].value='" + password + "';", driver);

        waitAndFindElementById("submitLogin", driver, wait);
        executeJsScriptById("submitLogin", "arguments[0].click();", driver);

        //Check if already in Home page
        Assertions.assertEquals("Home", driver.getTitle());
        waitAndFindElementById("submitLogout", driver, wait);

        /**
         * Test case: Write a test that creates a set of credentials, verifies that they are displayed, and verifies that the displayed password is encrypted.
         */
        //Click in the field nav-credentials-tab
        waitAndFindElementById("nav-credentials-tab", driver, wait);
        executeJsScriptById("nav-credentials-tab", "arguments[0].click();", driver);

        //Click in the field credential-submit-button
        waitAndFindElementById("credential-submit-button", driver, wait);
        executeJsScriptById("credential-submit-button", "arguments[0].click();", driver);

        //Click and fill field credential-url
        waitAndFindElementById("credential-url", driver, wait);
        executeJsScriptById("credential-url", "arguments[0].click();", driver);
        executeJsScriptById("credential-url", "arguments[0].value='" + URL + "';", driver);

        //Click and fill field credential-username
        waitAndFindElementById("credential-username", driver, wait);
        executeJsScriptById("credential-username", "arguments[0].click();", driver);
        executeJsScriptById("credential-username", "arguments[0].value='" + credentialUsername + "';", driver);

        //Click and fill field credential-password
        waitAndFindElementById("credential-password", driver, wait);
        executeJsScriptById("credential-password", "arguments[0].click();", driver);
        executeJsScriptById("credential-password", "arguments[0].value='" + credentialPassword + "';", driver);

        //Click in the field //*[@id="noteModal"]/div/div/div[3]/button[2] (Save changes button of modal)
        wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath("//*[@id=\"credentialModal\"]/div/div/div[3]/button[2]"))));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", driver.findElement(By.xpath("//*[@id=\"credentialModal\"]/div/div/div[3]/button[2]")));

        //Create credential success & move to home
        wait.until(ExpectedConditions.titleContains("Result"));
        Assertions.assertEquals("Result", driver.getTitle());
        waitAndFindElementById("homeHref-success", driver, wait);
        executeJsScriptById("homeHref-success", "arguments[0].click();", driver);

        //Click in the field nav-credentials-tab
        wait.until(ExpectedConditions.titleContains("Home"));
        Assertions.assertEquals("Home", driver.getTitle());
        wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.id("nav-credentials-tab"))));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", driver.findElement(By.id("nav-credentials-tab")));

        /**
         * verifies that they are displayed
         */
        //Verify the inclusion of Credential URL
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//*[@id=\"credentialTable\"]/tbody/tr/td[2]"))));
        String URLCredential = driver.findElement(By.xpath("//*[@id=\"credentialTable\"]/tbody/tr/td[2]")).getText();
        Assertions.assertEquals(URL, URLCredential);

        //Verify the inclusion of Credential Username
        wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath("//*[@id=\"credentialTable\"]/tbody/tr/td[3]"))));
        String usernameCredential = driver.findElement(By.xpath("//*[@id=\"credentialTable\"]/tbody/tr/td[3]")).getText();
        Assertions.assertEquals(credentialUsername, usernameCredential);

        /**
         * Verifies that the displayed password is encrypted
         */
        //Verify the inclusion of Credential Password
        wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath("//*[@id=\"credentialTable\"]/tbody/tr/td[4]"))));
        String passwordEncryptedCredential = driver.findElement(By.xpath("//*[@id=\"credentialTable\"]/tbody/tr/td[4]")).getText();

        wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.id("delete-credential-button"))));
        WebElement credentialElement = driver.findElement(By.id("delete-credential-button"));

        String href = credentialElement.getAttribute("href");
        String[] hrefParse = href.split("/");
        int credentialId = Integer.parseInt(hrefParse[5]);
        Credentials credential = credentialsMapper.getCredentialById(credentialId);
        String decryptedPassword = encryptionService.decryptValue(passwordEncryptedCredential, credential.getKey());

        Assertions.assertEquals(credentialPassword, decryptedPassword);

        /**
         * Test case: Write a test that views an existing set of credentials, verifies that the viewable password is unencrypted, edits the credentials, and verifies that the changes are displayed.
         */
        wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath("//*[@id=\"credentialTable\"]/tbody/tr/td[3]"))));
        passwordEncryptedCredential = driver.findElement(By.xpath("//*[@id=\"credentialTable\"]/tbody/tr/td[4]")).getText();

        //Click in the field edit-credential-button
        waitAndFindElementById("edit-credential-button", driver, wait);
        executeJsScriptById("edit-credential-button", "arguments[0].click();", driver);

        /**
         * Verifies that the viewable password is unencrypted
         */
        waitAndFindElementById("credential-password", driver, wait);
        String viewablePassword = driver.findElement(By.id("credential-password")).getAttribute("value");
        decryptedPassword = encryptionService.decryptValue(passwordEncryptedCredential, credential.getKey());
        Assertions.assertEquals(viewablePassword, decryptedPassword);

        /**
         * Edits the credentials
         */
        //Click and fill field credential-url
        waitAndFindElementById("credential-url", driver, wait);
        executeJsScriptById("credential-url", "arguments[0].click();", driver);
        executeJsScriptById("credential-url", "arguments[0].value='" + updatedURL + "';", driver);

        //Click and fill field credential-username
        waitAndFindElementById("credential-username", driver, wait);
        executeJsScriptById("credential-username", "arguments[0].click();", driver);
        executeJsScriptById("credential-username", "arguments[0].value='" + updatedUsername + "';", driver);

        //Click and fill field credential-password
        waitAndFindElementById("credential-password", driver, wait);
        executeJsScriptById("credential-password", "arguments[0].click();", driver);
        executeJsScriptById("credential-password", "arguments[0].value='" + updatedPassword + "';", driver);

        //Click in the submit-button
        //Click in the field //*[@id="noteModal"]/div/div/div[3]/button[2] (Save changes button of modal)
        wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath("//*[@id=\"credentialModal\"]/div/div/div[3]/button[2]"))));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", driver.findElement(By.xpath("//*[@id=\"credentialModal\"]/div/div/div[3]/button[2]")));

        //Update credential success & move to home
        wait.until(ExpectedConditions.titleContains("Result"));
        Assertions.assertEquals("Result", driver.getTitle());
        waitAndFindElementById("homeHref-success", driver, wait);
        executeJsScriptById("homeHref-success", "arguments[0].click();", driver);

        //Click in the field nav-credentials-tab
        wait.until(ExpectedConditions.titleContains("Home"));
        Assertions.assertEquals("Home", driver.getTitle());
        wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.id("nav-credentials-tab"))));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", driver.findElement(By.id("nav-credentials-tab")));

        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.id("url-credential"))));
        String modifiedURL = driver.findElement(By.id("url-credential")).getText();

        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.id("username-credential"))));
        String modifiedUsername = driver.findElement(By.id("username-credential")).getText();

        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.id("password-credential"))));
        String modifiedPassword = driver.findElement(By.id("password-credential")).getText();

        credential = credentialsMapper.getCredentialById(credentialId);
        String encryptedPassword = encryptionService.encryptValue(updatedPassword, credential.getKey());

        /**
         * Verifies that the changes are displayed
         */
        Assertions.assertEquals(updatedURL, modifiedURL);
        Assertions.assertEquals(updatedUsername, modifiedUsername);
        Assertions.assertEquals(encryptedPassword, modifiedPassword);

        /**
         * Test case: Write a test that deletes an existing set of credentials and verifies that the credentials are no longer displayed.
         */
        //Click in the field delete-button
        waitAndFindElementById("delete-credential-button", driver, wait);
        executeJsScriptById("delete-credential-button", "arguments[0].click();", driver);

        //Delete credential success & move to home
        wait.until(ExpectedConditions.titleContains("Result"));
        Assertions.assertEquals("Result", driver.getTitle());
        waitAndFindElementById("homeHref-success", driver, wait);
        executeJsScriptById("homeHref-success", "arguments[0].click();", driver);

        //Click in the field nav-credentials-tab
        wait.until(ExpectedConditions.titleContains("Home"));
        Assertions.assertEquals("Home", driver.getTitle());
        wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.id("nav-credentials-tab"))));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", driver.findElement(By.id("nav-credentials-tab")));

        Assertions.assertNotEquals(updatedURL,driver.findElement(By.id("credential-url")).getText());
        Assertions.assertNotEquals(updatedUsername,driver.findElement(By.id("credential-username")).getText());
        Assertions.assertNotEquals(updatedPassword,driver.findElement(By.id("credential-password")).getText());
    }
}
