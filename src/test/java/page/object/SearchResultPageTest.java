package page.object;

import model.ProductPriceInfo;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class SearchResultPageTest {
    private static final String HOME_PAGE_URL = "http://prestashop-automation.qatestlab.com.ua/";
    private static final String SEARCH_REQUEST = "dress";

    private RemoteWebDriver driver;
    private SearchResultPage searchResultPage;

    private List<ProductPriceInfo> products;

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
        Assert.assertEquals(searchResultPage.getTotalProductsCount(), searchResultPage.countFoundProducts());
    }

    @Test
    public void itemsCurrencyCheck() {
        String[] currentCurrency = searchResultPage.getCurrentCurrency();
        searchResultPage.getProducts().forEach(p -> {
                    Assert.assertTrue(
                            p.getCurrency().contains(currentCurrency[0]) ||
                                    p.getCurrency().contains(currentCurrency[1])
                    );
                }
        );
    }


    @Test
    public void descendingSort() throws Exception {
        searchResultPage.setPriceSortDesc();
//        WebDriverWait wait = new WebDriverWait(driver, 5);
//        wait.withTimeout(Duration.ofSeconds(10));
        products = searchResultPage.getProducts();

        if (products.size() < 2) throw new Exception("not enough results to compare");

        for (int i = 1; i < products.size(); i++) {
            Assert.assertTrue(products.get(i - 1).compareTo(products.get(i)) >= 0);
        }

    }
}