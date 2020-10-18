package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;

import dao.UserDao;
import vo.User;

@WebServlet(urlPatterns =  "/ajaxLoginCheck")
public class AjaxLoginCheck extends HttpServlet {

	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		String userName = request.getParameter("userName");
		String password= request.getParameter("password");
		String Verifycode = request.getParameter("Verifycode");
		String autoLogin = request.getParameter("autoLogin");
		
		HttpSession session = request.getSession();
		String saveVcode = (String) session.getAttribute("verifyCode");
		Map<String, Object> map = new HashMap<String, Object>();
//		System.out.println(">>>>>>>>>>>>>>>>>>>>"+saveVcode);
		if (!Verifycode.equalsIgnoreCase(saveVcode)) {
			map.put("code", 1);
			map.put("info", "验证码不正确!!!请重试");
		}else {
			UserDao userDao = new UserDao();
			User user = userDao.getUser(userName);
			if (user == null) {
				map.put("code",2);
				map.put("info","用户名不存在!!!请重试");
			}else {
				if (!user.getPassword().equals(password)) {
					map.put("code",3);
					map.put("info","密码不正确!!!请重试");
				}else {
					session.setAttribute("currentUser",user);
					if (autoLogin != null) {
						Cookie cookie1 = new Cookie("userName", userName);
						cookie1.setMaxAge(7*24*24*24);
						response.addCookie(cookie1);
						Cookie cookie2 = new Cookie("password", password);
						cookie2.setMaxAge(7*24*24*24);
						response.addCookie(cookie2);					
					}
					map.put("code",0);
					map.put("info","登陆成功");
				}
			}
		}
			String jsonStr = new Gson().toJson(map);
			response.setContentType("text/html;charset=utf-8");
			PrintWriter out = response.getWriter();
			out.print(jsonStr);
			out.flush();
			out.close();
	}

}
