package pl.polsl.tai.domain.address;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.*;
import pl.polsl.tai.domain.EntityBase;
import pl.polsl.tai.domain.user.UserEntity;

import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "addresses")
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class AddressEntity extends EntityBase implements Serializable {

  private String street;

  private String buildingNumber;

  private String apartmentNumber;

  private String city;

  @OneToOne
  @JoinColumn
  private UserEntity user;

  @Override
  public String toString() {
    return "{" +
      "street=" + street +
      ", buildingNumber=" + buildingNumber +
      ", apartmentNumber=" + apartmentNumber +
      ", city=" + city +
      '}';
  }
}
