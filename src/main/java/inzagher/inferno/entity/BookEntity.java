package inzagher.inferno.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "books")
public class BookEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Long id;

    @Version
    private Long version;

    @Column(name = "title", nullable = false)
    private String title;

    @JoinColumn(name = "book_id", nullable = false, updatable = false)
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<AuthorEntity> authors = new HashSet<>();
}
