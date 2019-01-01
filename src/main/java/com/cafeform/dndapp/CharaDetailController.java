package com.cafeform.dndapp;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class CharaDetailController {
	
	@RequestMapping("/chara_detail")
    public String test(Model model, @ModelAttribute("name") String name) 
	{
        model.addAttribute("detail", "Character name is " + name);
        return "chara_detail";
    }
}
