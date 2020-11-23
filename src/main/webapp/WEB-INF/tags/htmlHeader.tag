<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<%--
PetClinic :: a Spring Framework demonstration
--%>

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <%-- The above 4 meta tags *must* come first in the head; any other head content must come *after* these tags --%>

    <spring:url value="/resources/images/favicon.png" var="favicon"/>
    <link rel="shortcut icon" type="image/x-icon" href="${favicon}">

    <title>PetClinic :: a Spring Framework demonstration</title>
    
    <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.11.2/css/all.css">
	<link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Roboto:300,400,500,700&display=swap">
	
    <%-- VENDOR CSS --%>
    <spring:url value="/resources/css/vendor.css" var="vendorCss"/>
    <link href="${vendorCss}" rel="stylesheet"/>
    
    <spring:url value="/resources/css/mdb.css" var="mdbCss"/>
    <link href="${mdbCss}" rel="stylesheet"/>
    
    <%-- CSS generated from SCSS --%>
    <spring:url value="/webjars/bootstrap-datepicker/1.9.0/css/bootstrap-datepicker.min.css" var="bootstrapDatepickerCss"/>
    <link href="${bootstrapDatepickerCss}" rel="stylesheet"/>
    
    <%-- CSS generated from SCSS --%>
    <spring:url value="/resources/css/petclinic.css" var="petclinicCss"/>
    <link href="${petclinicCss}" rel="stylesheet"/>


    <%-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries --%>
    <!--[if lt IE 9]>
    <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
    <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->
</head>
