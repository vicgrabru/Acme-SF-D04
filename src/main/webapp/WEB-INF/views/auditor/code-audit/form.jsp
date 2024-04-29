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

	<acme:input-textbox code="auditor.code-audit.form.label.code" path="code" readonly="${readOnlyCode}"/>	
	<acme:input-moment readonly="true" code="auditor.code-audit.form.label.execution-date" path="executionDate"/>
	<acme:input-select code="auditor.code-audit.form.label.type" path="type" choices="${types}"/>
	<acme:input-textarea code="auditor.code-audit.form.label.corrective-actions" path="correctiveActions"/>
	<acme:input-textbox readonly="true" code="auditor.code-audit.form.label.mark" path="mark"/>
	<acme:input-url code="auditor.code-audit.form.label.link" path="link"/>
	<acme:input-checkbox readonly="true" code="auditor.code-audit.form.label.draft-mode" path="draftMode"/>
	
	<jstl:choose>
		<jstl:when test="${acme:anyOf(_command, 'show|update|publish')}">
			<acme:input-select readonly="true" code="auditor.code-audit.form.label.project" path="project" choices="${projects}"/>
			<acme:button code="auditor.code-audit.form.button.project" action="/any/project/show?id=${projectId}"/>
			<acme:button code="auditor.code-audit.form.button.audit-record.list" action="/auditor/audit-record/list?codeAuditId=${codeAuditId}"/>
		</jstl:when>
		<jstl:when test="${acme:matches(_command, 'create')}">
			<acme:input-select code="auditor.code-audit.form.label.project" path="project" choices="${projects}"/>
			<acme:submit code="auditor.code-audit.form.button.create" action="/auditor/code-audit/create"/>
		</jstl:when>
	</jstl:choose>

	<jstl:if test="${acme:anyOf(_command, 'show|update|delete|publish') && draftMode == true}">
		<acme:submit code="auditor.code-audit.form.button.update" action="/auditor/code-audit/update?id=${codeAuditId}"/>
		<acme:submit code="auditor.code-audit.form.button.delete" action="/auditor/code-audit/delete"/>
		<acme:submit code="auditor.code-audit.form.button.publish" action="/auditor/code-audit/publish?id=${codeAuditId}"/>
	</jstl:if>
	
</acme:form>
