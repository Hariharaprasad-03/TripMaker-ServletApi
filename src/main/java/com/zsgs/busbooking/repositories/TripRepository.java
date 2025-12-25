package com.zsgs.busbooking.repositories;

import com.zsgs.busbooking.config.BeanFactory;
import com.zsgs.busbooking.enums.BusType;
import com.zsgs.busbooking.enums.SeatStatus;
import com.zsgs.busbooking.enums.SeatType;
import com.zsgs.busbooking.enums.TripStatus;
import com.zsgs.busbooking.model.Trip;
import com.zsgs.busbooking.payloads.SeatDto;
import com.zsgs.busbooking.payloads.TripDto;
import com.zsgs.busbooking.payloads.UpdatetripRequest;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TripRepository  extends BaseRepository{

    String insert = "INSERT INTO trip (trip_id , route_id , bus_id ,start_time,end_time,status,date,price)values(?,?,?,?,?,?,?,?)";

    public boolean createTrip(Trip trip) throws SQLException {

        try(Connection connection = getConnection();
            PreparedStatement pstmt = connection.prepareStatement(insert)){

            pstmt.setString(1, trip.getTripId());
            pstmt.setString(2, trip.getRouteId());
            pstmt.setString(3, trip.getBusId());
            pstmt.setTime(4, Time.valueOf(trip.getStartTime()));
            pstmt.setTime(5,Time.valueOf(trip.getEndTime()));
            pstmt.setString(6, trip.getStaus().toString());
            pstmt.setDate(7, Date.valueOf(trip.getTripDate()));
            pstmt.setDouble(8,trip.getPrice());

            int row = pstmt.executeUpdate();
            return row > 0 ;
        }
    }

    public boolean cancelTrip(String tripId) throws SQLException{

        String sql = "UPDATE trip SET status = (?) where trip_id = (?)";

        try(Connection connection = getConnection();
        PreparedStatement stmt = connection.prepareStatement(sql)){

            stmt.setString(1, TripStatus.FINISHED.toString());
            stmt.setString(2,tripId);

            int row = stmt.executeUpdate();

            return row >0 ;

        }
    }

    public boolean finishTrip(String tripId) throws SQLException{

        String sql = "UPDATE trip SET status = (?) where trip_id = (?)";

        try(Connection connection = getConnection();
        PreparedStatement stmt = connection.prepareStatement(sql)){

            stmt.setString(1, TripStatus.CANCELLED.toString());
            stmt.setString(2,tripId);

            int row = stmt.executeUpdate();

            return row >0 ;

        }
    }

    public List<Trip> getBusTripsByDate(String busId , LocalDate date) throws SQLException {

        List<Trip> trips = new ArrayList<>();

        String sql = "SELECT * FROM trip WHERE date =(?) AND bus_id = (?)";

        try( Connection connection = getConnection();
        PreparedStatement pstmt = connection.prepareStatement(sql)){

            pstmt.setDate(1, Date.valueOf(date));
            pstmt.setString(2,busId);

            try(ResultSet rs = pstmt.executeQuery()){

                while( rs.next()) {

                    Trip trip = BeanFactory.getInstance().createTrip();
                    trip.setBusId(rs.getString("bus_id"));
                    trip.setTripId(rs.getString("trip_id"));
                    trip.setStartTime(rs.getTime("start_time").toLocalTime());
                    trip.setEndTime(rs.getTime("end_time").toLocalTime());
                    trip.setTripDate(rs.getDate("date").toLocalDate());

                    trips.add(trip);
                }

            }
            return trips;
        }


    }

    public void renderTripSeats(String tripId, String busId) throws SQLException {

        String sql = """
            INSERT INTO trip_seat (trip_id, bus_id, seat_number, status)
            SELECT ?, bus_id, seat_number, 'AVAILABLE'
            FROM seat
            WHERE bus_id = ?
            """;

        try (Connection connection = getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setString(1, tripId);
            pstmt.setString(2, busId);

            int rowsInserted = pstmt.executeUpdate();

            if (rowsInserted == 0) {
                throw new SQLException("No seats found for bus: " + busId);
            }
        }
    }

    public List<TripDto>getAllTrips() throws SQLException{

        String sql = """
                SELECT t.trip_id , t.start_time ,t.end_time ,t.price ,t.date ,
                       b.bus_id , b.bus_name , b.bus_number , b.bus_type,
                       r.source , r.destination ,r.distance_km
                FROM trip t
                LEFT JOIN bus b on b.bus_id = t.bus_id 
                LEFT JOIN route r on t.route_id = r.route_id
                WHERE  t.date =(?)
                """;

        try(Connection connection = getConnection();
        PreparedStatement pstmt = connection.prepareStatement(sql)){

            pstmt.setDate(1,Date.valueOf(LocalDate.now()));

            List<TripDto> tripDetails  = new ArrayList<>();

            try( ResultSet rs = pstmt.executeQuery()){

                while(rs.next()){

                    TripDto  dto = new TripDto(
                            rs.getString("trip_id"),
                            rs.getString("bus_name"),
                            rs.getString("bus_number"),
                            rs.getString("source"),
                            rs.getString("destination"),
                            rs.getInt("distance_km"),
                            rs.getTime("start_time").toLocalTime(),
                            rs.getTime("end_time").toLocalTime(),
                            rs.getDate("date").toLocalDate(),
                            BusType.valueOf(rs.getString("bus_type")),
                            rs.getDouble("price")

                    );

                    tripDetails.add(dto);
                }
            }
            return tripDetails;
        }
    }

    public List<TripDto>findAllTripsBySourceDestinationAndTime(String source , String destination , LocalDate date) throws SQLException{

        String sql = """
                SELECT t.trip_id , t.start_time ,t.end_time ,t.price ,t.date,
                       b.bus_id , b.bus_name , b.bus_number , b.bus_type,
                       r.source , r.destination ,r.distance_km
                FROM trip t
                LEFT JOIN bus b on b.bus_id = t.bus_id 
                LEFT JOIN route r on t.route_id = r.route_id
                WHERE  t.date =(?) AND r.source = (?) AND r.destination =(?);
                """;

        try(Connection connection = getConnection();
            PreparedStatement pstmt = connection.prepareStatement(sql)){

            pstmt.setDate(1,Date.valueOf(date));
            pstmt.setString(2,source);
            pstmt.setString(3,destination);

            List<TripDto> tripDetails  = new ArrayList<>();

            try( ResultSet rs = pstmt.executeQuery()){

                while(rs.next()){

                    TripDto  dto = new TripDto(
                            rs.getString("trip_id"),
                            rs.getString("bus_name"),
                            rs.getString("bus_number"),
                            rs.getString("source"),
                            rs.getString("destination"),
                            rs.getInt("distance_km"),
                            rs.getTime("start_time").toLocalTime(),
                            rs.getTime("end_time").toLocalTime(),
                            rs.getDate("date").toLocalDate(),
                            BusType.valueOf(rs.getString("bus_type")),
                            rs.getDouble("price")

                    );

                    tripDetails.add(dto);
                }
            }
            return tripDetails;
        }
    }


    public Trip findTripById(String tripId) throws SQLException{

        String sql  = """
                SELECT * FROM trip WHERE trip_id = (?)
                """;

        try(Connection connection = getConnection();
        PreparedStatement pstmt = connection.prepareStatement(sql)){

            pstmt.setString(1,tripId);

            try(ResultSet rs = pstmt.executeQuery()){

                if( ! rs.next()){

                    return null ;
                }

                Trip trip = BeanFactory.getInstance().createTrip();

                trip.setTripId(rs.getString("trip_id"));
                trip.setBusId(rs.getString("bus_id"));
                trip.setStaus(TripStatus.valueOf(rs.getString("status")));
                trip.setStartTime(rs.getTime("start_time").toLocalTime());
                trip.setEndTime(rs.getTime("end_time").toLocalTime());
                trip.setTripDate(rs.getDate("date").toLocalDate());
                trip.setPrice(rs.getDouble("price"));

                return trip;
            }
        }
    }

    public List<TripDto> getCurrentTrips() {

        String sql = """
        SELECT
            t.trip_id,
            b.bus_name,
            b.bus_number,
            r.source,
            r.destination,
            r.distance_km,
            t.start_time,
            t.end_time,
            t.date,
            b.bus_type,
            t.price
        FROM trip t
        JOIN route r ON t.route_id = r.route_id
        JOIN bus b ON t.bus_id = b.bus_id
        WHERE t.date >= CURRENT_DATE
          AND t.status = 'ACTIVE'
        ORDER BY t.date, t.start_time
    """;

        List<TripDto> trips = new ArrayList<>();

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                TripDto dto = new TripDto(
                        rs.getString("trip_id"),
                        rs.getString("bus_name"),
                        rs.getString("bus_number"),
                        rs.getString("source"),
                        rs.getString("destination"),
                        rs.getInt("distance_km"),
                        rs.getTime("start_time").toLocalTime(),
                        rs.getTime("end_time").toLocalTime(),
                        rs.getDate("date").toLocalDate(),
                        BusType.valueOf(rs.getString("bus_type")),
                        rs.getDouble("price")
                );

                trips.add(dto);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch current trips", e);
        }

        return trips;
    }

    public  void updateTrip(UpdatetripRequest updateRequest)throws SQLException{

        String sql = "UPDATE trip SET date = (?) ,start_time =(?) , end_time = (?)" +
                "WHERE trip_id = (?)";

        try(Connection connection = getConnection();
        PreparedStatement pstmt = connection.prepareStatement(sql)){

            pstmt.setDate(1,Date.valueOf(updateRequest.date()));
            pstmt.setTime(2,Time.valueOf(updateRequest.startTime()));
            pstmt.setTime(3,Time.valueOf(updateRequest.endTime()));

            pstmt.executeUpdate();
        }
    }

    public TripDto getTripDetailsById(String tripId)throws SQLException {

        String sql = """
                SELECT t.trip_id , t.start_time ,t.end_time ,t.price ,t.date,
                       b.bus_id , b.bus_name , b.bus_number , b.bus_type,
                       r.source , r.destination ,r.distance_km
                FROM trip t
                LEFT JOIN bus b on b.bus_id = t.bus_id 
                LEFT JOIN route r on t.route_id = r.route_id
                WHERE  trip_id = (?)
                """;


        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ) {

            ps.setString(1,tripId);
            ResultSet rs = ps.executeQuery();

            if ( ! rs.next()) {

                return null;

            }
            TripDto dto = new TripDto(
                    rs.getString("trip_id"),
                    rs.getString("bus_name"),
                    rs.getString("bus_number"),
                    rs.getString("source"),
                    rs.getString("destination"),
                    rs.getInt("distance_km"),
                    rs.getTime("start_time").toLocalTime(),
                    rs.getTime("end_time").toLocalTime(),
                    rs.getDate("date").toLocalDate(),
                    BusType.valueOf(rs.getString("bus_type")),
                    rs.getDouble("price")
            );
            return dto;
        }
    }

    public boolean updateTripStatus(String tripId , String status)throws  SQLException {

        String sql = """
               UPDATE trip
               set status = (?)
               where trip_id = (?)
                """;

        try( Connection connection = getConnection();
        PreparedStatement pstmt  = connection.prepareStatement(sql)){

            pstmt.setString(1,TripStatus.valueOf(status).toString());
            pstmt.setString(2,tripId);

            return pstmt.executeUpdate() >0 ;
        }
    }

    public List<SeatDto> getSeatsDataOfTrip(String tripId , String busId) throws SQLException{

        String sql = """
                SELECT  ts.seat_number , ts.status , s.seat_type , s.row_num , s.col_num
                FROM trip_seat ts
                LEFT JOIN seat s on  ts.seat_number = s.seat_number
                WHERE s.bus_id =(?) and ts.trip_id = (?)
                """;

        try( Connection connection = getConnection();
        PreparedStatement pstmt = connection.prepareStatement(sql) ){

            pstmt.setString(1,busId);
            pstmt.setString(2,tripId);

            List<SeatDto> response = new ArrayList<>();

            try( ResultSet rs = pstmt.executeQuery()){

                while( rs.next() ){

                    SeatDto seat = new SeatDto(
                            rs.getInt("seat_number"),
                            rs.getInt("row_num"),
                            rs.getInt("col_num"),
                            SeatStatus.valueOf(rs.getString("status")),
                            SeatType.valueOf(rs.getString("seat_type"))
                    );

                    response.add(seat);
                }
            }
            return response;
        }
    }


}
