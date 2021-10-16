package com.todo.dao;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

import com.todo.service.TodoSortByDate;
import com.todo.service.TodoSortByName;
import com.todo.service.DbConnect;

public class TodoList {
	private Connection conn;

	public TodoList() {
		this.conn = DbConnect.getConnection();
	}
	
	public TodoItem getItem(int index) {
		Statement stmt;
		TodoItem t = null;
		try {
			stmt = conn.createStatement();
			String sql = "SELECT id, title, memo, category, current_date, due_date";
			ResultSet rs = stmt.executeQuery(sql);
			if (rs.getRow() < 1) return null;
			rs.next();
			int id= rs.getInt("id");
			String category = rs.getString("category");
			String title = rs.getString("title");
			String description = rs.getString("memo");
			String due_date = rs.getString("due_date");
			String current_date = rs.getString("current_date");
			stmt.close();
			t = new TodoItem(title, description, category, due_date);
			t.setId(id);
			t.setCurrent_date(current_date);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return t;
	}
	
	public int getList(int comp_check) {
		String sql = "UPDATE list set is_completed=1 WHERE id=?;";
		int cnt = 0;
		PreparedStatement pstmt;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, comp_check);
			cnt = pstmt.executeUpdate();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return cnt;
	}

	public int addItem(TodoItem t) {
		String sql = "insert into list (title, memo, category, current_date, due_date, time, name)"
				+ " values (?,?,?,?,?,?,?);";
		PreparedStatement pstmt;
		int cnt = 0;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, t.getTitle());
			pstmt.setString(2, t.getDesc());
			pstmt.setString(3, t.getCategory());
			pstmt.setString(4, t.getCurrent_date());
			pstmt.setString(5, t.getDue_date());
			pstmt.setInt(6, t.getTime());
			pstmt.setString(7, t.getname());
			cnt = pstmt.executeUpdate();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return cnt;
	}
	
	public int updateItem(TodoItem t) {
		String sql = "update list set title=?, memo=?, category=?, current_date=?, due_date=?, time=?, name=?"
				+ "where id = ?;";
		PreparedStatement pstmt;
		int cnt = 0;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, t.getTitle());
			pstmt.setString(2, t.getDesc());
			pstmt.setString(3, t.getCategory());
			pstmt.setString(4, t.getCurrent_date());
			pstmt.setString(5, t.getDue_date());
			pstmt.setInt(6, t.getTime());
			pstmt.setString(7, t.getname());
			pstmt.setInt(8, t.getId());
			cnt = pstmt.executeUpdate();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return cnt;
	}

	public int deleteItem(int index) {
		String sql = "delete from list where id=?;";
		PreparedStatement pstmt;
		int cnt = 0;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, index);
			cnt = pstmt.executeUpdate();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return cnt;
	}

	public ArrayList<TodoItem> getList() {
		ArrayList<TodoItem> list = new ArrayList<TodoItem>();
		Statement stmt;
		try {
			stmt = conn.createStatement();
			String sql = "SELECT * FROM list";
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next()) {
				int id = rs.getInt("id");
				String category = rs.getString("category");
				String title = rs.getString("title");
				String description = rs.getString("memo");
				String due_date = rs.getString("due_date");
				String current_date = rs.getString("current_date");
				int is_completed = rs.getInt("is_completed");
				int time = rs.getInt("time");
				String name = rs.getString("name");
				list.add(new TodoItem(id, title, description, category, due_date, current_date, is_completed, time, name));
			}
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	

	public int getCount() {
		Statement stmt;
		int cnt = 0;
		try {
			stmt = conn.createStatement();
			String sql = "SELECT count(id) FROM list;";
			ResultSet rs = stmt.executeQuery(sql);
			rs.next();
			cnt = rs.getInt("count(id)");
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return cnt;
	}
	
	public void importData(String filename) {
		try {
			BufferedReader br = new BufferedReader(new FileReader (filename));
			String line;
			String sql = "insert into list (title, memo, category, current_date, due_date)"
					+ " values (?,?,?,?,?);";
			int record_cnt = 0;
			while ((line = br.readLine()) != null) {
				StringTokenizer st = new StringTokenizer(line, "##");
				String category = st.nextToken();
				String title = st.nextToken();
				String description = st.nextToken();
				String due_date = st.nextToken();
				String current_date = st.nextToken();
				
				PreparedStatement pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, title);
				pstmt.setString(2, description);
				pstmt.setString(3, category);
				pstmt.setString(4, current_date);
				pstmt.setString(5, due_date);
				int cnt = pstmt.executeUpdate();
				if (cnt > 0) record_cnt++;
				pstmt.close();
			}
			System.out.println(record_cnt + " records read!!");
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ArrayList<TodoItem> getList(String keyword) {
		ArrayList<TodoItem> list = new ArrayList<TodoItem>();
		PreparedStatement pstmt;
		keyword = "%" + keyword + "%";
		try {
			String sql = "SELECT * FROM list WHERE title like ? or memo like ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, "%" + keyword + "%");
			pstmt.setString(2, "%" + keyword + "%");
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				int id = rs.getInt("id");
				String category = rs.getString("category");
				String title = rs.getString("title");
				String description = rs.getString("memo");
				String due_date = rs.getString("due_date");
				String current_date = rs.getString("current_date");
				int is_completed = rs.getInt("is_completed");
				int time = rs.getInt("time");
				String name = rs.getString("name");
				list.add(new TodoItem(id, title, description, category, due_date, current_date, is_completed, time, name));
			}
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	public ArrayList<TodoItem> getListCategory(String cate_keyword) {
		ArrayList<TodoItem> list = new ArrayList<TodoItem>();
		PreparedStatement pstmt;
		try {
			String sql = "SELECT * FROM list WHERE category = ?;";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, cate_keyword);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				int id = rs.getInt("id");
				String category = rs.getString("category");
				String title = rs.getString("title");
				String description = rs.getString("memo");
				String due_date = rs.getString("due_date");
				String current_date = rs.getString("current_date");
				int is_completed = rs.getInt("is_completed");
				int time = rs.getInt("time");
				String name = rs.getString("name");
				list.add(new TodoItem(id, title, description, category, due_date, current_date, is_completed, time, name));
			}
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	public ArrayList<String> getCategories() {
		ArrayList<String> list = new ArrayList<String>();
		Statement stmt;
		try {
			stmt = conn.createStatement();
			String sql = "SELECT DISTINCT category FROM list;";
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next()) {
				list.add(rs.getString("category"));
			}
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	public ArrayList<TodoItem> getOrderedList(String orderby, int ordering) {
		ArrayList<TodoItem> list = new ArrayList<TodoItem>();
		Statement stmt;
		try {
			stmt = conn.createStatement();
			String sql = "SELECT * FROM list ORDER BY " + orderby;
			if (ordering == 0) sql += " desc";
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next()) {
				int id = rs.getInt("id");
				String category = rs.getString("category");
				String title = rs.getString("title");
				String description = rs.getString("memo");
				String due_date = rs.getString("due_date");
				String current_date = rs.getString("current_date");
				int is_completed = rs.getInt("is_completed");
				int time = rs.getInt("time");
				String name = rs.getString("name");
				list.add(new TodoItem(id, title, description, category, due_date, current_date, is_completed, time, name));
			}
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	public Boolean isDuplicate(String title) {
		for (TodoItem item : getList()) {
			if (title.equals(item.getTitle())) return true;
		}
		return false;
	}

	public int getCompCount() {
		Statement stmt;
		int cnt = 0;
		try {
			stmt = conn.createStatement();
			String sql = "SELECT count(id) FROM list where is_completed=1;";
			ResultSet rs = stmt.executeQuery(sql);
			rs.next();
			cnt = rs.getInt("count(id)");
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return cnt;
	}

	public ArrayList<String> getFriends() {
		ArrayList<String> list = new ArrayList<String>();
		Statement stmt;
		try {
			stmt = conn.createStatement();
			String sql = "SELECT DISTINCT name FROM list;";
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next()) {
				list.add(rs.getString("name"));
			}
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
}
