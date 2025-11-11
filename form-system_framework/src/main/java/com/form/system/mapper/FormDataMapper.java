package com.form.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.form.system.entity.FormData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FormDataMapper extends BaseMapper<FormData> {
    
    /**
     * 根据动态SQL查询表单数据
     * @param sql SQL语句
     * @return 表单数据列表
     */
    List<FormData> selectListByDynamicSql(@Param("sql") String sql);
}