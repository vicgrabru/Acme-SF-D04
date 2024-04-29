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
			<acme:message code="developer.developer-dashboard.form.label.avg"/>
		</th>
		<td>
			<acme:print value="${averageTrainingModuleTime}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="developer.developer-dashboard.form.label.deviation"/>
		</th>
		<td>
			<acme:print value="${deviationTrainingModuleTime}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="developer.developer-dashboard.form.label.max"/>
		</th>
		<td>
			<acme:print value="${maximumTrainingModuleTime}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="developer.developer-dashboard.form.label.min"/>
		</th>
		<td>
			<acme:print value="${minimumTrainingModuleTime}"/>
		</td>
	</tr>
	
</table>
<acme:return/>

