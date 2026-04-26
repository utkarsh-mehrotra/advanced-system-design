package airlinemanagementsystem_sde3.flight;

import java.time.LocalDate;
import java.util.List;

public interface FlightSearchService {
    List<Flight> searchFlights(String source, String destination, LocalDate date);
}
