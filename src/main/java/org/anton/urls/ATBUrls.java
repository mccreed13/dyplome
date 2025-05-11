package org.anton.urls;

import java.util.List;

public class ATBUrls extends BaseUrls{
    public static final String url_syhari_panirovochni = "https://www.atbmarket.com/catalog/suska-ta-suhari/f/pmin=13;pmax=14";
    public static final String url_kyrka = "https://www.atbmarket.com/catalog/344-m-yaso-okholodzhene";

    public static List<String> getAllUrls() {
        return List.of(
                url_syhari_panirovochni,
                url_kyrka
        );
    }
}
