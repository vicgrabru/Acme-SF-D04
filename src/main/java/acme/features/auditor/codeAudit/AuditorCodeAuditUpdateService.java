/*
 * AuditorCodeAuditUpdateService.java
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
import java.util.Comparator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.client.views.SelectChoices;
import acme.entities.codeAudit.AuditType;
import acme.entities.codeAudit.CodeAudit;
import acme.entities.codeAudit.Mark;
import acme.entities.project.Project;
import acme.roles.Auditor;
import acme.utils.SpamRepository;

@Service
public class AuditorCodeAuditUpdateService extends AbstractService<Auditor, CodeAudit> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AuditorCodeAuditRepository	repository;

	@Autowired
	private SpamRepository				spamRepository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int codeAuditId;
		CodeAudit codeAudit;

		codeAuditId = super.getRequest().getData("id", int.class);
		codeAudit = this.repository.findOneCodeAuditById(codeAuditId);
		status = codeAudit != null && //
			codeAudit.isDraftMode() && //
			super.getRequest().getPrincipal().hasRole(codeAudit.getAuditor());

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		CodeAudit object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findOneCodeAuditById(id);
		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final CodeAudit object) {
		assert object != null;

		super.bind(object, "type", "correctiveActions", "link");
	}

	@Override
	public void validate(final CodeAudit object) {
		assert object != null;

		if (!super.getBuffer().getErrors().hasErrors("correctiveActions"))
			super.state(!this.spamRepository.checkTextValue(object.getCorrectiveActions()), //
				"correctiveActions", "auditor.code-audit.form.error.spam");

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
		Mark mark;

		projects = this.repository.findAllPublishedProjects();

		choicesProject = SelectChoices.from(projects, "title", object.getProject());
		choicesType = SelectChoices.from(AuditType.class, object.getType());

		mark = this.repository.findOrderedMarkAmountsByCodeAuditId(object.getId()) //
			.stream() //
			.sorted(Comparator.comparingInt(Mark::ordinal)) //
			.findFirst() //
			.orElse(Mark.None);

		dataset = super.unbind(object, "code", "executionDate", "correctiveActions", "link", "auditor", "draftMode");
		dataset.put("project", choicesProject.getSelected().getKey());
		dataset.put("projects", choicesProject);
		dataset.put("type", choicesType.getSelected());
		dataset.put("types", choicesType);
		dataset.put("mark", mark);

		dataset.put("projectId", object.getProject().getId());
		dataset.put("codeAuditId", object.getId());
		dataset.put("readOnlyCode", true);

		super.getResponse().addData(dataset);
	}

}
