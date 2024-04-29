/*
 * EmployerApplicationUpdateService.java
 *
 * Copyright (C) 2012-2024 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.features.authenticated.notice;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Authenticated;
import acme.client.data.accounts.Principal;
import acme.client.data.accounts.UserAccount;
import acme.client.data.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractService;
import acme.entities.notice.Notice;
import spamDetector.SpamDetector;

@Service
public class AuthenticatedNoticeCreateService extends AbstractService<Authenticated, Notice> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AuthenticatedNoticeRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Notice object;
		Date instantiationMoment;

		instantiationMoment = MomentHelper.getCurrentMoment();

		Principal p = super.getRequest().getPrincipal();
		String username = p.getUsername();
		UserAccount ua = this.repository.findUserAccountById(p.getAccountId());
		String fullName = ua.getIdentity().getFullName();

		object = new Notice();
		object.setInstantiationMoment(instantiationMoment);
		object.setAuthor(username + " - " + fullName);

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final Notice object) {
		assert object != null;

		super.bind(object, "title", "message", "email", "link");
	}

	@Override
	public void validate(final Notice object) {
		assert object != null;

		boolean confirmation = super.getRequest().getData("confirmation", boolean.class);

		if (!super.getBuffer().getErrors().hasErrors("confirmation"))
			super.state(confirmation, "confirmation", "authenticated.notice.form.error.confirmation-needed");

		if (!super.getBuffer().getErrors().hasErrors("title"))
			super.state(!SpamDetector.checkTextValue(object.getTitle()), "title", "authenticated.notice.form.error.spam");
		if (!super.getBuffer().getErrors().hasErrors("message"))
			super.state(!SpamDetector.checkTextValue(object.getMessage()), "message", "authenticated.notice.form.error.spam");
		if (!super.getBuffer().getErrors().hasErrors("email"))
			super.state(!SpamDetector.checkTextValue(object.getEmail()), "email", "authenticated.notice.form.error.spam");
		if (!super.getBuffer().getErrors().hasErrors("link"))
			super.state(!SpamDetector.checkTextValue(object.getLink()), "link", "authenticated.notice.form.error.spam");

	}

	@Override
	public void perform(final Notice object) {
		assert object != null;

		this.repository.save(object);
	}

	@Override
	public void unbind(final Notice object) {
		assert object != null;

		Dataset dataset;

		dataset = super.unbind(object, "title", "message", "email", "link", "author", "instantiationMoment");

		super.getResponse().addData(dataset);
	}

}
