/*
 * AuthenticatedDeveloperCreateService.java
 *
 * Copyright (C) 2012-2024 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.features.authenticated.developer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Authenticated;
import acme.client.data.accounts.Principal;
import acme.client.data.accounts.UserAccount;
import acme.client.data.models.Dataset;
import acme.client.helpers.PrincipalHelper;
import acme.client.services.AbstractService;
import acme.roles.Developer;
import acme.utils.SpamRepository;

@Service
public class AuthenticatedDeveloperCreateService extends AbstractService<Authenticated, Developer> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AuthenticatedDeveloperRepository	repository;

	@Autowired
	private SpamRepository						spamRepository;

	// AbstractService<Authenticated, Provider> ---------------------------


	@Override
	public void authorise() {
		boolean status;

		status = !super.getRequest().getPrincipal().hasRole(Developer.class);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Developer object;
		Principal principal;
		int userAccountId;
		UserAccount userAccount;

		principal = super.getRequest().getPrincipal();
		userAccountId = principal.getAccountId();
		userAccount = this.repository.findUserAccountById(userAccountId);

		object = new Developer();
		object.setUserAccount(userAccount);

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final Developer object) {
		assert object != null;

		super.bind(object, "degree", "specialisation", "skills", "email", "link");
	}

	@Override
	public void validate(final Developer object) {
		assert object != null;
		if (!super.getBuffer().getErrors().hasErrors("degree"))
			super.state(!this.spamRepository.checkTextValue(super.getRequest().getData("degree", String.class)), "degree", "authenticated.developer.form.error.spam");
		if (!super.getBuffer().getErrors().hasErrors("specialisation"))
			super.state(!this.spamRepository.checkTextValue(super.getRequest().getData("specialisation", String.class)), "specialisation", "authenticated.developer.form.error.spam");
		if (!super.getBuffer().getErrors().hasErrors("skills"))
			super.state(!this.spamRepository.checkTextValue(super.getRequest().getData("skills", String.class)), "skills", "authenticated.developer.form.error.spam");
		if (!super.getBuffer().getErrors().hasErrors("email"))
			super.state(!this.spamRepository.checkTextValue(super.getRequest().getData("email", String.class)), "email", "authenticated.developer.form.error.spam");
		if (!super.getBuffer().getErrors().hasErrors("link"))
			super.state(!this.spamRepository.checkTextValue(super.getRequest().getData("link", String.class)), "link", "authenticated.developer.form.error.spam");
	}

	@Override
	public void perform(final Developer object) {
		assert object != null;
		object.setId(0);
		this.repository.save(object);
	}

	@Override
	public void unbind(final Developer object) {
		Dataset dataset;

		dataset = super.unbind(object, "degree", "specialisation", "skills", "email", "link");

		super.getResponse().addData(dataset);
	}

	@Override
	public void onSuccess() {
		if (super.getRequest().getMethod().equals("POST"))
			PrincipalHelper.handleUpdate();
	}

}
