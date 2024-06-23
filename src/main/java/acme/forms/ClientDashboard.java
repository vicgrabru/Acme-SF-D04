/*
 * ClientDashboard.java
 *
 * Copyright (C) 2012-2024 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

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
