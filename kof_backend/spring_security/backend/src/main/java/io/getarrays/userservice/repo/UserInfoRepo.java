package io.getarrays.userservice.repo;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.getarrays.userservice.domain.UserInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserInfoRepo extends BaseMapper<UserInfo> {
}
