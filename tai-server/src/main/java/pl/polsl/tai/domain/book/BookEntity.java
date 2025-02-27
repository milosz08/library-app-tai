package pl.polsl.tai.domain.book;

import jakarta.persistence.*;
import lombok.*;
import pl.polsl.tai.domain.EntityBase;
import pl.polsl.tai.domain.author.AuthorEntity;
import pl.polsl.tai.domain.user.UserEntity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "books")
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class BookEntity extends EntityBase implements Serializable {

  private String title;

  private String isbn;

  private Integer year;

  private String publisher;

  private String city;

  private Long copies;

  @OneToMany(mappedBy = "book", cascade = CascadeType.ALL)
  @Builder.Default
  private Set<AuthorEntity> authors = new HashSet<>();

  @ManyToMany
  @JoinTable(
    name = "user_books",
    joinColumns = @JoinColumn(name = "book_id"),
    inverseJoinColumns = @JoinColumn(name = "user_id")
  )
  @Builder.Default
  private List<UserEntity> users = new ArrayList<>();

  public void attachUser(UserEntity user) {
    users.add(user);
    user.getBooks().add(this);
  }

  public void detachUser(UserEntity user) {
    users.remove(user);
    user.getBooks().remove(this);
  }

  public void attachAuthor(AuthorEntity author) {
    authors.add(author);
    author.setBook(this);
  }

  @Override
  public String toString() {
    return "{" +
      "title=" + title +
      ", isbn=" + isbn +
      ", year=" + year +
      ", publisher=" + publisher +
      ", city=" + city +
      ", copies=" + copies +
      ", authors=" + authors +
      '}';
  }
}
