/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.adrees.wordle.controller;

import com.adrees.wordle.service.WordleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

/**
 *
 * @author adrees
 */

@Controller
public class WordleController {
    
    @Autowired
    WordleService service;
    
    @GetMapping("/home")
    public String displayHome(){        
        return "home";
    }
    
    @GetMapping("/redirectToPlayGame")
    public String redirectToPlayGame() {
        return "redirect:playGame";
    }
    
    @GetMapping("/playGame")
    public String playGame() {
        //needs to get answer
        return "play_game";
    }
    
    @PostMapping("/playGame")
    public String makeGuess() {
        //needs to check guess with dictionary
        return "/playGame";
    }
    
    
    
}
