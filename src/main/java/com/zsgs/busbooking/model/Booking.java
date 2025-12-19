package com.zsgs.busbooking.model;

public class Booking {

    private String bookingId;
    private String tripId;
    private String busId;
    private double price;
    private String paymentId;
    private String UserId;

    public Booking() {}

    private Booking(Builder builder) {
        this.bookingId = builder.bookingId;
        this.tripId = builder.tripId;
        this.busId = builder.busId;
        this.price = builder.price;
        this.paymentId = builder.paymentId;
        UserId = builder.userId;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getTripId() {
        return tripId;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }

    public String getBusId() {
        return busId;
    }

    public void setBusId(String busId) {
        this.busId = busId;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public static Builder builder(){
        return new Builder();
    }

    public static class Builder {

        private String bookingId = null;
        private String tripId = null;
        private String busId = null;
        private double price ;
        private String paymentId = null;
        private String userId ;


        public Builder withBookingId(String bookingId){
            this.bookingId = bookingId;
            return this ;
        }

        public Builder withTripId(String tripId){
            this.tripId = tripId ;
            return this ;

        }
        public Builder withBusId(String busId){
            this.busId = busId;
            return this;
        }

        public Builder withPrice(double price){
            this.price = price;
            return this;
        }
        public Builder withUserId(String userId){
            this.userId = userId;
            return this ;
        }
        public Builder withPaymentId(String paymentId){
            this.paymentId = paymentId;
            return this;
        }
        public Booking build(){

            if ( userId != null && tripId != null && busId != null && paymentId != null && price != 0 && bookingId != null){

                return new Booking(this);
            }
            else {
                throw  new IllegalStateException(" invlaid Requests");
            }
        }
    }
}
