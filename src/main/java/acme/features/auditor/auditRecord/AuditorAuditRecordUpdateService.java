/*
 * AuditorAuditRecordUpdateService.java
 *
 * Copyright (C) 2012-2024 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.features.auditor.auditRecord;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.client.views.SelectChoices;
import acme.entities.codeAudit.AuditRecord;
import acme.entities.codeAudit.Mark;
import acme.roles.Auditor;

@Service
public class AuditorAuditRecordUpdateService extends AbstractService<Auditor, AuditRecord> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AuditorAuditRecordRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int auditRecordId;
		AuditRecord auditRecord;

		auditRecordId = super.getRequest().getData("id", int.class);
		auditRecord = this.repository.findOneAuditRecordById(auditRecordId);
		status = auditRecord != null && //
			auditRecord.isDraftMode() && //
			super.getRequest().getPrincipal().hasRole(auditRecord.getCodeAudit().getAuditor());

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		AuditRecord object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findOneAuditRecordById(id);
		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final AuditRecord object) {
		assert object != null;

		super.bind(object, "code", "periodStart", "periodEnd", "mark", "link");
	}

	@Override
	public void validate(final AuditRecord object) {
		assert object != null;

		if (!super.getBuffer().getErrors().hasErrors("code")) {
			boolean duplicatedCode = this.repository.findAllAuditRecords().stream() //
				.anyMatch(ar -> ar.getCode().equals(object.getCode()) && ar.getId() != object.getId());
			super.state(!duplicatedCode, "code", "auditor.audit-record.form.error.duplicated-code");
		}

		if (!super.getBuffer().getErrors().hasErrors("periodStart") && //
			!super.getBuffer().getErrors().hasErrors("periodEnd")) {
			boolean startBeforeEnd = object.getPeriodStart().before(object.getPeriodEnd());
			super.state(startBeforeEnd, "*", "auditor.audit-record.form.error.start-not-before-end");
			if (startBeforeEnd) {
				boolean hourStartToEnd = Duration.between(object.getPeriodStart().toInstant(), //
					object.getPeriodEnd().toInstant()).toHours() >= 1;
				super.state(hourStartToEnd, "*", "auditor.audit-record.form.error.not-enough-time");
			}

		}
	}

	@Override
	public void perform(final AuditRecord object) {
		assert object != null;

		this.repository.save(object);
	}

	@Override
	public void unbind(final AuditRecord object) {
		assert object != null;

		Dataset dataset;
		SelectChoices choicesMark;

		choicesMark = SelectChoices.from(Mark.class, object.getMark());

		dataset = super.unbind(object, "code", "periodStart", "periodEnd", "link", "draftMode");
		dataset.put("readOnlyCode", true);
		dataset.put("mark", choicesMark.getSelected());
		dataset.put("marks", choicesMark);

		dataset.put("auditRecordId", object.getId());

		super.getResponse().addData(dataset);
	}

}
