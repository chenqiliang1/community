package lift.majiang.community.controller;

import com.alibaba.fastjson.JSONObject;
import lift.majiang.community.dto.GitHubUser;
import lift.majiang.community.mapper.UserMapper;
import lift.majiang.community.model.User;
import lift.majiang.community.provider.GitHubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    private UserMapper userMapper;


    @GetMapping("/auth")
    public String auth(HttpSession session){
        // 用于第三方应用防止CSRF攻击
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        session.setAttribute("state", uuid);

        // Step1：获取Authorization Code
        String url = "https://gitee.com/oauth/authorize?response_type=code" +
                "&client_id=" + CLIENTID +
                "&redirect_uri=" + URL +
                "&state=" + uuid +
                "&scope=user_info";

        return "redirect:"+url;
    }

    @GetMapping(value = "/callback")
    public String qqCallback(@RequestParam(name = "code") String code,
                             @RequestParam(name = "state") String state,
                             HttpSession session) throws Exception {
        String uuid = (String) session.getAttribute("state");
        // 验证信息我们发送的状态码
        if (null != uuid) {
            // 状态码不正确，直接返回登录页面
            if (!uuid.equals(state)) {
                return "redirect:/login";
            }
        }
        String url = "https://gitee.com/oauth/token?grant_type=authorization_code" +
                "&client_id=" + CLIENTID +
                "&client_secret=" + CLIENTSECRET +
                "&code=" + code +
                "&redirect_uri=" + URL;
        JSONObject accessTokenJson = gitHubProvider.getAccessToken(url);
        url = "https://gitee.com/api/v5/user?access_token=" + accessTokenJson.get("access_token");
        GitHubUser gitHubUser = gitHubProvider.getUserInfo(url);
        if(gitHubUser != null){
            User user = new User();
            user.setToken(UUID.randomUUID().toString());
            user.setName(gitHubUser.getName());
            user.setAccountId(String.valueOf(gitHubUser.getId()));
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            userMapper.insert(user);
            session.setAttribute("user",gitHubUser);
            return "redirect:/";
        }else{
            System.out.println(1);
        }
        return "index";
    }
}