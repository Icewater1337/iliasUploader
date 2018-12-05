import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;

import java.awt.*;
import java.io.File;
import java.util.List;

/**
 * Usage: Start script by doing: java IliasFileUploader username password Serie0X
 * Only works for mms currently. links need to be adapted for others.
 */
public class IliasFileUploader {
    private static WebDriver webDriver;
    private static int uploadsDone = 0;

    public static void main(String[] args) throws AWTException, InterruptedException {

        WebDriver driver;
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        webDriver = driver;

        Login.login(Constants.universityName, args[0], args[1]);

        driver.get(Constants.courseLink);

        driver.get(Constants.courseExerciseLink);

        Select assignments = new Select(driver.findElement(By.id("ass_id")));
        assignments.selectByVisibleText(args[2].replace("_", " "));

        WebElement submitBtn = driver.findElement(By.name("cmd[selectAssignment]"));
        submitBtn.click();

        List<File> files = FileReader.loadFilesInFolder();
        loopThroughPagesAndUpload(files);


    }

    private static void loopThroughPagesAndUpload(List<File> files) {
        // get pages
        Select initialPages = new Select(webDriver.findElement(By.id("tab_page_sel_2")));
        int initialPagesSIze = initialPages.getOptions().size();
        for (int i = 0; i < initialPagesSIze; i++) {
            Select pages = new Select(webDriver.findElement(By.id("tab_page_sel_2")));
            pages.selectByIndex(i);
            WebElement initialTable = webDriver.findElement(By.id("exc_mem"));
            List<WebElement> initialRows = initialTable.findElements(By.tagName("tr"));
            //Remove header row
            initialRows.remove(0);
            int rowsNbr = initialRows.size();
            // bestanden setzen
            bestandenSetzen(files, initialRows);

            // upload files
            uploadFiles(files, rowsNbr);
        }
    }

    private static void uploadFiles(List<File> files, int rowsNbr) {
        for (int j = 0; j < rowsNbr; j++) {
            WebElement table = webDriver.findElement(By.id("exc_mem"));
            WebElement elt = table.findElements(By.tagName("tr")).get(j + 1);
            List<WebElement> columnsOfRow = elt.findElements(By.tagName("td"));
            String lastName = escapeName(columnsOfRow.get(2).getText().split(",")[0]);
            File fileToUpload = FileReader.returnFileIfContainedIn(files, lastName);
            if (fileToUpload != null) {
                attachFileProcess(table, columnsOfRow, lastName, fileToUpload);
            }
        }
    }

    private static void attachFileProcess(WebElement table, List<WebElement> columnsOfRow, String lastName, File fileToUpload) {
        WebElement actionsButton = columnsOfRow.get(6);
        actionsButton.click();
        WebElement rückmeldungPerDateiBtn = table.findElements(By.partialLinkText("Rückmeldung per Datei")).get(0);
        rückmeldungPerDateiBtn.click();
        attachFile(fileToUpload);
        System.out.println("Uploaded for ilias Name: " + lastName);
        // press back
        WebElement backLink = webDriver.findElement(By.partialLinkText("Zurück"));
        backLink.click();
    }

    private static void bestandenSetzen(List<File> files, List<WebElement> initialRows) {
        boolean setBestanden = false;
        for (WebElement elt : initialRows) {
            List<WebElement> columnsOfRow = elt.findElements(By.tagName("td"));
            String lastName = escapeName(columnsOfRow.get(2).getText().split(",")[0]);
            File fileToUpload = FileReader.returnFileIfContainedIn(files, lastName);
            if (fileToUpload != null) {
                Select passedSelect = new Select(columnsOfRow.get(5).findElement(By.tagName("select")));
                if ( fileToUpload.getName().contains("_ok")){
                    passedSelect.selectByIndex(1);
                } else if ( fileToUpload.getName().contains("_nok")){
                    passedSelect.selectByIndex(0);
                }
                setBestanden = true;


            }

        }
        if (setBestanden) {
            WebElement saveButton = webDriver.findElements(By.xpath("//*[@id=\"ilExcIDlForm\"]/div[5]/div[4]/div[2]/input")).get(0);
            saveButton.click();
        }
    }

    private static String escapeName(String s) {
        return s.replace("ä", "ae").replace("ö", "oe").replace("ü", "ue");
    }

    private static void attachFile(File fileToUpload) {
        webDriver.findElement(By.id("new_file")).sendKeys(fileToUpload.getAbsolutePath());
        webDriver.findElement(By.name("cmd[uploadFile]")).click();
        System.out.println("Uploads done: " + uploadsDone);
        System.out.println("Uploaded: " + fileToUpload.getName());
        uploadsDone++;
    }

    public static WebDriver getDriver() {
        return webDriver;
    }

    public static void setDriver(WebDriver driver) {
        webDriver = driver;
    }

}
