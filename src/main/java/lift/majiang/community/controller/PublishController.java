package lift.majiang.community.controller;

import lift.majiang.community.model.Question;
import lift.majiang.community.model.User;
import lift.majiang.community.service.QuestionService;
import lift.majiang.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Controller
public class PublishController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private UserService userService;

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

        Cookie[] cookies = request.getCookies();
        User user = null;
        for (Cookie cookie : cookies) {
            if("token".equals(cookie.getName())){
                String token = cookie.getValue();
                user = userService.findByToken(token);
                if(user != null){
                    request.getSession().setAttribute("user",user);
                }
                break;
            }
        }

        if (user == null){
            model.addAttribute("error","用户未登录");
            return "publish";
        }
        question.setGmtCreate(System.currentTimeMillis());
        question.setGmtModified(System.currentTimeMillis());
        questionService.create(question);
        return "redirect:/";
    }

}
