package com.codecool.shop.dao;

import com.codecool.shop.model.ShippingAddress;

import java.util.List;

public interface ShippingAddressDao {

    void addShippingAddress(int userId, String address, String country, String city, String zipCode);

    List<ShippingAddress> findShippingAddressesByUser(int userId);

}
