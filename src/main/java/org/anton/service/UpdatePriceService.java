package org.anton.service;

import org.anton.Brands;
import org.anton.Units;
import org.anton.items.BaseItem;
import org.anton.items.MetroItem;
import org.anton.items.SilpoItem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;

public class UpdatePriceService {
    private static final Logger log = LogManager.getLogger(UpdatePriceService.class);
    private final String SQL_INSERT = """
            INSERT INTO product_price(shop_id, product_id, price, old_price, updated_at, is_available)
            SELECT ?, ?, ?, ?, ?, ?
            WHERE
            NOT EXISTS (
            SELECT product_id, shop_id FROM product_price WHERE product_id = ? and shop_id = ?);
            """;

    private final String SQL_UPDATE = """
            UPDATE product_price SET price = ?, old_price = ?, updated_at = ?, is_available = ?  WHERE product_id = ? and shop_id = ?;
            """;

    private final String SQL_FIND_PRODUCT_ID = """
            SELECT general_product_id FROM shop_general_products
            WHERE shop_product_id = (SELECT id from shop_products where url = ?);
            """;

    private final String SQL_SELECT_ALL = "select * from shop_products";

    private final String SQL_CHECK_UNITS = """
            select units.name from units where id =
            (select units from products where id = ?);
            """;

    private final Connection conn;

    public UpdatePriceService(Connection conn) {
        this.conn = conn;
    }

    public ResultSet selectAll() throws SQLException {
        PreparedStatement selectAll = conn.prepareStatement(SQL_SELECT_ALL);
        return selectAll.executeQuery();
    }

    public void insert(BaseItem item, Brands brand) throws SQLException {
        Integer shop_id;

        switch (brand) {
            case ATB -> shop_id = 1;
            case METRO -> shop_id = 2;
            case SILPO -> shop_id = 3;
            default -> throw new SQLException("Brand " + brand + " not supported.");
        }
        Integer product_id = findProductId(item.getUrl());
        Float price = item.getPrice();
        Float old_price = item.getOldPrice();
        if(item instanceof SilpoItem) {
            String unitsString = getUnits(product_id);
            if(unitsString != null) {
                Units units = Units.valueOf(unitsString);
                if(units.equals(Units.KILOGRAMS) && ((SilpoItem) item).getUnits().equals(Units.GRAMS)){
                    if(price != null) {
                        price*=10;
                    }
                    if(old_price != null) {
                        old_price*=10;
                    }
                }
            }
        }
        LocalDateTime updated_at_local = LocalDateTime.now();
        Boolean is_available;
        if(item instanceof MetroItem) {
            is_available = ((MetroItem) item).isAvailable();
            is_available = is_available != null && is_available;
        }else {
            is_available = price != null;
        }
        int resUpdate = exUpdate(shop_id, product_id, price, old_price, updated_at_local, is_available);
        if(resUpdate == 0) {
            int res = exInsert(shop_id, product_id, price, old_price, updated_at_local, is_available);
            if(res == 0) {
                throw new SQLException("Insert failed. Not found product in shop_general_products.");
            }
        }
    }

    private int exInsert(Integer shop_id, Integer product_id, Float price, Float old_price, LocalDateTime updated_at_local, Boolean is_available) throws SQLException {
        PreparedStatement insert = conn.prepareStatement(SQL_INSERT);
        insert.setInt(1, shop_id);
        insert.setInt(8, shop_id);
        insert.setInt(2, product_id);
        insert.setInt(7, product_id);
        if(price != null) {
            insert.setFloat(3, price);
        }else {
            insert.setObject(3, price);
        }
        if (old_price != null){
            insert.setFloat(4, old_price);
        }else {
            insert.setObject(4, old_price);
        }
        insert.setTimestamp(5, Timestamp.valueOf(updated_at_local));
        insert.setBoolean(6, is_available);
        return insert.executeUpdate();
    }

    private int exUpdate(Integer shop_id, Integer product_id, Float price, Float old_price, LocalDateTime updated_at_local, Boolean is_available) throws SQLException {
        PreparedStatement update = conn.prepareStatement(SQL_UPDATE);
        if (price != null) {
            update.setFloat(1, price);
        }else {
            update.setObject(1, price);
        }

        if (old_price != null) {
            update.setFloat(2, old_price);
        }else {
            update.setObject(2, old_price);
        }
        update.setTimestamp(3, Timestamp.valueOf(updated_at_local));
        update.setBoolean(4, is_available);
        update.setInt(5, product_id);
        update.setInt(6, shop_id);
        return update.executeUpdate();
    }

    public void insert(List<? extends BaseItem> items, Brands brand) {
       items.forEach(item -> {
           try {
               insert(item, brand);
           } catch (SQLException e) {
               log.error("Can't find product id for " + item.getUrl());
               log.error(e.getMessage());
           }
       });
    }

    private String getUnits(Integer product_id) throws SQLException {
        PreparedStatement update = conn.prepareStatement(SQL_CHECK_UNITS);
        update.setInt(1, product_id);
        ResultSet rs = update.executeQuery();
        if(rs.next()) {
            return rs.getString(1);
        }
        return null;
    }

    private Integer findProductId(String url) throws SQLException {
        PreparedStatement select = conn.prepareStatement(SQL_FIND_PRODUCT_ID);
        select.setString(1, url);
        ResultSet rs = select.executeQuery();
        rs.next();
        return rs.getInt("general_product_id");
    }
}
