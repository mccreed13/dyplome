package org.anton.urls;

import java.util.List;

public class MetroUrls extends BaseUrls{
    public static String url_kyrka = "https://shop.metro.ua/shop/category/%D0%BF%D1%80%D0%BE%D0%B4%D1%83%D0%BA%D1%82%D0%B8/%D0%BC%E2%80%99%D1%8F%D1%81%D0%BE-%D1%82%D0%B0-%D0%BF%D1%82%D0%B8%D1%86%D1%8F/%D1%81%D0%B2%D1%96%D0%B6%D0%B5-%D0%BC'%D1%8F%D1%81%D0%BE/%D0%BA%D1%83%D1%80%D1%8F%D1%82%D0%B8%D0%BD%D0%B0";
    public static String url_syhari_panirovochni = "https://shop.metro.ua/shop/category/%D0%BF%D1%80%D0%BE%D0%B4%D1%83%D0%BA%D1%82%D0%B8/%D0%B1%D0%B0%D0%BA%D0%B0%D0%BB%D1%96%D1%8F/%D0%BF%D0%B0%D0%BD%D1%96%D1%80%D1%83%D0%B2%D0%B0%D0%BB%D1%8C%D0%BD%D1%96-%D1%81%D1%83%D1%85%D0%B0%D1%80%D1%96";

    public static List<String> getAllUrls() {
        return List.of(
                url_kyrka,
                url_syhari_panirovochni
        );
    }
}
