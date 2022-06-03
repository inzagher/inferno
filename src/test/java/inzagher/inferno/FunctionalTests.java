package inzagher.inferno;

import inzagher.inferno.dto.BookDTO;
import inzagher.inferno.service.BookService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.persistence.EntityNotFoundException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test")
class FunctionalTests {
	@Autowired
	private BookService service;

	@Test
	void createBookWithoutAuthors() {
		List<String> authors = Collections.emptyList();
		Long id  = service.createBook("MY VERY FIRST BOOK", authors);
		assertThat(id).isNotNull();
	}

	@Test
	void createBookWithMultipleAuthors() {
		List<String> authors = Arrays.asList("ME", "SOMEBODY");
		Long id = service.createBook("THE BEST BOOK IN THE WORLD", authors);
		assertThat(id).isNotNull();
	}

	@Test
	void createBookThenEdit() {
		Long id = service.createBook("BOOK_V1", Collections.emptyList());
		BookDTO created = service.getBookById(id);
		assertThat(created.getTitle()).isEqualTo("BOOK_V1");
		assertThat(created.getVersion()).isZero();

		service.editBookTitle(id, "BOOK_V2");
		BookDTO edited = service.getBookById(id);
		assertThat(edited.getTitle()).isEqualTo("BOOK_V2");
		assertThat(edited.getVersion()).isEqualTo(1);
	}

	@Test
	void tryEditMissingBook() {
		assertThatThrownBy(() -> service.editBookTitle(1000L, "MISSING_BOOK"))
				.isExactlyInstanceOf(EntityNotFoundException.class);
	}

	@Test
	void deleteBook() {
		List<String> authors = List.of("ME", "SOMEBODY");
		Long id = service.createBook("NOT INTERESTING BOOK", authors);
		assertThat(id).isNotNull();
		service.deleteBookById(id);
	}
}
