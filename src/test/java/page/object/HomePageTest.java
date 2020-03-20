package page.object;

import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.util.concurrent.TimeUnit;

@Slf4j
public class HomePageTest {
    private static final String HOME_PAGE_URL = "http://prestashop-automation.qatestlab.com.ua/";
    private static final String HOME_PAGE_TITLE = "prestashop-automation";
    private RemoteWebDriver driver;
    private HomePage homePage;

    @Before
    public void setUp() throws Exception {
        log.info("Driver setup and configure");

        System.setProperty("webdriver.gecko.driver", "geckodriver.exe");

        driver = new FirefoxDriver();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.get(HOME_PAGE_URL);

        homePage = new HomePage(driver);
    }

    @After
    public void tearDown() throws Exception {
        log.info("Quit from browser and close driver");
        driver.quit();
    }


    @Test
    public void mainPageTitle() {
        log.info("--->Start 'Right page destination test'.");

        Assert.assertEquals(HOME_PAGE_TITLE, driver.getTitle());
    }

    @Test
    public void itemsCurrencyCheck() {
        log.info("--->Start 'Featured products currency test'.");

        String[] currentCurrency = homePage.getCurrentCurrency();

        log.info("Current currency :" + currentCurrency[0] + " " + currentCurrency[1]);

        homePage.getProductsPrices().forEach(s -> {
                    log.info(s + " currency found.");
                    Assert.assertTrue(s.contains(currentCurrency[0]) || s.contains(currentCurrency[1]));
                }
        );
    }

}