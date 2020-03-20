package page.object;

import model.ProductPriceInfo;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SearchResultPage extends HomePage {

    //currency
    @FindBy(css = ".currency-selector.dropdown span.expand-more")
    private WebElement currentCurrency;

    ///search
    @FindBy(css = "#search_widget input[type='text']")
    private WebElement searchInput;
    @FindBy(css = "#search_widget button[type='submit']")
    private WebElement searchButton;

    //sort
    @FindBy(css = ".products-sort-order .select-title")
    private WebElement sortDropdownA;
    @FindBy(xpath = "//a[contains(@href,'price.desc')]")
    private WebElement sortDescA;

    //products
    @FindBy(css = "#products .total-products")
    private WebElement totalProductsCountP;
    @FindAll(@FindBy(css = "#products article.product-miniature"))
    List<WebElement> productArticles;

    //product info
    @FindAll(@FindBy(css = ".product-price-and-shipping"))
    List<WebElement> productPricesDivs;

    private By productRegularPriceSpan = By.cssSelector("span.regular-price");
    private By productPriceSpan = By.cssSelector("span.price");
    private By productDiscountSpan = By.cssSelector("span.discount-percentage");


    public SearchResultPage(WebDriver driver) {
        super(driver);
        AjaxElementLocatorFactory factory = new AjaxElementLocatorFactory(driver,100);
        PageFactory.initElements(factory,this);
    }


    private By getCurrencySelector(Currency currency) {
        return By.xpath("//*[@id='_desktop_currency_selector']/div/ul/li/a[text()[contains(.,'" + currency.toString() + "')]]");
    }

    private void insertSearchRequest(String request) {
        searchInput.sendKeys(request);
    }

    private void submitSearchRequest() {
        searchButton.click();
    }


    public void selectCurrency(Currency currency) {
        showCurrencyDropdown();
        driver.findElement(getCurrencySelector(currency)).click();
    }

    private void showCurrencyDropdown() {
        currentCurrency.click();
    }

    public int getTotalProductsCount() {
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(totalProductsCountP.getText());
        if (matcher.find()) {
            return Integer.parseInt(matcher.group());
        }
        return -1;
    }

    public int countFoundProducts() {
        //TODO impl pagination check
        return productArticles.size();
    }

    public void findItems(String searchRequest) {
        insertSearchRequest(searchRequest);
        submitSearchRequest();
    }

    public void setPriceSortDesc() {
        showOrderDropdown();
        sortDescA.click();
//        driver.get("http://prestashop-automation.qatestlab.com.ua/uk/search?controller=search&order=product.price.desc&s=dress");
    }

    private void showOrderDropdown() {
        sortDropdownA.click();
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
        return productPricesDivs;
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




}
