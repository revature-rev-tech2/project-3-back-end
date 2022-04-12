package com.project3.revtech.E2E.runner;

import com.project3.revtech.E2E.poms.Admin.*;
import com.project3.revtech.E2E.poms.User.*;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.util.concurrent.TimeUnit;

@RunWith(Cucumber.class)
    @CucumberOptions(features = "classpath:features", glue = "com.project3.revtech.E2E.steps", plugin = {"pretty",
            "html:src/test/java/resources/reports/html-reports.html"})
	@Ignore
    public class TestRunner {
    public static WebDriver driver;
    public static WebDriverWait explicitWait;
    public static WebDriverWait implicitWait;
    public static UserRegisterPOM userRegisterPOM;
    public static UserLoginPOM userLoginPOM;
    public static UserLogoutPOM userLogoutPOM;
    public static UserSearchProductsPOM userSearchProductsPOM;
    public static PutItemInCartPOM putItemInCartPOM;
    public static PurchaseDiscountedPOM purchaseDiscountedPOM;
    public static OrderHistoryPOM orderHistoryPOM;
    public static UserAccountInfoPOM userAccountInfoPOM;
    public static UserReviewsPOM userReviewsPOM;
    public static AdminAddDiscountPOM adminAddDiscountPOM;
    public static AdminRemoveDiscountPOM adminRemoveDiscountPOM;
    public static AdminRemoveProductPOM adminRemoveProductPOM;
    public static AdminAddProductPOM adminAddProductPOM;
    public static AdminLogoutPOM adminLogoutPOM;


    @BeforeClass
    public static void setup() {
        File file = new File("src/test/resources/chromedriver.exe");
        System.setProperty("webdriver.chrome.driver", file.getAbsolutePath());
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        explicitWait = new WebDriverWait(driver, 5);
        implicitWait = new WebDriverWait(driver, 2);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        userRegisterPOM = new UserRegisterPOM(driver);
        userLoginPOM = new UserLoginPOM(driver);
        userLogoutPOM = new UserLogoutPOM(driver);
        userSearchProductsPOM = new UserSearchProductsPOM(driver);
        putItemInCartPOM = new PutItemInCartPOM(driver);
        purchaseDiscountedPOM = new PurchaseDiscountedPOM(driver);
        orderHistoryPOM = new OrderHistoryPOM(driver);
        userAccountInfoPOM = new UserAccountInfoPOM(driver);
        userReviewsPOM = new UserReviewsPOM(driver);
        adminAddDiscountPOM = new AdminAddDiscountPOM(driver);
        adminRemoveDiscountPOM = new AdminRemoveDiscountPOM(driver);
        adminRemoveProductPOM = new AdminRemoveProductPOM(driver);
        adminAddProductPOM = new AdminAddProductPOM(driver);
        adminLogoutPOM = new AdminLogoutPOM(driver);



        System.out.println("Set up complete!");
    }

    @AfterClass
    public static void teardown(){
        driver.quit();
        System.out.println("teardown complete!");
    }
}


