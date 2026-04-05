package ridesharingservice_sde3;

public class RideSharingDemoSDE3 {
    public static void main(String[] args) {
        new LocationTrackingService(); // Boot async map GUI

        RideDispatcher dispatcher = new RideDispatcher();
        Ride ride = new Ride("RIDE_999");
        
        dispatcher.requestRide(ride);

        System.out.println("Driver accepts ride. Initiating assignment...");
        dispatcher.updateRideState("RIDE_999", RideStatus.REQUESTED, RideStatus.ASSIGNED);
    }
}
