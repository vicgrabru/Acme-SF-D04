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
	<acme:input-textbox code="authenticated.client.form.label.identification" path="identification" readonly="${readOnlyCode}"/>
	<acme:input-textbox code="authenticated.client.form.label.company-name" path="companyName"/>
	<acme:input-select code="authenticated.client.form.label.type" path="type" choices="${types}"/>
	<acme:input-email code="authenticated.client.form.label.email" path="email"/>
	<acme:input-url code="authenticated.client.form.label.optional-link" path="optionalLink"/>
	
	<acme:submit test="${_command == 'create'}" code="authenticated.client.form.button.create" action="/authenticated/client/create"/>
	<acme:submit test="${_command == 'update'}" code="authenticated.client.form.button.update" action="/authenticated/client/update"/>
</acme:form>
