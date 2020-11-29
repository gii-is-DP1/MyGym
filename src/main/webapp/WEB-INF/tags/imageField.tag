<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags"%>
<%@ attribute name="name" required="true" rtexprvalue="true"
	description="Name of corresponding property in bean object"%>
<%@ attribute name="label" required="true" rtexprvalue="true"
	description="Label appears in red color if input is considered as invalid after submission"%>
<%@ attribute name="tip" required="false" rtexprvalue="true"
	description="A tip to show input constraints like recommended bounds or max size"%>

<spring:bind path="${name}">
	<c:set var="cssGroup" value="form-group ${status.error ? 'has-error' : '' }"/>
    <c:set var="valid" value="${not status.error and not empty status.actualValue}"/>
    <div class="${cssGroup}">
        <label class="control-label">${label}</label>

        <div class="col col-sm-10 col-md-6 pl-0 image-picker">
			<form:input class="form-control" path="${name}" type="hidden"/>
			
			<div class="preview" hidden>
				<img alt="preview">
			</div>
			
			<div class="custom-file">
				<input type="file" class="custom-file-input" id="input-file-${name}">
				<label class="custom-file-label" for="input-file-${name}">Elegir imagen</label>
			</div>
			<c:if test="${not empty tip}">
				<small class="tip">${tip}</small>
			</c:if>
			
            <c:if test="${valid}">
                <span class="glyphicon glyphicon-ok form-control-feedback" aria-hidden="true"></span>
            </c:if>
            <c:if test="${status.error}">
                <span class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>
                <span class="help-inline">${status.errorMessage}</span>
            </c:if>
        </div>
    </div>
</spring:bind>
