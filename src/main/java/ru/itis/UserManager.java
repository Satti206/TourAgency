package ru.itis;

import ru.itis.models.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class UserManager {
    public void auth(User user, HttpServletRequest req, HttpServletResponse resp) {
        getSession(req).setAttribute("user", user);
    }

    public boolean isNonAnonymous(HttpServletRequest req, HttpServletResponse resp) {
        return getUser(req) != null;
    }

    public User getUser(HttpServletRequest req) {
        return (User) getSession(req).getAttribute("user");
    }

    private HttpSession getSession(HttpServletRequest req) {
        return req.getSession();
    }
}
