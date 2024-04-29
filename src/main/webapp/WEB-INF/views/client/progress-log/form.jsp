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
	<acme:input-textbox code="client.progress-log.form.label.record-id" path="recordId" readonly="${readOnlyCode}"/>
	<acme:input-double code="client.progress-log.form.label.completeness" path="completeness"  placeholder="client.progress-log.form.completeness-interval"/>
	<acme:input-textarea code="client.progress-log.form.label.comment" path="comment"/>
	<acme:input-textbox code="client.progress-log.form.label.responsible-person" path="responsiblePerson"/>
	<acme:input-moment readonly="true" code="client.progress-log.form.label.registration-moment" path="registrationMoment"/>
	<acme:input-checkbox readonly="true" code="client.progress-log.form.label.draft-mode" path="draftMode"/>
	
	<jstl:if test="${acme:matches(_command, 'create')}">
			<acme:submit code="client.progress-log.form.button.create" action="/client/progress-log/create?contractId=${contractId}"/>
	</jstl:if>
	<jstl:if test="${acme:anyOf(_command, 'show|update|delete|publish') && draftMode == true}">
		<acme:submit code="client.progress-log.form.button.update" action="/client/progress-log/update"/>
		<acme:submit code="client.progress-log.form.button.delete" action="/client/progress-log/delete"/>
		<acme:submit code="client.progress-log.form.button.publish" action="/client/progress-log/publish"/>
	</jstl:if>
	
</acme:form>
