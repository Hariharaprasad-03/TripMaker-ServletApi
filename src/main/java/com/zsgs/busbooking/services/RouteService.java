package com.zsgs.busbooking.services;

import com.zsgs.busbooking.exception.DuplicateEntityException;
import com.zsgs.busbooking.exception.InvalidRequest;
import com.zsgs.busbooking.model.Route;
import com.zsgs.busbooking.repositories.RouteRepository;
import com.zsgs.busbooking.util.IdGenerator;

import java.sql.SQLException;

public class RouteService {

    private RouteRepository routeRepository ;
    private static long id = 1 ;

    public RouteService(RouteRepository routeRepository){
        this.routeRepository = routeRepository ;
    }

    public Route addRouteService ( Route route ) throws SQLException {


        if ( route.getSource().isEmpty() ){

            throw new InvalidRequest("Bad Request  souce cannot be Null");
        }

        if (route.getDestination().isEmpty()){
            throw new InvalidRequest(" bad Request destionation cannot be Null");
        }

        if ( route.getDistanceKm()<40){

        }

        route.setSource(route.getSource().toLowerCase().trim());
        route.setDestination(route.getDestination().toLowerCase().trim());

        String exist = routeRepository.findRouteIdBySourceAndDestination(route.getSource(),route.getDestination());

        if (exist != null) {

            throw  new DuplicateEntityException(" Already route is available ");
        }

        route.setRouteId( new IdGenerator().genarateId("ROUTE",id++));

        if (routeRepository.addRoute(route)){
            return route;
        }
        return null;
    }

    public Route getRouteById(String routeId)throws SQLException{

        return routeRepository.findRouteById(routeId);

    }
    public String getRouteIdBySourceAndDestination( String source , String destionation) throws SQLException {

        return  routeRepository.findRouteIdBySourceAndDestination(source,destionation);
    }


}
