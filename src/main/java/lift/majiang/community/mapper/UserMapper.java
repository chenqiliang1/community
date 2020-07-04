package lift.majiang.community.mapper;

import lift.majiang.community.model.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface UserMapper {

    @Insert("insert into user (name,account_id,token,gmt_create,gmt_modified,avatar_url) values " +
            "(#{name},#{accountId},#{token},#{gmtCreate},#{gmtModified},#{avatarUrl})")
    int insert(User user);

    @Select("select * from user where token = #{token}")
    User findByToken(@Param("token") String token);


    @Select("select * from user where account_id = #{id}")
    User findById(@Param("id") Long id);

    @Select("select * from user where account_id = #{accountId}")
    User findByAoountId(@Param("accountId") String accountId);

    @Update("update user set token = #{token} ," +
            "avatar_url = #{avatarUrl} ," +
            "name = #{name}," +
            "gmt_modified = #{gmtModified} " +
            "where account_id = #{accountId}")
    void updateToken(User user);
}
