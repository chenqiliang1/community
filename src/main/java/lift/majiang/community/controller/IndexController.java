package lift.majiang.community.controller;

import lift.majiang.community.dto.QuestionDTO;
import lift.majiang.community.mapper.UserMapper;
import lift.majiang.community.model.Question;
import lift.majiang.community.model.User;
import lift.majiang.community.service.QuestionService;
import lift.majiang.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
public class IndexController {

    @Autowired
    private UserService userService;

    @Autowired
    private QuestionService questionService;

    @GetMapping("/")
    public String index(HttpServletRequest request, Model model){
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
           if("token".equals(cookie.getName())){
               String token = cookie.getValue();
               User user = userService.findByToken(token);
               if(user != null){
                   request.getSession().setAttribute("user",user);
               }
               break;
           }
        }
        List<QuestionDTO> questionList = questionService.list();
        model.addAttribute("questions",questionList);

        return "index";
    }

}
