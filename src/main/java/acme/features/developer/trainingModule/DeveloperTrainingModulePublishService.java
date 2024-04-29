
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

@Service
public class DeveloperTrainingModulePublishService extends AbstractService<Developer, TrainingModule> {

	@Autowired
	private DeveloperTrainingModuleRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int trainingModuleId;
		TrainingModule trainingModule;

		trainingModuleId = super.getRequest().getData("id", int.class);
		trainingModule = this.repository.findTrainingModuleById(trainingModuleId);
		status = trainingModule != null;

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

		super.bind(object, "code", "creationMoment", "details", "difficulty", "updateMoment", "startTotalTime", "endTotalTime", "link", "draftMode");
		object.setUpdateMoment(updateMoment);
	}

	@Override
	public void validate(final TrainingModule object) {
		assert object != null;
		if (!super.getBuffer().getErrors().hasErrors("draftMode"))
			super.state(!this.repository.findTrainingSessionsOfTrainingModule(object.getId()).isEmpty(), "draftMode", "developer.training-module.form.error.emptyTrainingSessions");
		if (!super.getBuffer().getErrors().hasErrors("startTotalTime")) {
			super.state(object.getStartTotalTime().after(object.getCreationMoment()), "startTotalTime", "developer.training-module.form.error.startTotalTime.not-after-creationMoment");
			if (!super.getBuffer().getErrors().hasErrors("endTotalTime"))
				super.state(object.getEndTotalTime().after(object.getStartTotalTime()), "endTotalTime", "developer.training-module.form.error.endTotalTime.not-after-startTotalTime");
		}
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

		dataset = super.unbind(object, "code", "creationMoment", "details", "difficulty", "updateMoment", "startTotalTime", "endTotalTime", "link", "draftMode");
		final SelectChoices choices;
		choices = SelectChoices.from(Difficulty.class, object.getDifficulty());
		dataset.put("difficulty", choices.getSelected().getKey());
		dataset.put("difficulties", choices);
		super.getResponse().addData(dataset);
	}
}
