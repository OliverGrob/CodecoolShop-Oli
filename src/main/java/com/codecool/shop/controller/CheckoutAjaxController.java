package com.codecool.shop.controller;

import com.codecool.shop.dao.ShippingAddressDao;
import com.codecool.shop.dao.ShoppingCartDao;
import com.codecool.shop.dao.UserDao;
import com.codecool.shop.dao.implementation.ShippingAddressDaoJDBC;
import com.codecool.shop.dao.implementation.ShoppingCartDaoJDBC;
import com.codecool.shop.dao.implementation.UserDaoJDBC;
import com.codecool.shop.email.EmailManager;
import com.codecool.shop.model.ShoppingCartStatus;
import com.google.gson.Gson;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@WebServlet(urlPatterns = {"/handle-checkout"})
public class CheckoutAjaxController extends HttpServlet {
    private SessionManager sessionManager = SessionManager.getInstance();
    private EmailManager emailManager = EmailManager.getInstance();
    private ShoppingCartDao shoppingCartDataStore = ShoppingCartDaoJDBC.getInstance();
    private UserDao userHandler = UserDaoJDBC.getInstance();
    private ShippingAddressDao shippingAddressHandler = ShippingAddressDaoJDBC.getInstance();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = sessionManager.getHttpSessionRedirect(req);

        if (session == null) {
            resp.sendRedirect("/");
            return;
        }

        Map<String, String> newData = new HashMap<>();

        int userId = (Integer) session.getAttribute("userId");

        switch (req.getParameter("event")) {
            case "checkout":
                userHandler.setBillingAddress(userId,
                        req.getParameter("firstName"),
                        req.getParameter("lastName"),
                        req.getParameter("address"),
                        req.getParameter("country"),
                        req.getParameter("city"),
                        req.getParameter("zipCode"),
                        Boolean.parseBoolean(req.getParameter("isShippingSame")));
                shoppingCartDataStore.changeCartStatus(userId,
                        Integer.parseInt(req.getParameter("shoppingCartId")),
                        ShoppingCartStatus.CHECKED);
                if (shoppingCartDataStore.findActiveCartForUser(userId) == null) {
                    shoppingCartDataStore.add(userId, new java.sql.Date(new Date().getTime()));
                }

                if (Boolean.parseBoolean(req.getParameter("isShippingSame"))) {
                    shippingAddressHandler.addShippingAddress(userId,
                            req.getParameter("address"),
                            req.getParameter("country"),
                            req.getParameter("city"),
                            req.getParameter("zipCode"));
                    newData.put("alertColor", "success");
                    newData.put("alertMessage", "Billing address saved!");
                } else {
                    shippingAddressHandler.addShippingAddress(userId,
                            req.getParameter("addressShipping"),
                            req.getParameter("countryShipping"),
                            req.getParameter("cityShipping"),
                            req.getParameter("zipCodeShipping"));
                    newData.put("alertColor", "success");
                    newData.put("alertMessage", "Billing and shipping address saved!");
                }
                break;
            case "payment":
                shoppingCartDataStore.changeCartStatus(userId,
                        Integer.parseInt(req.getParameter("shoppingCartId")),
                        ShoppingCartStatus.PAID);
                break;
            case "confirmation":
                shoppingCartDataStore.changeCartStatus(userId,
                        Integer.parseInt(req.getParameter("shoppingCartId")),
                        ShoppingCartStatus.CONFIRMED);
                emailManager.sendEmail(userHandler.find(userId).getEmailAddress());
                break;
        }

        String json = new Gson().toJson(newData);

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(json);
    }

}
