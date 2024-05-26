/*
 * AnyCodeAuditShowService.java
 *
 * Copyright (C) 2012-2024 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.features.any.codeAudit;

import java.util.Comparator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Any;
import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.codeAudit.CodeAudit;
import acme.entities.codeAudit.Mark;

@Service
public class AnyCodeAuditShowService extends AbstractService<Any, CodeAudit> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AnyCodeAuditRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int codeAuditId;
		CodeAudit codeAudit;

		codeAuditId = super.getRequest().getData("id", int.class);
		codeAudit = this.repository.findOneCodeAuditById(codeAuditId);
		status = codeAudit != null && !codeAudit.isDraftMode();

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
	public void unbind(final CodeAudit object) {
		assert object != null;

		Dataset dataset;

		Mark mark;

		mark = this.repository.findOrderedMarkAmountsByCodeAuditId(object.getId()) //
			.stream() //
			.sorted(Comparator.comparingInt(Mark::ordinal)) //
			.findFirst() //
			.orElse(Mark.None);

		dataset = super.unbind(object, "code", "executionDate", "correctiveActions", "link", "auditor", "draftMode", "type");
		dataset.put("project", object.getProject().getTitle());
		dataset.put("mark", mark);

		dataset.put("projectId", object.getProject().getId());
		dataset.put("codeAuditId", object.getId());

		super.getResponse().addData(dataset);
	}

}
