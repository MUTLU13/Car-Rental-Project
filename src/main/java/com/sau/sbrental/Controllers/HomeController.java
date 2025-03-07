package com.sau.sbrental.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String homePage(Model model) {
        model.addAttribute("title", "Car Rental Home");
        return "index"; // templates/index.html dosyasına yönlendirme yapar
    }
}
