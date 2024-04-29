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

<acme:list>	
	<acme:list-column code="client.contract.list.label.code" path="code"/>
	<acme:list-column code="client.contract.list.label.project" path="project"/>
	<acme:list-column code="client.contract.list.label.budget" path="budget"/>
	<acme:list-column code="client.contract.list.label.draft-mode" path="draftMode"/>
</acme:list>

<acme:button code="client.contract.list.button.create" action="/client/contract/create"/>