package page.object;

import lombok.extern.slf4j.Slf4j;
import model.Currency;
import model.ProductPriceInfo;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static java.util.Objects.nonNull;
@Slf4j
public class SearchResultPageTest {
    private static final String HOME_PAGE_URL = "http://prestashop-automation.qatestlab.com.ua/";
    private static final String SEARCH_REQUEST = "dress";

    private RemoteWebDriver driver;
    private SearchResultPage searchResultPage;


    @Before
    public void setUp() throws Exception {
        log.info("Driver setup and configure");

        System.setProperty("webdriver.gecko.driver", "geckodriver.exe");

        driver = new FirefoxDriver();
        driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
        driver.manage().timeouts().setScriptTimeout(20, TimeUnit.SECONDS);
        driver.manage().timeouts().pageLoadTimeout(20, TimeUnit.SECONDS);
        driver.get(HOME_PAGE_URL);


        searchResultPage = new SearchResultPage(driver);
        searchResultPage.selectCurrency(Currency.USD);
        searchResultPage.findItems(SEARCH_REQUEST);
    }

    @After
    public void tearDown() throws Exception {
        log.info("Quit from browser and close driver");
        driver.quit();
    }


    @Test
    public void countFoundProducts() {
        log.info("--->Start 'Found products count test'.");
        Assert.assertEquals(searchResultPage.getTotalProductsCount(), searchResultPage.countFoundProducts());
    }

    @Test
    public void itemsCurrencyCheck() {
        log.info("--->Start 'Products currency test'.");

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
        log.info("--->Start 'Sorting products test'.");

        searchResultPage.setPriceSortDesc();
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.urlContains("order=product.price.desc"));
        List<ProductPriceInfo> products = searchResultPage.getProducts();

        if (products.size() < 2) throw new Exception("not enough results to compare");

        for (int i = 1; i < products.size(); i++) {
            Assert.assertTrue(products.get(i - 1).compareTo(products.get(i)) >= 0);
        }

    }

    @Test
    public void discountPercentMath() {
        log.info("--->Start 'Discount price test'.");

        List<ProductPriceInfo> products = searchResultPage.getProducts();

        products.forEach(p -> {
            if (nonNull(p.getDiscount())) {
                Assert.assertEquals(
                        p.getRegularPrice(),
                        java.util.Optional.of(round(p.getPrice() / (100 - p.getDiscount()) * 100)).get());
            }
        });
    }

    private double round(double d) {
        return (double) Math.round(d * 100) / 100;
    }
}