package com.zsgs.busbooking.services;

import com.zsgs.busbooking.exception.DuplicateEntityException;
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


}
