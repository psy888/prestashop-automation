package page.object;

import lombok.RequiredArgsConstructor;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class HomePage {

    protected final WebDriver driver;
    //todo recheck selectors
    //currency
    protected By currentCurrency = By.cssSelector(".currency-selector.dropdown span.expand-more");

    //products
    private By productsSection = By.cssSelector(".products");

    //product info
    protected By productPriceSpan = By.cssSelector("article .product-price-and-shipping span.price");


    protected String[] getCurrencyFromString(String str) {
        return str.split(" ", 2);
    }


    public String[] getCurrentCurrency() {
        return getCurrencyFromString(driver.findElement(currentCurrency).getText());
    }

    /**
     * get item price with currency as string
     *
     * @return list of prices
     */
    public List<String> getItemsPrices() {
        return driver.findElement(productsSection)
                .findElements(productPriceSpan)
                .stream()
                .map(WebElement::getText)
                .collect(Collectors.toList());
    }


}
