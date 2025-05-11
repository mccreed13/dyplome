package org.anton.service;

import org.anton.Brands;
import org.anton.items.ATBItem;
import org.anton.items.BaseItem;
import org.anton.items.MetroItem;
import org.anton.items.SilpoItem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ShopProductService {
    private final String SQL_INSERT = """
        INSERT INTO shop_products(name, shopbrand, url)
        SELECT ?, ?, ?
        WHERE
        NOT EXISTS (
        SELECT url FROM shop_products WHERE url = ?);
        """;

    private final String SQL_SELECT_ALL = "select * from shop_products";

    private final Connection conn;

    public ShopProductService(Connection conn) {
        this.conn = conn;
    }

    public ResultSet selectAll() throws SQLException {
        PreparedStatement selectAll = conn.prepareStatement(SQL_SELECT_ALL);
        return selectAll.executeQuery();
    }

    public boolean insert(BaseItem item) throws SQLException {
        PreparedStatement insert = conn.prepareStatement(SQL_INSERT);
        insert.setString(1, item.getName());
        if(item.getClass().equals(MetroItem.class)) {
            insert.setObject(2, Brands.METRO,java.sql.Types.OTHER);
        }
        else if(item.getClass().equals(ATBItem.class)) {
            insert.setObject(2, Brands.ATB,java.sql.Types.OTHER);
        }else if(item.getClass().equals(SilpoItem.class)) {
            insert.setObject(2, Brands.SILPO,java.sql.Types.OTHER);
        }else {
            throw new SQLException("Invalid item class: " + item.getClass());
        }
        String url = item.getUrl();
        insert.setString(3, url);
        insert.setString(4, url);
        return insert.execute();
    }

}
