package onlinestockbrokeragesystem;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.io.IOException;

public class AlphaVantageStockPriceService implements StockPriceService {
    private static final String API_URL = "https://www.alphavantage.co/query";
    private final String apiKey;
    private final HttpClient httpClient;
    private final Gson gson;

    public AlphaVantageStockPriceService() {
        this.apiKey = System.getenv("ALPHA_VANTAGE_API_KEY");
        if (this.apiKey == null || this.apiKey.isEmpty()) {
            System.err.println("WARNING: ALPHA_VANTAGE_API_KEY environment variable not found.");
        }
        this.httpClient = HttpClient.newHttpClient();
        this.gson = new Gson();
    }

    @Override
    public double getPrice(String symbol) {
        if (apiKey == null) {
            // Fallback for demo if no key
            return 100.0 + Math.random() * 50;
        }

        try {
            // Construct URL: ?function=GLOBAL_QUOTE&symbol=IBM&apikey=demo
            String uriString = String.format("%s?function=GLOBAL_QUOTE&symbol=%s&apikey=%s", API_URL, symbol, apiKey);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(uriString))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return parsePrice(response.body());
            } else {
                System.err.println("Error fetching price: " + response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return 0.0;
    }

    private double parsePrice(String jsonBody) {
        try {
            JsonObject jsonObject = gson.fromJson(jsonBody, JsonObject.class);
            if (jsonObject.has("Global Quote")) {
                JsonObject quote = jsonObject.getAsJsonObject("Global Quote");
                if (quote.has("05. price")) {
                    return quote.get("05. price").getAsDouble();
                }
            }
            // Handle API limit or errors
            // System.out.println("Debug API response: " + jsonBody);=
        } catch (Exception e) {
            System.err.println("Error parsing JSON: " + e.getMessage());
        }
        return 0.0;
    }
}
