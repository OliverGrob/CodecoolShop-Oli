package com.codecool.shop.dao;

import com.codecool.shop.model.ShippingAddress;

public interface ShippingAddressDao {

    void addShippingAddress(int userId, String address, String country, String city, String zipCode);

    ShippingAddress find(int userId);

}
