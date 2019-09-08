package com.colt.app.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface SquMapper {
	@Select("select value from t_squ where id=#{id}")
	public int getValueById(int id);
	
	@Update("update t_squ set value=#{value} where id=#{id}")
	public int updateValueById(int id, int value);
}
