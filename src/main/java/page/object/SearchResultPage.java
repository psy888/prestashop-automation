package page.object;

import model.ProductPriceInfo;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SearchResultPage extends HomePage {

    //currency
    private By currentCurrency = By.cssSelector(".currency-selector.dropdown span.expand-more");

    ///search
    private By searchInput = By.cssSelector("#search_widget input[type='text']");
    private By searchButton = By.cssSelector("#search_widget button[type='submit']");

    //sort
    private By sortDropdownA = By.cssSelector(".products-sort-order .select-title");
    private By sortDescA = By.xpath("//a[contains(@href,'price.desc')]");

    //products
    private By totalProductsCountP = By.cssSelector("#products .total-products");
    private By productArticle = By.cssSelector("#products article.product-miniature");

    //product info
    private By productPricesDiv = By.cssSelector(".product-price-and-shipping");
    private By productRegularPriceSpan = By.cssSelector("span.regular-price");
    private By productPriceSpan = By.cssSelector("span.price");
    private By productDiscountSpan = By.cssSelector("span.discount-percentage");


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

    public void setPriceSortDesc() {
        showOrderDropdown();
        driver.findElement(sortDescA).click();
    }

    private void showOrderDropdown() {
        driver.findElement(sortDropdownA).click();
    }


    public List<ProductPriceInfo> getProducts() {
        List<ProductPriceInfo> productPriceInfos = new ArrayList<>();
        List<WebElement> webElements = getProductWebElements();
        webElements.forEach(w -> {
            productPriceInfos.add(ProductPriceInfo.builder()
                    .price(getPrice(w, productPriceSpan))
                    .regularPrice(getPrice(w, productRegularPriceSpan))
                    .discount(getDiscountPercent(w))
                    .currency(getCurrency(w))
                    .build());
        });
        return productPriceInfos;
    }

    private List<WebElement> getProductWebElements() {
        return driver.findElements(productPricesDiv);
    }

    private Double getPrice(WebElement w, By targetPrice) {
        try {
            return Double.parseDouble(cutPriceString(w.findElement(targetPrice).getText()).replace(",","."));
        } catch (Exception e) {
            return null;
        }
    }

    private String cutPriceString(String str) throws RuntimeException {
        Matcher matcher = Pattern.compile("\\d+.\\d+").matcher(str);
        if (matcher.find()) {
            return matcher.group();
        }
        throw new RuntimeException("price parse error");
    }

    private String getCurrency(WebElement w){
        Matcher matcher = Pattern.compile("\\s\\D+").matcher(w.findElement(productPriceSpan).getText());
        if(matcher.find()) {
            return matcher.group().trim();
        }
        throw new RuntimeException("Currency parse error");
    }


    private Integer getDiscountPercent(WebElement w){
        try {
            return Integer.parseInt(cutDiscountString(w.findElement(productDiscountSpan).getText()));
        }catch (Exception e){
            return null;
        }
    }

    private String cutDiscountString(String str){
        Matcher matcher = Pattern.compile("\\d+").matcher(str);
        if(matcher.find()){
            return matcher.group();
        }
        throw new RuntimeException("discount parse error");
    }


    public By getMarkerSelector(){
        return productPricesDiv;
    }
}
