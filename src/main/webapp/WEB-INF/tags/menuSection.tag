<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ attribute name="active" required="true" rtexprvalue="true" %>
<%@ attribute name="title" required="false" rtexprvalue="true" %>

<li class="py-1">
	<a 
		class="collapsible waves-effect arrow- white-text ${active ? '' : 'collapsed'} pl-5 pr-4"
		data-toggle="collapse"
		role="button"
		aria-expanded="${active ? 'true' : 'false'}"
		aria-controls="multiCollapseExample1"
		href="#${title}"
		title="${fn:escapeXml(title)}">
		${title}
		<i class="float-right fa fa-angle-down rotate-icon"></i>
	</a>
	<div id="${title}" class="${active ? 'show' : ''} white collapse">
		<ul class="pl-3">
        	<jsp:doBody/>
		</ul>
	</div>
</li>