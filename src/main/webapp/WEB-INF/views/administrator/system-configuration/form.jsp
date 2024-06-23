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
	<acme:input-textbox code="administrator.system-configuration.form.label.system-currency" path="systemCurrency"/>
	<acme:input-textbox code="administrator.system-configuration.form.label.accepted-currencies" path="acceptedCurrencies"/>
	<acme:input-textarea code="administrator.system-configuration.form.label.spam-terms" path="spamTerms"/>
	<acme:input-double code="administrator.system-configuration.form.label.spam-threshold" path="spamThreshold"/>
	
	<jstl:if test="${acme:anyOf(_command, 'show|update')}">
		<acme:submit code="administrator.system-configuration.form.button.update" action="/administrator/system-configuration/update"/>
	</jstl:if>
</acme:form>
