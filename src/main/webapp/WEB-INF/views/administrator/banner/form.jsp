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
	<acme:input-textbox code="administrator.banner.form.label.slogan" path="slogan"/>
	<acme:input-url code="administrator.banner.form.label.picture-link" path="pictureLink"/>
	<acme:input-moment code="administrator.banner.form.label.period-start" path="periodStart"/>
	<acme:input-moment code="administrator.banner.form.label.period-end" path="periodEnd"/>
	<acme:input-moment code="administrator.banner.form.label.instantiation-moment" path="instantiationMoment" readonly="true"/>
	<acme:input-url code="administrator.banner.form.label.web-document-link" path="webDocumentLink"/>
	
	
	<jstl:if test="${acme:anyOf(_command, 'show|update|delete')}">
		<acme:submit code="administrator.banner.form.button.update" action="/administrator/banner/update"/>
		<acme:submit code="administrator.banner.form.button.delete" action="/administrator/banner/delete"/>
	</jstl:if>
	<jstl:if test="${acme:matches(_command, 'create')}">
		<acme:submit code="administrator.banner.form.button.create" action="/administrator/banner/create"/>
	</jstl:if>
</acme:form>


