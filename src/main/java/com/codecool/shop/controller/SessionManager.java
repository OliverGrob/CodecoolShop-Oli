package com.codecool.shop.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class SessionManager {

    private static SessionManager instance = null;


    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public HttpSession getHttpSessionRedirect(HttpServletRequest req) {
        HttpSession session = req.getSession(false);

        if (session == null) {
            session = req.getSession(true);
            session.setAttribute("userId", null);
            return null;
        } else {
            if (session.getAttribute("userId") == null) {
                return null;
            }
        }

        return session;
    }

    public HttpSession getHttpSession(HttpServletRequest req) {
        HttpSession session = req.getSession(false);

        if (session == null) {
            session = req.getSession(true);
            session.setAttribute("userId", null);
            return null;
        }

        return session;
    }

}
