package org.anton.items;

public class MetroItem extends BaseItem{
    private Boolean isAvailable;

    public MetroItem(String url, String name, Float price, Float oldPrice, Boolean isAvailable) {
        super(url, name, price, oldPrice);
        this.isAvailable = isAvailable;
    }

    public Boolean isAvailable() {
        return isAvailable;
    }

    @Override
    public String toString() {
        return "MetroItem {" +
                " url='" + url + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", oldPrice=" + oldPrice +
                " }";
    }
}
