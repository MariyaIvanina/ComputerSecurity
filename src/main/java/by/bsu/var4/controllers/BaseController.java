package by.bsu.var4.controllers;

import by.bsu.var4.dao.ResourceDAO;
import by.bsu.var4.dao.ResourceGroupDAO;
import by.bsu.var4.dao.UserDAO;
import by.bsu.var4.dao.UserGroupDAO;
import by.bsu.var4.entity.User;
import by.bsu.var4.entity.UserRole;
import by.bsu.var4.exception.DAOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import javax.rmi.PortableRemoteObject;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Asus on 06.10.2016.
 */
@Controller
public class BaseController {
    @Autowired
    protected UserDAO userDAO;

    @Autowired
    protected UserGroupDAO userGroupDAO;

    @Autowired
    protected ResourceDAO resourceDAO;

    @Autowired
    protected ResourceGroupDAO resourceGroupDAO;

    protected void addCookie(String cookieName, HttpServletResponse resp)
    {
        Cookie cookie = new Cookie("login",cookieName);
        cookie.setMaxAge(1000000);
        cookie.setPath("/");
        resp.addCookie(cookie);
    }

    protected void removeCookie(String cookieName, HttpServletRequest req, HttpServletResponse resp)
    {
        Cookie[] cookies = req.getCookies();
        if(cookies == null)
        {
            return;
        }
        for (Cookie cookie: cookies)
        {
            if("login".equals(cookie.getName()))
            {
                cookie.setMaxAge(0);
                break;
            }
        }
    }

    public String manageRequests(HttpServletRequest req, HttpServletResponse resp, Model model) throws IOException, DAOException {
        User user = getCurrecntUser(req);
        if(user == null)
        {
            return "index";
        }
        model.addAttribute("currentuser", user.getLogin());
        if(user != null && user.getRole() == UserRole.Admin.ordinal())
        {
            model.addAttribute("userGroupConnections", userGroupDAO.getConnections());
            return "admin";
        }
        model.addAttribute("resources", resourceDAO.getAvailableResources(user.getUserId()));
        return "index";
    }

    protected User getCurrecntUser(HttpServletRequest req) throws DAOException {
        Cookie[] cookies = req.getCookies();
        String login = "";
        if(cookies == null)
        {
            return null;
        }
        for (Cookie cookie: cookies)
        {
            if("login".equals(cookie.getName()))
            {
                login = cookie.getValue();
                break;
            }
        }
        User userDb = userDAO.getUser(login);
        return userDb;
    }
}
