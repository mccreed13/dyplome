package org.anton;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

abstract class SeleniumParser implements AutoCloseable {
    WebDriver driver;
    List<WebElement> webElements;
    protected abstract void parseElements();

    public int getSize() {
        return webElements.size();
    }

    protected abstract String getName(WebElement e);

    protected abstract String getPrice(WebElement e);

    public abstract List<?> getItemsList();

    public abstract void printCatalogItems();

    @Override
    public void close() {
        driver.close();
    }

    public abstract String getUrl(WebElement e);
}
