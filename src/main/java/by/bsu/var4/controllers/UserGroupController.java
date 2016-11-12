package by.bsu.var4.controllers;

import by.bsu.var4.entity.UserGroup;
import by.bsu.var4.entity.UserGroupConnection;
import by.bsu.var4.exception.DAOException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
 * Created by Asus on 08.10.2016.
 */
@Controller
@RequestMapping("/userGroup")
public class UserGroupController extends BaseController{
    @RequestMapping(value = "/createGroupUser", method = RequestMethod.GET)
    public ModelAndView getUserGroup(HttpServletRequest req, Model model) throws DAOException {
        model.addAttribute("currentuser", getCurrentUser(req).getLogin());
        return new ModelAndView("groupUserName", "userGroup", new UserGroup());
    }

    @RequestMapping(value = "/createGroupUser", method = RequestMethod.POST)
    public String processUserGroup(
            @ModelAttribute("userGroup") UserGroup userGroup,
            BindingResult result,HttpServletRequest req, HttpServletResponse resp, Model model) throws IOException, SQLException, DAOException {

        userGroupDAO.create(userGroup);
        return getGroupUserNames(req, model);
    }

    @RequestMapping(value = "/editGroupUser", method = RequestMethod.GET)
    public ModelAndView editUserGroup(@RequestParam("id") Integer param, HttpServletRequest req, Model model) throws DAOException {
        UserGroup userGroup = userGroupDAO.retrieve(param);
        model.addAttribute("currentuser", getCurrentUser(req).getLogin());
        return new ModelAndView("groupUserName", "userGroup", userGroup);
    }

    @RequestMapping(value = "/editGroupUser", method = RequestMethod.POST)
    public String processEditUserGroup(@ModelAttribute("userGroup") UserGroup userGroup,
                                       HttpServletRequest req, HttpServletResponse resp, Model model) throws IOException, SQLException, DAOException {

        userGroupDAO.update(userGroup);
        return getGroupUserNames(req, model);
    }

    @RequestMapping(value = "/deleteGroupUser", method = RequestMethod.GET)
    public String deleteUserGroup(@RequestParam("id") Integer param, HttpServletRequest req, Model model) throws DAOException {
        userGroupDAO.delete(param);
        model.addAttribute("currentuser", getCurrentUser(req).getLogin());
        return getGroupUserNames(req, model);
    }

    @RequestMapping(value = "/groupUser", method = RequestMethod.GET)
    public String getGroupUserNames(HttpServletRequest req,Model model) throws DAOException {
        model.addAttribute("currentuser", getCurrentUser(req).getLogin());
        model.addAttribute("userGroups", userGroupDAO.retrieveAll());
        return "groupUser";
    }

    @RequestMapping(value = "/showUsers", method = RequestMethod.GET)
    public String getGroupUsers(@RequestParam("id") Integer param,HttpServletRequest req,Model model) throws DAOException {
        model.addAttribute("currentuser", getCurrentUser(req).getLogin());
        model.addAttribute("users", userGroupDAO.getUsers(param));
        model.addAttribute("groupUserId", param);
        return "groupUserManagement";
    }

    @RequestMapping(value = "/addUser", method = RequestMethod.GET)
    public ModelAndView addUser(@RequestParam("group_id") Integer param, HttpServletRequest req, Model model) throws DAOException {
        UserGroupConnection userGroupConnection = new UserGroupConnection();
        userGroupConnection.setUserGroupId(param);
        model.addAttribute("users", userGroupDAO.getAvailableUsers(param));
        model.addAttribute("currentuser", getCurrentUser(req).getLogin());
        return new ModelAndView("addUser", "userGroupConnection", userGroupConnection);
    }

    @RequestMapping(value = "/addUser", method = RequestMethod.POST)
    public String addUserToDb(@ModelAttribute("userGroupConnection") UserGroupConnection userGroupConnection,
                                       HttpServletRequest req, HttpServletResponse resp, Model model) throws IOException, SQLException, DAOException {

        userGroupDAO.connectUserToGrroup(userGroupConnection.getUserGroupId(), userGroupConnection.getUserId());
        return getGroupUsers(userGroupConnection.getUserGroupId(),req, model);
    }

    @RequestMapping(value = "/deleteUser", method = RequestMethod.GET)
    public String deleteUser(@RequestParam("user_id") Integer userId, @RequestParam("group_id") Integer groupId,
                              HttpServletRequest req, HttpServletResponse resp, Model model) throws IOException, SQLException, DAOException {

        userGroupDAO.deleteUserFromGroup(groupId, userId);
        return getGroupUsers(groupId,req, model);
    }
}
