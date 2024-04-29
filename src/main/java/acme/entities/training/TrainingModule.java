
package acme.entities.training;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

import acme.client.data.AbstractEntity;
import acme.entities.project.Project;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class TrainingModule extends AbstractEntity {
	// Serialisation identifier -----------------------------------------------

	private static final long	serialVersionUID	= 1L;

	@NotBlank
	@Pattern(regexp = "[A-Z]{1,3}-[0-9]{3}")//AAA-111, A-111
	@Column(unique = true)
	private String				code;

	@Temporal(TemporalType.TIMESTAMP)
	@Past
	@NotNull
	private Date				creationMoment;

	@NotBlank
	@Length(max = 100)
	private String				details;

	@NotNull
	private Difficulty			difficulty;

	@Temporal(TemporalType.TIMESTAMP)
	@Past
	private Date				updateMoment;

	@Temporal(TemporalType.TIMESTAMP)
	private Date				startTotalTime;
	@Temporal(TemporalType.TIMESTAMP)
	private Date				endTotalTime;

	@URL
	private String				link;

	private boolean				draftMode;

	@NotNull
	@Valid
	@ManyToOne(optional = false)
	protected Project			project;

}
