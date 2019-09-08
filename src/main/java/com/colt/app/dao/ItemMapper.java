package com.colt.app.dao;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.colt.app.daoobj.Cart;
import com.colt.app.daoobj.CartItem;
import com.colt.app.daoobj.Item;
import com.colt.app.daoobj.ItemInfo;
import com.colt.app.daoobj.ItemStock;

@Mapper
public interface ItemMapper {
	
	@Select("select info.id id, info.name name, info.description description, info.price price, info.img img, stock.stock stock, stock.sales sales from t_item_info info left join t_item_stock stock on stock.item_id = info.id")
	public List<Item> selectAll();
	
	@Select("select info.id id, info.name name, info.description description, info.price price, info.img img, stock.stock stock, stock.sales sales from t_item_info info left join t_item_stock stock on stock.item_id = info.id where info.id=#{id}")
	public Item selectById(int id);
	
	
	//@Insert("insert into t_item_info(name, description, price, img) values(#{name}, #{description}, #{price}, #{img})")
	public int insertItemInfo(ItemInfo itemInfo);
	
	@Insert("insert into t_item_stock(item_id, stock, sales) values(#{itemId}, #{stock}, #{sales})")
	public int insertItemStock(ItemStock itemStock);
	
	@Select("select cart.id id, cart.user_id userId from t_cart cart where cart.user_id=#{userId}")
	public Cart getCartByUserId(int userId);
	
	@Insert("insert into t_cart(user_id) values(#{userId})")
	public int insertCart(int userId);
	
	@Insert("insert into t_cart_item(item_id, price, count, cart_id) values(#{itemId}, #{price}, #{count}, #{cartId})")
	public int insertCartItem(CartItem cartItem);
	
	@Select("select ci.id id, ci.item_id itemId, ci.price price, ci.count count, ci.cart_id cartId from t_cart_item ci where ci.item_id=#{itemId} and ci.cart_id=#{cartId} and order_flag=false")
	public CartItem getCartItem(int itemId, int cartId);
	
	@Update("update t_cart_item set count=#{count} where item_id=#{itemId} and cart_id=#{cartId}")
	public int updateCartItem(CartItem cartItem);
	
	@Select("select id id, item_id itemId, price price, count count, cart_id cartId from t_cart_item where cart_id=#{cartId} and order_flag=false")
	public List<CartItem> getCart(int cartId);
	
	@Select("select id from t_cart where user_id=#{userId}")
	public int getCartId(int userId);
	
	@Update("update t_cart_item set order_flag = 1 where item_id=#{itemId} and cart_id=#{cartId}")
	public int updateOrderFlag(int cartId, int itemId);
	
	@Update("update t_item_stock set stock=#{stock}, sales=#{sales} where item_id=#{itemId}")
	public int updateStock(ItemStock itemStock);
}
