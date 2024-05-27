
package acme.forms;

import acme.client.data.AbstractForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeveloperDashboard extends AbstractForm {

	// Serialisation identifier -----------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------
	private Integer				numberOfTrainingModulesWithUpdateMoment;

	private Integer				numberOfTrainingSessionWithLink;

	private Double				averageTrainingModuleTime;
	private Double				deviationTrainingModuleTime;
	private Double				maximumTrainingModuleTime;
	private Double				minimumTrainingModuleTime;
}
