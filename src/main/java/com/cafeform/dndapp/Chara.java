package com.cafeform.dndapp;

public class Chara {
	
	public Chara(String name, String klass) {
		_name = name;
		_klass = klass;
	}
	
	private String _name;
	private String _klass;
	
	public String getName() {
		return _name;
	}
	public void setName(String name) {
		_name = name;
	}
	public String getKlass() {
		return _klass;
	}
	public void setKlass(String klass) {
		_klass = klass;
	}
}
