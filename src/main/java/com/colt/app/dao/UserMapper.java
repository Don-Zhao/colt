package com.colt.app.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.colt.app.daoobj.FindObj;
import com.colt.app.daoobj.UserAuth;
import com.colt.app.daoobj.UserAuthObj;
import com.colt.app.daoobj.UserInfo;
import com.colt.app.daoobj.UserPassObj;

@Mapper
public interface UserMapper {
	@Select("SELECT * FROM t_user_info WHERE email=#{email}")
	public UserInfo getUserInfoByEmail(String email);
	
	@Insert("insert into t_user_info(name, email, age) values(#{name}, #{email}, #{age})")
	public int insertUserInfo(UserInfo userInfo);
	
	@Insert("insert into t_user_auth(user_id, password, captcha) values(#{userId}, #{password}, #{captcha})")
	public int insertUserAuth(UserAuth userAuth);
	
	@Select("select auth.id id, auth.user_id userId, auth.password password, auth.captcha captcha, info.email email from t_user_auth auth inner join t_user_info info on info.id = auth.user_id where info.email=#{email}")
	public UserAuthObj getUserAuthObject(String email);
	
	@Update("update t_user_info set name=#{name}, age=#{age} where id=#{id}")
	public int updateUserInfo(UserInfo userInfo);
	
	@Update("update t_user_auth set password=#{password} where user_id=#{userId}")
	public int updateUserAuth(UserAuth userAuth);
	
	@Select("select info.id id, info.name, info.email, auth.password password from t_user_auth auth inner join t_user_info info on info.id = auth.user_id where info.email=#{email}")
	public UserPassObj getUserPassByEmail(String email); 
	
	@Update("update t_user_auth set password=#{password} where user_id=#{userId}")
	public int updateUserPasswordByEmail(FindObj findObj);
}
