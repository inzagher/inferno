package inzagher.inferno;

import inzagher.inferno.service.BookService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.concurrent.*;

@SpringBootTest
class ConcurrencyTest {
    @Autowired
    private BookService service;
    private volatile CountDownLatch latch = null;

    @BeforeEach
    public void beforeEach() {
        latch = new CountDownLatch(1);
    }

    @Test
    void concurrentUpdate() throws InterruptedException {
        Long id = service.createBook("TEST", Collections.emptyList());
        ExecutorService executor = Executors.newFixedThreadPool(2);
        executor.execute(() -> edit(id, "TEST_1"));
        executor.execute(() -> edit(id, "TEST_2"));

        Thread.sleep(50);
        latch.countDown();

        executor.shutdown();
        executor.awaitTermination(100, TimeUnit.MILLISECONDS);
    }

    @Transactional
    void edit(Long id, String title) {
        try {
            // Начинаем транзакцию чтением
            service.getBookById(id);

            // Ждем старта, одновременно редактируем
            latch.await(100, TimeUnit.MILLISECONDS);
            service.editBookTitle(id, title);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
