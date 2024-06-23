/*
 * AnyClaimCreateService.java
 *
 * Copyright (C) 2012-2024 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.features.any.claim;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Any;
import acme.client.data.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractService;
import acme.entities.claim.Claim;
import acme.utils.SpamRepository;

@Service
public class AnyClaimCreateService extends AbstractService<Any, Claim> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AnyClaimRepository	repository;

	@Autowired
	private SpamRepository		spamRepository;

	// AbstractService interface  ---------------------------------------------------------	


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Claim object;

		object = new Claim();
		object.setInstantiationMoment(MomentHelper.getCurrentMoment());

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final Claim object) {
		assert object != null;

		super.bind(object, "code", "heading", "description", "department", "email", "link");

	}

	@Override
	public void validate(final Claim object) {
		assert object != null;

		if (!super.getBuffer().getErrors().hasErrors("code")) {
			Claim existing;
			existing = this.repository.findOneClaimByCode(object.getCode());
			super.state(existing == null, "code", "any.claim.form.error.duplicated");

		}
		if (!super.getBuffer().getErrors().hasErrors("confirmation"))
			super.state(super.getRequest().getData("confirmation", boolean.class), "confirmation", "any.claim.form.error.not-confirmed");
		if (!super.getBuffer().getErrors().hasErrors("code"))
			super.state(!this.spamRepository.checkTextValue(super.getRequest().getData("code", String.class)), "code", "any.claim.form.error.spam");
		if (!super.getBuffer().getErrors().hasErrors("heading"))
			super.state(!this.spamRepository.checkTextValue(super.getRequest().getData("heading", String.class)), "heading", "any.claim.form.error.spam");
		if (!super.getBuffer().getErrors().hasErrors("description"))
			super.state(!this.spamRepository.checkTextValue(super.getRequest().getData("description", String.class)), "description", "any.claim.form.error.spam");
		if (!super.getBuffer().getErrors().hasErrors("department"))
			super.state(!this.spamRepository.checkTextValue(super.getRequest().getData("department", String.class)), "department", "any.claim.form.error.spam");
		if (!super.getBuffer().getErrors().hasErrors("email"))
			super.state(!this.spamRepository.checkTextValue(super.getRequest().getData("email", String.class)), "email", "any.claim.form.error.spam");
		if (!super.getBuffer().getErrors().hasErrors("link"))
			super.state(!this.spamRepository.checkTextValue(super.getRequest().getData("link", String.class)), "link", "any.claim.form.error.spam");

	}

	@Override
	public void perform(final Claim object) {
		assert object != null;

		object.setId(0);
		this.repository.save(object);
	}

	@Override
	public void unbind(final Claim object) {
		assert object != null;

		Dataset dataset;

		dataset = super.unbind(object, "code", "instantiationMoment", "heading", "description", "department", "email", "link");

		super.getResponse().addData(dataset);
	}
}
