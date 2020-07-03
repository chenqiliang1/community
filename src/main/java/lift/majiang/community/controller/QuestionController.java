package lift.majiang.community.controller;

import lift.majiang.community.dto.QuestionDTO;
import lift.majiang.community.model.Question;
import lift.majiang.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @GetMapping("/question/{id}")
    public String question(@PathVariable("id")Integer id , Model model){
        QuestionDTO question = questionService.getById(id);
        model.addAttribute("question",question);
        return "question";
    }
}
