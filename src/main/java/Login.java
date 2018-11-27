import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.awt.*;

/**
 * Created by Icewater on 20/11/2018.
 */
public class Login {
    public static void login(String uniName, String username, String password) throws InterruptedException, AWTException {
        WebDriver driver = IliasFileUploader.getDriver();
        driver.get(Constants.iliasLink);
        
        selectUni(driver, uniName);
        enterLoginInfoAndLogin(driver, username, password);
        IliasFileUploader.setDriver(driver);
       

    }

    private static void enterLoginInfoAndLogin(WebDriver driver, String username, String password) {
        WebElement usernameField = driver.findElement(By.id("username"));
        WebElement passwordField = driver.findElement(By.id("password"));
        WebElement loginButton = driver.findElement(By.name("_eventId_proceed"));

        usernameField.sendKeys(username);
        passwordField.sendKeys(password);
        loginButton.click();
    }

    private static void selectUni(WebDriver driver, String uniName) throws AWTException { 
        Robot robi = new Robot();
        WebElement uniField = driver.findElement(By.id("user_idp_iddtext"));
        uniField.clear();
        uniField.click();
        uniField.sendKeys(uniName);
        uniField.sendKeys(Keys.RETURN);

        robi.delay(1000);
        WebElement loginButton = driver.findElement(By.id("wayf_submit_button"));
        loginButton.click();

    }
}
