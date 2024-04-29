
package acme.forms;

import java.util.Date;
import java.util.Map;

import acme.client.data.AbstractForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeveloperDashboard extends AbstractForm {

	// Serialisation identifier -----------------------------------------------

	private static final long		serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------
	private Map<Date, Integer>		numberOfTrainingModulesPerUpdateMoment;

	private Map<String, Integer>	numberOfTrainingSessionPerLink;

	private Double					averageTrainingModuleTime;
	private Double					deviationTrainingModuleTime;
	private Double					maximumTrainingModuleTime;
	private Double					minimumTrainingModuleTime;
}
