package org.anton;

import org.anton.items.MetroItem;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.List;

public class MetroSelenium extends SeleniumParser {
//    private final String URL = "https://shop.metro.ua/shop/category/%D0%BF%D1%80%D0%BE%D0%B4%D1%83%D0%BA%D1%82%D0%B8/%D0%BC%E2%80%99%D1%8F%D1%81%D0%BE-%D1%82%D0%B0-%D0%BF%D1%82%D0%B8%D1%86%D1%8F/%D1%81%D0%B2%D1%96%D0%B6%D0%B5-%D0%BC'%D1%8F%D1%81%D0%BE/%D0%BA%D1%83%D1%80%D1%8F%D1%82%D0%B8%D0%BD%D0%B0";

    private final String paginationItemBy = "pull-right";
    private final String buttonMoreBy = "mfcss_load-more-articles";

    private final String cardElementBy = "sd-articlecard";

    private final String nameInCardBy = "title";
    private final String mainPriceInCardBy = "primary";
    private final String oldPriceInCardBy = "strike-through";
    private final String isAvailableBy = "ex-delivery-store";

    List<WebElement> items;

    public MetroSelenium(String url) {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("user-data-dir=D:\\TEST\\Profile 10");
        options.addArguments("--profile-directory=Profile 10");
        driver = new ChromeDriver(options);
        goUrl(url);
    }

    protected void parseElements(){
        webElements = driver.findElements(By.className(cardElementBy));
    }

    public void printCatalogItems() {
        System.out.println(webElements.size());
                webElements.forEach(c-> {
                    System.out.println(getName(c));
                    System.out.println("Price:" + getPrice(c));
                    System.out.println("Old price:" + getOldPrice(c));
                });
    }

    @Override
    public String getUrl(WebElement e) {
        List<WebElement> elements = e.findElements(By.className(nameInCardBy));
        return (!elements.isEmpty() ? "https://shop.metro.ua"+elements.getFirst().getDomAttribute("href") : null);
    }

    @Override
    public void goUrl(String url) {
        driver.get(url);
        try {
            Thread.sleep(5000);
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("window.scrollTo(0,document.body.scrollHeight)");
            List<WebElement> elements = driver.findElements(By.className(paginationItemBy));
            if (!elements.isEmpty()) {
                String text = elements.get(elements.size()-1).getText();
                String[] splitted = text.split(" ");
                while (splitted.length > 2 && !splitted[1].equals(splitted[3])){
                    List<WebElement> el = driver.findElements(By.className(buttonMoreBy));
                    if (!el.isEmpty()) {
                        el.get(0).click();
                    }
                    Thread.sleep(5000);
                    js.executeScript("window.scrollTo(0,document.body.scrollHeight)");
                    elements = driver.findElements(By.className(paginationItemBy));
                    splitted = elements.get(elements.size()-1).getText().split(" ");
                }
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        parseElements();
    }

    public List<MetroItem> getItemsList(){
        return webElements.stream()
                .map(e-> new MetroItem(getUrl(e), getName(e), getPrice(e), getOldPrice(e), getAvailability(e)))
                .toList();
    }

    @Override
    public String getName(WebElement e){
        return e.findElement(By.className(nameInCardBy))
                .getText();
    }

    @Override
    public Float getPrice(WebElement e) {
        return parseMainPrice(e.findElement(By.className(mainPriceInCardBy))
                .getText());
    }

    public Boolean getAvailability(WebElement e) {
        String available = "AVAILABLE";
        String unavailable = "UNAVAILABLE";
        WebElement el = e.findElement(By.className(isAvailableBy));
        if (!el.findElements(By.className(available)).isEmpty()) {
            return true;
        }else if (!el.findElements(By.className(unavailable)).isEmpty()) {
            return false;
        }
        return null;
    }
    private Float parseMainPrice(String price){
        return Float.valueOf(price.split(" ")[3].replaceAll(",", "."));
    }

    private Float parseOldPrice(String price){
        return Float.valueOf(price.split(" ")[0].replaceAll(",", "."));
    }

    private Float getOldPrice(WebElement e) {
        List<WebElement> elements = e.findElements(By.className(oldPriceInCardBy));
        return (!elements.isEmpty() ? parseOldPrice(elements.getFirst().getText()) : null);
    }
}
