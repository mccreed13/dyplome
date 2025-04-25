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

public class SilpoRequester {

    public SilpoRequester() {
        try {
            requestBody = getRequestBody();
            elements = getCatalogItems();
        } catch (IOException e) {
            throw new RuntimeException("Request failed ", e);
        }

    }

    private String requestBody;
    private Elements elements;
    private final String URL = "https://silpo.ua/category/frukty-ovochi-4788";

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
        return body.string();
    }

    private int getMaxPages(){
        Document doc = Jsoup.parse(requestBody);
        return Integer.parseInt(doc
                .getElementsByClass("pagination-item")
                .last()
                .val()

        );
    }

    public int getSize(){
        return elements.size();
    }

    private void sendRequest(int page) throws IOException {
        Request request = new Request
                .Builder()
                .url(URL+"?page="+page)
                .get()
                .build();
        OkHttpClient client = new OkHttpClient();
        OkHttpClient httpClient = client.newBuilder().build();
        Response response = httpClient.newCall(request).execute();
        ResponseBody body = response.body();
        requestBody = body.string();
    }

    private Elements getCatalogItems() throws IOException {
        Elements elements = new Elements();
        for (int i = 1; i <= getMaxPages(); i++) {
            sendRequest(i);
            Document doc = Jsoup.parse(requestBody);
            elements.addAll(doc
                    .getElementsByClass("products-list__item"));
        }

        return elements;
    }

    public void printCatalogItems() throws IOException {
        elements
                .forEach(c-> {
                    System.out.println(getName(c));
                    System.out.println("Price:" + getPrice(c));
                    System.out.println("Old price:" + getOldPrice(c));
                });
    }

    private static String getName(Element e){
        return e.getElementsByClass("product-card__title")
                .get(0)
                .text();
    }

    private static String getPrice(Element e) {
        Elements elements = e.getElementsByClass("product-card-price__displayPrice");
        return (!elements.isEmpty() ? elements.get(0).val() : null);

    }

    private static String getOldPrice(Element e) {
        Elements elements = e.getElementsByClass("product-card-price__displayOldPrice");
        return (!elements.isEmpty() ? elements.get(0).val() : null);
    }
}

