package lift.majiang.community.service;

import lift.majiang.community.dto.QuestionDTO;
import lift.majiang.community.model.Question;

import java.util.List;

public interface QuestionService {
    void create(Question question);

    List<QuestionDTO> list();
}
