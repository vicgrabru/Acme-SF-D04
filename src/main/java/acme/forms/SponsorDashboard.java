/*
 * SponsorDashboard.java
 *
 * Copyright (C) 2012-2024 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.forms;

import acme.client.data.AbstractForm;
import acme.client.data.datatypes.Money;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SponsorDashboard extends AbstractForm {

	// Serialisation identifier -----------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	private Integer				totalNumberOfInvoicesWithLessOrEqualThan21;

	private Integer				totalNumberOfSponsorshipsWithLink;

	private Money				avgAmountOfSponsorships;
	private Money				devAmountOfSponsorships;
	private Money				minAmountOfSponsorships;
	private Money				maxAmountOfSponsorships;

	private Money				avgQuantityOfInvoices;
	private Money				devQuantityOfInvoices;
	private Money				minQuantityOfInvoices;
	private Money				maxQuantityOfInvoices;

}
