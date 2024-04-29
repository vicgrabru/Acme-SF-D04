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
	<acme:message code="manager.manager-dashboard.form.title.general-indicators"/>
</h2>

<table class="table table-sm">
	<tr>
		<th scope="row">
			<acme:message code="manager.manager-dashboard.form.label.avg-cost-user-stories"/>
		</th>
		<td>
			<acme:print value="${avgEstimatedCostOfUserStories}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="manager.manager-dashboard.form.label.min-cost-user-stories"/>
		</th>
		<td>
			<acme:print value="${minEstimatedCostOfUserStories}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="manager.manager-dashboard.form.label.max-cost-user-stories"/>
		</th>
		<td>
			<acme:print value="${maxEstimatedCostOfUserStories}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="manager.manager-dashboard.form.label.std-cost-user-stories"/>
		</th>
		<td>
			<acme:print value="${stdEstimatedCostOfUserStories}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="manager.manager-dashboard.form.label.avg-cost-projects"/>
		</th>
		<td>
			<acme:print value="${avgCostOfProjects}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="manager.manager-dashboard.form.label.min-cost-projects"/>
		</th>
		<td>
			<acme:print value="${minCostOfProjects}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="manager.manager-dashboard.form.label.max-cost-projects"/>
		</th>
		<td>
			<acme:print value="${maxCostOfProjects}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="manager.manager-dashboard.form.label.std-cost-projects"/>
		</th>
		<td>
			<acme:print value="${stdCostOfProjects}"/>
		</td>
	</tr>
</table>

<h2>
	<acme:message code="manager.manager-dashboard.form.title.user-story-priorities"/>
</h2>

<div>
	<canvas id="canvas"></canvas>
</div>

<script type="text/javascript">
	$(document).ready(function() {
		var data = {
			labels : [
					"MUST", "SHOULD", "COULD", "WONT"
			],
			datasets : [
				{
					data : [
						<jstl:out value="${totalNumberOfUserStoriesWithMustPriority}"/>, 
						<jstl:out value="${totalNumberOfUserStoriesWithShouldPriority}"/>, 
						<jstl:out value="${totalNumberOfUserStoriesWithCouldPriority}"/>, 
						<jstl:out value="${totalNumberOfUserStoriesWithWontPriority}"/>
					]
				}
			]
		};
		var options = {
			scales : {
				yAxes : [
					{
						ticks : {
							suggestedMin : 0.0,
							suggestedMax : 1.0
						}
					}
				]
			},
			legend : {
				display : false
			}
		};
	
		var canvas, context;
	
		canvas = document.getElementById("canvas");
		context = canvas.getContext("2d");
		new Chart(context, {
			type : "bar",
			data : data,
			options : options
		});
	});
</script>

<acme:return/>

