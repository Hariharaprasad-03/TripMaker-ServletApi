package com.zsgs.busbooking.repositories;

import com.zsgs.busbooking.config.BeanFactory;
import com.zsgs.busbooking.enums.BusStatus;
import com.zsgs.busbooking.enums.BusType;
import com.zsgs.busbooking.model.Bus;
import com.zsgs.busbooking.model.Seat;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class BusRepository extends BaseRepository{

    public boolean addBus(Bus bus) throws SQLException {

        String sql = "INSERT INTO bus (bus_id , bus_name ,bus_number ,bus_type ,bus_registration_id ,status,current_trip_id) values( ?,?,?,?,?,?,?)";

        try ( Connection connection = getConnection() ;
              PreparedStatement pstmt = connection.prepareStatement(sql)){

            pstmt.setString(1,bus.getBusId());
            pstmt.setString(2,bus.getBusName());
            pstmt.setString(4,bus.getBusType().toString());
            pstmt.setString(5, bus.getBusRegistrationId());
            pstmt.setString(3,bus.getBusNumber());
            pstmt.setString(6,bus.getBusStatus().toString());



            pstmt.setString(7,bus.getCurrentTripId());

             int row = pstmt.executeUpdate();

            return row > 0 ;
        }
    }

    public  Bus findBusById(String id) throws SQLException{

        String sql = "SELECT * FROM bus WHERE bus_id = (?)" ;


        Connection connection = getConnection();
        PreparedStatement pstmt = getConnection().prepareStatement(sql);
        try{

            pstmt.setString(1,id);

            ResultSet rs = pstmt.executeQuery();

            if (! rs.next()){
                return null;
            }

            Bus bus = BeanFactory.getInstance().createBus();
            bus.setBusId(rs.getString("bus_id"));
            bus.setBusName(rs.getString("bus_name"));
            bus.setBusType(BusType.valueOf(rs.getString("bus_type")));
            bus.setBusRegistrationId(rs.getString("bus_registration_id"));
            bus.setBusNumber(rs.getString("bus_number"));
            bus.setBusStatus(BusStatus.valueOf(rs.getString("status")));


            return bus ;
        }finally {
            closeResourses(connection,pstmt);
        }
    }

    public Bus findBusByNumber(String busNumber) throws SQLException {

        String sql = "SELECT * FROM bus WHERE bus_number = ?";

        try (Connection connection = getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setString(1, busNumber);

            try (ResultSet rs = pstmt.executeQuery()) {

                if (!rs.next()) {

                    return null;

                }

                Bus bus = BeanFactory.getInstance().createBus();

                bus.setBusId(rs.getString("bus_id"));
                bus.setBusName(rs.getString("bus_name"));
                bus.setBusType(BusType.valueOf(rs.getString("bus_type")));
                bus.setBusRegistrationId(rs.getString("bus_registration_id"));
                bus.setBusNumber(rs.getString("bus_number"));
                bus.setBusStatus(BusStatus.valueOf(rs.getString("status")));

                return bus;
            }
        }
    }


    public boolean removeBusbyId(String id ) throws SQLException{

        String sql = "UPDATE bus SET status = (?) WHERE bus_id = (?)";

        try(Connection connection = getConnection();
        PreparedStatement pstmt = connection.prepareStatement(sql)){

            pstmt.setString(1,BusStatus.DISABLED.toString());
            pstmt.setString(1,id);
            int row = pstmt.executeUpdate();
            return row > 0 ;
        }
    }

    public BusStatus getBusStatus( String busId) throws SQLException {

        String sql = "SELECT status FROM bus WHERE bus_id = (?)";
        BusStatus status =null;

        try( Connection connection = getConnection() ;
        PreparedStatement pstmt = connection.prepareStatement(sql)){

            pstmt.setString(1,busId);

            try(ResultSet rs = pstmt.executeQuery()){

                if ( rs.next()) {
                    status =BusStatus.valueOf(rs.getString("status"));
                }
            }

            return status;

        }



    }

    public boolean saveSeats(Seat seat) throws SQLException {

        String sql = "INSERT INTO seat (seat_number, bus_id, row_num, col_num, seat_type)VALUES (?, ?, ?, ?, ?)";

        try( Connection connection = getConnection();
        PreparedStatement pstmt = connection.prepareStatement(sql)){

            pstmt.setInt(1,seat.getSeatNumber());
            pstmt.setString(2,seat.getBusId());
            pstmt.setInt(3,seat.getRowNumber());
            pstmt.setInt(4,seat.getColNumber());
            pstmt.setString(5,String.valueOf(seat.getSeatType()));

            int row = pstmt.executeUpdate();

            return row>0;
        }

    }

    public boolean updateBusStatus(String bus_id , BusStatus status) throws SQLException{

        String sql = """
                UPDATE bus 
                SET  status = (?)
                WHERE bus_id = (?)
                """;
        try( Connection connection = getConnection() ;
        PreparedStatement pstmt = connection.prepareStatement(sql)){

            pstmt.setString(1,status.toString());
            pstmt.setString(2,bus_id);

            int row = pstmt.executeUpdate();

            return row >0;
        }

    }

    public void updateBusCurrentTrip(String busId , String tripId)throws SQLException{

        String sql = "UPDATE bus SET current_trip_id = (?) WHERE bus_id =(?) ";

        try(Connection connection = getConnection();
        PreparedStatement pstmt = connection.prepareStatement(sql)){

            pstmt.setString(1,tripId);
            pstmt.setString(2,busId);


            pstmt.executeUpdate();

        }
    }

    public List<Bus> getAllBuses() throws SQLException {

        String sql = """
        SELECT bus_id, bus_name, bus_number, bus_type,
               bus_registration_id, status, current_trip_id
        FROM bus
    """;

        List<Bus> buses = new ArrayList<>();

        try (Connection connection = getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {

                Bus bus = new Bus();

                bus.setBusId(rs.getString("bus_id"));
                bus.setBusName(rs.getString("bus_name"));
                bus.setBusNumber(rs.getString("bus_number"));
                bus.setBusType(BusType.valueOf(rs.getString("bus_type")));
                bus.setBusRegistrationId(rs.getString("bus_registration_id"));
                bus.setBusStatus(BusStatus.valueOf(rs.getString("status")));
                bus.setCurrentTripId(rs.getString("current_trip_id"));

                buses.add(bus);
            }
        }

        return buses;
    }






}
