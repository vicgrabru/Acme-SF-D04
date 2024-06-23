/*
 * AuditorCodeAuditPublishService.java
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.client.views.SelectChoices;
import acme.entities.codeAudit.AuditRecord;
import acme.entities.codeAudit.CodeAudit;
import acme.entities.codeAudit.Mark;
import acme.entities.codeAudit.AuditType;
import acme.entities.project.Project;
import acme.roles.Auditor;

@Service
public class AuditorCodeAuditPublishService extends AbstractService<Auditor, CodeAudit> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AuditorCodeAuditRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int codeAuditId;
		CodeAudit codeAudit;

		codeAuditId = super.getRequest().getData("id", int.class);
		codeAudit = this.repository.findOneCodeAuditById(codeAuditId);
		status = codeAudit != null && //
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
	}

	@Override
	public void validate(final CodeAudit object) {
		assert object != null;

		Collection<AuditRecord> auditRecords;
		int codeAuditId;

		codeAuditId = super.getRequest().getData("id", int.class);

		auditRecords = this.repository.findAuditRecordsByCodeAuditId(codeAuditId);

		Mark mark = this.repository.findOrderedMarkAmountsByCodeAuditId(object.getId()) //
			.stream() //
			.sorted(Comparator.comparingInt(Mark::ordinal)) //
			.findFirst() //
			.orElse(Mark.None);

		Collection<Mark> pass = new ArrayList<Mark>();
		pass.add(Mark.APlus);
		pass.add(Mark.A);
		pass.add(Mark.B);
		pass.add(Mark.C);

		super.state(!auditRecords.isEmpty(), "*", //
			"auditor.code-audit.form.error.no-audit-records");

		if (!auditRecords.isEmpty()) {

			super.state(auditRecords.stream().allMatch(x -> !x.isDraftMode()), //
				"*", "auditor.code-audit.form.error.has-draft-audit-records");

			if (!super.getBuffer().getErrors().hasErrors("mark"))
				super.state(pass.contains(mark), //
					"mark", "auditor.code-audit.form.error.minimum-mark-not-reached");
		}

	}

	@Override
	public void perform(final CodeAudit object) {
		assert object != null;

		object.setDraftMode(false);
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
			.stream().findFirst().orElse(Mark.None);

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
