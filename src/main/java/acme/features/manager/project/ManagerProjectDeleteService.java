/*
 * ManagerProjectDeleteService.java
 *
 * Copyright (C) 2012-2024 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.features.manager.project;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.datatypes.Money;
import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.project.Project;
import acme.entities.project.UserStoryAssign;
import acme.roles.Manager;
import acme.utils.MoneyExchangeRepository;

@Service
public class ManagerProjectDeleteService extends AbstractService<Manager, Project> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private ManagerProjectRepository	repository;

	@Autowired
	private MoneyExchangeRepository		exchangeRepository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int projectId;
		Project project;

		projectId = super.getRequest().getData("id", int.class);
		project = this.repository.findOneProjectById(projectId);
		status = project != null && project.isDraftMode() && super.getRequest().getPrincipal().hasRole(project.getManager());

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Project object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findOneProjectById(id);

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final Project object) {
		assert object != null;
	}

	@Override
	public void validate(final Project object) {
		assert object != null;
	}

	@Override
	public void perform(final Project object) {
		assert object != null;
		Collection<UserStoryAssign> relationships;

		relationships = this.repository.findManyUserStoryAssignsByProjectId(object.getId());

		if (!relationships.isEmpty())
			this.repository.deleteAll(relationships);

		this.repository.delete(object);
	}

	@Override
	public void unbind(final Project object) {
		assert object != null;

		Money exchangedCost;
		Dataset dataset;

		dataset = super.unbind(object, "code", "title", "abstractField", "hasFatalErrors", "cost", "optionalLink", "draftMode");
		dataset.put("masterId", object.getId());
		dataset.put("readOnlyCode", true);

		exchangedCost = this.exchangeRepository.exchangeMoney(object.getCost());
		dataset.put("exchangedCost", exchangedCost);

		super.getResponse().addData(dataset);
	}

}
