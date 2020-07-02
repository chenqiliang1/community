package lift.majiang.community.service;

import lift.majiang.community.dto.GitHubUser;
import lift.majiang.community.model.User;

public interface AuthorizeService {

    User InsertAndReturnUser(GitHubUser gitHubUser);
}
