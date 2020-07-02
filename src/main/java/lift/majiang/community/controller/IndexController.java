package lift.majiang.community.controller;

import lift.majiang.community.mapper.UserMapper;
import lift.majiang.community.model.User;
import lift.majiang.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Controller
public class IndexController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String index(HttpServletRequest request){
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

        return "index";
    }

}
