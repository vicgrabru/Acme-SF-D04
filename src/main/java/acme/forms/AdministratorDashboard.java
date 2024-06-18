/*
 * AdministratorDashboard.java
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
