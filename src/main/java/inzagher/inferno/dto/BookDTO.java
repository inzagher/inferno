package inzagher.inferno.dto;

import lombok.Data;

import java.util.Set;

@Data
public class BookDTO {
    private Long id;
    private Long version;
    private String title;
    private Set<AuthorDTO> authors;
}
