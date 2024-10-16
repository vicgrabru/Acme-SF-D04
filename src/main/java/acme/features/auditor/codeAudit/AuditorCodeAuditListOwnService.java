/*
 * AuditorCodeAuditListOwnService.java
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

import acme.client.data.accounts.Principal;
import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.codeAudit.CodeAudit;
import acme.entities.codeAudit.Mark;
import acme.roles.Auditor;

@Service
public class AuditorCodeAuditListOwnService extends AbstractService<Auditor, CodeAudit> {

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
		Collection<CodeAudit> objects;
		Principal p = super.getRequest().getPrincipal();
		Integer auditorId = p.getActiveRoleId();

		objects = this.repository.findCodeAuditsByAuditorId(auditorId);

		super.getBuffer().addData(objects);
	}

	@Override
	public void unbind(final CodeAudit object) {
		assert object != null;

		Dataset dataset;
		String isDraftMode;

		Mark mark;

		if (object.isDraftMode())
			isDraftMode = "✓";
		else
			isDraftMode = "✗";

		mark = this.repository.findOrderedMarkAmountsByCodeAuditId(object.getId()) //
			.stream() //
			.min((m1, m2) -> {
				Mark mark1 = (Mark) m1[0];
				Mark mark2 = (Mark) m2[0];
				Long count1 = (Long) m1[1];
				Long count2 = (Long) m2[1];
				int frequencyComparison = count2.compareTo(count1);
				if (frequencyComparison == 0)
					return Integer.compare(mark1.ordinal(), mark2.ordinal());
				return frequencyComparison;
			}).map(entry -> (Mark) entry[0]).orElse(Mark.None);

		dataset = super.unbind(object, "code", "executionDate", "type");
		dataset.put("mark", mark);
		dataset.put("draftMode", isDraftMode);

		super.getResponse().addData(dataset);
	}

}
