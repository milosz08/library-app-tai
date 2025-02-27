package pl.polsl.tai.domain.ota;

import jakarta.persistence.*;
import lombok.*;
import pl.polsl.tai.domain.EntityBase;
import pl.polsl.tai.domain.user.UserEntity;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "ota_tokens")
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class OtaTokenEntity extends EntityBase implements Serializable {

  @Column(updatable = false)
  private String token;

  @Column(updatable = false)
  private LocalDateTime expires;

  @Column(insertable = false)
  private Boolean used;

  @Enumerated(EnumType.STRING)
  private OtaType type;

  @JoinColumn
  @ManyToOne
  private UserEntity user;

  @Override
  public String toString() {
    return "{" +
      "token=" + token +
      ", expires=" + expires +
      ", used=" + used +
      '}';
  }
}
