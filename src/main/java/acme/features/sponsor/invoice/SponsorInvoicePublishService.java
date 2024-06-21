
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
public class SponsorInvoicePublishService extends AbstractService<Sponsor, Invoice> {

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

		super.bind(object, "code", "registrationTime", "dueDate", "quantity", "tax", "link", "draftMode");
	}

	@Override
	public void validate(final Invoice object) {

		if (!super.getBuffer().getErrors().hasErrors("dueDate"))
			super.state(MomentHelper.isLongEnough(object.getRegistrationTime(), object.getDueDate(), 1, ChronoUnit.MONTHS), "dueDate", "sponsor.invoice.form.error.atLeast1MonthLong");

	}

	@Override
	public void perform(final Invoice object) {
		assert object != null;
		object.setDraftMode(false);
		this.repository.save(object);
	}

	@Override
	public void unbind(final Invoice object) {
		assert object != null;

		Dataset dataset;

		dataset = super.unbind(object, "code", "registrationTime", "dueDate", "quantity", "tax", "link", "draftMode");
		dataset.put("masterId", object.getSponsorship().getId());
		dataset.put("sponsorship", object.getSponsorship().getCode());

		dataset.put("totalAmount", object.totalAmount());
		Money eb = this.exchangeRepo.exchangeMoney(object.totalAmount());
		dataset.put("exchangedTotalAmount", eb);
		dataset.put("readOnlyCode", true);

		super.getResponse().addData(dataset);
	}

}
