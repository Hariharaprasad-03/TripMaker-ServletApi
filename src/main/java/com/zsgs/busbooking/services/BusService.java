package com.zsgs.busbooking.services;

import com.zsgs.busbooking.config.BeanFactory;
import com.zsgs.busbooking.enums.BusStatus;
import com.zsgs.busbooking.enums.BusType;
import com.zsgs.busbooking.enums.SeatType;
import com.zsgs.busbooking.exception.DuplicateEntityException;
import com.zsgs.busbooking.model.Bus;
import com.zsgs.busbooking.model.Seat;
import com.zsgs.busbooking.payloads.AddBusRequest;
import com.zsgs.busbooking.payloads.BusDto;
import com.zsgs.busbooking.payloads.TripDto;
import com.zsgs.busbooking.repositories.BusRepository;
import com.zsgs.busbooking.repositories.TripRepository;
import com.zsgs.busbooking.util.IdGenerator;

import java.sql.SQLException;
import java.util.List;

public class BusService {


    private final BusRepository busRepository;
    private  final TripRepository tripRepository = new TripRepository();
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
        newBus.setCurrentTripId(null);


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

    public void setBusCurrentTrip(String busId , String tripId)throws SQLException{

        busRepository.updateBusCurrentTrip(busId, tripId);
    }

    public List<BusDto> getAllBuses() throws SQLException {

        List<Bus> buses = busRepository.getAllBuses();

        return buses.stream()
                .map(bus -> {

                    BusDto dto = new BusDto();

                    dto.setBusId(bus.getBusId());
                    dto.setBusName(bus.getBusName());
                    dto.setBusNumber(bus.getBusNumber());
                    dto.setBusType(bus.getBusType());
                    dto.setBusRegistrationId(bus.getBusRegistrationId());
                    dto.setBusStatus(bus.getBusStatus());


                    if (bus.getCurrentTripId() != null) {


                        try {

                            TripDto tripDto = tripRepository.getTripDetailsById(bus.getCurrentTripId());
                            dto.setTripDto(tripDto);
                        }
                        catch (SQLException e){

                            e.printStackTrace();
                        }
                    }

                    return dto;
                })
                .toList();
    }



}
