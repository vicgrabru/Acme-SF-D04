/*
 * TrainingSession.java
 *
 * Copyright (C) 2012-2024 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.entities.training;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

import acme.client.data.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class TrainingSession extends AbstractEntity {
	// Serialisation identifier -----------------------------------------------

	private static final long	serialVersionUID	= 1L;

	@NotBlank
	@Pattern(regexp = "TS-[A-Z]{1,3}-[0-9]{3}")
	@Column(unique = true)
	private String				code;

	//a time period (at least one week ahead the training module creation moment, at least one week long)
	@Temporal(TemporalType.TIMESTAMP)
	@NotNull
	private Date				startPeriod;

	@Temporal(TemporalType.TIMESTAMP)
	@NotNull
	private Date				endPeriod;

	@NotBlank
	@Length(max = 75)
	private String				location;

	@NotBlank
	@Length(max = 75)
	private String				instructor;

	@NotBlank
	@Email
	@Length(max = 255)
	private String				contactEmail;

	@URL
	@Length(max = 255)
	private String				link;

	private boolean				draftMode;

	@NotNull
	@Valid
	@ManyToOne(optional = false)
	private TrainingModule		trainingModule;
}
