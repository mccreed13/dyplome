package org.anton;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.anton.items.ATBItem;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.List;

public class ATBRequester {

    private String URL;

    public ATBRequester(String url) {
        goUrl(url);
    }

    private String requestBody;

    private Elements elements;

//    private final String URL = "https://www.atbmarket.com/catalog/294-napoi-bezalkogol-ni";


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

    public int getSize(){
        return elements.size();
    }

    private int getMaxPages(){
        Document doc = Jsoup.parse(requestBody);
        Elements el = doc.getElementsByClass("product-pagination__item");
        if(!el.isEmpty()){
            return Integer.parseInt(
                    el.get(el.size()-2)
                            .text()
            );
        }else return 1;
    }

    private void parseElements() throws IOException {
        Elements elements = new Elements();
        for (int i = 1; i <= getMaxPages(); i++) {
            sendRequest(i);
            Document doc = Jsoup.parse(requestBody);
            elements.addAll(doc
                    .getElementsByClass("catalog-item"));
        }
        this.elements = elements;
    }

    public void printCatalogItems() throws IOException {
        this.elements
                .forEach(c-> {
                    System.out.println(getName(c));
                    System.out.println("Price:" + getPrice(c));
                    System.out.println("Old price:" + getOldPrice(c));
                });
    }

    private static String getName(Element e){
        return e.getElementsByClass("catalog-item__title")
                .getFirst()
                .getElementsByTag("a")
                .getFirst()
                .text();
    }

    private static String getPrice(Element e) {
        return e.getElementsByClass("catalog-item__bottom")
                .getFirst()
                .getElementsByClass("product-price__top")
                .getFirst()
                .val();
    }

    private static String getOldPrice(Element e) {
        Elements elements = e.getElementsByClass("catalog-item__bottom")
                .getFirst()
                .getElementsByClass("product-price__bottom");
        return (!elements.isEmpty() ? elements.getFirst().val() : null);
    }

    public String getUrl(Element e) {
        return "https://www.atbmarket.com" + e.getElementsByClass("catalog-item__title")
                .getFirst()
                .getElementsByTag("a")
                .getFirst()
                .attr("href");
    }

    public List<ATBItem> getItemsList(){
        return elements.stream()
                .map(e-> new ATBItem(getUrl(e), getName(e), getPrice(e), getOldPrice(e)))
                .toList();
    }

    public void goUrl(String url){
        this.URL = url;
        try {
            requestBody = getRequestBody();
        } catch (IOException e) {
            throw new RuntimeException("Request failed ", e);
        }

        try {
            parseElements();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
