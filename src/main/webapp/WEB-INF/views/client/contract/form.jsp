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
	<acme:input-textbox code="client.contract.form.label.code" path="code" readonly="${readOnlyCode}"/>
	<acme:input-textarea code="client.contract.form.label.goals" path="goals"/>
	<acme:input-money code="client.contract.form.label.budget" path="budget"/>
	<jstl:if test="${acme:anyOf(_command, 'show|publish|update')}">
		<acme:input-money readonly="true" code="client.contract.form.label.exchanged-budget" path="exchangedBudget"/>
		<jstl:if test="${acme:anyOf(_command, 'show|publish|update') && draftMode == true}">
			<acme:input-money readonly="true" code="client.contract.form.label.remaining-cost" path="remainingCost"/>
		</jstl:if>
	</jstl:if>
	<acme:input-textbox code="client.contract.form.label.provider-name" path="providerName"/>
	<acme:input-textbox code="client.contract.form.label.customer-name" path="customerName"/>
	<acme:input-moment readonly="true" code="client.contract.form.label.instantiation-moment" path="instantiationMoment"/>
	<acme:input-checkbox readonly="true" code="client.contract.form.label.draft-mode" path="draftMode"/>
	
	<jstl:choose>
		<jstl:when test="${acme:anyOf(_command, 'show|update|publish')}">
			<acme:input-select readonly="true" code="client.contract.form.label.project" path="project" choices="${projects}"/>
			<acme:button code="client.contract.form.button.project" action="/any/project/show?id=${projectId}"/>
			<acme:button code="client.contract.form.button.progress-log.list" action="/client/progress-log/list?contractId=${contractId}"/>
		</jstl:when>
		<jstl:when test="${acme:matches(_command, 'create')}">
			<acme:input-select code="client.contract.form.label.project" path="project" choices="${projects}"/>
			<acme:submit code="client.contract.form.button.create" action="/client/contract/create"/>
		</jstl:when>
	</jstl:choose>

	<jstl:if test="${acme:anyOf(_command, 'show|update|delete|publish') && draftMode == true}">
		<acme:submit code="client.contract.form.button.update" action="/client/contract/update?id=${contractId}"/>
		<acme:submit code="client.contract.form.button.delete" action="/client/contract/delete"/>
		<acme:submit code="client.contract.form.button.publish" action="/client/contract/publish?id=${contractId}"/>
	</jstl:if>
	
</acme:form>
