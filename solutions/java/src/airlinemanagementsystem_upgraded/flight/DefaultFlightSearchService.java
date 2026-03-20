package airlinemanagementsystem_upgraded.flight;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class DefaultFlightSearchService implements FlightSearchService {
    private final List<Flight> flights;

    public DefaultFlightSearchService(List<Flight> flights) {
        this.flights = flights;
    }

    @Override
    public List<Flight> searchFlights(String source, String destination, LocalDate date) {
        return flights.stream()
                .filter(flight -> flight.getSource().equalsIgnoreCase(source)
                        && flight.getDestination().equalsIgnoreCase(destination)
                        && flight.getDepartureTime().toLocalDate().equals(date))
                .collect(Collectors.toList());
    }
}
