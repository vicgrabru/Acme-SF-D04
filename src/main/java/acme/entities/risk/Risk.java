
package acme.entities.risk;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

import acme.client.data.AbstractEntity;
import acme.entities.project.Project;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Risk extends AbstractEntity {

	// Serialisation identifier -----------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@NotBlank
	@Column(unique = true)
	@Pattern(regexp = "R-[0-9]{3}")
	private String				reference;

	@NotNull
	@Past
	@Temporal(TemporalType.TIMESTAMP)
	private Date				identificationDate;

	@NotNull
	@Positive
	private Double				impact;

	@NotNull
	@Positive
	@Digits(integer = 3, fraction = 2)
	@Max(100)
	private Double				probability;

	@NotBlank
	@Length(max = 100)
	private String				description;

	@URL
	private String				link;

	// Derived attributes -----------------------------------------------------


	@Transient
	public Double getValue() {
		return this.impact * this.probability;
	}

	// Relationships ----------------------------------------------------------


	@NotNull
	@Valid
	@ManyToOne(optional = false)
	protected Project project;

}
