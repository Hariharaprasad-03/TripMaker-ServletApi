package com.zsgs.busbooking.config;

import com.zsgs.busbooking.model.*;

public class BeanFactory {



    private static BeanFactory instance ;

    private BeanFactory(){

    }

    public static  BeanFactory getInstance(){

        if ( instance == null){
            instance = new BeanFactory();
        }
        return instance ;
    }

    public Passenger createPassanger(){
        return  new Passenger();
    }

    public Bus createBus(){
        return new Bus();
    }

    public Trip createTrip(){
        return new Trip();
    }

    public Route createRoute(){
        return new Route();
    }

}
