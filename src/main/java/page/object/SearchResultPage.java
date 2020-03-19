package page.object;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;


public class SearchResultPage extends HomePage {

//    private final WebDriver driver;

    //currency
    private By currentCurrency = By.cssSelector(".currency-selector.dropdown span.expand-more");
    private By currency = By.xpath("//*[@id='_desktop_currency_selector']/div/ul/li/a");

    ///search
    private By searchInput = By.cssSelector("#search_widget input[type='text']");
    private By searchButton = By.cssSelector("#search_widget button[type='submit']");

    //products
    private By featuredProductsSection = By.cssSelector(".featured-products");
    private By totalProductsCountP = By.cssSelector("#products .total-products");
    private By productArticle = By.cssSelector("#products article.product-miniature");

    //product info
    private By productPriceAndShippingDiv = By.xpath(".//div[@class='product-price-and-shipping']");
    private By productPriceSpan = By.cssSelector("article .product-price-and-shipping span.price");


    private int itemsFound;

    public SearchResultPage(WebDriver driver) {
        super(driver);
    }


    private By getCurrencySelector(Currency currency) {
        return By.xpath("//*[@id='_desktop_currency_selector']/div/ul/li/a[text()[contains(.,'" + currency.toString() + "')]]");
    }

    private void insertSearchRequest(String request) {
        driver.findElement(searchInput).sendKeys(request);
    }

    private void submitSearchRequest() {
        driver.findElement(searchButton).click();
    }



    public void selectCurrency(Currency currency) {
        driver.findElement(getCurrencySelector(currency)).click();
    }

    public int getTotalProductsCount() {
        return Integer.parseInt(driver.findElement(totalProductsCountP).getText().split("\\d*")[0]);
    }

    public int countFoundProducts() {
        //TODO impl pagination check
        return driver.findElements(productArticle).size();
    }

    public void findItems(String searchRequest) {
        insertSearchRequest(searchRequest);
        submitSearchRequest();
    }


}
