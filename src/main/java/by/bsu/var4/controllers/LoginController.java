package by.bsu.var4.controllers;

import static by.bsu.var4.util.ConstantUtil.*;

import by.bsu.var4.entity.User;
import by.bsu.var4.entity.UserModel;
import by.bsu.var4.entity.UserRole;
import by.bsu.var4.exception.DAOException;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.ErrorCoded;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Locale;

@Controller
public class LoginController extends BaseController{

    @Autowired
    MessageSource messageSource;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView getLogin(){
        return new ModelAndView(LOGIN, USER, new User());
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String getLogout(HttpServletRequest req, HttpServletResponse resp, Model model) throws IOException, DAOException {

        HttpSession session = req.getSession();
        session.removeAttribute(LOGIN);
        session.removeAttribute(ROLE);
        session.removeAttribute(PIN);
        session.invalidate();

        removeCookie(LOGIN, req, resp);
        return "index";
    }


    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String processLogin( @Valid User user, Errors errors, Locale locale,
                                HttpServletRequest req, HttpServletResponse resp,Model model) throws IOException, SQLException, DAOException {

        if(errors.hasErrors()){
            return LOGIN_PAGE;
        }

        System.out.println(user);

        User userDb = userDAO.getUser(user.getLogin(), DigestUtils.md5Hex(user.getPassword()));
        HttpSession session = req.getSession();

        System.out.println(userDb);

        if(userDb == null)
        {
            errors.reject(ERROR_EMPTY_USER, messageSource.getMessage(KEY_EMPTY_USER, null, locale));
        }

        if (errors.hasErrors()) {
            return LOGIN_PAGE;
        }
        else
        {
            session.setAttribute(LOGIN, userDb.getLogin());
            session.setAttribute(ROLE, userDb.getRole());
            session.setAttribute(PIN, userDb.getPinCode());
        }

        addCookie(user.getLogin(), resp);
        model.addAttribute(CURRENT_USER, user.getLogin());
        if(user != null && user.getRole() == UserRole.Admin.ordinal())
        {
            model.addAttribute("userGroupConnections", userGroupDAO.getConnections());
            return "admin";
        }
        model.addAttribute(RESOURCES, resourceDAO.getAvailableResources(user.getUserId()));
        return "redirect:/";
    }


    @RequestMapping(value = "/registration", method = RequestMethod.GET)
    public ModelAndView processRegister() {
        return new ModelAndView("registration", PERSON, new UserModel());

    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public String processSubmit(
            @ModelAttribute(PERSON) UserModel person,
            HttpServletRequest req, HttpServletResponse resp, Model model) throws IOException, SQLException, DAOException {

        System.out.println(person);

        User user = new User();
        user.setEmail(person.getEmail());
        user.setLogin(person.getLogin());
        user.setPassword(DigestUtils.md5Hex(person.getPassword()));
        user.setRole(UserRole.User.ordinal());
        user.setPinCode(DigestUtils.md5Hex(person.getPinCode()));
        userDAO.create(user);

        HttpSession session = req.getSession();
        session.setAttribute(LOGIN, user.getLogin());
        session.setAttribute(ROLE, user.getRole());
        session.setAttribute(PIN, user.getPinCode());

        model.addAttribute(CURRENT_USER, user.getLogin());

        if(user != null && user.getRole() == UserRole.Admin.ordinal())
        {
            model.addAttribute(USER_GROUP, userGroupDAO.getConnections());
            return "admin";
        }
        model.addAttribute(RESOURCES, resourceDAO.getAvailableResources(user.getUserId()));
        return "index";

    }
}
