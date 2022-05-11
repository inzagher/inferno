package inzagher.inferno;

import inzagher.inferno.dto.BookDTO;
import inzagher.inferno.exception.BookServiceException;
import inzagher.inferno.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.concurrent.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test")
@Transactional(propagation = Propagation.NEVER)
class ConcurrencyTests {
    @Autowired
    private BookService service;
    private volatile CyclicBarrier barrier = null;

    @BeforeEach
    public void beforeEach() {
        barrier = new CyclicBarrier(2);
    }

    @Test
    void concurrentUpdate() throws InterruptedException {
        Long id = service.createBook("TEST", Collections.emptyList());
        BookDTO created = service.getBookById(id);
        assertThat(created.getTitle()).isEqualTo("TEST");
        assertThat(created.getVersion()).isZero();

        ExecutorService executor = Executors.newFixedThreadPool(2);
        executor.execute(() -> edit(id, "TEST_1"));
        executor.execute(() -> edit(id, "TEST_2"));

        executor.shutdown();
        executor.awaitTermination(100, TimeUnit.MILLISECONDS);

        BookDTO edited = service.getBookById(id);
        assertThat(edited.getTitle()).isIn("TEST_1", "TEST_2");
        assertThat(edited.getVersion()).isEqualTo(1);
    }

    @Test
    void sequentialUpdate() {
        Long id = service.createBook("TEST", Collections.emptyList());
        BookDTO created = service.getBookById(id);
        assertThat(created.getTitle()).isEqualTo("TEST");
        assertThat(created.getVersion()).isZero();

        service.editBookTitle(id, "TEST_1", 0L);
        BookDTO edited = service.getBookById(id);
        assertThat(edited.getTitle()).isEqualTo("TEST_1");
        assertThat(edited.getVersion()).isEqualTo(1);

        assertThatThrownBy(() -> service.editBookTitle(id, "TEST_2", 0L))
                .isExactlyInstanceOf(BookServiceException.class)
                .hasMessage("CONCURRENT_UPDATE");
    }

    @Transactional
    void edit(Long id, String title) {
        try {
            // Начинаем транзакцию чтением
            service.getBookById(id);

            // Ждем старта, одновременно редактируем
            barrier.await(100, TimeUnit.MILLISECONDS);
            service.editBookTitle(id, title);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
