package com.colt.app.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.util.StringUtils;

import com.colt.app.activemq.Producer;
import com.colt.app.error.BusinessError;
import com.colt.app.response.CommonReturnType;
import com.colt.app.service.UserService;
import com.colt.app.session.UserDetails;
import com.colt.app.utils.ColtUtils;
import com.colt.app.vo.CodeViewModel;
import com.colt.app.vo.FindViewModel;
import com.colt.app.vo.LoginViewModel;
import com.colt.app.vo.UserViewModel;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private Producer producer;
	
	@RequestMapping("/code")
	public String toCode(Model model) {
		CodeViewModel codeViewModel = new CodeViewModel();
		model.addAttribute("codeViewModel", codeViewModel);
		
		return "code";
	}
	
	@RequestMapping("/toLogin")
	public String toLogin(Model model) {
		LoginViewModel loginViewModel = new LoginViewModel();
		model.addAttribute("loginViewModel", loginViewModel);
		
		return "login";
	}
	
	@RequestMapping("/toFind")
	public String toFind(Model model) {
		FindViewModel findViewModel = new FindViewModel();
		model.addAttribute("findViewModel", findViewModel);
		
		return "find";
	}
	
	@RequestMapping("/getCode")
	@ResponseBody
	public CommonReturnType getCode(@ModelAttribute CodeViewModel codeViewModel, BindingResult result, HttpSession session, Model model) {
		String email = codeViewModel.getEmail();
		if (!userService.checkExistByEmail(email)) {
			return CommonReturnType.create("fail", BusinessError.PARAMETER_VALIDATION_ERROR.setErrorMsg("邮箱已经存在，请重新换个邮箱试试"));
		}
		
		userService.generateCode(codeViewModel);
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("username", codeViewModel.getEmail());
		params.put("code", codeViewModel.getCode());
		params.put("template", "mail_get_code");
		producer.sendMessage(new ActiveMQQueue("mail/test"), params);
		
		return CommonReturnType.create(null);
	}
	
	@RequestMapping("/checkCaptcha")
	public String checkCaptcha(@ModelAttribute CodeViewModel codeViewModel, BindingResult result, Model model) {
		if (!userService.checkCaptcha(codeViewModel)) {
			model.addAttribute("errorMsg", "验证码不正确");
			return "code";
		}
		
		UserViewModel userViewModel = new UserViewModel();
		userViewModel.setEmail(codeViewModel.getEmail());
		model.addAttribute("userViewModel", userViewModel);
		
		return "register";
	}
	
	@RequestMapping("/register")
	public String register(@ModelAttribute UserViewModel userViewModel, BindingResult result, Model model) {
		if (!StringUtils.equals(userViewModel.getPassword(), userViewModel.getConfirmPassword())) {
			model.addAttribute("errorMsg", "密码和确认密码不一致，请重新填写");
			return "register";
		}
		if (!userService.register(userViewModel)) {
			model.addAttribute("errorMsg", "注册失败，请重新注册");
			return "register";
		}
		
		model.addAttribute("msg", "注册成功，请转向登录页面登录");
		return "success";
	}
	
	@RequestMapping("/login")
	public String login(@ModelAttribute LoginViewModel loginViewModel, BindingResult result, HttpSession session, Model model) {
		UserDetails userDetails = userService.login(loginViewModel);
		if (userDetails == null) {
			model.addAttribute("errorMsg", "用户名或者密码错误");
			return "login";
		}
		
		session.setAttribute(ColtUtils.IS_USER_LOGIN, true);
		session.setAttribute(ColtUtils.USER_AUTH_DETAILS, userDetails);
		
		return "redirect:/item/list";
	}
	
	@RequestMapping("/find")
	public String find(@ModelAttribute FindViewModel findViewModel, BindingResult result, Model model) {
		if (!userService.findPassword(findViewModel)) {
			model.addAttribute("errorMsg", "用户邮箱不存在");
			return "find";
		}
		
		model.addAttribute("msg", "密码已经重置，请查看邮箱");
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("username", findViewModel.getEmail());
		params.put("code", findViewModel.getDefaultPassword());
		params.put("template", "mail_get_password");
		producer.sendMessage(new ActiveMQQueue("mail/test"), params);
		
		return "success";
	}
	
	@RequestMapping("/logout")
	public String logout(Model model, HttpSession session) {
		session.removeAttribute(ColtUtils.IS_USER_LOGIN);
		session.removeAttribute(ColtUtils.USER_AUTH_DETAILS);
		
		return "redirect:/user/toLogin";
	}
}
