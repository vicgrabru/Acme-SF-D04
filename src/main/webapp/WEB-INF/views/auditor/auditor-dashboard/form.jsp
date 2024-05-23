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

<table class="table table-sm">
	<tr>
		<th scope="row">
			<acme:message code="auditor.auditor-dashboard.form-label.total-number-of-static-code-audits"/>
		</th>
		<td>
			<acme:print value="${totalNumberOfStaticCodeAudits}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="auditor.auditor-dashboard.form-label.total-number-of-dynamic-code-audits"/>
		</th>
		<td>
			<acme:print value="${totalNumberOfDynamicCodeAudits}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="auditor.auditor-dashboard.form-label.avg-audit-records-per-code-audit"/>
		</th>
		<td>
			<acme:print value="${avgAuditRecordsPerCodeAudit}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="auditor.auditor-dashboard.form-label.min-audit-records-per-code-audit"/>
		</th>
		<td>
			<acme:print value="${minAuditRecordsPerCodeAudit}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="auditor.auditor-dashboard.form-label.max-audit-records-per-code-audit"/>
		</th>
		<td>
			<acme:print value="${maxAuditRecordsPerCodeAudit}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="auditor.auditor-dashboard.form-label.std-audit-records-per-code-audit"/>
		</th>
		<td>
			<acme:print value="${stdAuditRecordsPerCodeAudit}"/>
		</td>
	</tr>
	<tr>
 		<th scope="row">
			<acme:message code="auditor.auditor-dashboard.form-label.avg-audit-record-period-length"/>
		</th>
		<td>
			<acme:print value="${avgAuditRecordPeriodLength}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="auditor.auditor-dashboard.form-label.min-audit-record-period-length"/>
		</th>
		<td>
			<acme:print value="${minAuditRecordPeriodLength}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="auditor.auditor-dashboard.form-label.max-audit-record-period-length"/>
		</th>
		<td>
			<acme:print value="${maxAuditRecordPeriodLength}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="auditor.auditor-dashboard.form-label.std-audit-record-period-length"/>
		</th>
		<td>
			<acme:print value="${stdAuditRecordPeriodLength}"/>
		</td>
	</tr>
</table>