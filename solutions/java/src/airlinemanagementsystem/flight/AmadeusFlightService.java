package airlinemanagementsystem.flight;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AmadeusFlightService implements FlightSearchService {
    // Sandbox API URL
    private static final String API_URL = "https://test.api.amadeus.com/v2/shopping/flight-offers";
    private final String apiKey;
    private final HttpClient httpClient;
    private final Gson gson;

    public AmadeusFlightService() {
        // In reality, Amadeus requires a POST to /v1/security/oauth2/token to get a
        // Bearer token.
        // For this demo, we assume the user provides a valid access token.
        this.apiKey = System.getenv("AMADEUS_API_TOKEN");
        this.httpClient = HttpClient.newHttpClient();
        this.gson = new Gson();
    }

    @Override
    public List<Flight> searchFlights(String source, String destination, LocalDate date) {
        System.out.println("Searching flights via Amadeus API...");

        try {
            // Build Query:
            // ?originLocationCode=SYD&destinationLocationCode=BKK&departureDate=2024-05-01&adults=1
            String queryString = String.format(
                    "?originLocationCode=%s&destinationLocationCode=%s&departureDate=%s&adults=1",
                    source, destination, date.toString());

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_URL + queryString))
                    .header("Authorization", "Bearer " + apiKey)
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return parseFlights(response.body(), source, destination, date);
            } else {
                System.err.println("Amadeus API error: " + response.statusCode());
                System.err.println("Body: " + response.body());
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        // Return empty list on failure or no results
        return new ArrayList<>();
    }

    private List<Flight> parseFlights(String jsonBody, String source, String destination, LocalDate date) {
        List<Flight> flights = new ArrayList<>();
        try {
            JsonObject root = gson.fromJson(jsonBody, JsonObject.class);
            if (root.has("data")) {
                JsonArray data = root.getAsJsonArray("data");
                for (JsonElement element : data) {
                    flights.add(new Flight("AMD" + System.nanoTime(), source, destination, date.atStartOfDay(), date.atStartOfDay().plusHours(2)));
                }
            }
        } catch (Exception e) {
            System.err.println("Error parsing Amadeus JSON: " + e.getMessage());
        }
        return flights;
    }
}
