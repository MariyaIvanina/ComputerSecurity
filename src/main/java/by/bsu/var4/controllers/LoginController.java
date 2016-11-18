package by.bsu.var4.controllers;

import static by.bsu.var4.util.ConstantUtil.*;

import by.bsu.var4.entity.User;
import by.bsu.var4.entity.UserModel;
import by.bsu.var4.entity.UserRole;
import by.bsu.var4.exception.DAOException;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
import java.sql.Timestamp;
import java.util.Calendar;
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

    @RequestMapping(value = "/pin", method = RequestMethod.GET)
    public String getPin(HttpServletRequest req, Model model) throws DAOException, IOException {
        User userDb = getCurrentUser(req);
        if(userDb == null)
        {
            return "redirect:/login";
        }
        model.addAttribute("currentuser", userDb.getLogin());
        model.addAttribute("user", new User());
        return "pin";
    }

    @RequestMapping(value = "/pin", method = RequestMethod.POST)
    public String getPinPost(@ModelAttribute("user") User user, Errors errors, Locale locale,HttpServletRequest req, HttpServletResponse resp, Model model) throws IOException, DAOException {
        if(errors.hasErrors()){
            model.addAttribute("currentuser", getCurrentUser(req).getLogin());
            return "pin";
        }
        HttpSession session = req.getSession();
        String realPin = (String) session.getAttribute("pinCode");
        User userDb = userDAO.getUser((String)session.getAttribute("login"));
        if(userDb.getPinAttemptCount() > 2)
        {
            int addMinuteTime = 15 *60 * 1000;
            Timestamp targetTime = userDb.getPinLastAttempt();
            targetTime.setTime(targetTime.getTime() + addMinuteTime);
            if(targetTime.compareTo(new java.sql.Timestamp(Calendar.getInstance().getTimeInMillis())) > 0)
            {
                userDb.setPinAttemptCount(0);
                userDb.setPinLastAttempt(new java.sql.Timestamp(Calendar.getInstance().getTimeInMillis()));
                userDAO.update(userDb);
                return getLogout(req, resp, model);
            }
            else
            {
                userDb.setPinAttemptCount(0);
            }
        }
        if(!realPin.equals(DigestUtils.md5Hex(user.getPinCode()))){
            userDb.setPinAttemptCount(userDb.getPinAttemptCount() + 1);
            userDb.setPinLastAttempt(new java.sql.Timestamp(Calendar.getInstance().getTimeInMillis()));
            userDAO.update(userDb);
            errors.reject(PIN_INCORRECT, messageSource.getMessage(PIN_INCORRECT, null, locale));
            model.addAttribute("currentuser", getCurrentUser(req).getLogin());
            return "pin";
        }
        userDb.setPinAttemptCount(0);
        userDb.setPinLastAttempt(new java.sql.Timestamp(Calendar.getInstance().getTimeInMillis()));
        userDAO.update(userDb);
        return manageRequests(req,resp,model);
    }


    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String processLogin( @Valid User user, Errors errors, Locale locale,
                                HttpServletRequest req, HttpServletResponse resp,Model model) throws IOException, SQLException, DAOException {

        if(errors.hasErrors()){
            return LOGIN_PAGE;
        }

        User userDb = userDAO.getUser(user.getLogin());
        HttpSession session = req.getSession();

        if(userDb == null)
        {
            errors.reject(ERROR_EMPTY_USER, messageSource.getMessage(KEY_EMPTY_USER, null, locale));
        }

        if(userDb.getAttemptCount() > 2)
        {
            int addMinuteTime = 15 *60 * 1000;
            Timestamp targetTime = userDb.getLastAttempt();
            targetTime.setTime(targetTime.getTime() + addMinuteTime);
            if(targetTime.compareTo(new java.sql.Timestamp(Calendar.getInstance().getTimeInMillis())) > 0)
            {
                errors.reject(BLOCK_USER, messageSource.getMessage(BLOCK_USER, null, locale));
                return LOGIN_PAGE;
            }
            else
            {
                userDb.setAttemptCount(0);
            }
        }

        if(!userDb.getPassword().equals(DigestUtils.md5Hex(user.getPassword())))
        {
            userDb.setAttemptCount(userDb.getAttemptCount() + 1);
            userDb.setLastAttempt(new java.sql.Timestamp(Calendar.getInstance().getTimeInMillis()));
            userDAO.update(userDb);
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
            userDb.setAttemptCount(0);
            userDb.setLastAttempt(new java.sql.Timestamp(Calendar.getInstance().getTimeInMillis()));
            userDb.setPinAttemptCount(0);
            userDb.setPinLastAttempt(new java.sql.Timestamp(Calendar.getInstance().getTimeInMillis()));
            userDAO.update(userDb);
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
        user.setAttemptCount(0);
        user.setLastAttempt(new java.sql.Timestamp(Calendar.getInstance().getTimeInMillis()));
        user.setPinAttemptCount(0);
        user.setPinLastAttempt(new java.sql.Timestamp(Calendar.getInstance().getTimeInMillis()));
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
