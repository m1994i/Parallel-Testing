package autoTesting;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

import java.time.Duration;
import java.util.List;

public class test {
    static ExtentReports extent;
    ExtentTest test;
    WebDriver driver;
    @BeforeClass
    public void setDriver(){
        String path = System.getProperty("user.dir")+"\\reports\\report.html";
        ExtentSparkReporter reporter = new ExtentSparkReporter(path);
        reporter.config().setReportName("Web Automation Test");
        reporter.config().setDocumentTitle("Test");

        extent = new ExtentReports();
        extent.attachReporter(reporter);
        extent.setSystemInfo("Milan", "Ivanovic");
    }
    @Test
    public void Test1() throws InterruptedException {
        //Kreiran test u reportsu koji ce biti prikazan
       test=extent.createTest("Booking test", "Functional testing");
        /*Test izvrsava, ulazak na sajt, zatim unosi tekst u polje za pretragu grada "pat"
        zatim klikne na grad, nakon toga se bira grad za sletanje, ponovo se
        unosi rec "ba" zatim se bira grad koji je prikazan*/
        try {
            ChromeOptions options = new ChromeOptions();
            driver = new ChromeDriver(options);
            driver.manage().window().maximize();
            driver.get("https://rahulshettyacademy.com/dropdownsPractise/");
            driver.findElement(By.id("ctl00_mainContent_ddl_originStation1_CTXT")).sendKeys("pat");
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.cssSelector("li[class='livecl city_selected'] a[value='PAT']"))));
            Thread.sleep(1000);
            driver.findElement(By.cssSelector("li[class='livecl city_selected'] a[value='PAT']")).click();
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
            driver.findElement(By.name("ctl00_mainContent_ddl_destinationStation1_CTXT")).sendKeys("ba");
            Thread.sleep(1000);
            driver.findElement(By.xpath("(//a[@value='BKK'])[2]")).click();
            Thread.sleep(1000);
            //Bira se datum polaska i proverava se da li je ocekivani datum prikazan ispravno
            driver.findElement(By.xpath("(//a[@class='ui-state-default'][normalize-space()='14'])[1]")).click();
            String selectedDate1 = driver.findElement(By.id("view_fulldate_id_1")).getText();
            String expectedDate1 = "Wed, Aug 14 2024";
            Assert.assertEquals(selectedDate1, expectedDate1);
            test.pass("Ocekivani datum za poletanje prikazan ispravno.");
            //Bira se datum povratka i proverava se da li je uneti datum prikazan ispravno
            driver.findElement(By.id("ctl00_mainContent_view_date2")).click();
            Thread.sleep(1000);
            driver.findElement(By.xpath("//a[@class='ui-datepicker-next ui-corner-all']")).click();
            Thread.sleep(1000);
            driver.findElement(By.xpath("//span[@class='ui-icon ui-icon-circle-triangle-e']")).click();
            wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath("(//td[@data-handler='selectDay'])[43]"))));
            driver.findElement(By.xpath("(//td[@data-handler='selectDay'])[43]")).click();
            String selectedDate2 = driver.findElement(By.id("view_fulldate_id_2")).getText();
            String expectedDate2 = "Tue, Nov 12 2024";
            Assert.assertEquals(selectedDate2, expectedDate2);
            test.pass("Ocekivani datum za povratak prikazan ispravno.");
            //Testira se funkcija dropdown menija i odabir putnika i dece
            WebElement passengersDropdown = driver.findElement(By.id("divpaxinfo"));
            passengersDropdown.click();
            WebElement adultButton = driver.findElement(By.id("hrefIncAdt"));
            for (int i = 0; i < 4; i++) {
                adultButton.click();
            }
            WebElement childButton = driver.findElement(By.id("hrefIncChd"));
            for (int i = 0; i < 2; i++) {
                childButton.click();
            }
            //provera da li je broj putnika prikazan ispravno
            String passengers = driver.findElement(By.id("divpaxinfo")).getText();
            Assert.assertEquals(passengers, "5 Adult, 2 Child");
            test.pass("Ocekivani broj putnika ispravno prikazan.");
            WebElement buttonDone =driver.findElement(By.id("btnclosepaxoption"));
            buttonDone.click();
            boolean isButtonDoneClosed = wait.until(ExpectedConditions.invisibilityOf(buttonDone));
            Assert.assertTrue(isButtonDoneClosed, "Prozor nije zatvoren nakon klika na dugme Done");
            test.pass("Dugme 'Done' ispravno funkcionise.");
            WebElement currencyDropdown = driver.findElement(By.id("ctl00_mainContent_DropDownListCurrency"));
            Select select = new Select(currencyDropdown);
            select.selectByValue("USD");
            //Proveravamo da li je valuta selektovana pravilno
            String selectedOpt = select.getFirstSelectedOption().getText();
            Assert.assertEquals(selectedOpt, "USD", "Selektovana valuta nije USD");
            test.pass("Ocekivana valuta prikazana ispravno.");
            //Izvrsavamo klik na search button i cekamo da se stranica ponovo ucita
            WebElement searchButton = driver.findElement(By.id("ctl00_mainContent_btn_FindFlights"));
            searchButton.click();
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        }
        catch (Exception e){
            test.fail("Doslo je do greske: "+e.getMessage());
            e.getStackTrace();
        }
        catch (AssertionError e){
            test.fail("Doslo je do greske: "+e.getMessage());
        }
    }
    @Test
    public void Test2() throws InterruptedException {
        test= extent.createTest("Booking flight", "Functional testing 2");
        try {
            FirefoxOptions options = new FirefoxOptions();
            driver = new FirefoxDriver(options);
            driver.manage().window().maximize();

            driver.get("https://rahulshettyacademy.com/dropdownsPractise/");
            driver.findElement(By.id("ctl00_mainContent_ddl_originStation1_CTXT")).click();
            WebElement chenai = driver.findElement(By.cssSelector("a[text='Chennai (MAA)']"));
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            wait.until(ExpectedConditions.elementToBeClickable(chenai));
            chenai.click();
            Thread.sleep(1000);
            WebElement bengaluru = driver.findElement(By.xpath("(//a[@value='BLR'][normalize-space()='Bengaluru (BLR)'])[2]"));
            wait.until(ExpectedConditions.elementToBeClickable(bengaluru));
            bengaluru.click();
            driver.findElement(By.cssSelector(".ui-state-default.ui-state-highlight")).click();
            driver.findElement(By.id("ctl00_mainContent_view_date2")).click();
            WebElement date2 = driver.findElement(By.xpath("(//a[@class='ui-state-default'][normalize-space()='19'])[1]"));
            wait.until(ExpectedConditions.elementToBeClickable(date2));
            date2.click();
            WebElement currencyDropdown = driver.findElement(By.id("ctl00_mainContent_DropDownListCurrency"));
            Select dropdown = new Select(currencyDropdown);
            dropdown.selectByValue("USD");
            WebElement FandFCheckbox = driver.findElement(By.name("ctl00$mainContent$chk_friendsandfamily"));
            FandFCheckbox.click();
            Assert.assertTrue(FandFCheckbox.isSelected(), "Checkbox nije selektovan");
            test.pass("Checkbox Family and Friends je selektovan.");
            WebElement studentCheckbox = driver.findElement(By.name("ctl00$mainContent$chk_StudentDiscount"));
            studentCheckbox.click();
            Assert.assertFalse(FandFCheckbox.isSelected(), "Checkbox je i dalje selektovan");
            test.pass("Checkbox Family and Friends je diselektovan.");
            Assert.assertTrue(studentCheckbox.isSelected(), "Checkbox nije selektovan");
            test.pass("Checkbox Student je selektovan.");
        }
        catch (Exception e){
            test.fail("Doslo je do greske: "+e.getMessage());
            e.getStackTrace();
        }
    }
    @AfterMethod
    /*Ovaj test se izvrsava poslednji i njegova uloga je da zabelezi sve
    reportse koji su uneti u testu i zatvori browser nakon zavrsetka testiranja*/
    public void tearDown(){
        extent.flush();
        driver.quit();
    }

}
