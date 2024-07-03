/*
 * SponsorSponsorshipCreateService.java
 *
 * Copyright (C) 2012-2024 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.features.sponsor.sponsorship;

import java.time.temporal.ChronoUnit;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractService;
import acme.client.views.SelectChoices;
import acme.entities.project.Project;
import acme.entities.sponsorship.Sponsorship;
import acme.entities.sponsorship.SponsorshipType;
import acme.roles.Sponsor;

@Service
public class SponsorSponsorshipCreateService extends AbstractService<Sponsor, Sponsorship> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private SponsorSponsorshipRepository repository;

	// AbstractService interface ---------------------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Sponsorship object;
		Sponsor sponsor;

		sponsor = this.repository.findOneSponsorById(super.getRequest().getPrincipal().getActiveRoleId());
		object = new Sponsorship();
		object.setMoment(MomentHelper.getCurrentMoment());
		object.setDraftMode(true);
		object.setSponsor(sponsor);

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final Sponsorship object) {
		assert object != null;

		int projectId;
		Project project;

		projectId = super.getRequest().getData("project", int.class);
		project = this.repository.findOneProjectById(projectId);

		super.bind(object, "code", "moment", "startDuration", "endDuration", "amount", "type", "email", "link");
		object.setProject(project);
	}

	@Override
	public void validate(final Sponsorship object) {
		assert object != null;
		String currencies;

		if (!super.getBuffer().getErrors().hasErrors("amount")) {
			currencies = this.repository.findAcceptedCurrencies();
			super.state(currencies.contains(object.getAmount().getCurrency()), "amount", "sponsor.sponsorship.form.error.amount.invalid-currency");
		}

		if (!super.getBuffer().getErrors().hasErrors("amount"))
			super.state(object.getAmount().getAmount() >= 0., "amount", "sponsor.sponsorship.form.error.amount.no-negative");

		if (!super.getBuffer().getErrors().hasErrors("code")) {
			Sponsorship existing;

			existing = this.repository.findOneSponsorshipByCode(object.getCode());
			super.state(existing == null, "code", "sponsor.sponsorship.form.error.duplicated");
		}

		if (!super.getBuffer().getErrors().hasErrors("startDuration"))
			super.state(MomentHelper.isAfter(object.getStartDuration(), object.getMoment()), "startDuration", "sponsor.sponsorship.form.error.durationAfter");

		if (!super.getBuffer().getErrors().hasErrors("endDuration"))
			super.state(MomentHelper.isLongEnough(object.getStartDuration(), object.getEndDuration(), 1, ChronoUnit.MONTHS), "endDuration", "sponsor.sponsorship.form.error.atLeast1MonthLong");

	}

	@Override
	public void perform(final Sponsorship object) {
		assert object != null;
		object.setId(0);
		this.repository.save(object);
	}

	@Override
	public void unbind(final Sponsorship object) {
		assert object != null;

		Collection<Project> projects;
		SelectChoices choicesProject;
		SelectChoices choicesType;
		Dataset dataset;

		projects = this.repository.findAllProjects();
		choicesProject = SelectChoices.from(projects, "code", object.getProject());
		choicesType = SelectChoices.from(SponsorshipType.class, object.getType());
		dataset = super.unbind(object, "code", "moment", "startDuration", "endDuration", "amount", "type", "email", "link", "draftMode");
		dataset.put("project", choicesProject.getSelected().getKey());
		dataset.put("projects", choicesProject);
		dataset.put("types", choicesType);
		dataset.put("readOnlyCode", false);

		super.getResponse().addData(dataset);
	}

}
