
package acme.entities.configuration;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;

import acme.client.data.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class ExchangeRate extends AbstractEntity {

	// Serialisation identifier -----------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@NotBlank
	@Pattern(regexp = "[A-Z]{3}")
	private String				source;

	@NotBlank
	@Pattern(regexp = "[A-Z]{3}")
	private String				target;

	@NotNull
	@Positive
	private double				rate;

	@NotNull
	private LocalDate			instantiationMoment;

}
