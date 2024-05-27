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
	<acme:input-textbox code="sponsor.sponsorship.form.label.code" path="code" readonly="${readOnlyCode}"/>
	<acme:input-moment code="sponsor.sponsorship.form.label.moment" path="moment" readonly="true"/>
	<acme:input-moment code="sponsor.sponsorship.form.label.startDuration" path="startDuration"/>
	<acme:input-moment code="sponsor.sponsorship.form.label.endDuration" path="endDuration"/>
	<acme:input-money code="sponsor.sponsorship.form.label.amount" path="amount"/>
	<jstl:if test="${acme:anyOf(_command, 'show|update|publish')}">
		<acme:input-money readonly="true" code="sponsor.invoice.form.label.exchanged-amount" path="exchangedAmount"/>
	</jstl:if>
	<acme:input-select code="sponsor.sponsorship.form.label.type" path="type" choices="${types}" readonly="${acme:anyOf(status, 'FINANCIAL|IN_KIND')}"/>
	<acme:input-email code="sponsor.sponsorship.form.label.email" path="email"/>
	<acme:input-url code="sponsor.sponsorship.form.label.link" path="link"/>
	<acme:input-select code="sponsor.sponsorship.form.label.project" path="project" choices="${projects}"/>
	<acme:input-checkbox code="sponsor.sponsorship.form.label.draftmode" path="draftMode" readonly="true"/>
	
	<jstl:choose>
		<jstl:when test="${_command == 'show' && draftMode == false}">
			<acme:button code="sponsor.sponsorship.form.button.invoices" action="/sponsor/invoice/list?masterId=${id}"/>			
		</jstl:when>
		<jstl:when test="${acme:anyOf(_command, 'show|update|delete|publish') && draftMode == true}">
			<acme:button code="sponsor.sponsorship.form.button.invoices" action="/sponsor/invoice/list?masterId=${id}"/>			
			<acme:submit code="sponsor.sponsorship.form.button.update" action="/sponsor/sponsorship/update"/>
			<acme:submit code="sponsor.sponsorship.form.button.delete" action="/sponsor/sponsorship/delete"/>
			<acme:submit code="sponsor.sponsorship.form.button.publish" action="/sponsor/sponsorship/publish"/>
		</jstl:when>
		<jstl:when test="${_command == 'create' }">
			<acme:submit code="sponsor.sponsorship.form.button.create" action="/sponsor/sponsorship/create"/>
		</jstl:when>
	</jstl:choose>
	
</acme:form>
