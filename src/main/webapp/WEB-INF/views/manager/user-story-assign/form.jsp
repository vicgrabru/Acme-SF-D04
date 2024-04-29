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
	<acme:input-select code="manager.user-story-assign.form.label.project" path="project" choices="${projects}"/>
		
	<jstl:if test="${acme:anyOf(_command, 'show|delete')}">
		<acme:submit code="manager.user-story-assign.form.button.delete" action="/manager/user-story-assign/delete?userStoryId=${userStoryId}"/>
	</jstl:if>
	<jstl:if test="${acme:matches(_command, 'create')}">
		<acme:submit code="manager.user-story-assign.form.button.create" action="/manager/user-story-assign/create?userStoryId=${userStoryId}"/>
	</jstl:if>
</acme:form>


