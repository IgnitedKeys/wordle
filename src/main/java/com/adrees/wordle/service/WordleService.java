/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.adrees.wordle.service;

import com.adrees.wordle.dto.Game;
import com.adrees.wordle.dto.Round;
import java.util.List;

/**
 *
 * @author adrees
 */
public interface WordleService {

    //get all the games-will I need this in the final product?
    public List<Game> getAllGames();

    public Game getGameById(int id);

    public Game createGame();

    //deleting game also deletes rounds-cannot delete individual rounds in final
    //product...that would be cheating >:)
    public void deleteGame(int id);

    //needs to take user input
    public Round playRound(int gameId, String guess);

    
    
    //needed to display all the rounds
    public List<Round> getRoundsForGame(int id);

    //check if game is won, or if too many tries(game over)
    public boolean checkIfGameIsFinished(int gameId);

}
