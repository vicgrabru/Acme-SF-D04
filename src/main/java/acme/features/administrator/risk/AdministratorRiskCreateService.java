/*
 * AdministratorRiskCreateService.java
 *
 * Copyright (C) 2012-2024 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.features.administrator.risk;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Administrator;
import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.risk.Risk;
import acme.utils.SpamRepository;

@Service
public class AdministratorRiskCreateService extends AbstractService<Administrator, Risk> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AdministratorRiskRepository	repository;

	@Autowired
	private SpamRepository				spamRepository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Risk object;

		object = new Risk();

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final Risk object) {
		assert object != null;

		super.bind(object, "reference", "identificationDate", "impact", "probability", "description", "link");

	}

	@Override
	public void validate(final Risk object) {
		assert object != null;

		if (!super.getBuffer().getErrors().hasErrors("description"))
			super.state(!this.spamRepository.checkTextValue(object.getDescription()), "description", "administrator.risk.form.error.spam-in-description");

		if (!super.getBuffer().getErrors().hasErrors("reference")) {
			Risk existing;

			existing = this.repository.findOneRiskByReference(object.getReference());
			super.state(existing == null, "reference", "administrator.risk.form.error.reference-duplicated");
		}
	}

	@Override
	public void perform(final Risk object) {
		assert object != null;

		object.setId(0);
		this.repository.save(object);
	}

	@Override
	public void unbind(final Risk object) {
		assert object != null;

		Dataset dataset;

		dataset = super.unbind(object, "reference", "identificationDate", "impact", "probability", "description", "link");
		dataset.put("readOnlyReference", false);

		super.getResponse().addData(dataset);
	}

}
