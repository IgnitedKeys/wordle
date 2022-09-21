/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.adrees.wordle.dao;

import com.adrees.wordle.dto.Game;
import java.util.List;

/**
 *
 * @author adrees
 */
public interface GameDao {
    
    Game add(Game game);
    
    List<Game> getAllGames();
    
    Game getGameById(int id);
    
    Game updateGame(Game game);
    
    void deleteGameById(int id);
    
}
