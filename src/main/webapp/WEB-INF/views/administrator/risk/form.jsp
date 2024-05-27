<%--
- form.jsp
-
- Copyright (C) 2012-2024 Rafael Corchuelo.
-
- In keeping with the traditional purpose of furthering education and research, it is
- the policy of the copyright owner to permit non-commercial use and redistribution of
- this software. It has been tested carefully, but it is not guaranteed for any particular
- purposes.  The copyright owner does not offer any warranties or representations, nor do
- they accept any liabilities with respect to them.
--%>

<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:input-textbox code="administrator.risk.form.label.reference" path="reference" readonly="${readOnlyReference}"/>
	<acme:input-moment code="administrator.risk.form.label.identification-date" path="identificationDate"/>
	<acme:input-double code="administrator.risk.form.label.impact" path="impact"/>
	<acme:input-double code="administrator.risk.form.label.probability" path="probability"/>
	<jstl:if test="${readOnlyReference}">
		<acme:input-double readonly="true" code="administrator.risk.form.label.risk-value" path="riskValue"/>
	</jstl:if>
	<acme:input-textarea code="administrator.risk.form.label.description" path="description"/>
	<acme:input-url code="administrator.risk.form.label.link" path="link"/>
	<acme:input-select code="administrator.risk.form.label.project" path="project" choices="${projects}"/>
	
	<jstl:if test="${acme:anyOf(_command, 'show|update|delete')}">
		<acme:submit code="administrator.risk.form.button.update" action="/administrator/risk/update?id=${riskId}"/>
		<acme:submit code="administrator.risk.form.button.delete" action="/administrator/risk/delete?id=${riskId}"/>
		<acme:button code="administrator.risk.form.button.project" action="/any/project/show?id=${projectId}"/>
	</jstl:if>
	<jstl:if test="${acme:matches(_command, 'create')}">
		<acme:submit code="administrator.risk.form.button.create" action="/administrator/risk/create"/>
	</jstl:if>

</acme:form>


