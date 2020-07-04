package lift.majiang.community.service;

import lift.majiang.community.dto.PaginationDTO;
import lift.majiang.community.dto.QuestionDTO;
import lift.majiang.community.model.Question;

public interface QuestionService {
    void create(Question question);

    PaginationDTO list(Integer page, Integer size);

    PaginationDTO listByUserId(long id, Integer page, Integer size);

    QuestionDTO getById(Integer id);

    void createOrUpdate(Question question);
}
