package lift.majiang.community.controller;

import com.alibaba.fastjson.JSONObject;
import lift.majiang.community.dto.GitHubUser;
import lift.majiang.community.mapper.UserMapper;
import lift.majiang.community.model.User;
import lift.majiang.community.provider.GitHubProvider;
import lift.majiang.community.service.AuthorizeService;
import lift.majiang.community.service.UserService;
import org.apache.http.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.UUID;

@Controller
public class AuthorizeController {

    @Autowired
    private GitHubProvider gitHubProvider;


    @Value("${gitee.oauth.clientid}")
    private String CLIENTID;
    @Value("${gitee.oauth.clientsecret}")
    private String CLIENTSECRET;
    @Value("${gitee.oauth.callback}")
    private String URL;

   @Autowired
   private AuthorizeService authorizeService;

   @Autowired
   private UserService userService;

    @GetMapping("/auth")
    public String auth(HttpSession session){

        return "redirect:"+authUrl(session);
    }

    private String authUrl(HttpSession session){
        // 用于第三方应用防止CSRF攻击
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        session.setAttribute("state", uuid);

        // Step1：获取Authorization Code
        String url = "https://gitee.com/oauth/authorize?response_type=code" +
                "&client_id=" + CLIENTID +
                "&redirect_uri=" + URL +
                "&state=" + uuid +
                "&scope=user_info";
        return url;
    }


    @GetMapping(value = "/callback")
    public String Callback(@RequestParam(name = "code") String code,
                             @RequestParam(name = "state") String state,
                             HttpServletResponse httpResponse,
                             HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession();
        String uuid = (String)session.getAttribute("state");
        // 验证信息我们发送的状态码
        if (null != uuid) {
            // 状态码不正确，直接返回登录页面
            if (!uuid.equals(state)) {
                return "redirect:/";
            }
        }
        String url = callbackUrl(code);
        JSONObject accessTokenJson = gitHubProvider.getAccessToken(url);
        url = "https://gitee.com/api/v5/user?access_token=" + accessTokenJson.get("access_token");
        GitHubUser gitHubUser = gitHubProvider.getUserInfo(url);
        if(gitHubUser != null){
            User user = authorizeService.InsertAndReturnUser(gitHubUser);
            httpResponse.addCookie(new Cookie("token",user.getToken()));
            session.setAttribute("user",user);
            return "redirect:/";
        }else{

            return "redirect:/";
        }
    }
    private String callbackUrl(String code){
        String url = "https://gitee.com/oauth/token?grant_type=authorization_code" +
                "&client_id=" + CLIENTID +
                "&client_secret=" + CLIENTSECRET +
                "&code=" + code +
                "&redirect_uri=" + URL;
        return url;
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request,HttpServletResponse response){
        request.getSession().removeAttribute("user");
        Cookie cookie = new Cookie("token",null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return "redirect:/";
    }
}