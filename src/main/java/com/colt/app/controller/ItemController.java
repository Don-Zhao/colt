package com.colt.app.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import com.colt.app.error.BusinessError;
import com.colt.app.response.CommonReturnType;
import com.colt.app.service.ItemService;
import com.colt.app.session.UserDetails;
import com.colt.app.utils.ColtUtils;
import com.colt.app.vo.CartViewModel;
import com.colt.app.vo.ItemViewModel;

@Controller
@RequestMapping("/item")
public class ItemController {
	
	@Autowired
	private ItemService itemService;
	
	@RequestMapping("/list")
	public String list(Model model) {
		List<ItemViewModel> items = itemService.getAll();
		if (items == null || items.size() == 0) {
			model.addAttribute("errorMsg", "现在没有任何商品信息");
			return "list";
		}
		
		model.addAttribute("items", items);
		return "admin/list";
		
	}
	
	@RequestMapping("/details")
	public String details(@RequestParam int itemId, Model model) {
		ItemViewModel itemViewModel = itemService.getItemById(itemId);
		if (itemViewModel == null) {
			model.addAttribute("errorMsg", "该商品不存在");
			return "details";
		}
		
		model.addAttribute("itemViewModel", itemViewModel);
		return "admin/details";
	}
	
	@RequestMapping("/toRecord")
	public String toRecord(Model model) {
		ItemViewModel itemViewModel = new ItemViewModel();
		model.addAttribute("itemViewModel", itemViewModel);

		return "admin/record";
	}
	
	@RequestMapping("/add")
	public String add(@ModelAttribute ItemViewModel itemViewModel, @RequestParam MultipartFile photo, Model model) {
		String name = photo.getOriginalFilename();
		System.out.println("name = " + name);
		itemService.addFile(itemViewModel, photo);
		model.addAttribute("msg", "添加商品成功");
		return "success";
	}
	
	@RequestMapping("/addCart")
	@ResponseBody
	public CommonReturnType addCart(@RequestParam int itemId, @RequestParam BigDecimal price, Model model, HttpSession session) {
		Object loginFlag = session.getAttribute(ColtUtils.IS_USER_LOGIN);
		if (loginFlag == null) {
			return CommonReturnType.create("NOT_LOGIN", BusinessError.PARAMETER_VALIDATION_ERROR.setErrorMsg("用户没有登录"));
		}
		
		if (!(boolean)loginFlag) {
			return CommonReturnType.create("NOT_LOGIN", BusinessError.PARAMETER_VALIDATION_ERROR.setErrorMsg("用户没有登录"));
		}
		
		UserDetails userDetails = (UserDetails)session.getAttribute(ColtUtils.USER_AUTH_DETAILS);
		int userId = (int)userDetails.getId();
		itemService.addCart(itemId, price, userId);
		
		return CommonReturnType.create(null);
	}
	
	@RequestMapping("/getCart")
	public String getCart(HttpSession session, Model model) {
		Object loginFlag = session.getAttribute(ColtUtils.IS_USER_LOGIN);
		if (loginFlag == null) {
			return "error";
		}
		
		if (!(boolean)loginFlag) {
			return "error";
		}
		
		UserDetails userDetails = (UserDetails)session.getAttribute(ColtUtils.USER_AUTH_DETAILS);
		int userId = (int)userDetails.getId();
		CartViewModel cartViewModel = itemService.getCart(userId);
		
		model.addAttribute("cartViewModel", cartViewModel);
		
		return "admin/cart";
	}
	
	@RequestMapping("/createOrder")
	public String createOrder(@RequestParam int accountPrice, @RequestParam int[] itemIds, @RequestParam int[] counts, HttpSession session, RedirectAttributesModelMap attr) {
		Object loginFlag = session.getAttribute(ColtUtils.IS_USER_LOGIN);
		if (loginFlag == null) {
			return "error";
		}
		
		if (!(boolean)loginFlag) {
			return "error";
		}
		
		UserDetails userDetails = (UserDetails)session.getAttribute(ColtUtils.USER_AUTH_DETAILS);
		int userId = (int)userDetails.getId();
		CartViewModel cartViewModel = itemService.toOrder(itemIds, counts, userId);
		attr.addFlashAttribute("cartViewModel", cartViewModel);
		
		return "redirect:/item/order";
	}
	
	@RequestMapping("/order")
	public String order(Model model) {
		Map<String, Object> maps = model.asMap();
		CartViewModel cartViewModel = (CartViewModel)maps.get("cartViewModel");
		
		String orderId = itemService.order(cartViewModel);
		model.addAttribute("orderId", orderId);
		
		return "admin/order";
	}
	
	@RequestMapping("/pay")
	public String pay(@RequestParam BigDecimal accountPrice, @RequestParam String orderId, Model model) {
		itemService.pay(orderId);
		model.addAttribute("msg", "您已经支付" + accountPrice + "元");
		
		return "success";
	}
	
	@RequestMapping("/buy")
	public String buy(@RequestParam int itemId, Model model, HttpSession session, RedirectAttributesModelMap attr) {
		Object loginFlag = session.getAttribute(ColtUtils.IS_USER_LOGIN);
		if (loginFlag == null) {
			return "error";
		}
		
		if (!(boolean)loginFlag) {
			return "error";
		}
		
		UserDetails userDetails = (UserDetails)session.getAttribute(ColtUtils.USER_AUTH_DETAILS);
		int userId = (int)userDetails.getId();
		CartViewModel cartViewModel = itemService.toOrder(new int[]{itemId}, new int[]{1}, userId);
		attr.addFlashAttribute("cartViewModel", cartViewModel);
		
		return "redirect:/item/order";
	}
}
