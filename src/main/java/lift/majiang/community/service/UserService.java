package lift.majiang.community.service;

import lift.majiang.community.model.User;

public interface UserService {
    void insert(User user);

    User findByToken(String token);
}
