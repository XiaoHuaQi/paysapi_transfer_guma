package com.zixu.paysapi.jpa;

public class Dto {

	private String card_id;
	private String favoriteTime;
	private String name_;
	private String company;
	private String position;
	private String file_path;
	private String group_id;
	private String group_name;
	private String addTime;
	private String shareTime;
	
	private int count;	//用于记录数查询
	private int favorite; //是否收藏
	private int myupload;
	
	public int getMyupload() {
		return myupload;
	}
	public void setMyupload(int myupload) {
		this.myupload = myupload;
	}
	public String getCard_id() {
		return card_id;
	}
	public void setCard_id(String card_id) {
		this.card_id = card_id;
	}
	public String getFavoriteTime() {
		return favoriteTime;
	}
	public void setFavoriteTime(String favoriteTime) {
		this.favoriteTime = favoriteTime;
	}
	public String getName_() {
		return name_;
	}
	public void setName_(String name_) {
		this.name_ = name_;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public String getFile_path() {
		return file_path;
	}
	public void setFile_path(String file_path) {
		this.file_path = file_path;
	}
	public String getGroup_id() {
		return group_id;
	}
	public void setGroup_id(String group_id) {
		this.group_id = group_id;
	}
	public String getGroup_name() {
		return group_name;
	}
	public void setGroup_name(String group_name) {
		this.group_name = group_name;
	}
	public String getAddTime() {
		return addTime;
	}
	public void setAddTime(String addTime) {
		this.addTime = addTime;
	}
	public String getShareTime() {
		return shareTime;
	}
	public void setShareTime(String shareTime) {
		this.shareTime = shareTime;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public int getFavorite() {
		return favorite;
	}
	public void setFavorite(int favorite) {
		this.favorite = favorite;
	}
	
}
