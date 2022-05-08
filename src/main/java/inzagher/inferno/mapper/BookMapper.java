package inzagher.inferno.mapper;

import inzagher.inferno.dto.BookDTO;
import inzagher.inferno.entity.BookEntity;
import org.mapstruct.Mapper;

@Mapper
public interface BookMapper {
    BookDTO toDTO(BookEntity entity);
}
