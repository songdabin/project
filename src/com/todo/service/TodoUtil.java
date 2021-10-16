package com.todo.service;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.*;

import com.todo.dao.TodoItem;
import com.todo.dao.TodoList;

public class TodoUtil {
	
	public static void createItem(TodoList list) {
		String title, desc, category, due_date, name = null;
		int time = 0;
		Scanner sc = new Scanner(System.in);
		
		System.out.print("\n"
				+ "---------- Create item Section ----------\n"
				+ "Enter the title of the item to create: ");
		
		title = sc.nextLine().trim();
		if (list.isDuplicate(title)) {
			System.out.print("Title can't be duplicate. please change the title.");
			//return;
		}
		System.out.print("Enter the category: ");
		category = sc.nextLine().trim();
		//sc.nextLine();
		System.out.print("Enter the description: ");
		desc = sc.nextLine().trim();
		System.out.print("Enter the due_date: ");
		due_date = sc.nextLine().trim();
		System.out.print("Enter the time: ");
		time = sc.nextInt();
		sc.nextLine();
		System.out.print("Enter the person name: ");
		name = sc.nextLine().trim();
				
		TodoItem t = new TodoItem(title, desc, category, due_date, time, name);
		if (list.addItem(t) > 0)
			System.out.println("Item added.");
	}

	public static void deleteItem(TodoList l) {
		Scanner sc = new Scanner(System.in);
		
		System.out.print("\n"
				+ "---------- Delete Item Section ----------\n"
				+ "Enter the number of the item to remove: ");
		
		int no = sc.nextInt();
		
		if (l.deleteItem(no) > 0) {
			System.out.println("Item deleted.");
		}
	}


	public static void updateItem(TodoList l) {
		// cnt -> 현재 번호, no -> 입력받은 번호 (찾아야하는 번호), total -> 전체번호
		Scanner sc = new Scanner(System.in);
		String new_title, new_desc, new_category, new_due_date, new_name = null;
		int new_time = 0;
		
		System.out.print("\n"
				+ "---------- Edit Item Section ----------\n"
				+ "Enter the number of the item to update: ");
		
		int no = sc.nextInt();
		
//		sc.nextLine();
		System.out.print("Enter the new title of the item: ");
		new_title = sc.next().trim();
		
		System.out.print("Enter the new category: ");
		new_category = sc.next();
		
		sc.nextLine();
		System.out.print("Enter the new description: ");
		new_desc = sc.nextLine().trim();
		
		System.out.print("Enter the new due_date: ");
		new_due_date = sc.nextLine().trim();
		
		System.out.print("Enter the time: ");
		new_time = sc.nextInt();
		
		sc.nextLine();
		System.out.print("Enter the person name: ");
		new_name = sc.nextLine().trim();
				
		TodoItem t = new TodoItem(new_title, new_desc, new_category, new_due_date, new_time, new_name);
		
		t.setId(no);
		if (l.updateItem(t) > 0)
			System.out.println("Item updated.");
	}
	
	public static void listAll(TodoList l) {
		System.out.println("\n"
				+ "---------- List " + l.getCount() + " item ----------");
		for (TodoItem item : l.getList()) {
			System.out.println(item.toString());
		}
	}
	
	public static void listComp(TodoList l) {
		System.out.println("\n"
				+ "---------- List " + l.getCompCount() + " item ----------");
		for (TodoItem item : l.getList()) {
			if (item.getIs_completed() == 1)
				System.out.println(item.toString());
		}
	}
	
	public static void saveList(TodoList l, String filename) {
		try {
			Writer w = new FileWriter(filename);
			for (TodoItem item : l.getList()) {
				w.write(item.toSaveString());
			}
			w.close();
			
			System.out.println("File saved.");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void findList(TodoList l, String keyword) {		
		int cnt = 0;
		for (TodoItem item : l.getList(keyword)) {
			cnt++;
			System.out.println(item.toString());
		}
		System.out.println("Find "+ cnt +" items.");
	}
	
	public static void completeItem(TodoList l, int comp_check) {
		if (l.getList(comp_check) > 0)
			System.out.println("Complete check.");
	}

	public static void findCate(TodoList l, String cate_keyword) {
		int cnt = 0;
		
		for (TodoItem item : l.getListCategory(cate_keyword)) {
			cnt++;
			System.out.println(item.toString());
		}
		System.out.println("\nFind "+ cnt +" items.");
	}

	public static void listCateAll(TodoList l) {
		int cnt = 0;
		for (String item : l.getCategories()) {
			System.out.print(item + " ");
			cnt++;
		}
		System.out.println("\n" + cnt + " categories.");
	}
	
	public static boolean isExistCategory(List<String> clist, String cate) {
		for (String c : clist) if (c.equals(cate)) return true;
		return false;
	}

	public static void listAll(TodoList l, String orderby, int ordering) {
		System.out.println("[total " + l.getCount() + " list]");
		for (TodoItem item : l.getOrderedList(orderby, ordering)) {
			System.out.println(item.toString());
		}
	}

	public static void countComp(TodoList l) {
		double cnt = 0;
		double total = l.getCount();
		double percent = 1.0;
		for (TodoItem item : l.getList()) {
			if (item.getIs_completed() == 1)
				cnt++;
		}
		percent = (cnt / total) * 100;
		System.out.printf("%.1f percentage completed.", percent);
	}

	public static void listFriend(TodoList l) {
		int cnt = 0;
		for (String item : l.getFriends()) {
			System.out.print(item + " ");
			cnt++;
		}
		System.out.println("\n" + cnt + " friends.");
	}
}
