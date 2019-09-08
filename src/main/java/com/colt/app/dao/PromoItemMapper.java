package com.colt.app.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.colt.app.daoobj.PromoItem;

@Mapper
public interface PromoItemMapper {
	@Select("select id, name, item_id itemId, promo_price promoPrice, start_time startTime, end_time endTime from t_promo_item where item_id=#{itemId} and end_time > current_timestamp ")
	public PromoItem getById(int itemId);
}
