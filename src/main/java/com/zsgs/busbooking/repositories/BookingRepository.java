package com.zsgs.busbooking.repositories;

import com.zsgs.busbooking.enums.BookinsStatus;
import com.zsgs.busbooking.enums.SeatStatus;
import com.zsgs.busbooking.model.Booking;
import com.zsgs.busbooking.payloads.UserBookingDto;


import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class BookingRepository extends BaseRepository {

    public boolean bookTrip(Booking booking) throws SQLException {

        String sql = """
                INSERT INTO booking (booking_id ,passenger_id , trip_id , bus_id,payment_id ,price ,booking_status ,created_at ,no_of_seats )
                values(?,?,?,?,?,?,?,?,?)
                """;

        try (Connection connection = getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setString(1, booking.getBookingId());
            pstmt.setString(2, booking.getPassengerId());
            pstmt.setString(3, booking.getTripId());
            pstmt.setString(4, booking.getBusId());
            pstmt.setString(5, booking.getPaymentId());
            pstmt.setDouble(6, booking.getPrice());
            pstmt.setString(7, BookinsStatus.BOOKED.toString());
            pstmt.setTimestamp(8, Timestamp.valueOf(LocalDateTime.now()));
            pstmt.setInt(9, booking.getNoOfSeats());

            int row = pstmt.executeUpdate();

            return row > 0;

        }
    }

    public boolean BookSeats(String tripId, String busId, int seatNumber) throws SQLException {
        String sql = """
                UPDATE trip_seat 
                SET status = (?) , /*bookingId =(?)*/
                WHERE trip_id = (?) AND bus_id = (?) AND seat_number =(?)
                """;

        try (Connection connection = getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setString(1, SeatStatus.BOOKED.toString());
            pstmt.setString(2, tripId);
            pstmt.setString(3, busId);
            pstmt.setInt(4, seatNumber);

            int row = pstmt.executeUpdate();
            return row > 0;
        }
    }

    public boolean BookSeats(String busId, String bookingId ,String tripId, List<Integer> seatNumbers) throws SQLException {


        String placeholders = seatNumbers.stream()
                .map(n -> "?")
                .collect(Collectors.joining(","));

        String sql =
                "UPDATE trip_seat " +
                        "SET status = 'BOOKED'" +
                        "SET booking_id = (?) " +
                        "WHERE trip_id = ? " +
                        "AND bus_id = ? " +
                        "AND seat_number IN (" + placeholders + ")";

        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            int index = 1;
            ps.setString(index++,bookingId);
            ps.setString(index++, tripId);
            ps.setString(index++, busId);

            for (Integer seat : seatNumbers) {
                ps.setInt(index++, seat);
            }

            int updated = ps.executeUpdate();

            if (updated != seatNumbers.size()) {
                throw new SQLException("Some seats were not booked");
            }
            return updated == seatNumbers.size();
        }


    }

    public SeatStatus chcekSeatStatus(String tripId, int seatNumber) throws SQLException {

        String sql = """
                SELECT status
                FROM trip_seat
                WHERE trip_id = (?) AND seat_number =(?)
                """;

        try (Connection connection = getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setString(1, tripId);
            pstmt.setInt(2, seatNumber);

            try (ResultSet rs = pstmt.executeQuery()) {

                if (!rs.next()) {

                    return null;
                }

                return SeatStatus.valueOf(rs.getString("status"));
            }
        }
    }

    public void revokeBooking(Booking booking) throws SQLException {

        String sql = """
                DELETE FROM booking
                WHERE booking_id = (?)
                """;

        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, booking.getBookingId());

            ps.executeUpdate();
        }

    }

    public boolean revokeSeats(String busId, String tripId, List<Integer> seatNumbers) throws SQLException {


        String placeholders = seatNumbers.stream()
                .map(n -> "?")
                .collect(Collectors.joining(","));

        String sql =
                "UPDATE trip_seat " +
                        "SET status = 'AVAILABLE' " +
                        "WHERE trip_id = ? " +
                        "AND bus_id = ? " +
                        "AND seat_number IN (" + placeholders + ")";

        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            int index = 1;

            ps.setString(index++, tripId);
            ps.setString(index++, busId);

            for (Integer seat : seatNumbers) {
                ps.setInt(index++, seat);
            }

            int updated = ps.executeUpdate();

            if (updated != seatNumbers.size()) {
                throw new SQLException("Some seats were not booked");
            }
            return updated == seatNumbers.size();
        }


    }

    public boolean bookTrip(Connection connection, Booking booking) throws SQLException {

        String sql = """
                INSERT INTO booking (booking_id ,passenger_id , trip_id , bus_id,payment_id ,price ,booking_status ,created_at ,no_of_seats )
                values(?,?,?,?,?,?,?,?,?)
                """;

        try (
                PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setString(1, booking.getBookingId());
            pstmt.setString(2, booking.getPassengerId());
            pstmt.setString(3, booking.getTripId());
            pstmt.setString(4, booking.getBusId());
            pstmt.setString(5, booking.getPaymentId());
            pstmt.setDouble(6, booking.getPrice());
            pstmt.setString(7, BookinsStatus.BOOKED.toString());
            pstmt.setTimestamp(8, Timestamp.valueOf(LocalDateTime.now()));
            pstmt.setInt(9, booking.getNoOfSeats());

            int row = pstmt.executeUpdate();

            return row > 0;

        }
    }

    public boolean BookSeats(Connection connection, String busId, String bookingId, String tripId, List<Integer> seatNumbers) throws SQLException {


        String placeholders = seatNumbers.stream()
                .map(n -> "?")
                .collect(Collectors.joining(","));

        String sql =
                "UPDATE trip_seat " +
                        "SET status = 'BOOKED' " +
                        ",booking_id = (?)" +
                        "WHERE trip_id = ? " +
                        "AND bus_id = ? " +
                        "AND seat_number IN (" + placeholders + ")";

        try (
                PreparedStatement ps = connection.prepareStatement(sql)) {

            int index = 1;
            ps.setString(index++, bookingId);
            ps.setString(index++, tripId);
            ps.setString(index++, busId);

            for (Integer seat : seatNumbers) {
                ps.setInt(index++, seat);
            }

            int updated = ps.executeUpdate();

            if (updated != seatNumbers.size()) {
                throw new SQLException("Some seats were not booked");
            }
            return updated == seatNumbers.size();
        }


    }

    public List<UserBookingDto> getPassengerBookings(String passengerId) {

        String sql = """
        WITH trip_cte AS (
            SELECT
                t.trip_id,
                t.date,
                t.start_time,
                t.end_time,
                r.source,
                r.destination,
                r.distance_km
            FROM trip t
            JOIN route r ON t.route_id = r.route_id
        )
        SELECT
            b.booking_id,
            b.passenger_id,
            b.booking_status,
            b.no_of_seats,
            b.price,
            b.created_at,

            bu.bus_id,
            bu.bus_name,
            bu.bus_number,

            t.trip_id,
            t.source,
            t.destination,
            t.distance_km,
            t.start_time,
            t.end_time,
            t.date,

            GROUP_CONCAT(bs.seat_number ORDER BY bs.seat_number) AS seat_numbers
        FROM booking b
        JOIN trip_cte t ON b.trip_id = t.trip_id
        JOIN bus bu ON b.bus_id = bu.bus_id
        LEFT JOIN booking_seat bs ON b.booking_id = bs.booking_id
        WHERE b.passenger_id = ?
        GROUP BY b.booking_id
        ORDER BY b.created_at DESC
    """;

        List<UserBookingDto> bookings = new ArrayList<>();

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, passengerId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {

                    // Parse seat numbers
                    String seatStr = rs.getString("seat_numbers");
                    List<Integer> seats =
                            (seatStr == null || seatStr.isBlank())
                                    ? List.of()
                                    : Arrays.stream(seatStr.split(","))
                                    .map(Integer::parseInt)
                                    .toList();

                    UserBookingDto dto = new UserBookingDto(
                            // Booking
                            rs.getString("booking_id"),
                            rs.getString("passenger_id"),
                            rs.getString("booking_status"),
                            rs.getInt("no_of_seats"),
                            rs.getDouble("price"),
                            rs.getTimestamp("created_at").toLocalDateTime(),
                            seats,

                            // Trip
                            rs.getString("trip_id"),
                            rs.getString("source"),
                            rs.getString("destination"),
                            rs.getDate("date").toLocalDate(),
                            rs.getTime("start_time").toLocalTime(),
                            rs.getTime("end_time").toLocalTime(),
                            rs.getDouble("distance_km"),

                            // Bus
                            rs.getString("bus_id"),
                            rs.getString("bus_name"),
                            rs.getString("bus_number")
                    );

                    bookings.add(dto);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch user bookings", e);
        }

        return bookings;
    }



}
