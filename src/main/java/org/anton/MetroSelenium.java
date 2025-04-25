package org.anton;

import org.anton.items.MetroItem;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.List;

public class MetroSelenium extends SeleniumParser {
    private final String URL = "https://shop.metro.ua/shop/category/%D0%BF%D1%80%D0%BE%D0%B4%D1%83%D0%BA%D1%82%D0%B8/%D1%85%D0%BE%D0%BB%D0%BE%D0%B4%D0%BD%D1%96-%D0%BD%D0%B0%D0%BF%D0%BE%D1%97/%D1%81%D0%BE%D0%BB%D0%BE%D0%B4%D0%BA%D1%96-%D0%BD%D0%B0%D0%BF%D0%BE%D1%97";

    private final String paginationItemBy = "pull-right";
    private final String buttonMoreBy = "mfcss_load-more-articles";

    private final String cardElementBy = "sd-articlecard";

    private final String nameInCardBy = "title";
    private final String mainPriceInCardBy = "primary";
    private final String oldPriceInCardBy = "strike-through";

    private final String urlBy = "price";
    List<WebElement> items;

    public MetroSelenium() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("user-data-dir=D:\\TEST\\Profile 10");
        options.addArguments("--profile-directory=Profile 10");
        driver = new ChromeDriver(options);
        driver.get(URL);
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
        return (!elements.isEmpty() ? "https://shop.metro.ua"+elements.get(0).getDomAttribute("href") : null);
    }

    public List<MetroItem> getItemsList(){
        return webElements.stream()
                .map(e-> new MetroItem(getUrl(e), getName(e), getPrice(e), getOldPrice(e)))
                .toList();
    }

    @Override
    public String getName(WebElement e){
        return e.findElement(By.className(nameInCardBy))
                .getText();
    }

    @Override
    public String getPrice(WebElement e) {
        return parseMainPrice(e.findElement(By.className(mainPriceInCardBy))
                .getText());
    }

    private String parseMainPrice(String price){
        return price.split(" ")[3].replaceAll(",", ".");
    }

    private String parseOldPrice(String price){
        return price.split(" ")[0].replaceAll(",", ".");
    }

    private String getOldPrice(WebElement e) {
        List<WebElement> elements = e.findElements(By.className(oldPriceInCardBy));
        return (!elements.isEmpty() ? parseOldPrice(elements.get(0).getText()) : null);
    }
}
