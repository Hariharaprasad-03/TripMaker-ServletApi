package com.zsgs.busbooking.config;

import com.zsgs.busbooking.repositories.BusRepository;
import com.zsgs.busbooking.repositories.PassengerRepository;
import com.zsgs.busbooking.services.BusService;
import com.zsgs.busbooking.services.PassengerService;

public class AppContext {

    private PassengerService passengerService ;
    private BusService busService ;

    private static AppContext appContext ;

    private AppContext () {
        passengerService = new PassengerService(new PassengerRepository());
        busService = new BusService( new BusRepository());
    }

    public static AppContext getInstance() {
        if(appContext == null)
            appContext = new AppContext();
        return  appContext;
    }

    public PassengerService getPassengerService() {
        return passengerService;
    }

    public void setPassengerService(PassengerService passengerService) {
        this.passengerService = passengerService;
    }

    public BusService getBusService() {
        return busService;
    }

    public void setBusService(BusService busService) {
        this.busService = busService;
    }
}
