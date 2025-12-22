package com.zsgs.busbooking.services;

import com.zsgs.busbooking.config.BeanFactory;
import com.zsgs.busbooking.exception.DuplicateEntityException;
import com.zsgs.busbooking.exception.InvalidCreadiantialsExcaption;
import com.zsgs.busbooking.model.Passenger;
import com.zsgs.busbooking.payloads.PassengerSignUpRequest;
import com.zsgs.busbooking.repositories.PassengerRepository;
import com.zsgs.busbooking.util.CrediantialsValidator;
import com.zsgs.busbooking.util.IdGenerator;

import java.sql.SQLException;

public class PassengerService {

    private final PassengerRepository passengerRepository;
    private static long id = 1 ;

    public PassengerService(PassengerRepository passengerRepository){

        this.passengerRepository = passengerRepository;
    }

    public Passenger addPassenger(PassengerSignUpRequest request) throws SQLException {

        validateEmail(request.getEmail());
        validateMoblieNumber(request.getMobileNumber());

        Passenger passenger = BeanFactory.getInstance().createPassanger();

        passenger.setPassengerId(new IdGenerator().genarateId("PASG",++id));
        passenger.setPassengerName(request.getPassengerName());
        passenger.setPassword(request.getPassword());
        passenger.setMobileNumber(request.getMobileNumber());
        passenger.setEmail(request.getEmail());

        if (passengerRepository.addPassenger(passenger)){

            return passenger;
        } else {

            throw new  RuntimeException("Failed Request");
        }
    }




    public boolean validateEmail(String email) throws SQLException{

        if(  !new CrediantialsValidator().validateEmail(email)){

            throw new InvalidCreadiantialsExcaption(" invalid Email");
        }
        Passenger exist = passengerRepository.findPassenerByEmail(email);
        if ( exist != null){
            throw  new DuplicateEntityException(" Email id Already used");
        }
        return true;

    }
    public boolean validateMoblieNumber(String mobile) throws SQLException {

        if ( ! new CrediantialsValidator().validateMobileNumber(mobile)){
            throw new  InvalidCreadiantialsExcaption(" mobile Number is valid");
        }
        if(passengerRepository.ifMobileNumberAlreadyExist(mobile)) {
            throw new DuplicateEntityException(" Mobile Number Already Used");
        }
        return true;
    }

    public Passenger getPassengerById(String id)throws  SQLException{

        return passengerRepository.findPassengerById(id);
    }

    public Passenger getPassengerByMobileNumber(String mobileNumber)throws SQLException{

        return passengerRepository.findPassengerByMobileNumber(mobileNumber);
    }
}
