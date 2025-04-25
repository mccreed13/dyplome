package org.anton;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class MetroRequester {


    public MetroRequester() {
        try {
            requestBody = getRequestBody();
        } catch (IOException e) {
            throw new RuntimeException("Request failed ", e);
        }
    }

    private String requestBody;

    private final String URL = "https://shop.metro.ua/shop/category/%D0%BF%D1%80%D0%BE%D0%B4%D1%83%D0%BA%D1%82%D0%B8/%D1%85%D0%BE%D0%BB%D0%BE%D0%B4%D0%BD%D1%96-%D0%BD%D0%B0%D0%BF%D0%BE%D1%97/%D1%81%D0%BE%D0%BB%D0%BE%D0%B4%D0%BA%D1%96-%D0%BD%D0%B0%D0%BF%D0%BE%D1%97";

    private String getRequestBody() throws IOException {
        Request request = new Request
                .Builder()
                .url(URL)
                .get()
                .build();

        OkHttpClient client = new OkHttpClient();
        OkHttpClient httpClient = client.newBuilder().build();
        Response response = httpClient.newCall(request).execute();
        ResponseBody body = response.body();
        Document document = Jsoup.connect(URL).get();
        return document.body().text();
    }

    private Elements getCatalogItems() throws IOException {
        Document doc = Jsoup.parse(requestBody);
        return doc.getElementsByClass("sd-articlecard");
    }

    public void printCatalogItems() throws IOException {
        getCatalogItems()
                .forEach(c-> {
                    System.out.println(getName(c));
                    System.out.println("Price:" + getPrice(c));
                    System.out.println("Old price:" + getOldPrice(c));
                });
    }

    private static String getName(Element e){
        return e.getElementsByClass("title-wrapper")
                .get(0)
                .text();
    }

    private static String getPrice(Element e) {
        return e.getElementsByClass("primary")
                .get(0)
                .val();
    }

    private static String getOldPrice(Element e) {
        Elements elements = e.getElementsByClass("strike-through");
        return (!elements.isEmpty() ? elements.get(0).val() : null);
    }
}
