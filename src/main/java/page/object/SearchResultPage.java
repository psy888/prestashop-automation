package page.object;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SearchResultPage extends HomePage {

    //currency
    private By currentCurrency = By.cssSelector(".currency-selector.dropdown span.expand-more");

    ///search
    private By searchInput = By.cssSelector("#search_widget input[type='text']");
    private By searchButton = By.cssSelector("#search_widget button[type='submit']");


    //products
    private By totalProductsCountP = By.cssSelector("#products .total-products");
    private By productArticle = By.cssSelector("#products article.product-miniature");

    //product info
    private By productPriceSpan = By.cssSelector("article .product-price-and-shipping span.price");


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
        showCurrencyDropdown();
        driver.findElement(getCurrencySelector(currency)).click();
    }

    private void showCurrencyDropdown() {
        driver.findElement(currentCurrency).click();
    }

    public int getTotalProductsCount() {
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(driver.findElement(totalProductsCountP).getText());
        if (matcher.find()) {
            return Integer.parseInt(matcher.group());
        }
        return -1;
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
