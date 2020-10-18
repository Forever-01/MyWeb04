package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import tools.JdbcUtil;
import vo.User;

public class UserDao {
	
	public User getUser(String userName) {
		JdbcUtil db = new JdbcUtil();
		Connection conn = db.getConn();
		User user = new User();
		String sql = "select * from t_user where userName=?";
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1,userName);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				user.setUserName(rs.getString(1));
				user.setPassword(rs.getString(2));
				user.setChrName(rs.getString(3));
				user.setRole(rs.getString(4));
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return user;
	}
}

