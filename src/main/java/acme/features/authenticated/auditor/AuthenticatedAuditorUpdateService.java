/*
 * AuthenticatedAuditorUpdateService.java
 *
 * Copyright (C) 2012-2024 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.features.authenticated.auditor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Authenticated;
import acme.client.data.accounts.Principal;
import acme.client.data.models.Dataset;
import acme.client.helpers.PrincipalHelper;
import acme.client.services.AbstractService;
import acme.roles.Auditor;
import acme.utils.SpamRepository;

@Service
public class AuthenticatedAuditorUpdateService extends AbstractService<Authenticated, Auditor> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AuthenticatedAuditorRepository	repository;

	@Autowired
	private SpamRepository					spamRepository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Auditor object;
		Principal principal;
		int userAccountId;

		principal = super.getRequest().getPrincipal();
		userAccountId = principal.getAccountId();
		object = this.repository.findOneAuditorByUserAccountId(userAccountId);

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final Auditor object) {
		assert object != null;

		super.bind(object, "firm", "professionalId", "certifications", "link");
	}

	@Override
	public void validate(final Auditor object) {
		assert object != null;

		if (!super.getBuffer().getErrors().hasErrors("firm"))
			super.state(!this.spamRepository.checkTextValue(object.getFirm()), "firm", //
				"authenticated.auditor.form.error.spam-in-firm");

		if (!super.getBuffer().getErrors().hasErrors("professionalId"))
			super.state(!this.spamRepository.checkTextValue(object.getProfessionalId()), "professionalId", //
				"authenticated.auditor.form.error.spam-in-professional-id");

		if (!super.getBuffer().getErrors().hasErrors("certifications"))
			super.state(!this.spamRepository.checkTextValue(object.getCertifications()), "certifications", //
				"authenticated.auditor.form.error.spam-in-certifications");
	}

	@Override
	public void perform(final Auditor object) {
		assert object != null;

		this.repository.save(object);
	}

	@Override
	public void unbind(final Auditor object) {
		assert object != null;

		Dataset dataset;

		dataset = super.unbind(object, "firm", "professionalId", "certifications", "link");
		super.getResponse().addData(dataset);
	}

	@Override
	public void onSuccess() {
		if (super.getRequest().getMethod().equals("POST"))
			PrincipalHelper.handleUpdate();
	}

}
