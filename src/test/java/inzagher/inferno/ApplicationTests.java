package inzagher.inferno;

import inzagher.inferno.service.BookService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.Collections;

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
		service.editBookTitle(id, "BOOK_V2");
	}
}
