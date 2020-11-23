<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<%-- Placed at the end of the document so the pages load faster --%>
<spring:url value="/webjars/mdbootstrap-bootstrap-material-design/4.5.3/js/jquery-3.3.1.min.js" var="jQuery"/>
<script src="${jQuery}"></script>

<spring:url value="/webjars/mdbootstrap-bootstrap-material-design/4.5.3/js/bootstrap.min.js" var="bootstrapJs"/>
<script src="${bootstrapJs}"></script>

<spring:url value="/webjars/mdbootstrap-bootstrap-material-design/4.5.3/js/popper.min.js" var="popperJs"/>
<script src="${popperJs}"></script>

<spring:url value="/webjars/mdbootstrap-bootstrap-material-design/4.5.3/js/mdb.min.js" var="mdbJs"/>
<script src="${mdbJs}"></script>

<spring:url value="/webjars/bootstrap-datepicker/1.9.0/js/bootstrap-datepicker.min.js" var="bootstrapDatepicker"/>
<script src="${bootstrapDatepicker}"></script>

<spring:url value="/webjars/bootstrap-datepicker/1.9.0/locales/bootstrap-datepicker.es.min.js" var="bootstrapDatepickerLocale"/>
<script src="${bootstrapDatepickerLocale}"></script>
