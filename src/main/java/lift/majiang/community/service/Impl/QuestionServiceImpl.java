package lift.majiang.community.service.Impl;

import lift.majiang.community.mapper.QuestionMapper;
import lift.majiang.community.model.Question;
import lift.majiang.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QuestionServiceImpl implements QuestionService {

    @Autowired
    private QuestionMapper questionMapper;


    @Override
    public void create(Question question) {
        questionMapper.create(question);
    }
}
