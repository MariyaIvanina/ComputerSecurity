<%--
    Document   : login
    Created on : Mar 14, 2015, 11:33:45 PM
    Author     : Mary
--%>

<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
    <link href="<c:url value="/resources/css/united.css"/>" rel="stylesheet" type="text/css">
    <script src="<c:url value="/resources/scripts/jquery-2.1.1.min.js"/>" type="text/javascript"></script>
    <script src="<c:url value="/resources/scripts/bootstrap.js"/>" type="text/javascript"></script>
    <script src="<c:url value="/resources/scripts/jquery.idle.js"/>" type="text/javascript"></script>
    <script src="<c:url value="/resources/scripts/script.js"/>" type="text/javascript"></script>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Log In</title>
</head>
<body>
<div class="navbar navbar-inverse navbar-fixed-top">
    <div class="container">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-responsive-collapse">
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>

        </div>
        <div class="navbar-collapse collapse navbar-responsive-collapse">

            <ul class="nav navbar-nav navbar-right">
                <li> <a href="${pageContext.request.contextPath}/" title="Home">Back</a></li>
                <c:if test="${currentuser == null}">
                    <li><a href="${pageContext.request.contextPath}/login" title ="LogIn" id="LoginPopup">Login</a></li>
                    <li><a href="${pageContext.request.contextPath}/registration" title="Registration">Registration</a></li>
                </c:if>
                <c:if test="${currentuser != null}">
                    <li><a href="${pageContext.request.contextPath}/admin" title ="User" >${currentuser}</a></li>
                    <li><a href="${pageContext.request.contextPath}/logout" title="LogOut">LogOut</a></li>
                </c:if>
            </ul>
        </div>
    </div>
</div>
<div class="container body-content">
    <h1>Resources </h1>
    <form:form commandName="resource" cssClass="form-horizontal" id="editForm">
        <span><form:errors/></span>
        <div class="control-group">
            <label class="control-label">Project Name:</label>
            <div class="controls">
                <form:input cssClass="input-xlarge" path="projectName" value=""/>
                <span class="error"><form:errors path="projectName" /></span>
            </div>
            <label class="control-label">Project Info:</label>
            <div class="controls">
                <form:textarea cssClass="input-xlarge" path="projectInfo" value=""/>
                <span class="error"><form:errors path="projectInfo" /></span>
            </div>
            <div class="controls">
                <form:input cssClass="input-xlarge" path="resourceId" value="" type="hidden"/>
                <span class="error"><form:errors path="resourceId" /></span>
            </div>
        </div>
        <br>
        <div class="form-actions">
            <tr><td></td></tr>
        </div>
    </form:form>

    <!-- Button trigger modal -->
    <button type="button" class="btn btn-primary btn-lg" data-toggle="modal" data-target="#myModal">
        Submit
    </button>

    <!-- Modal -->
    <div class="modal fade " id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
        <div class="modal-dialog modal-sm" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title" id="myModalLabel">Enter pin code:</h4>
                </div>
                <div class="modal-body">
                    <input type="password" name="pinCode" form="editForm" value="">
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                    <input  type="submit" value="Submit" class="btn btn-primary" form="editForm">
                </div>
            </div>
        </div>
    </div>

    <hr />
    <footer>
    </footer>
</div>
</body>
</html>
