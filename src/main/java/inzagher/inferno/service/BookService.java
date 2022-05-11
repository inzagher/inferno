package inzagher.inferno.service;

import inzagher.inferno.dto.BookDTO;
import inzagher.inferno.entity.AuthorEntity;
import inzagher.inferno.entity.BookEntity;
import inzagher.inferno.exception.BookServiceException;
import inzagher.inferno.mapper.BookMapper;
import inzagher.inferno.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository repository;
    private final BookMapper bookMapper = Mappers.getMapper(BookMapper.class);


    @Transactional
    public BookDTO getBookById(Long id) {
        return bookMapper.toDTO(repository.getById(id));
    }

    @Transactional
    public Long createBook(String title, List<String> authors) {
        List<AuthorEntity> authorEntities = authors.stream()
                .map(AuthorEntity::new)
                .toList();
        BookEntity entity = new BookEntity();
        entity.setTitle(title);
        entity.getAuthors().addAll(authorEntities);
        entity = repository.save(entity);
        return entity.getId();
    }

    @Transactional
    public void editBookTitle(Long id, String title) {
        BookEntity entity = repository.getById(id);
        entity.setTitle(title);
        repository.save(entity);
    }

    @Transactional
    public void editBookTitle(Long id, String title, Long version) {
        BookEntity entity = repository.getById(id);
        if (entity.getVersion().equals(version)) {
            entity.setTitle(title);
            repository.save(entity);
        } else {
            throw new BookServiceException("CONCURRENT_UPDATE");
        }
    }

    @Transactional
    public void deleteBookById(Long id) {
        repository.deleteById(id);
    }
}
