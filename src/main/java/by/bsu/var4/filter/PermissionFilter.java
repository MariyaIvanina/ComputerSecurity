package by.bsu.var4.filter;

import by.bsu.var4.entity.User;
import by.bsu.var4.entity.UserRole;
import by.bsu.var4.exception.DAOException;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class PermissionFilter implements Filter {

    private static final String USER = "user";
    private static final String VIEW_RESOURCE = "/resourceGroup/viewResource";
    private static final String EDIT_RESOURCE = "/resourceGroup/editResource";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        HttpSession session = req.getSession();
        Integer userRole = (Integer) session.getAttribute("role");

        System.out.println("userRole: " + userRole);

        if (userRole == null) {
            RequestDispatcher dispatcher = req.getServletContext().getRequestDispatcher("/WEB-INF/error.jsp");
            dispatcher.forward(req, resp);
            return;
        }

        if (userRole == UserRole.User.ordinal()){
            String path = req.getRequestURI();
            if(!path.equals(VIEW_RESOURCE) && !path.equals(EDIT_RESOURCE)) {
                RequestDispatcher dispatcher = req.getServletContext().getRequestDispatcher("/WEB-INF/error.jsp");
                dispatcher.forward(req, resp);
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}
