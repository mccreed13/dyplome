package org.anton.items;

import org.anton.Units;

public class SilpoItem extends BaseItem{
    private final Units units;

    public SilpoItem(String url, String name, Float price, Float oldPrice, Units units) {
        super(url, name, price, oldPrice);
        this.units = units;
    }

    public Units getUnits() {
        return units;
    }

    @Override
    public String toString() {
        return "SilpoItem{" +
                "url='" + url + '\'' +
                ", name='" + name + '\'' +
                ", price='" + price + '\'' +
                ", oldPrice='" + oldPrice + '\'' +
                '}';
    }
}
