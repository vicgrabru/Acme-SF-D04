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
			<acme:message code="client.client-dashboard.form.label.below-25-completeness-progress-logs"/>
		</th>
		<td>
			<acme:print value="${below25CompletenessProgressLogs}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="client.client-dashboard.form.label.between-25-and-50-completeness-progress-logs"/>
		</th>
		<td>
			<acme:print value="${between25and50CompletenessProgressLogs}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="client.client-dashboard.form.label.between-50-and-75-completeness-progress-logs"/>
		</th>
		<td>
			<acme:print value="${between50and75CompletenessProgressLogs}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="client.client-dashboard.form.label.above-75-completeness-progress-logs"/>
		</th>
		<td>
			<acme:print value="${above75CompletenessProgressLogs}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="client.client-dashboard.form.label.avg-contract-budget"/>
		</th>
		<td>
			<acme:print value="${avgContractBudget}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="client.client-dashboard.form.label.std-contract-budget"/>
		</th>
		<td>
			<acme:print value="${stdContractBudget}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="client.client-dashboard.form.label.min-contract-budget"/>
		</th>
		<td>
			<acme:print value="${minContractBudget}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="client.client-dashboard.form.label.max-contract-budget"/>
		</th>
		<td>
			<acme:print value="${maxContractBudget}"/>
		</td>
	</tr>
</table>
<acme:return/>

