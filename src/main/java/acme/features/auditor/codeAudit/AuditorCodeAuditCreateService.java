/*
 * AuditorCodeAuditCreateService.java
 *
 * Copyright (C) 2012-2024 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.features.auditor.codeAudit;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractService;
import acme.client.views.SelectChoices;
import acme.entities.codeAudit.CodeAudit;
import acme.entities.codeAudit.Type;
import acme.entities.project.Project;
import acme.roles.Auditor;
import spamDetector.SpamDetector;

@Service
public class AuditorCodeAuditCreateService extends AbstractService<Auditor, CodeAudit> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AuditorCodeAuditRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		CodeAudit object;
		Auditor auditor;

		auditor = this.repository.findOneAuditorById(super.getRequest().getPrincipal().getActiveRoleId());

		object = new CodeAudit();
		object.setExecutionDate(MomentHelper.getCurrentMoment());
		object.setAuditor(auditor);
		object.setDraftMode(true);

		super.getBuffer().addData(object);

	}

	@Override
	public void bind(final CodeAudit object) {
		assert object != null;

		super.bind(object, "code", "type", "correctiveActions", "mark", "link", "project");
	}

	@Override
	public void validate(final CodeAudit object) {
		assert object != null;

		if (!super.getBuffer().getErrors().hasErrors("code")) {

			boolean duplicatedCode = this.repository.findAllCodeAudits().stream().anyMatch(ca -> ca.getCode().equals(object.getCode()));
			super.state(!duplicatedCode, "code", "auditor.code-audit.form.error.duplicated-code");

			super.state(!SpamDetector.checkTextValue(object.getCode()), //
				"code", "auditor.code-audit.form.error.spam");
		}

		if (!super.getBuffer().getErrors().hasErrors("correctiveActions"))
			super.state(!SpamDetector.checkTextValue(object.getCorrectiveActions()), //
				"correctiveActions", "auditor.code-audit.form.error.spam");

		if (!super.getBuffer().getErrors().hasErrors("link"))
			super.state(!SpamDetector.checkTextValue(object.getLink()), //
				"link", "auditor.code-audit.form.error.spam");
	}

	@Override
	public void perform(final CodeAudit object) {
		assert object != null;

		this.repository.save(object);
	}

	@Override
	public void unbind(final CodeAudit object) {
		assert object != null;

		Dataset dataset;
		SelectChoices choicesProject;
		SelectChoices choicesType;

		Collection<Project> projects;

		projects = this.repository.findAllPublishedProjects();

		choicesProject = SelectChoices.from(projects, "title", object.getProject());
		choicesType = SelectChoices.from(Type.class, object.getType());

		dataset = super.unbind(object, "code", "executionDate", "correctiveActions", "link", "auditor", "draftMode");
		dataset.put("project", choicesProject.getSelected().getKey());
		dataset.put("projects", choicesProject);
		dataset.put("type", choicesType.getSelected());
		dataset.put("types", choicesType);
		dataset.put("mark", "None");
		dataset.put("readOnlyCode", false);

		super.getResponse().addData(dataset);
	}

}
