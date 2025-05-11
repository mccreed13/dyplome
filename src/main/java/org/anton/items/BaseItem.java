package org.anton.items;

public abstract class BaseItem {
    protected String url;
    protected String name;
    protected Float price;
    protected Float oldPrice;

    public BaseItem(String url, String name, String price, String oldPrice) {
        this.url = url;
        this.name = name;
        this.price = price != null ? Float.valueOf(price) : null;
        this.oldPrice = oldPrice != null ? Float.valueOf(oldPrice) : null;
    }

    public BaseItem(String url, String name, Float price, Float oldPrice) {
        this.url = url;
        this.name = name;
        this.price = price;
        this.oldPrice = oldPrice;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price != null ? Float.valueOf(price) : null;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Float getOldPrice() {
        return oldPrice;
    }

    public void setOldPrice(String oldPrice) {
        this.oldPrice = oldPrice != null ? Float.valueOf(oldPrice) : null;
    }

    public void setOldPrice(Float oldPrice) {
        this.oldPrice = oldPrice;
    }
}
