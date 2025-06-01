package org.anton;

import org.anton.items.ATBItem;
import org.anton.items.BaseItem;
import org.anton.items.MetroItem;
import org.anton.items.SilpoItem;
import org.anton.service.ShopProductService;
import org.anton.service.UpdatePriceService;
import org.anton.urls.ATBUrls;
import org.anton.urls.MetroUrls;
import org.anton.urls.SilpoUrls;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;


public class Main {
    private static final String jdbcUrl = "jdbc:postgresql://localhost:5432/productdb";
    private static final String username = "postgres";
    private static final String password = "123456789";

    private static final Connection connection;

    static {
        try {
            connection = DriverManager.getConnection(jdbcUrl, username, password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws IOException, SQLException, ClassNotFoundException {
        SilpoSelenium silpoSelenium = null;
        for(String url : SilpoUrls.getAllUrls()){
            if(silpoSelenium == null){
                silpoSelenium = new SilpoSelenium(url);
            }else {
                silpoSelenium.goUrl(url);
            }
            List<SilpoItem> itemsList = silpoSelenium.getItemsList();
            insertAllItems(itemsList);
            updateAllPrices(itemsList);
        }
        if (silpoSelenium != null) {
            silpoSelenium.close();
        }

        MetroSelenium metroSelenium = null;
        for(String url : MetroUrls.getAllUrls()){
            if(metroSelenium == null){
                metroSelenium = new MetroSelenium(url);
            }else {
                metroSelenium.goUrl(url);
            }
            List<MetroItem> itemsList = metroSelenium.getItemsList();
            insertAllItems(itemsList);
            updateAllPrices(itemsList);
        }
        if(metroSelenium != null){
            metroSelenium.close();
        }
        ATBRequester atbRequester = null;
        for(String url : ATBUrls.getAllUrls()){
            if(atbRequester == null){
                atbRequester = new ATBRequester(url);
            }else {
                atbRequester.goUrl(url);
            }
            List<ATBItem> itemsList = atbRequester.getItemsList();
            insertAllItems(itemsList);
            updateAllPrices(itemsList);
        }
        connection.close();
    }

    private static void writeCSV(String fileName, List<? extends BaseItem> list) throws IOException {
        String[] HEADERS = {"URL", "NAME", "PRICE"};
        FileWriter sw = new FileWriter(fileName);

        CSVFormat csvFormat = CSVFormat.DEFAULT.builder()
                .setHeader(HEADERS)
                .get();

        try (final CSVPrinter printer = new CSVPrinter(sw, csvFormat)) {
            list.forEach(el -> {
                try {
                    printer.printRecord(el.getUrl(), el.getName(), el.getPrice());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    private static void updateAllPricesATB(){
        ATBUrls.getAllUrls().forEach(url -> {
            ATBRequester requester = new ATBRequester(url);
            try {
                updateAllPrices(requester.getItemsList());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private static void updateAllPricesMetro(){
        MetroUrls.getAllUrls().forEach(url -> {
            MetroSelenium selenium = new MetroSelenium(url);
            try {
                updateAllPrices(selenium.getItemsList());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            selenium.close();
        });
    }

    private static void updateAllPricesSilpo(){
        SilpoUrls.getAllUrls().forEach(url -> {
            MetroSelenium selenium = new MetroSelenium(url);
            try {
                updateAllPrices(selenium.getItemsList());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            selenium.close();
        });
    }

    private static boolean insertAllItems(List<? extends BaseItem> list) {
        ShopProductService service = new ShopProductService(connection);
        list.forEach(e -> {
            try {
                service.insert(e);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });
        return true;
    }

    private static void updateAllPrices(List<? extends BaseItem> list) throws SQLException {
        UpdatePriceService service = new UpdatePriceService(connection);
        if(list.getFirst() instanceof ATBItem) {
            service.insert(list, Brands.ATB);
        }else if(list.getFirst() instanceof MetroItem) {
            service.insert(list, Brands.METRO);
        }else if(list.getFirst() instanceof SilpoItem) {
            service.insert(list, Brands.SILPO);
        }else {
            System.out.println("Unknown item brand");
        }

    }
}

