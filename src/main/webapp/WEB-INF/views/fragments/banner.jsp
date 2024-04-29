<%--
- banner.jsp
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


<jstl:choose>
		<jstl:when test="${nAvailableBanners>0}">
			<div class="rounded" style="background: <acme:message code='master.banner.background'/>">
				<img src="${bannerToDisplay.pictureLink}" id="imagenBanner" style="width: 100% !important;max-height: 300px"
					alt="<acme:message code='master.banner.alt'/>" class="img-fluid mx-auto d-block rounded" />
			</div>
		</jstl:when>
		<jstl:otherwise>
			<div class="rounded" style="background: <acme:message code='master.banner.background'/>">
				<img src="images/banner.png" alt="<acme:message code='master.banner.alt'/>" class="img-fluid rounded"/>
			</div>
		</jstl:otherwise>
</jstl:choose>

