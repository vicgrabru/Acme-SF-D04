
package acme.forms;

import java.util.Map;

import acme.client.data.AbstractForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdministratorDashboard extends AbstractForm {

	// Serialisation identifier -----------------------------------------------

	private static final long		serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	private Map<String, Integer>	numberOfPrincipalsByRole;
	private Double					ratioOfNoticesWithEmailAdressAndLink;
	private Double					ratioOfCriticalObjectives;
	private Double					ratioOfNonCriticalObjectives;

	private Double					avgRiskValue;
	private Double					minRiskValue;
	private Double					maxRiskValue;
	private Double					stdRiskValue;

	private Double					avgNumberOfClaims;
	private Integer					minNumberOfClaims;
	private Integer					maxNumberOfClaims;
	private Double					stdNumberOfClaims;

}
