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

<h2>
	<acme:message code="administrator.administrator-dashboard.form.title.general-indicators"/>
</h2>

<table class="table table-sm">
	<tr>
		<th scope="row">
			<acme:message code="administrator.administrator-dashboard.form.label.number-administrator-principals"/>
		</th>
		<td>
			<acme:print value="${numberOfPrincipalsByRole.get('Administrator')}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="administrator.administrator-dashboard.form.label.number-auditor-principals"/>
		</th>
		<td>
			<acme:print value="${numberOfPrincipalsByRole.get('Auditor')}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="administrator.administrator-dashboard.form.label.number-client-principals"/>
		</th>
		<td>
			<acme:print value="${numberOfPrincipalsByRole.get('Administrator')}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="administrator.administrator-dashboard.form.label.number-consumer-principals"/>
		</th>
		<td>
			<acme:print value="${numberOfPrincipalsByRole.get('Consumer')}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="administrator.administrator-dashboard.form.label.number-developer-principals"/>
		</th>
		<td>
			<acme:print value="${numberOfPrincipalsByRole.get('Developer')}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="administrator.administrator-dashboard.form.label.number-manager-principals"/>
		</th>
		<td>
			<acme:print value="${numberOfPrincipalsByRole.get('Manager')}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="administrator.administrator-dashboard.form.label.number-provider-principals"/>
		</th>
		<td>
			<acme:print value="${numberOfPrincipalsByRole.get('Provider')}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="administrator.administrator-dashboard.form.label.number-sponsor-principals"/>
		</th>
		<td>
			<acme:print value="${numberOfPrincipalsByRole.get('Sponsor')}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="administrator.administrator-dashboard.form.label.ratio-of-notices-with-email-and-link"/>
		</th>
		<td>
			<acme:print value="${100.0 * ratioOfNoticesWithEmailAdressAndLink} %"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="administrator.administrator-dashboard.form.label.ratio-of-critical-objectives"/>
		</th>
		<td>
			<acme:print value="${100.0 * ratioOfCriticalObjectives} %"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="administrator.administrator-dashboard.form.label.ratio-of-non-critical-objectives"/>
		</th>
		<td>
			<acme:print value="${100.0* ratioOfNonCriticalObjectives} %"/>
		</td>
	</tr>
	
	<tr>
		<th scope="row">
			<acme:message code="administrator.administrator-dashboard.form.label.avg-risk-value"/>
		</th>
		<td>
			<acme:print value="${avgRiskValue}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="administrator.administrator-dashboard.form.label.min-risk-value"/>
		</th>
		<td>
			<acme:print value="${minRiskValue}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="administrator.administrator-dashboard.form.label.max-risk-value"/>
		</th>
		<td>
			<acme:print value="${maxRiskValue}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="administrator.administrator-dashboard.form.label.std-risk-value"/>
		</th>
		<td>
			<acme:print value="${stdRiskValue}"/>
		</td>
	</tr>
	
	<tr>
		<th scope="row">
			<acme:message code="administrator.administrator-dashboard.form.label.avg-number-claims"/>
		</th>
		<td>
			<acme:print value="${avgNumberOfClaims}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="administrator.administrator-dashboard.form.label.min-number-claims"/>
		</th>
		<td>
			<acme:print value="${minNumberOfClaims}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="administrator.administrator-dashboard.form.label.max-number-claims"/>
		</th>
		<td>
			<acme:print value="${maxNumberOfClaims}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="administrator.administrator-dashboard.form.label.std-number-claims"/>
		</th>
		<td>
			<acme:print value="${stdNumberOfClaims}"/>
		</td>
	</tr>
	
	
</table>
<acme:return/>

