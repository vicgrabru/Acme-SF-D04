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
	<acme:input-textbox code="auditor.audit-record.form.label.code" path="code"/>
	<acme:input-moment code="auditor.audit-record.form.label.period-start" path="periodStart"/>
	<acme:input-moment code="auditor.audit-record.form.label.period-end" path="periodEnd"/>
	<acme:input-select code="auditor.audit-record.form.label.mark" path="mark" choices="${marks}"/>
	<acme:input-url code="auditor.audit-record.form.label.link" path="link"/>
	<acme:input-checkbox readonly="true" code="auditor.audit-record.form.label.draft-mode" path="draftMode"/>
	
	<jstl:choose>
		<jstl:when test="${acme:matches(_command, 'create')}">
			<acme:submit code="auditor.audit-record.form.button.create" action="/auditor/audit-record/create?codeAuditId=${codeAuditId}"/>
		</jstl:when>
	</jstl:choose>
	<jstl:if test="${acme:anyOf(_command, 'show|update|delete|publish') && draftMode == true}">
		<acme:submit code="auditor.audit-record.form.button.update" action="/auditor/audit-record/update?id=${auditRecordId}"/>
		<acme:submit code="auditor.audit-record.form.button.delete" action="/auditor/audit-record/delete?id=${auditRecordId}"/>
		<acme:submit code="auditor.audit-record.form.button.publish" action="/auditor/audit-record/publish?id=${auditRecordId}"/>
	</jstl:if>
	
</acme:form>
