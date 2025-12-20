package com.zsgs.busbooking.services;

import com.zsgs.busbooking.config.BeanFactory;
import com.zsgs.busbooking.enums.BusStatus;
import com.zsgs.busbooking.enums.TripStatus;
import com.zsgs.busbooking.exception.InvalidCreadiantialsExcaption;
import com.zsgs.busbooking.exception.InvalidRequest;
import com.zsgs.busbooking.model.Bus;
import com.zsgs.busbooking.model.Route;
import com.zsgs.busbooking.model.Trip;
import com.zsgs.busbooking.payloads.CreateTripRequest;
import com.zsgs.busbooking.repositories.TripRepository;
import com.zsgs.busbooking.util.IdGenerator;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class TripService {

    private final TripRepository tripRepository ;
    private final BusService busService ;
    private final RouteService routeService ;
    private static  long id = 1;

    public TripService( TripRepository tripRepository ,RouteService routeService , BusService busService){
        this.tripRepository = tripRepository ;
        this.busService = busService ;
        this.routeService = routeService ;
    }

    public Trip createTrip(CreateTripRequest request)throws SQLException {

            if (    request.busId().isEmpty() ||
                    request.source().isEmpty() ||
                    request.destination().isEmpty() ||
                    request.tripDate()== null ||
                    request.startTime() == null ||
                    request.endTime() == null
            ) {

                throw  new InvalidCreadiantialsExcaption(" Bad Request");
            }


            if ( ! validateBus(request.busId() , request.busNumber(),request.tripDate(),request.startTime(),request.endTime())){

                throw new InvalidRequest("Check the Bus Crediantials");
            }

            String routeId = validateRoute(request.source(), request.destination());

            Trip trip = BeanFactory.getInstance().createTrip();
            trip.setTripId(new IdGenerator().genarateId("TRIP",id++));
            trip.setTripDate(request.tripDate());
            trip.setStartTime(request.startTime());
            trip.setEndTime(request.endTime());
            trip.setBusId(request.busId());
            trip.setStaus(TripStatus.ACTIVE);
            trip.setRouteId(routeId);

            if (tripRepository.createTrip(trip)) {

                return trip;
            }
            else  {

                throw  new InvalidRequest(" Trip creation Failed");
            }

    }

    public boolean validateBus(String busId ,String busNumber ,LocalDate date , LocalTime start ,LocalTime end)throws SQLException{

        Bus bus = busService.getBusByNumber(busNumber);


        if (bus == null || bus.getBusStatus()!= BusStatus.FREE){
            throw new InvalidRequest(" verify the Bus Creadiantials");
        }

        List<Trip> trips = tripRepository.getBusTripsByDate(busId, date);

        for ( Trip trip : trips){

            if (trip.getStartTime().isBefore(start) && trip.getEndTime().isAfter(start) ||
                trip.getStartTime().isAfter(start) && trip.getEndTime().isBefore(end)
                ){
                    return false;
            }

        }
        return true ;

    }

    public String  validateRoute(String source , String destination) throws SQLException{

        String  routeId  = routeService.getRouteIdBySourceAndDestination(source, destination);

        if ( routeId == null ){

            throw  new InvalidRequest(" Service Not Availale for the this Route ");

        }
        return routeId;

    }

    public boolean cancelTrip(String tripId) throws SQLException{

        return tripRepository.cancelTrip(tripId);

    }

    public boolean endTrip(String tripId ) throws SQLException {

        return tripRepository.finishTrip(tripId);
    }
}
