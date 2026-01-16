package com.microservice.auth.acceptance;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

public class AuthAcceptanceTest {

    private static WebDriver driver;

    @BeforeEach
    public void setup(){
        driver = new FirefoxDriver();
        driver.manage().window().maximize();
        driver.get("http://localhost:5173/sign-up");
    }

    @AfterEach
    public void tearDown() {
        driver.quit();
    }

    @Test
    public void testUserRegistration() throws InterruptedException {
        // elementos
        WebElement emailField = driver.findElement(By.id("email"));
        WebElement passwordField = driver.findElement(By.id("password"));
        WebElement confirmPasswordField = driver.findElement(By.id("confirmPassword"));
        WebElement submitButton = driver.findElement(By.xpath("//button[@type='button']"));

        // Llenar formulario
        emailField.clear();
        emailField.sendKeys("testuser2@example.com");
        Thread.sleep(800);

        passwordField.clear();
        passwordField.sendKeys("SecurePassword123");
        Thread.sleep(800);

        confirmPasswordField.clear();
        confirmPasswordField.sendKeys("SecurePassword123");
        Thread.sleep(800);

        submitButton.click();

        // registro completo?
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(12));
        wait.until(ExpectedConditions.urlToBe("http://localhost:5173/"));

        String currentUrl = driver.getCurrentUrl();
        assertEquals("http://localhost:5173/", currentUrl);
        System.out.println("¡Redirección correcta al dashboard!");

        Cookie accessTokenCookie = driver.manage().getCookieNamed("accessToken");
        Cookie refreshTokenCookie = driver.manage().getCookieNamed("refreshToken");

        assertNotNull(accessTokenCookie, "La cookie accessToken no se creó");
        assertNotNull(refreshTokenCookie, "La cookie refreshToken no se creó");

        Thread.sleep(5000);
    }

    @Test
    public void testEmailAlreadyExists(){
        WebElement emailField = driver.findElement(By.id("email"));
        WebElement passwordField = driver.findElement(By.id("password"));
        WebElement confirmPasswordField = driver.findElement(By.id("confirmPassword"));
        WebElement submitButton = driver.findElement(By.xpath("//button[@type='button']"));

        emailField.sendKeys("testuser2@example.com");
        passwordField.sendKeys("SecurePassword123");
        confirmPasswordField.sendKeys("SecurePassword123");

        submitButton.click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement toastMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("toast:signup-error:description")));

        assertEquals("User with email 'testuser@example.com' already exists.", toastMessage.getText());
    }

    @Test
    public void testInvalidEmailFormat() throws InterruptedException {
        WebElement emailField = driver.findElement(By.id("email"));
        WebElement passwordField = driver.findElement(By.id("password"));
        WebElement confirmPasswordField = driver.findElement(By.id("confirmPassword"));
        WebElement submitButton = driver.findElement(By.xpath("//button[@type='button']"));

        emailField.sendKeys("invalid-email");
        passwordField.sendKeys("SecurePassword123");
        confirmPasswordField.sendKeys("SecurePassword123");

        submitButton.click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement errorMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("toast:signup-error:description")));


        assertNotNull(errorMessage);
        assertEquals("must be a well-formed email address", errorMessage.getText());
    }


    @Test
    public void testEmptyFields() throws InterruptedException {
        WebElement emailField = driver.findElement(By.id("email"));
        WebElement passwordField = driver.findElement(By.id("password"));
        WebElement confirmPasswordField = driver.findElement(By.id("password"));
        WebElement submitButton = driver.findElement(By.xpath("//button[@type='button']"));

        emailField.sendKeys("");
        passwordField.sendKeys("");
        confirmPasswordField.sendKeys("");

        Thread.sleep(5000);

        assertFalse(submitButton.isEnabled());
    }


    // Sign in

    @Test
    void testUserWithEmailNotFound(){
        WebElement emailField = driver.findElement(By.id("email"));
        WebElement passwordField = driver.findElement(By.id("password"));
        WebElement submitButton = driver.findElement(By.xpath("//button[@type='button']"));

        emailField.sendKeys("email@email.com");
        passwordField.sendKeys("SomePassword123");

        submitButton.click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement toastMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("toast:signin-error:description")));

        assertEquals("User with Email: email@email.com not found", toastMessage.getText());
    }

    @Test
    void testInvalidCredentialsWhenIncorrectPassword(){
        WebElement emailField = driver.findElement(By.id("email"));
        WebElement passwordField = driver.findElement(By.id("password"));
        WebElement submitButton = driver.findElement(By.xpath("//button[@type='button']"));

        emailField.sendKeys("testuser@example.com");
        passwordField.sendKeys("not-right-password");

        submitButton.click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement toastMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("toast:signin-error:description")));

        assertEquals("Invalid credentials: Incorrect password.", toastMessage.getText());

    }
}
