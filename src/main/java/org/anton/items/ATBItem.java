package org.anton.items;

public class ATBItem extends BaseItem{

    public ATBItem(String url, String name, String price, String oldPrice) {
        super(url, name, price, oldPrice);
    }

    @Override
    public String toString() {
        return "ATBItem {" +
                " url='" + url + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", oldPrice=" + oldPrice +
                " }";
    }
}
