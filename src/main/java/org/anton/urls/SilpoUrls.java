package org.anton.urls;

import java.util.List;

public class SilpoUrls extends BaseUrls {
    public static final String url_kyrka = "https://silpo.ua/category/kuriatyna-4426";
    public static final String url_syhari_panirovochni = "https://silpo.ua/category/paniruvannia-4943";
    public static final String url_sil = "https://silpo.ua/category/sil-4901";
    public static final String url_specii = "https://silpo.ua/category/spetsii-4941";

    public static List<String> getAllUrls() {
        return List.of(
                url_kyrka,
                url_syhari_panirovochni,
                url_sil,
                url_specii
        );
    }
}
