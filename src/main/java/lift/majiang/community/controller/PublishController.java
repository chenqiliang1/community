package lift.majiang.community.controller;

import lift.majiang.community.dto.QuestionDTO;
import lift.majiang.community.model.Question;
import lift.majiang.community.model.User;
import lift.majiang.community.service.QuestionService;
import lift.majiang.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Controller
public class PublishController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private UserService userService;

    @GetMapping("/publish/{id}")
    public String edit(@PathVariable("id")Integer id,Model model){
        QuestionDTO question = questionService.getById(id);
        model.addAttribute("title",question.getTitle());
        model.addAttribute("description",question.getDescription());
        model.addAttribute("tag",question.getTag());
        model.addAttribute("id",question.getId());
        return "publish";
    }

    @GetMapping("/publish")
    public String publish(){
        return "publish";
    }

    @PostMapping("/publish")
    public String doPublish(Question question,
                            HttpServletRequest request,
                            Model model){
        model.addAttribute("title",question.getTitle());
        model.addAttribute("description",question.getDescription());
        model.addAttribute("tag",question.getTag());

        if(question.getTitle()==null || question.getTitle().equals("")){
            model.addAttribute("error","标题不能为空");
            return "publish";
        }

        if(question.getDescription()==null || question.getDescription().equals("")){
            model.addAttribute("error","内容不能为空");
            return "publish";
        }

        if(question.getTag()==null || question.getTag().equals("")){
            model.addAttribute("error","标签不能为空");
            return "publish";
        }

        User user = (User) request.getSession().getAttribute("user");

        if (user == null){
            model.addAttribute("error","用户未登录");
            return "publish";
        }
        question.setCreator(Long.parseLong(user.getAccountId()));
        questionService.createOrUpdate(question);
        return "redirect:/";
    }

}
