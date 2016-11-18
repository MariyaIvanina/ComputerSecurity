<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
    <link href="<c:url value="/resources/css/united.css"/>" rel="stylesheet" type="text/css">
    <script src="<c:url value="/resources/scripts/jquery-2.1.1.min.js"/>" type="text/javascript"></script>
    <script src="<c:url value="/resources/scripts/bootstrap.js"/>" type="text/javascript"></script>
    <script src="<c:url value="/resources/scripts/jquery.idle.js"/>" type="text/javascript"></script>
    <script src="<c:url value="/resources/scripts/script.js"/>" type="text/javascript"></script>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Welcome</title>
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
                    <li><a href="${pageContext.request.contextPath}/registration" title="Registration">Register</a></li>
                </c:if>
                <c:if test="${currentuser != null && !isAdmin}">
                    <li><a href="${pageContext.request.contextPath}/" title ="User" >${currentuser}</a></li>
                </c:if>
                <c:if test="${currentuser != null && isAdmin}">
                    <li><a href="${pageContext.request.contextPath}/admin" title ="User" >${currentuser}</a></li>
                </c:if>
                <c:if test="${currentuser != null}">
                    <li><a href="${pageContext.request.contextPath}/logout" title ="User" >Logout</a></li>
                </c:if>
            </ul>
        </div>
    </div>
</div>
<div class="container body-content">
    <div class="jumbotron">
        <h1>Corporate Project Viewer</h1>
    </div>
    <c:if test="${resources != null}">
        <table class="table table-striped table-hover">

            <!-- column headers -->
            <thead>
            <th>Resource Name</th>
            <th></th>
            </thead>
            <!-- column data -->
            <tbody>
            <c:forEach var="row" items="${resources}">
                <tr>
                    <td><a href="${pageContext.request.contextPath}/resourceGroup/viewResource?id=${row.getResourceId()}">${row.getResourceName()}</a></td>
                    <td><c:if test="${row.isFullPermission()}">
                        <a class="btn btn-default" href="${pageContext.request.contextPath}/resourceGroup/editResource?id=${row.getResourceId()}" role="button">Edit Resource</a>
                    </c:if>

                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </c:if>

    <hr />
    <footer>
    </footer>
</div>


</body>
</html>
