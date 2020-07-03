package lift.majiang.community.service.Impl;

import lift.majiang.community.dto.GitHubUser;
import lift.majiang.community.mapper.UserMapper;
import lift.majiang.community.model.User;
import lift.majiang.community.service.AuthorizeService;
import lift.majiang.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AuthorizeServiceImpl implements AuthorizeService {

    @Autowired
    private UserMapper userMapper;


    @Override
    public User InsertAndReturnUser(GitHubUser gitHubUser) {
        User user = new User();
        String token = UUID.randomUUID().toString();
        user.setToken(token);
        user.setName(gitHubUser.getName());
        user.setAccountId(String.valueOf(gitHubUser.getId()));
        user.setGmtCreate(System.currentTimeMillis());
        user.setGmtModified(user.getGmtCreate());
        user.setAvatarUrl(gitHubUser.getAvatar_url());
        userMapper.insert(user);
        return user;
    }
}
