<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ attribute name="active" required="true" rtexprvalue="true" %>
<%@ attribute name="url" required="true" rtexprvalue="true" %>
<%@ attribute name="title" required="false" rtexprvalue="true" %>

<li class="py-1">
	<a
		href="<spring:url value="${url}" htmlEscape="true" />"
	 	title="${fn:escapeXml(title)}"
		class="${active ? 'active' : ''} pl-5 pr-3"
	>
		<jsp:doBody/>
	</a>
</li>