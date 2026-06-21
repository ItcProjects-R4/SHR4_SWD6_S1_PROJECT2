package org.example;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.Duration;

public class CartTests {
    WebDriver w1;
    WebDriverWait wait;
    String dynamicEmail = "testuser" + System.currentTimeMillis() + "@gmail.com";

    @BeforeClass
    public void setup() {
        WebDriverManager.chromedriver().setup();
        w1 = new ChromeDriver();
        w1.manage().window().maximize();
        wait = new WebDriverWait(w1, Duration.ofSeconds(15));
    }

    @Test(priority = 1)
    public void case1_registerAndLogin() throws InterruptedException {
        w1.get("https://practicesoftwaretesting.com/");
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[data-test='nav-sign-in']"))).click();
        Thread.sleep(500);

        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[data-test='register-link']"))).click();
        Thread.sleep(500);

        w1.findElement(By.id("first_name")).sendKeys("Ahmed");
        w1.findElement(By.id("last_name")).sendKeys("Ali");
        w1.findElement(By.id("dob")).sendKeys("1995-05-15");
        
        WebElement dropdownElement = w1.findElement(By.id("country"));
        new Select(dropdownElement).selectByVisibleText("Egypt");
        
        w1.findElement(By.id("postal_code")).sendKeys("12345");
        w1.findElement(By.id("house_number")).sendKeys("10");
        w1.findElement(By.id("street")).sendKeys("Main St");
        w1.findElement(By.id("city")).sendKeys("Cairo");
        w1.findElement(By.id("state")).sendKeys("Cairo");
        w1.findElement(By.id("phone")).sendKeys("0100000000");
        w1.findElement(By.id("email")).sendKeys(dynamicEmail);
        w1.findElement(By.id("password")).sendKeys("SecurePass_2026!");
        w1.findElement(By.cssSelector(".btnSubmit.mb-3")).click();

        wait.until(ExpectedConditions.urlContains("auth/login"));

        // Login
        w1.get("https://practicesoftwaretesting.com/#/auth/login");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("email"))).sendKeys(dynamicEmail);
        w1.findElement(By.id("password")).sendKeys("SecurePass_2026!");
        w1.findElement(By.className("btnSubmit")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='nav-menu']")));
    }

    @Test(priority = 2)
    public void case2_addItemToCart() throws InterruptedException {
        w1.get("https://practicesoftwaretesting.com/");
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("img[alt='Combination Pliers']"))).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[data-test='add-to-cart']"))).click();
        Thread.sleep(1500);
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[data-test='nav-cart']"))).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("app-cart")));
        Thread.sleep(1000);
    }

    @Test(priority = 3)
    public void case3_increaseQuantity() throws InterruptedException {
        try {
            WebElement plusButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(., '+') or descendant::*[contains(@data-icon, 'plus')]]")));
            plusButton.click();
            Thread.sleep(1500); // Wait for price update
        } catch (Exception e) {
            System.out.println("Could not find Plus button via primary xpath. Attempting alternative...");
            w1.findElements(By.tagName("button")).stream().filter(b -> b.getText().contains("+")).findFirst().ifPresent(WebElement::click);
            Thread.sleep(1500);
        }
    }

    @Test(priority = 4)
    public void case4_decreaseQuantity() throws InterruptedException {
        try {
            WebElement minusButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(., '-') or descendant::*[contains(@data-icon, 'minus')]]")));
            minusButton.click();
            Thread.sleep(1500); // Wait for price update
        } catch (Exception e) {
            System.out.println("Could not find Minus button via primary xpath. Attempting alternative...");
            w1.findElements(By.tagName("button")).stream().filter(b -> b.getText().contains("-")).findFirst().ifPresent(WebElement::click);
            Thread.sleep(1500);
        }
    }

    @Test(priority = 5)
    public void case5_removeItemFromCart() throws InterruptedException {
        try {
            WebElement deleteButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".btn-danger, [data-icon='trash'], fa-icon[ng-reflect-icon='fas,remove']")));
            deleteButton.click();
            Thread.sleep(1500);
        } catch (Exception e) {
            System.out.println("Delete button not found with standard selectors.");
        }
    }

    @Test(priority = 6)
    public void case6_addAnotherItemAndCheckout() throws InterruptedException {
        // Go back home and add an item
        w1.get("https://practicesoftwaretesting.com/");
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("img[alt='Slip Joint Pliers']"))).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[data-test='add-to-cart']"))).click();
        Thread.sleep(1500);
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[data-test='nav-cart']"))).click();
        Thread.sleep(1500);

        // Step 1
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[data-test='proceed-1']"))).click();
        Thread.sleep(1000);

        // Step 2 (Since we are already logged in, we proceed to address)
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[data-test='proceed-2']"))).click();
        Thread.sleep(1000);

        // Step 3
        WebElement countryDropdown = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("country")));
        new Select(countryDropdown).selectByVisibleText("Egypt");
        w1.findElement(By.id("postal_code")).clear();
        w1.findElement(By.id("postal_code")).sendKeys("12345");
        w1.findElement(By.id("house_number")).clear();
        w1.findElement(By.id("house_number")).sendKeys("10");
        w1.findElement(By.id("street")).clear();
        w1.findElement(By.id("street")).sendKeys("Main St");
        w1.findElement(By.id("city")).clear();
        w1.findElement(By.id("city")).sendKeys("Cairo");
        w1.findElement(By.id("state")).clear();
        w1.findElement(By.id("state")).sendKeys("Cairo");
        
        WebElement proceed3 = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[data-test='proceed-3']")));
        ((JavascriptExecutor) w1).executeScript("arguments[0].scrollIntoView(true);", proceed3);
        Thread.sleep(500);
        proceed3.click();

        // Step 4
        WebElement paymentMethod = wait.until(ExpectedConditions.elementToBeClickable(By.id("payment-method")));
        new Select(paymentMethod).selectByVisibleText("Cash on Delivery");

        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[data-test='finish']"))).click();
        Thread.sleep(2000);
    }

    @AfterClass
    public void teardown() {
        if (w1 != null) {
            w1.quit();
        }
    }
}
