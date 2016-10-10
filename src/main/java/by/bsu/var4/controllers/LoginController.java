package by.bsu.var4.controllers;

import by.bsu.var4.dao.UserDAO;
import by.bsu.var4.dao.impl.UserDAOImpl;
import by.bsu.var4.entity.User;
import by.bsu.var4.entity.UserModel;
import by.bsu.var4.entity.UserRole;
import by.bsu.var4.exception.DAOException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by Asus on 06.10.2016.
 */
@Controller
public class LoginController extends BaseController{
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView getLogin(){
        return new ModelAndView("login", "user", new User());
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String getLogout(HttpServletRequest req, HttpServletResponse resp, Model model) throws IOException, DAOException {
        removeCookie("login",req,resp);
        return "index";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String processLogin(
            @ModelAttribute("user") User user,
            BindingResult result,HttpServletRequest req, HttpServletResponse resp,Model model) throws IOException, SQLException, DAOException {

        User userDb = userDAO.getUser(user.getLogin(), user.getPassword());
        if(userDb == null)
        {
            result.rejectValue("email", "registration.email.wrongPattern", "Login or password is incorrect!");
        }
        if (result.hasErrors()) {
            return "login";
        }
        else
        {
            addCookie(userDb.getLogin(), resp);
        }
        model.addAttribute("currentuser", userDb.getLogin());
        if(userDb != null && userDb.getRole() == UserRole.Admin.ordinal())
        {
            model.addAttribute("userGroupConnections", userGroupDAO.getConnections());
            return "admin";
        }
        model.addAttribute("resources", resourceDAO.getAvailableResources(userDb.getUserId()));
        return "index";
    }

    @RequestMapping(value = "/registration", method = RequestMethod.GET)
    public ModelAndView processRegister() {
        return new ModelAndView("registration", "person", new UserModel());

    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public String processSubmit(
            @ModelAttribute("person") UserModel person,
            HttpServletRequest req, HttpServletResponse resp, Model model) throws IOException, SQLException, DAOException {


        User user = new User();
        user.setEmail(person.getEmail());
        user.setLogin(person.getLogin());
        user.setPassword(person.getPassword());
        user.setRole(UserRole.User.ordinal());
        userDAO.create(user);
        model.addAttribute("currentuser", user.getLogin());
        if(user != null && user.getRole() == UserRole.Admin.ordinal())
        {
            model.addAttribute("userGroupConnections", userGroupDAO.getConnections());
            return "admin";
        }
        model.addAttribute("resources", resourceDAO.getAvailableResources(user.getUserId()));
        return "index";

    }
}
