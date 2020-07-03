package lift.majiang.community.dto;

import lombok.Data;

@Data
public class GitHubUser {

    private String name;
    private Long id;
    private String email;
    private String phone;
    private String avatar_url;
}
