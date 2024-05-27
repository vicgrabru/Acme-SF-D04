
package acme.features.developer.trainingModule;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractService;
import acme.client.views.SelectChoices;
import acme.entities.training.Difficulty;
import acme.entities.training.TrainingModule;
import acme.roles.Developer;
import spamDetector.SpamDetector;

@Service
public class DeveloperTrainingModulePublishService extends AbstractService<Developer, TrainingModule> {

	@Autowired
	private DeveloperTrainingModuleRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int moduleId = super.getRequest().getData("id", int.class);
		TrainingModule module = this.repository.findTrainingModuleById(moduleId);
		status = module != null && module.isDraftMode() && super.getRequest().getPrincipal().hasRole(module.getDeveloper());

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		TrainingModule object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findTrainingModuleById(id);

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final TrainingModule object) {
		assert object != null;
		Date updateMoment;

		updateMoment = MomentHelper.getCurrentMoment();

		super.bind(object, "code", "creationMoment", "details", "difficulty", "updateMoment", "startTotalTime", "endTotalTime", "link", "project");
		object.setUpdateMoment(updateMoment);
	}

	@Override
	public void validate(final TrainingModule object) {
		assert object != null;
		if (!super.getBuffer().getErrors().hasErrors("draftMode"))
			super.state(!this.repository.findTrainingSessionsOfTrainingModule(object.getId()).isEmpty(), "draftMode", "developer.training-module.form.error.emptyTrainingSessions");
		if (!super.getBuffer().getErrors().hasErrors("code"))
			super.state(!SpamDetector.checkTextValue(super.getRequest().getData("code", String.class)), "code", "developer.training-module.form.error.spam");
		if (!super.getBuffer().getErrors().hasErrors("details"))
			super.state(!SpamDetector.checkTextValue(super.getRequest().getData("details", String.class)), "details", "developer.training-module.form.error.spam");
		if (!super.getBuffer().getErrors().hasErrors("difficulty"))
			super.state(!SpamDetector.checkTextValue(super.getRequest().getData("difficulty", String.class)), "difficulty", "developer.training-module.form.error.spam");
		if (!super.getBuffer().getErrors().hasErrors("link"))
			super.state(!SpamDetector.checkTextValue(super.getRequest().getData("link", String.class)), "link", "developer.training-module.form.error.spam");
		if (!super.getBuffer().getErrors().hasErrors("startTotalTime"))
			super.state(object.getStartTotalTime().after(object.getCreationMoment()), "startTotalTime", "developer.training-module.form.error.startTotalTime.not-after-creationMoment");
		if (!super.getBuffer().getErrors().hasErrors("endTotalTime"))
			super.state(object.getEndTotalTime().after(object.getStartTotalTime()), "endTotalTime", "developer.training-module.form.error.endTotalTime.not-after-startTotalTime");
	}

	@Override
	public void perform(final TrainingModule object) {
		assert object != null;
		object.setDraftMode(false);
		this.repository.save(object);
	}

	@Override
	public void unbind(final TrainingModule object) {
		assert object != null;

		Dataset dataset;

		dataset = super.unbind(object, "code", "creationMoment", "details", "difficulty", "updateMoment", "startTotalTime", "endTotalTime", "link", "project");
		final SelectChoices difficultyChoices;
		final SelectChoices projectChoices;
		difficultyChoices = SelectChoices.from(Difficulty.class, object.getDifficulty());
		dataset.put("difficulty", difficultyChoices.getSelected().getKey());
		dataset.put("difficulties", difficultyChoices);
		projectChoices = SelectChoices.from(this.repository.findProjects(), "title", object.getProject());
		dataset.put("project", projectChoices.getSelected().getKey());
		dataset.put("projects", projectChoices);
		super.getResponse().addData(dataset);
	}
}
