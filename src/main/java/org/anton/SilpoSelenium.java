package org.anton;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.List;

public class SilpoSelenium extends SeleniumParser{
    private final String URL = "https://silpo.ua/category/syry-plavleni-5007";

    private final String paginationItemBy = "pagination-item";
    private final String buttonMoreBy = "//span[contains(text(),'Показати ще')]";

    private final String cardElementBy = "product-card";

    private final String nameInCardBy = "product-card__title";
    private final String mainPriceInCardBy = "product-card-price__displayPrice";
    private final String oldPriceInCardBy = "strike-through";
    private final String currentPaginationItem = "pagination-item--current";
    public SilpoSelenium() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("user-data-dir=D:\\TEST\\Profile 10");
        options.addArguments("--profile-directory=Profile 10");
        driver = new ChromeDriver(options);
        driver.get(URL);
        try {
            Thread.sleep(5000);
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("window.scrollTo(0,document.body.scrollHeight-1500)");
            List<WebElement> elements = driver.findElements(By.className(paginationItemBy));
            List<WebElement> el = driver.findElements(By.xpath(buttonMoreBy));
            if (!elements.isEmpty()) {
                String maxPage = elements.get(elements.size()-1).getText();
                if (!el.isEmpty()) {
                    el.get(0).click();
                }
                Thread.sleep(5000);
                String textUrl = driver.getCurrentUrl();
                System.out.println(maxPage);
                String currentPage = textUrl.substring(textUrl.lastIndexOf("=")+1);
                while (!maxPage.equals(currentPage)) {
                    textUrl = driver.getCurrentUrl();
                    currentPage = textUrl.substring(textUrl.lastIndexOf("=")+1);
                    js.executeScript("window.scrollTo(0,document.body.scrollHeight-1500)");
                    Thread.sleep(3000);
                }
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        parseElements();
    }

    @Override
    protected void parseElements() {
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
        return "";
    }

    @Override
    protected String getName(WebElement e) {
        List<WebElement> elements = e.findElements(By.className(nameInCardBy));
        return (!elements.isEmpty() ? elements.get(0).getText() : null);
    }

    @Override
    protected String getPrice(WebElement e) {
        List<WebElement> elements = e.findElements(By.className(mainPriceInCardBy));
        return (!elements.isEmpty() ? elements.get(0).getText() : null);
    }

    @Override
    public List<?> getItemsList() {
        return List.of();
    }

    public String getOldPrice(WebElement e) {
        List<WebElement> elements = e.findElements(By.className(oldPriceInCardBy));
        return (!elements.isEmpty() ? elements.get(0).getText() : null);
    }
}
