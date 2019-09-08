package com.colt.app.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import com.colt.app.daoobj.OrderItem;

@Mapper
public interface OrderMapper {
	@Insert("insert into t_order_item(id, user_id, order_price, count, flag) values(#{id}, #{userId}, #{orderPrice}, #{count}, 0)")
	public int insertOrderItem(OrderItem orderItem);
	
	@Update("update t_order_item set flag=1")
	public int updateOrderItem(String id);
}
