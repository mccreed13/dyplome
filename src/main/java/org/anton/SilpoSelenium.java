package org.anton;

import org.anton.items.SilpoItem;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.List;

public class SilpoSelenium extends SeleniumParser{
    private final String URL = "https://silpo.ua/category/m-iaso-4411";

    private final String paginationItemBy = "pagination-item";
    private final String buttonMoreBy = "//span[contains(text(),'Показати ще')]";

    private final String cardElementBy = "product-card";

    private final String nameInCardBy = "product-card__title";
    private final String productCardBodyBy = "product-card__body";
    private final String mainPriceInCardBy = "product-card-price__displayPrice";
    private final String oldPriceInCardBy = "strike-through";
    private final String currentPaginationItem = "pagination-item--current";
    private final String unitsDivBy = ".//div[@class='product-card__body']/div[last()]/div[1]";

    public SilpoSelenium(String url) {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("user-data-dir=D:\\TEST\\Profile 10");
        options.addArguments("--profile-directory=Profile 10");
        driver = new ChromeDriver(options);
        goUrl(url);
//        driver.get(url);
//        try {
//            Thread.sleep(5000);
//            JavascriptExecutor js = (JavascriptExecutor) driver;
//            js.executeScript("window.scrollTo(0,document.body.scrollHeight-1500)");
//            List<WebElement> elements = driver.findElements(By.className(paginationItemBy));
//            List<WebElement> el = driver.findElements(By.xpath(buttonMoreBy));
//            if (!elements.isEmpty()) {
//                String maxPage = elements.getLast().getText();
//                if (!el.isEmpty()) {
//                    el.getFirst().click();
//                }
//                Thread.sleep(5000);
//                String textUrl = driver.getCurrentUrl();
//                System.out.println(maxPage);
//                String currentPage = textUrl.substring(textUrl.lastIndexOf("=")+1);
//                while (!maxPage.equals(currentPage)) {
//                    textUrl = driver.getCurrentUrl();
//                    currentPage = textUrl.substring(textUrl.lastIndexOf("=")+1);
//                    js.executeScript("window.scrollTo(0,document.body.scrollHeight-1500)");
//                    Thread.sleep(3000);
//                }
//            }
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
//        parseElements();
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
        return (e!=null ? "https://silpo.ua"+e.getDomAttribute("href") : null);
    }

    @Override
    public void goUrl(String url) {
        driver.get(url);
        try {
            Thread.sleep(5000);
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("window.scrollTo(0,document.body.scrollHeight-1500)");
            List<WebElement> elements = driver.findElements(By.className(paginationItemBy));
            List<WebElement> el = driver.findElements(By.xpath(buttonMoreBy));
            if (!elements.isEmpty()) {
                String maxPage = elements.getLast().getText();
                if (!el.isEmpty()) {
                    el.getFirst().click();
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
    protected String getName(WebElement e) {
        List<WebElement> elements = e.findElements(By.className(nameInCardBy));
        return (!elements.isEmpty() ? elements.getFirst().getText() : null);
    }

    protected Float getPrice(WebElement e) {
        List<WebElement> elements = e.findElements(By.className(mainPriceInCardBy));
        return (!elements.isEmpty() ? parseMainPrice(elements.getFirst().getText()) : null);
    }

    private Float parseMainPrice(String price){
        return Float.valueOf(price.split(" ")[0].replaceAll(",", "."));
    }

    private Float parseOldPrice(String price){
        return Float.valueOf(price.split(" ")[0].replaceAll(",", "."));
    }

    @Override
    public List<SilpoItem> getItemsList() {
        return webElements.stream()
                .map(e-> new SilpoItem(getUrl(e), getName(e), getPrice(e), getOldPrice(e), getUnits(e)))
                .toList();
    }

    private Units getUnits(WebElement e) {
        WebElement el = e.findElement(By.xpath(unitsDivBy));
        Units res = null;
        String units = el.getText().replaceAll("[0-9]", "");
        if(!units.isEmpty()) {
            res = mapShopUnitsToEnum(units);
        }
        return res;
    }

    public Float getOldPrice(WebElement e) {
        List<WebElement> elements = e.findElements(By.className(oldPriceInCardBy));
        return (!elements.isEmpty() ? parseOldPrice(elements.getFirst().getText()) : null);
    }

    private Units mapShopUnitsToEnum(String units){
        return switch (units) {
            case "г" -> Units.GRAMS;
            case "кг" -> Units.KILOGRAMS;
            case "л" -> Units.LITERS;
            case "мл" -> Units.MILLILITERS;
            case "шт" -> Units.PIECES;
            default -> null;
        };
    }
}
