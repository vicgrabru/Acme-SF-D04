
package acme.features.sponsor.sponsorship;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.datatypes.Money;
import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.client.views.SelectChoices;
import acme.entities.project.Project;
import acme.entities.sponsorship.Sponsorship;
import acme.entities.sponsorship.Type;
import acme.roles.Sponsor;
import acme.utils.MoneyExchangeRepository;

@Service
public class SponsorSponsorshipShowService extends AbstractService<Sponsor, Sponsorship> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private SponsorSponsorshipRepository	repository;

	@Autowired
	private MoneyExchangeRepository			exchangeRepo;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int masterId;
		Sponsorship sponsorship;
		Sponsor sponsor;

		masterId = super.getRequest().getData("id", int.class);
		sponsorship = this.repository.findOneSponsorshipById(masterId);
		sponsor = sponsorship == null ? null : sponsorship.getSponsor();
		status = sponsorship != null && super.getRequest().getPrincipal().hasRole(sponsor);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Sponsorship object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findOneSponsorshipById(id);

		super.getBuffer().addData(object);
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
		choicesType = SelectChoices.from(Type.class, object.getType());
		dataset = super.unbind(object, "code", "moment", "startDuration", "endDuration", "amount", "type", "email", "link", "draftMode");
		dataset.put("project", choicesProject.getSelected().getKey());
		dataset.put("projects", choicesProject);
		dataset.put("types", choicesType);

		Money eb = this.exchangeRepo.exchangeMoney(object.getAmount());
		dataset.put("exchangedAmount", eb);
		dataset.put("readOnlyCode", true);

		super.getResponse().addData(dataset);
	}

}
