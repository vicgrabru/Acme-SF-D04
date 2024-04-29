
package acme.forms;

import acme.client.data.AbstractForm;
import acme.client.data.datatypes.Money;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientDashboard extends AbstractForm {

	// Serialisation identifier -----------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	private Integer				below25CompletenessProgressLogs;
	private Integer				between25and50CompletenessProgressLogs;
	private Integer				between50and75CompletenessProgressLogs;
	private Integer				above75CompletenessProgressLogs;

	private Money				avgContractBudget;
	private Money				stdContractBudget;
	private Money				minContractBudget;
	private Money				maxContractBudget;

}
