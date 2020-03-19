package page.object;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

public class SearchResultPageTest {
    private static final String HOME_PAGE_URL = "http://prestashop-automation.qatestlab.com.ua/";
    private static final String SEARCH_REQUEST = "dress";

    private RemoteWebDriver driver;
    private SearchResultPage searchResultPage;

    @Before
    public void setUp() throws Exception {
        System.setProperty("webdriver.gecko.driver", "geckodriver.exe");
        driver = new FirefoxDriver();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.get(HOME_PAGE_URL);
        searchResultPage = new SearchResultPage(driver);
        searchResultPage.selectCurrency(Currency.USD);
        searchResultPage.findItems(SEARCH_REQUEST);
    }

    @After
    public void tearDown() throws Exception {
        driver.quit();
    }


    @Test
    public void countFoundProducts() {
        Assert.assertEquals(searchResultPage.getTotalProductsCount(),searchResultPage.countFoundProducts());
    }

    @Test
    public void itemsCurrencyCheck() {
        String[] currentCurrency = searchResultPage.getCurrentCurrency();
        searchResultPage.getItemsPrices().forEach(s -> {
                    Assert.assertTrue(s.contains(currentCurrency[0]) || s.contains(currentCurrency[1]));
                }
        );
    }


}