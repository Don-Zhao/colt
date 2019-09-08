package com.colt.app.service;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import com.colt.app.dao.ItemMapper;
import com.colt.app.dao.OrderMapper;
import com.colt.app.dao.PromoItemMapper;
import com.colt.app.dao.SquMapper;
import com.colt.app.daoobj.Cart;
import com.colt.app.daoobj.CartItem;
import com.colt.app.daoobj.Item;
import com.colt.app.daoobj.ItemInfo;
import com.colt.app.daoobj.ItemStock;
import com.colt.app.daoobj.OrderItem;
import com.colt.app.daoobj.PromoItem;
import com.colt.app.vo.CartItemViewModel;
import com.colt.app.vo.CartViewModel;
import com.colt.app.vo.ItemViewModel;

@Service
public class ItemService {
	
	@Autowired
	private ItemMapper itemMapper;
	
	@Autowired
	private OrderMapper orderMapper;
	
	@Autowired
	private SquMapper squMapper;
	
	@Autowired
	private PromoItemMapper promoItemMapper;
	
	@Transactional
	public List<ItemViewModel> getAll() {
		List<Item> items = itemMapper.selectAll();
		if (items == null || items.size() == 0) {
			return null;
		}
		
		List<ItemViewModel> itemViewModels = new ArrayList<>();
		for (int i = 0; i < items.size(); i++) {
			Item item = items.get(i);
			ItemViewModel itemViewModel = new ItemViewModel();
			itemViewModel.setId(item.getId());
			itemViewModel.setName(item.getName());
			itemViewModel.setDescription(item.getDescription());
			itemViewModel.setPrice(item.getPrice());
			itemViewModel.setImg(item.getImg());
			itemViewModel.setStock(item.getStock());
			itemViewModel.setSales(item.getSales());
			
			itemViewModels.add(itemViewModel);
		}
		
		return itemViewModels;
	}
	
	@Transactional
	public ItemViewModel getItemById(int id) {
		Item item = itemMapper.selectById(id);
		if (item == null) {
			return null;
		}
		
		ItemViewModel itemViewModel = new ItemViewModel();
		itemViewModel.setId(item.getId());
		itemViewModel.setName(item.getName());
		itemViewModel.setDescription(item.getDescription());
		itemViewModel.setPrice(item.getPrice());
		itemViewModel.setImg(item.getImg());
		itemViewModel.setStock(item.getStock());
		itemViewModel.setSales(item.getSales());
		
		PromoItem promoItem = promoItemMapper.getById(id);
		if (promoItem != null) {
			itemViewModel.setExist(true);
			itemViewModel.setPromoName(promoItem.getName());
			System.out.println(promoItem.getName());
			itemViewModel.setStartTime(promoItem.getStartTime());
			System.out.println(promoItem.getStartTime());
			itemViewModel.setEndTime(promoItem.getEndTime());
			itemViewModel.setPromoPrice(promoItem.getPromoPrice());
			System.out.println(promoItem.getEndTime());
			Date now = LocalDateTime.now().toDate();
			if (now.after(promoItem.getStartTime()) && now.before(promoItem.getEndTime())) {
				itemViewModel.setOn(true);
			} else {
				itemViewModel.setOn(false);
			}
		}
		
		return itemViewModel;
	}
	
