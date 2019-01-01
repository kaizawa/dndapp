package com.cafeform.dndapp;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;

@Controller
public class CharaEditController {

    @RequestMapping("/chara_edit")
    public String test(Model model, @ModelAttribute("name") String name, @ModelAttribute("klass") String klass) {

	Entity charaData = new Entity("CharaData");
	charaData.setProperty("name", name);
	charaData.setProperty("klass", klass);
	DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	datastore.put(charaData);
	
	model.addAttribute("name", name);
	model.addAttribute("klass", klass);

	return "chara_edit";
    }
}
