package page.object;

import lombok.RequiredArgsConstructor;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class HomePage {

    private final WebDriver driver;
    //todo recheck selectors
    //currency
    private By currentCurrency = By.cssSelector(".currency-selector.dropdown span.expand-more");
    private By currency = By.xpath("//*[@id='_desktop_currency_selector']/div/ul/li/a");

    //products
    private By featuredProductsSection = By.cssSelector(".featured-products");
    private By productsDiv = By.xpath("//div[@class='products']");
    private By productArticle = By.xpath(".//article[itemtype='http://schema.org/Product']");

    //product info
    private By productPriceAndShippingDiv = By.xpath(".//div[@class='product-price-and-shipping']");
    private By productPriceSpan = By.cssSelector("article .product-price-and-shipping span.price");


    private By getCurrencySelector(Currency currency) {
        return By.xpath("//*[@id='_desktop_currency_selector']/div/ul/li/a[text()[contains(.,'" + currency.toString() + "')]]");
    }

    private String[] getCurrencyFromString(String str) {
        return str.split(" ",2);
    }


    private void selectCurrency(Currency currency) {
        driver.findElement(getCurrencySelector(currency)).click();
    }
//#_desktop_currency_selector div.currency-selector.dropdown span.expand-more

    public String[] getCurrentCurrency() {
        return getCurrencyFromString(driver.findElement(currentCurrency).getText());
    }

    /**
     * get item price with currency as string
     *
     * @return list of prices
     */
    public List<String> getItemsPrices() {
        return driver.findElement(featuredProductsSection)
                .findElements(productPriceSpan)
                .stream()
                .map(WebElement::getText)
                .collect(Collectors.toList());
    }


}