	@Transactional
	public void addFile(ItemViewModel itemViewModel, MultipartFile photo) {
		try {
			ItemInfo itemInfo = new ItemInfo();
			itemInfo.setName(itemViewModel.getName());
			itemInfo.setDescription(itemViewModel.getDescription());
			itemInfo.setPrice(itemViewModel.getPrice());
			String fileName = photo.getOriginalFilename();
			itemInfo.setImg("/imgs/" + fileName);
			int itemId = itemMapper.insertItemInfo(itemInfo);
			String basePath = ResourceUtils.getURL("classpath:static/imgs/").getPath().substring(1);
			photo.transferTo(new File(basePath + photo.getOriginalFilename()));
			
			ItemStock itemStock = new ItemStock();
			itemStock.setItemId(itemId);
			itemStock.setStock(itemViewModel.getStock());
			itemStock.setSales(0);
			itemMapper.insertItemStock(itemStock);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	@Transactional
	public void addCart(int itemId, BigDecimal price, int userId) {
		Cart cart = itemMapper.getCartByUserId(userId);
		if (cart == null) {
			itemMapper.insertCart(userId);
			cart = itemMapper.getCartByUserId(userId);
		}
		
		CartItem item = itemMapper.getCartItem(itemId, cart.getId());
		if (item == null) {
			CartItem cartItem = new CartItem();
			cartItem.setCartId(cart.getId());
			cartItem.setCount(1);
			cartItem.setItemId(itemId);
			cartItem.setPrice(price);
			itemMapper.insertCartItem(cartItem);
		} else {
			item.setCount(item.getCount() + 1);
			itemMapper.updateCartItem(item);
		}
	}
	
	@Transactional
	public CartViewModel getCart(int userId) {
		int cartId = itemMapper.getCartId(userId);
		List<CartItem> cartItems = itemMapper.getCart(cartId);
		if (cartItems == null || cartItems.size() == 0) {
			return null;
		}
		
		CartViewModel cartViewModel = new CartViewModel();
		
		for (CartItem cartItem : cartItems) {
			ItemInfo itemInfo = itemMapper.selectById(cartItem.getItemId());
			
			CartItemViewModel cartItemViewModel = new CartItemViewModel();
			cartItemViewModel.setId(cartItem.getId());
			cartItemViewModel.setCount(cartItem.getCount());
			
			cartItemViewModel.setUserId(userId);
			
			ItemViewModel itemViewModel = new ItemViewModel();
			
			itemViewModel.setId(cartItem.getItemId());
			itemViewModel.setName(itemInfo.getName());
			itemViewModel.setDescription(itemInfo.getDescription());
			itemViewModel.setPrice(itemInfo.getPrice());
			itemViewModel.setImg(itemInfo.getImg());
			cartItemViewModel.setItemViewModel(itemViewModel);
			cartViewModel.addItem(cartItemViewModel);
		}
		
		return cartViewModel;
	}
	
	@Transactional
	public CartViewModel toOrder(int[] itemIds, int[] counts, int userId) {
		CartViewModel cartViewModel = new CartViewModel();
		BigDecimal balance = new BigDecimal(0);
		for (int i = 0; i < itemIds.length; i++) {
			CartItemViewModel cartItemViewModel = new CartItemViewModel();
			Item itemInfo = itemMapper.selectById(itemIds[i]);
			cartItemViewModel.setUserId(userId);
			cartItemViewModel.setCount(counts[i]);
			
			ItemViewModel itemViewModel = new ItemViewModel();
			itemViewModel.setId(itemInfo.getId());
			itemViewModel.setName(itemInfo.getName());
			itemViewModel.setImg(itemInfo.getImg());
			PromoItem promoItem = promoItemMapper.getById(itemIds[i]);
			if (promoItem != null) {
				itemViewModel.setExist(true);
				itemViewModel.setPromoName(promoItem.getName());
				System.out.println(promoItem.getName());
				itemViewModel.setStartTime(promoItem.getStartTime());
				System.out.println(promoItem.getStartTime());
				itemViewModel.setEndTime(promoItem.getEndTime());
				itemViewModel.setPromoPrice(promoItem.getPromoPrice());
				System.out.println(promoItem.getEndTime());
				Date now = LocalDateTime.now().toDate();
				if (now.after(promoItem.getStartTime()) && now.before(promoItem.getEndTime())) {
					itemViewModel.setOn(true);
					cartItemViewModel.setPrice(promoItem.getPromoPrice());
					itemViewModel.setPrice(promoItem.getPromoPrice());
				} else {
					itemViewModel.setOn(false);
					cartItemViewModel.setPrice(itemInfo.getPrice());
					itemViewModel.setPrice(itemInfo.getPrice());
				}
			} else {
				cartItemViewModel.setPrice(itemInfo.getPrice());
				itemViewModel.setPrice(itemInfo.getPrice());
			}
			
			itemViewModel.setSales(itemInfo.getSales());
			itemViewModel.setStock(itemInfo.getStock());
			itemViewModel.setDescription(itemInfo.getDescription());
			cartItemViewModel.setItemViewModel(itemViewModel);
			
			cartViewModel.addItem(cartItemViewModel);
			balance = balance.add(cartItemViewModel.getPrice().multiply(BigDecimal.valueOf(cartItemViewModel.getCount())));
		}
		
		cartViewModel.setBalance(balance);
		return cartViewModel;
	}
	
	@Transactional
	public String order(CartViewModel cartViewModel) {
		int userId = cartViewModel.getItems().get(0).getUserId();
		int cartId = itemMapper.getCartId(userId);
		String orderId = createOrderId();
		for (int i = 0; i < cartViewModel.getItems().size(); i++) {
			CartItemViewModel cartItemViewModel = cartViewModel.getItems().get(i);
			
			itemMapper.updateOrderFlag(cartId, cartItemViewModel.getItemViewModel().getId());
			Item item = itemMapper.selectById(cartItemViewModel.getItemViewModel().getId());
			ItemStock itemStock = new ItemStock();
			itemStock.setItemId(item.getId());
			itemStock.setStock(item.getStock() - cartItemViewModel.getCount());
			itemStock.setSales(item.getSales() + cartItemViewModel.getCount());
			
			itemMapper.updateStock(itemStock);
			
			OrderItem orderItem = new OrderItem();
			orderItem.setCount(cartItemViewModel.getCount());
			orderItem.setId(orderId);
			orderItem.setOrderPrice(cartItemViewModel.getPrice());
			orderItem.setUserId(cartItemViewModel.getUserId());
			orderItem.setFlag(0);
			orderMapper.insertOrderItem(orderItem);
		}
		
		return orderId;
	}
	
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	private String createOrderId() {
		StringBuilder sb = new StringBuilder();
		LocalDateTime time = LocalDateTime.now(); 
		sb.append(time.toLocalDate().toString().replace("-", ""));
		
		int value = squMapper.getValueById(1);
		squMapper.updateValueById(1, value + 1);
		sb.append(String.format("%06d", value));
		sb.append("01");
		
		return sb.toString();
	}
	
	public int pay(String orderId) {
		return orderMapper.updateOrderItem(orderId);
	}
}
