package lift.majiang.community.provider;


import com.alibaba.fastjson.JSONObject;

import lift.majiang.community.dto.GitHubUser;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class GitHubProvider {

    public  JSONObject getAccessToken(String url) throws IOException {
        HttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36");
        HttpResponse response = client.execute(httpPost);
        HttpEntity entity = response.getEntity();
        if (null != entity) {
            String result = EntityUtils.toString(entity, "UTF-8");
            return JSONObject.parseObject(result);
        }
        httpPost.releaseConnection();
        return null;
    }

    /**
     * 获取用户信息
     * get
     */
    public  GitHubUser getUserInfo(String url) throws IOException {
        JSONObject jsonObject = null;
        CloseableHttpClient client = HttpClients.createDefault();

        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36");
        HttpResponse response = client.execute(httpGet);
        HttpEntity entity = response.getEntity();

        if (entity != null) {
            String result = EntityUtils.toString(entity, "UTF-8");
            jsonObject = JSONObject.parseObject(result);
        }

        httpGet.releaseConnection();

        return toObject(jsonObject);
    }

    private GitHubUser toObject(JSONObject jsonObject){
        if(jsonObject == null){
            return null;
        }
        GitHubUser gitHubUser = new GitHubUser();
        gitHubUser.setId(jsonObject.getLong("id"));
        gitHubUser.setName(jsonObject.getString("name"));
        gitHubUser.setEmail(jsonObject.getString("email"));
        gitHubUser.setPhone(jsonObject.getString("phone"));
        gitHubUser.setAvatar_url(jsonObject.getString("avatar_url"));
        return gitHubUser;
    }
}
