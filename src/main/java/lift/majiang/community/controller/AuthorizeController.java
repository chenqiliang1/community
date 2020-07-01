package lift.majiang.community.controller;

import com.alibaba.fastjson.JSONObject;
import lift.majiang.community.dto.AccessTokenDTO;
import lift.majiang.community.dto.GitHubUser;
import lift.majiang.community.provider.GitHubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.UUID;

@Controller
public class AuthorizeController {

    @Autowired
    private GitHubProvider gitHubProvider;


    @Value("${gitee.oauth.clientid}")
    public String CLIENTID;
    @Value("${gitee.oauth.clientsecret}")
    public String CLIENTSECRET;
    @Value("${gitee.oauth.callback}")
    public String URL;

    @GetMapping("/auth")
    public String auth(HttpSession session){
        // 用于第三方应用防止CSRF攻击
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        session.setAttribute("state", uuid);

        // Step1：获取Authorization Code
        String url = "https://gitee.com/oauth/authorize?response_type=code" +
                "&client_id=" + CLIENTID +
                "&redirect_uri=" + URLEncoder.encode(URL) +
                "&state=" + uuid +
                "&scope=user_info";

        return "redirect:"+url;
    }

    @GetMapping(value = "/callback")
    public String qqCallback(HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession();
        // 得到Authorization Code
        String code = request.getParameter("code");
        // 我们放在地址中的状态码
        String state = request.getParameter("state");
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
        System.out.println(gitHubUser);
        return "index";
    }
}