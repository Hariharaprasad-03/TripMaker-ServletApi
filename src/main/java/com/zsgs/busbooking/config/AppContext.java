package com.zsgs.busbooking.config;

import com.zsgs.busbooking.repositories.*;
import com.zsgs.busbooking.services.*;

public class AppContext {

    private PassengerService passengerService ;
    private BusService busService ;
    private RouteService routeService ;
    private TripService tripService ;
    private BookingServices bookingServices ;

    private static AppContext appContext ;

    private AppContext () {
        passengerService = new PassengerService(new PassengerRepository());
        busService = new BusService( new BusRepository());
        routeService = new RouteService(new RouteRepository());
        tripService = new TripService(new TripRepository() ,routeService,busService);
        bookingServices = new BookingServices(passengerService,busService,tripService);

    }

    public static AppContext getInstance() {
        if(appContext == null)
            appContext = new AppContext();
        return  appContext;
    }

    public PassengerService getPassengerService() {
        return passengerService;
    }

    public BusService getBusService() {
        return busService;
    }

    public RouteService getRouteService() {
        return routeService;
    }

    public TripService getTripService() {
        return tripService;
    }

    public BookingServices getBookingServices() {
        return bookingServices;
    }
}
