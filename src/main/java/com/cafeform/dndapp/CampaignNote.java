package com.cafeform.dndapp;

import java.io.Serializable;

public class CampaignNote implements Serializable  {

	private static final long serialVersionUID = 2035005357460507005L;
	
	private String _key = "";
	private String _campaign_name = "デフォルトキャンペーン";
	private String _title = "タイトルなし";
	private String _note;
	private Boolean _deleted = false;
	
	public String getCampaign_name() {
		return _campaign_name;
	}
	public void setCampaign_name(String campaign_name) {
		_campaign_name = campaign_name;
	}
	public String getTitle() {
		return _title;
	}
	public void setTitle(String title) {
		_title = title;
	}
	public String getNote() {
		return _note;
	}
	public void setNote(String note) {
		_note = note;
	}
	public String getKey() {
		return _key;
	}
	public void setKey(String key) {
		_key = key;
	}
	public Boolean isDeleted() {
		return _deleted;
	}
	public void setDeleted(Boolean deleted) {
		_deleted = deleted;
	}	
}
