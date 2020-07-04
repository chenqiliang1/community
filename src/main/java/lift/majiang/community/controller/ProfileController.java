package lift.majiang.community.controller;

import lift.majiang.community.dto.PaginationDTO;
import lift.majiang.community.model.User;
import lift.majiang.community.service.QuestionService;
import lift.majiang.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Controller
public class ProfileController {

    @Autowired
    private UserService userService;

    @Autowired
    private QuestionService questionService;

    @GetMapping("/profile/{action}")
    public String profile(@PathVariable("action")String action,
                          Model model,
                          HttpServletRequest request,
                          @RequestParam(name = "page",defaultValue = "1")Integer page,
                          @RequestParam(name = "size",defaultValue = "5")Integer size){

        User user = (User) request.getSession().getAttribute("user");
        if(user == null){
            model.addAttribute("error","用户未登录");
            return "redirect:/";
        }
        if("questions".contains(action)){
            model.addAttribute("section","questions");
            model.addAttribute("sectionName","我的提问");
        }

        if("replies".contains(action)){
            model.addAttribute("section","replies");
            model.addAttribute("sectionName","最新回复");
        }
        PaginationDTO paginationDTO = questionService.listByUserId(Long.parseLong(user.getAccountId()),page, size);
        model.addAttribute("pagination",paginationDTO);
        return "profile";
    }


}