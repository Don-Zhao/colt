package com.colt.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import com.colt.app.dao.UserMapper;
import com.colt.app.daoobj.FindObj;
import com.colt.app.daoobj.UserAuth;
import com.colt.app.daoobj.UserAuthObj;
import com.colt.app.daoobj.UserInfo;
import com.colt.app.daoobj.UserPassObj;
import com.colt.app.enums.CharacterType;
import com.colt.app.session.UserDetails;
import com.colt.app.utils.ColtUtils;
import com.colt.app.vo.CodeViewModel;
import com.colt.app.vo.FindViewModel;
import com.colt.app.vo.LoginViewModel;
import com.colt.app.vo.UserViewModel;

@Service
public class UserService {
	
	@Autowired
	private UserMapper userMapper;
	
	public boolean checkExistByEmail(String email) {
		UserInfo userInfo = userMapper.getUserInfoByEmail(email);
		if (userInfo != null) {
			return false;
		}
		
		return true;
	}
	
	@Transactional
	public void generateCode(CodeViewModel codeViewModel) {
		String code = ColtUtils.random(5, CharacterType.HLLF_DIGIT);
		UserInfo userInfo = new UserInfo();
		userInfo.setEmail(codeViewModel.getEmail());
		userMapper.insertUserInfo(userInfo);
		
		userInfo = userMapper.getUserInfoByEmail(codeViewModel.getEmail());
		UserAuth userAuth = new UserAuth();
		userAuth.setCaptcha(code);
		userAuth.setUserId(userInfo.getId());
		codeViewModel.setCode(code);
		
		userMapper.insertUserAuth(userAuth);
	}
	
	public boolean checkCaptcha(CodeViewModel codeViewModel) {
		UserAuthObj userAuthObj = userMapper.getUserAuthObject(codeViewModel.getEmail());
		if (userAuthObj == null) {
			return false;
		}
		
		String captcha = codeViewModel.getCode();
		if (StringUtils.equals(captcha, userAuthObj.getCaptcha())) {
			return true;
		}
		
		return false;
	}
	
	@Transactional
	public boolean register(UserViewModel userViewModel) {
		UserInfo userInfo = userMapper.getUserInfoByEmail(userViewModel.getEmail());
		if (userInfo == null) {
			return false;
		}
		
		UserInfo updateUserInfo = new UserInfo();
		updateUserInfo.setId(userInfo.getId());
		updateUserInfo.setName(userViewModel.getName());
		updateUserInfo.setAge(userViewModel.getAge());
		
		UserAuth updateUserAuth = new UserAuth();
		updateUserAuth.setUserId(userInfo.getId());
		updateUserAuth.setPassword(userViewModel.getPassword());
		
		userMapper.updateUserInfo(updateUserInfo);
		userMapper.updateUserAuth(updateUserAuth);
		
		return true;
	}
	
	public UserDetails login(LoginViewModel loginViewModel) {
		String email = loginViewModel.getEmail();
		
		UserPassObj userPassObj = userMapper.getUserPassByEmail(email);
		if (userPassObj == null) {
			return null;
		}
		
		if (StringUtils.equals(userPassObj.getPassword(), loginViewModel.getPassword())) {
			UserDetails userDetails = new UserDetails();
			userDetails.setName(userPassObj.getPassword());
			userDetails.setId(userPassObj.getId());
			userDetails.setEmail(userPassObj.getEmail());
			
			return userDetails;
		}
		
		return null;
	}
	
	public boolean findPassword(FindViewModel findViewModel) {
		UserInfo userInfo = userMapper.getUserInfoByEmail(findViewModel.getEmail());
		if (userInfo == null) {
			return false;
		}
		
		String password = ColtUtils.random(8, CharacterType.HLLF_DIGIT_CHAR);
		FindObj findObj = new FindObj();
		findObj.setUserId(userInfo.getId());
		findObj.setPassword(password);
		
		userMapper.updateUserPasswordByEmail(findObj);
		findViewModel.setDefaultPassword(password);
		
		return true;
	}
}
