package com.zsgs.busbooking.services;

import com.zsgs.busbooking.config.BeanFactory;
import com.zsgs.busbooking.enums.BusStatus;
import com.zsgs.busbooking.enums.BusType;
import com.zsgs.busbooking.enums.SeatType;
import com.zsgs.busbooking.exception.DuplicateEntityException;
import com.zsgs.busbooking.model.Bus;
import com.zsgs.busbooking.model.Seat;
import com.zsgs.busbooking.payloads.AddBusRequest;
import com.zsgs.busbooking.repositories.BusRepository;
import com.zsgs.busbooking.util.IdGenerator;

import java.sql.Connection;
import java.sql.SQLException;

public class BusService {


    private final BusRepository busRepository;
    private  long id = 1;

    public BusService (BusRepository repository){
        this.busRepository = repository;
    }

    public Bus addBus(AddBusRequest request)throws SQLException {

        Bus exist = busRepository.findBusByNumber(request.getBusNumber());

        if (exist != null){

            throw  new DuplicateEntityException("Bus Already Exist with Number ");
        }
        Bus newBus = BeanFactory.getInstance().createBus();

        newBus.setBusId( new IdGenerator().genarateId("BUS",id++));
        newBus.setBusStatus(BusStatus.FREE);
        newBus.setBusNumber(request.getBusNumber());
        newBus.setBusRegistrationId(request.getBusRegistrationId());
        newBus.setBusName(request.getBusName());
        newBus.setBusType(BusType.valueOf(request.getBusType()));


        if(busRepository.addBus(newBus) ) {
            renderSeats(newBus.getBusId());
            return newBus;
        }

        return null ;
    }

    public Bus getBusById(String id) throws SQLException{
        return busRepository.findBusById(id);
    }

    public BusStatus getBusStatusById(String busId) throws SQLException {

        return busRepository.getBusStatus(busId);
    }

    public Bus getBusByNumber(String busNumber) throws SQLException{

        return busRepository.findBusByNumber(busNumber);

    }

    public boolean setBusStatus( String bus_id ,BusStatus status )throws SQLException{

        return busRepository.updateBusStatus(bus_id,status);
    }

    public void renderSeats(String busId)throws SQLException{

        int seatNumber = 1;

        for (int row = 1; row <= 9; row++) {

            for (int col = 1; col <= 5; col++) {

                Seat seat = new Seat();

                    // or auto-gen if DB handles it
                seat.setBusId(busId);
                seat.setSeatNumber(seatNumber++);
                seat.setRowNumber(row);
                seat.setColNumber(col);


                if (col == 1 || col == 4) {
                    seat.setSeatType(SeatType.WINDOW);
                } else {
                    seat.setSeatType(SeatType.NORMAL);
                }

                busRepository.saveSeats(seat);


            }
        }

    }


}
