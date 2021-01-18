<%@ page contentType="text/html;charset=UTF-8" session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="/WEB-INF/jsp-utils.tld" prefix="util" %>

<petclinic:layout pageName="assignWorkout">
    <jsp:body>
	    <h2 class="mb-5">
	        <c:out value="${message}" />
	    </h2>
    </jsp:body>
</petclinic:layout>








