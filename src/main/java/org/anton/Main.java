package org.anton;

import com.google.gson.Gson;
import org.anton.items.MetroItem;

import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        Gson gson = new Gson();

//        ATBRequester requester = new ATBRequester();
//        requester.printCatalogItems();
//        int atb = requester.getSize();
//        System.out.println("ATB: " + atb);
//
        try( MetroSelenium selenium = new MetroSelenium() ){
            int metro = selenium.getSize();
            System.out.println("Metro: " + metro);
            List<MetroItem> items = selenium.getItemsList();
            System.out.println(items.get(0).toString());
            System.out.println(getBiggestSale(items));
            System.out.println(getSmallestValue(items));
//            String jsonItems = gson.toJson(items);
//            File file = new File("metroItemsDB.json");
//            JsonWriter jsonWriter = gson.newJsonWriter(new FileWriter(file));
//            jsonWriter.jsonValue(jsonItems);
        } catch (Exception ignored) {
            throw ignored;
        }
//        try (SilpoSelenium silpoSelenium = new SilpoSelenium()) {
//            silpoSelenium.printCatalogItems();
//        }
    }
    private static MetroItem getSmallestValue(List<MetroItem> items) {
        MetroItem smallest = items.get(0);
        float smallestPrice = Float.parseFloat(items.get(0).getPrice());
        for (MetroItem item : items) {
            float bufPrice = Float.parseFloat(item.getPrice());
            if (bufPrice < smallestPrice) {
                smallestPrice = bufPrice;
                smallest = item;
            }
        }
        return smallest;
    }

    private static MetroItem getBiggestSale(List<MetroItem> items) {
        MetroItem biggest = items.get(0);
        float sale = 0;
        float fPrice = Float.parseFloat(biggest.getPrice());
        String sPrice = biggest.getOldPrice();
        if (sPrice != null) {
            float sfPrice = Float.parseFloat(sPrice);
            sale = sfPrice - fPrice;
        }
        for (MetroItem item : items) {
            float sale2 = 0;
            float fPrice2 = Float.parseFloat(item.getPrice());
            String sPrice2 = item.getOldPrice();
            if (sPrice2 != null) {
                float sfPrice = Float.parseFloat(sPrice2);
                sale2 = sfPrice - fPrice2;
            }
            if (sale2 > sale) {
                biggest = item;
                sale = sale2;
            }
        }
        return biggest;
    }
}

