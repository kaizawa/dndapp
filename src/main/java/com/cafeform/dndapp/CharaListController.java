package com.cafeform.dndapp;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.api.client.util.Maps;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.datastore.QueryResultList;
import com.google.apphosting.api.ApiProxy;

@Controller
public class CharaListController {

    private final DatastoreService datastore;
    static final int PAGE_SIZE = 100;

    public CharaListController() {
	datastore = DatastoreServiceFactory.getDatastoreService();
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String list(Model model) {
	
	Query q = new Query("CharaData").addSort("name", SortDirection.ASCENDING);
	PreparedQuery pq = datastore.prepare(q);

	FetchOptions fetchOptions = FetchOptions.Builder.withLimit(PAGE_SIZE);

	QueryResultList<Entity> charaDatas;
	try {
	    charaDatas = pq.asQueryResultList(fetchOptions);
	} catch (IllegalArgumentException e) {
	    return "chara_list";
	}

	List<Chara> charas = new ArrayList<>();
	for(Entity ch: charaDatas) {
	
	    charas.add(new Chara(ch.getProperty("name").toString(), ch.getProperty("klass").toString()));
	}
	model.addAttribute("charas", charas);
	return "chara_list";
    }
}
