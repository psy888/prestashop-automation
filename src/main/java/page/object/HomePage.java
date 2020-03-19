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
    By currentCurrency = By.xpath("//*[@id='_desktop_currency_selector']/div/ul/li[@class='current']/a");
    By currency = By.xpath("//*[@id='_desktop_currency_selector']/div/ul/li/a");

    //products
    By featuredProductsSection = By.xpath("//section[@class='featured-products'");
    By productsDiv = By.xpath("//div[@class='products']");
    By productArticle = By.xpath(".//article[itemtype='http://schema.org/Product']");

    //product info
    By productPriceAndShippingDiv = By.xpath(".//div[@class='product-price-and-shipping']");
    By productPriceSpan = By.xpath(".//span[@class='price']");


    private By getCurrencySelector(Currency currency) {
        return By.xpath("//*[@id='_desktop_currency_selector']/div/ul/li/a[text()[contains(.,'" + currency.toString() + "')]]");
    }

    private String getCurrencyFromString(String str){
        return Arrays.stream(str.split("[a-zA-Z]{3}")).findFirst().orElse("");
    }


    private void selectCurrency(Currency currency) {
        driver.findElement(getCurrencySelector(currency)).click();
    }



    public String getCurrentCurrency() {
        return getCurrencyFromString(driver.findElement(currentCurrency).getText());
    }

    /**
     * get item price with currency as string
     *
     * @return list of prices
     */
    private List<String> getItemsPrices() {
        return driver.findElement(featuredProductsSection)
                .findElements(productPriceSpan)
                .stream()
//                .map(WebElement::getText)
                .map(webElement -> getCurrencyFromString(webElement.getText()))
                .collect(Collectors.toList());
    }


}
