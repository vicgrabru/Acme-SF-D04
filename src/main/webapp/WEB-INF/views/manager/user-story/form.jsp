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
	<acme:input-textbox code="manager.user-story.form.label.title" path="title"/>
	<acme:input-textarea code="manager.user-story.form.label.description" path="description"/>
	<acme:input-integer code="manager.user-story.form.label.estimated-cost" path="estimatedCost"/>
	<acme:input-textarea code="manager.user-story.form.label.acceptance-criteria" path="acceptanceCriteria"/>
	<acme:input-select code="manager.user-story.form.label.priority" path="priority" choices="${priorities}"/>
	<acme:input-url code="manager.user-story.form.label.optional-link" path="optionalLink"/>
	<acme:input-checkbox code="manager.user-story.form.label.draft-mode" path="draftMode" readonly="true"/>
	
	
	<jstl:if test="${acme:anyOf(_command, 'show|update|delete|publish') && draftMode == true}">
		<acme:submit code="manager.user-story.form.button.update" action="/manager/user-story/update"/>
		<acme:submit code="manager.user-story.form.button.delete" action="/manager/user-story/delete"/>
		<acme:submit code="manager.user-story.form.button.publish" action="/manager/user-story/publish"/>
	</jstl:if>
	<jstl:choose>
		<jstl:when test="${acme:matches(_command, 'create')}">
			<acme:submit code="manager.user-story.form.button.create" action="/manager/user-story/create?masterId=${masterId}"/>
		</jstl:when>
		<jstl:otherwise>
			<jstl:if test="${showAssignButton}">
				<acme:button code="manager.user-story.form.button.assign" action="/manager/user-story-assign/create?userStoryId=${userStoryId}"/>
			</jstl:if>
			<jstl:if test="${showUnassignButton}">
				<acme:button code="manager.user-story.form.button.unassign" action="/manager/user-story-assign/delete?userStoryId=${userStoryId}"/>
			</jstl:if>
		</jstl:otherwise>
	</jstl:choose>
</acme:form>


