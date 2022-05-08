package inzagher.inferno;

import inzagher.inferno.dto.BookDTO;
import inzagher.inferno.service.BookService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class ApplicationTests {
	@Autowired
	private BookService service;

	@Test
	void createBookWithoutAuthors() {
		service.createBook("MY VERY FIRST BOOK", Collections.emptyList());
	}

	@Test
	void createBookWithMultipleAuthors() {
		service.createBook("THE BEST BOOK IN THE WORLD", Arrays.asList("ME", "SOMEBODY"));
	}

	@Test
	void createBookAndEdit() {
		Long id = service.createBook("BOOK_V1", Collections.emptyList());
		BookDTO created = service.getBookById(id);
		assertThat(created.getTitle()).isEqualTo("BOOK_V1");
		assertThat(created.getVersion()).isEqualTo(0);

		service.editBookTitle(id, "BOOK_V2");
		BookDTO edited = service.getBookById(id);
		assertThat(edited.getTitle()).isEqualTo("BOOK_V2");
		assertThat(edited.getVersion()).isEqualTo(1);
	}
}
