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
	<acme:message code="sponsor.sponsor-dashboard.form.title.general-indicators"/>
</h2>

<table class="table table-sm">
	<tr>
		<th scope="row">
			<acme:message code="sponsor.sponsor-dashboard.form.label.total-number-of-invoices-with-less-or-equal-than-21"/>
		</th>
		<td>
			<acme:print value="${totalNumberOfInvoicesWithLessOrEqualThan21}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="sponsor.sponsor-dashboard.form.label.total-number-of-sponsorships-with-link"/>
		</th>
		<td>
			<acme:print value="${totalNumberOfSponsorshipsWithLink}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="sponsor.sponsor-dashboard.form.label.avg-amount-of-sponsorships"/>
		</th>
		<td>
			<acme:print value="${avgAmountOfSponsorships}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="sponsor.sponsor-dashboard.form.label.dev-amount-of-sponsorships"/>
		</th>
		<td>
			<acme:print value="${devAmountOfSponsorships}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="sponsor.sponsor-dashboard.form.label.min-amount-of-sponsorships"/>
		</th>
		<td>
			<acme:print value="${minAmountOfSponsorships}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="sponsor.sponsor-dashboard.form.label.max-amount-of-sponsorships"/>
		</th>
		<td>
			<acme:print value="${maxAmountOfSponsorships}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="sponsor.sponsor-dashboard.form.label.avg-quantity-of-invoices"/>
		</th>
		<td>
			<acme:print value="${avgQuantityOfInvoices}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="sponsor.sponsor-dashboard.form.label.dev-quantity-of-invoices"/>
		</th>
		<td>
			<acme:print value="${devQuantityOfInvoices}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="sponsor.sponsor-dashboard.form.label.min-quantity-of-invoices"/>
		</th>
		<td>
			<acme:print value="${minQuantityOfInvoices}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="sponsor.sponsor-dashboard.form.label.max-quantity-of-invoices"/>
		</th>
		<td>
			<acme:print value="${maxQuantityOfInvoices}"/>
		</td>
	</tr>
</table>