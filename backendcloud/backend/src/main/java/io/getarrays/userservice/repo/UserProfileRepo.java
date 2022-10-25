package io.getarrays.userservice.repo;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.getarrays.userservice.domain.UserProfile;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserProfileRepo extends BaseMapper<UserProfile> {
}
