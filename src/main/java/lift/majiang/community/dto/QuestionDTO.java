package lift.majiang.community.dto;

import lift.majiang.community.model.User;
import lombok.Data;

@Data
public class QuestionDTO {
    private long id;
    private String title;
    private String description;
    private long gmtCreate;
    private long gmtModified;
    private long creator;
    private long commentCount;
    private long viewCount;
    private long likeCount;
    private String tag;
    private User user;

}
