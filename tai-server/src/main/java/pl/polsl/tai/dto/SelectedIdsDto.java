package pl.polsl.tai.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SelectedIdsDto {
	@NotNull(message = "Pole identyfikatorów musi istnieć")
	@NotEmpty(message = "Należy podać przynajmniej jeden identyfikator.")
	private List<Long> ids;
}
