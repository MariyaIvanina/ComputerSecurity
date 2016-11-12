package by.bsu.var4.controllers;

import by.bsu.var4.entity.UserResourceConnection;
import by.bsu.var4.exception.DAOException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by Asus on 04.10.2016.
 */
@Controller
public class HomeController extends BaseController{
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String getHome(HttpServletRequest req, HttpServletResponse resp, Model model) throws IOException, DAOException {
        return manageRequests(req,resp,model);
    }

    @RequestMapping(value = "/admin", method = RequestMethod.GET)
    public String getAdminPage(HttpServletRequest req, HttpServletResponse resp, Model model) throws IOException, DAOException {
        return manageRequests(req,resp,model);
    }

    @RequestMapping(value = "/addConnection", method = RequestMethod.GET)
    public ModelAndView addConnection(HttpServletRequest req, Model model) throws DAOException {
        model.addAttribute("userGroups", userGroupDAO.retrieveAll());
        model.addAttribute("resourceGroups", resourceGroupDAO.retrieveAll());
        model.addAttribute("currentuser", getCurrentUser(req).getLogin());
        return new ModelAndView("addConnection", "connection", new UserResourceConnection());
    }

    @RequestMapping(value = "/addConnection", method = RequestMethod.POST)
    public String addUserToDb(@ModelAttribute("connection") UserResourceConnection connection,
                              HttpServletRequest req, HttpServletResponse resp, Model model) throws IOException, SQLException, DAOException {
        userGroupDAO.createConnection(connection);
        return manageRequests(req, resp, model);
    }

    @RequestMapping(value = "/editConnection", method = RequestMethod.GET)
    public ModelAndView editConnection(@RequestParam("id") Integer id, HttpServletRequest req, Model model) throws DAOException {
        UserResourceConnection con = userGroupDAO.getConnection(id);
        model.addAttribute("userGroups", userGroupDAO.retrieveAll());
        model.addAttribute("resourceGroups", resourceGroupDAO.retrieveAll());
        model.addAttribute("currentuser", getCurrentUser(req).getLogin());
        return new ModelAndView("addConnection", "connection", con);
    }

    @RequestMapping(value = "/editConnection", method = RequestMethod.POST)
    public String editUserToDb(@ModelAttribute("connection") UserResourceConnection connection,
                              HttpServletRequest req, HttpServletResponse resp, Model model) throws IOException, SQLException, DAOException {
        userGroupDAO.updateConnection(connection);
        return manageRequests(req, resp, model);
    }

    @RequestMapping(value = "/deleteConnection", method = RequestMethod.GET)
    public String editUserToDb(@RequestParam("id") Integer id,
                               HttpServletRequest req, HttpServletResponse resp, Model model) throws IOException, SQLException, DAOException {
        userGroupDAO.deleteConnection(id);
        return manageRequests(req, resp, model);
    }
}
