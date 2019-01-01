package com.cafeform.dndapp;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class HelloController {
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
    public String test(Model model) 
	{
        model.addAttribute("msg", "sample message");
        return "test";
    }
}
