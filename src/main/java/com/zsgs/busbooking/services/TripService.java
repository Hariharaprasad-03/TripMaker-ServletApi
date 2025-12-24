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
import com.zsgs.busbooking.payloads.TripDto;
import com.zsgs.busbooking.payloads.UpdatetripRequest;
import com.zsgs.busbooking.repositories.TripRepository;
import com.zsgs.busbooking.stratergy.PricingStratergy;
import com.zsgs.busbooking.stratergy.PricingStratergyFactory;
import com.zsgs.busbooking.util.IdGenerator;
import com.zsgs.busbooking.util.IdGeneratorUtil;

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

    public Trip createTrip(CreateTripRequest request) throws SQLException {

        if (request == null) {
            throw new InvalidRequest("Request cannot be null");
        }

        if (request.source() == null || request.source().trim().isEmpty()
                || request.destination() == null || request.destination().trim().isEmpty()
                || request.tripDate() == null
                || request.startTime() == null
                || request.endTime() == null) {

            throw new InvalidRequest("Bad request: missing fields");
        }

        // Normalize
        String busNumber   = request.busNumber().trim();
        String source      = request.source().trim().toLowerCase();
        String destination = request.destination().trim().toLowerCase();

        Bus bus = busService.getBusByNumber(busNumber);

        // Date validation
        if (request.tripDate().isBefore(LocalDate.now())) {
            throw new InvalidRequest("Trip date cannot be in the past");
        }

        // Time validation
        if (!request.startTime().isBefore(request.endTime())) {
            throw new InvalidRequest("Start time must be before end time");
        }

        // Bus validation
        if (!validateBus(busNumber, request.tripDate(),
                request.startTime(), request.endTime())) {

            throw new InvalidRequest("Bus is not available for this schedule");
        }


        String routeId = validateRoute(source, destination);
        if (routeId == null) {
            throw new InvalidRequest("Route not found for given source & destination");
        }

        String busId = busService.getBusByNumber(busNumber).getBusId();
        int distance = routeService.getRouteById(routeId).getDistanceKm();



        Trip trip = BeanFactory.getInstance().createTrip();
        trip.setTripId(new IdGeneratorUtil().generateId("TRIP"));
        trip.setTripDate(request.tripDate());
        trip.setStartTime(request.startTime());
        trip.setEndTime(request.endTime());
        trip.setBusId(busId);
        trip.setStaus(TripStatus.ACTIVE);
        trip.setRouteId(routeId);

        PricingStratergy pricingStratergy =  new PricingStratergyFactory().getPricingStratergy(bus.getBusType());
        double price = pricingStratergy.calculatePrice(distance);

        trip.setPrice(price);



        if (!tripRepository.createTrip(trip)) {
            throw new InvalidRequest("Trip creation failed");
        }


        tripRepository.renderTripSeats(trip.getTripId(), busId);
        busService.setBusCurrentTrip(busId,trip.getTripId());
        busService.setBusStatus(busId ,BusStatus.ACTIVE);

        return trip;
    }


    public boolean validateBus(String busNumber ,LocalDate date , LocalTime start ,LocalTime end)throws SQLException{

        Bus bus = busService.getBusByNumber(busNumber);


        if (bus == null || bus.getBusStatus()!= BusStatus.FREE){
            throw new InvalidRequest(" verify the Bus Creadiantials");
        }

        List<Trip> trips = tripRepository.getBusTripsByDate(bus.getBusId(), date);

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

    public List<TripDto> getAllTrips()throws SQLException{

        return tripRepository.getAllTrips();
    }

    public List<TripDto>getAllTripsBySourceAndDestinationWithDate(String source , String destination , LocalDate date)throws  SQLException{

        source = source.toLowerCase().trim();
        destination = destination.toLowerCase().trim();
       return tripRepository.findAllTripsBySourceDestinationAndTime(source,destination,date);

    }

    public Trip getTripById(String tripId)throws SQLException{

        return tripRepository.findTripById(tripId);


    }

    public List<TripDto> getCurrentTrips()throws SQLException{

        List<TripDto> tripDtos = tripRepository.getCurrentTrips();

        return tripDtos;
    }

    public void updateTrip (UpdatetripRequest request)throws SQLException {

        tripRepository.updateTrip(request);
    }

    public boolean updateTripStatus( String tripId , String status)throws SQLException {

        return tripRepository.updateTripStatus(tripId, status);
    }


}
