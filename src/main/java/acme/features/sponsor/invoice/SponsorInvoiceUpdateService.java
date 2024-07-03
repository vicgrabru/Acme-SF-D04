/*
 * SponsorshipInvoiceUpdateService.java
 *
 * Copyright (C) 2012-2024 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.features.sponsor.invoice;

import java.time.temporal.ChronoUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.datatypes.Money;
import acme.client.data.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractService;
import acme.entities.sponsorship.Invoice;
import acme.entities.sponsorship.Sponsorship;
import acme.roles.Sponsor;
import acme.utils.MoneyExchangeRepository;

@Service
public class SponsorInvoiceUpdateService extends AbstractService<Sponsor, Invoice> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private SponsorInvoiceRepository	repository;

	@Autowired
	private MoneyExchangeRepository		exchangeRepo;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int masterId;
		Invoice invoice;
		Sponsorship sponsorship;

		masterId = super.getRequest().getData("id", int.class);
		invoice = this.repository.findOneInvoiceById(masterId);
		sponsorship = invoice == null ? null : invoice.getSponsorship();
		status = invoice != null && invoice.isDraftMode() && super.getRequest().getPrincipal().hasRole(sponsorship.getSponsor());

		super.getResponse().setAuthorised(status);

	}

	@Override
	public void load() {
		Invoice object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findOneInvoiceById(id);

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final Invoice object) {
		assert object != null;

		super.bind(object, "registrationTime", "dueDate", "quantity", "tax", "link");

	}

	@Override
	public void validate(final Invoice object) {
		assert object != null;
		String currencies;

		if (!super.getBuffer().getErrors().hasErrors("quantity")) {
			currencies = this.repository.findAcceptedCurrencies();
			super.state(currencies.contains(object.getQuantity().getCurrency()), "quantity", "sponsor.invoice.form.error.quantity.invalid-currency");
		}

		if (!super.getBuffer().getErrors().hasErrors("quantity"))
			super.state(object.getQuantity().getAmount() >= 0., "quantity", "sponsor.invoice.form.error.quantity.no-negative");

		if (!super.getBuffer().getErrors().hasErrors("dueDate"))
			super.state(MomentHelper.isLongEnough(object.getRegistrationTime(), object.getDueDate(), 1, ChronoUnit.MONTHS), "dueDate", "sponsor.invoice.form.error.atLeast1MonthLong");
	}

	@Override
	public void perform(final Invoice object) {
		assert object != null;

		this.repository.save(object);
	}

	@Override
	public void unbind(final Invoice object) {
		assert object != null;

		Dataset dataset;

		dataset = super.unbind(object, "code", "registrationTime", "dueDate", "quantity", "tax", "link", "draftMode");
		dataset.put("masterId", object.getSponsorship().getId());

		if (object.getQuantity() != null) {
			Money eb = this.exchangeRepo.exchangeMoney(object.getQuantity());
			dataset.put("exchangedQuantity", eb);
		}
		dataset.put("readOnlyCode", true);

		super.getResponse().addData(dataset);
	}
}
