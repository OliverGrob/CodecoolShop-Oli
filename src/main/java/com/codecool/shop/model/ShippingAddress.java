package com.codecool.shop.model;

public class ShippingAddress {

    private User user;
    private String country;
    private String city;
    private String address;
    private String zipCode;


    public ShippingAddress(User user, String country, String city, String address, String zipCode){
        this.user = user;
        this.country = country;
        this.city = city;
        this.address = address;
        this.zipCode = zipCode;
    }


    public User getUser() {
        return user;
    }

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }

    public String getAddress() {
        return address;
    }

    public String getZipCode() {
        return zipCode;
    }

    @Override
    public String toString() {
        return String.format("userId: %1$s, " +
                        "country: %2$s, " +
                        "city: %3$s" +
                        "address: %4$s" +
                        "zipCode: %5$s",

                this.user.getEmailAddress(),
                this.country,
                this.city,
                this.address,
                this.zipCode);

    }

}
