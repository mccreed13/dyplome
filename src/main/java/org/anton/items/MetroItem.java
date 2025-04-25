package org.anton.items;

public class MetroItem extends BaseItem{
    private final String oldPrice;

    public MetroItem(String url, String name, String price, String oldPrice) {
        super(url, name, price);
        this.oldPrice = oldPrice;
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

    public String getOldPrice() {
        return oldPrice;
    }
}
