package com.todo.dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class TodoItem {
	private int id;
    private String title;
    private String desc;
    private String current_date;
    private String category;
    private String due_date;
	private int is_completed;
	private int time;
	private String name;


    public TodoItem(String title, String desc, String category, String due_date, int time, String name){
        this.title=title;
        this.desc=desc;
        SimpleDateFormat f = new SimpleDateFormat("yyyy/MM/dd kk:mm:ss");
        this.current_date = f.format(new Date());
        this.category = category;
        this.due_date = due_date;
        this.time = time;
        this.name = name;
    }
    
    public TodoItem(int id, String title, String desc, String category, String due_date, String current_date, int is_completed, int time, String name){
        this.id = id;
    	this.title=title;
        this.desc=desc;
        this.category = category;
        this.due_date = due_date;
        this.current_date = current_date;
        this.is_completed = is_completed;
        this.time = time;
        this.name = name;
    }
    
    public TodoItem(String title, String desc, String category, String due_date){
        this.title=title;
        this.desc=desc;
        SimpleDateFormat f = new SimpleDateFormat("yyyy/MM/dd kk:mm:ss");
        this.current_date = f.format(new Date());
        this.category = category;
        this.due_date = due_date;
    }

	public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getCurrent_date() {
        return current_date;
    }

    public void setCurrent_date(String current_date) {
        this.current_date = current_date;
    }
    
	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getDue_date() {
		return due_date;
	}

	public void setDue_date(String due_date) {
		this.due_date = due_date;
	}
	
	public int getIs_completed() {
		return is_completed;
	}

	public void setIs_completed(int is_completed) {
		this.is_completed = is_completed;
	}
	
	public String toSaveString() {
		return category + "##" + title + "##" + desc + "##" + due_date + "##" + current_date + "\n";
	}

	public String getCate() {
		return category;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}	
	
	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public String getname() {
		return name;
	}

	public void setname(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		String check = " ";
		String check_name = " ";
		String person = name;
		int complete = is_completed;
		if (complete == 0) {
			check = " ";
		}
		else if (complete == 1) {
			check = "[V] ";
		}
		
		if (person == " ") {
			check_name = " ";
		}
		else if (person != " ") {
			check_name = "with " + name;
		}
			
		return id + " [ " + category + " ]" + " < " + title + " > " + check + desc + " (~ " + due_date + " time (" + time + ")) (" + current_date + ") " + check_name;
	}
}
