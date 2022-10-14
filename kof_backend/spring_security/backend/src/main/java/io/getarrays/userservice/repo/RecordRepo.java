package io.getarrays.userservice.repo;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.getarrays.userservice.domain.Record;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RecordRepo extends BaseMapper<Record> {
}
