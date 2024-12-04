package pl.polsl.tai.domain.log;

import jakarta.persistence.*;
import lombok.*;
import pl.polsl.tai.domain.EntityBase;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "logs")
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class LogEntity extends EntityBase {

	private String message;

	@Enumerated(EnumType.STRING)
	private Level level;

	@Column(updatable = false)
	private LocalDateTime executedTime;

	@Override
	public String toString() {
		return "{" +
			"message=" + message +
			", level=" + level +
			", executedTime=" + executedTime +
			'}';
	}
}
