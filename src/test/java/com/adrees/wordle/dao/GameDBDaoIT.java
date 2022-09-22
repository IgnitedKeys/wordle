/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package com.adrees.wordle.dao;

import com.adrees.wordle.dto.Game;
import com.adrees.wordle.dto.Round;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 *
 * @author adrees
 */

//add Spring Test Framework
@RunWith(SpringRunner.class)
@SpringBootTest
public class GameDBDaoIT {
    
    //add daos --delete unneeded ones later!
    @Autowired
    GameDao gameDao;
    
    @Autowired
    RoundDao roundDao;
    
    public GameDBDaoIT() {
    }
    
    @BeforeAll
    public static void setUpClass() {
    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    
    //delete all rounds to a game?
    @BeforeEach
    public void setUp() {
        
        
        List<Game> games = gameDao.getAllGames();
        for(Game game : games) {
            //check this!
//            List<Round> rounds = roundDao.roundsForGame(game.getGameId());
//            for(Round round : rounds) {
//                roundDao.deleteRound(round);
//            }
            
            gameDao.deleteGameById(game.getGameId());
        }
        
        
    }
    
    @AfterEach
    public void tearDown() {
    }

    /**
     * Test of add method and getGameById , of class GameDBDao.
     */
    @Test
    public void testAddGameAndGetGameById() {
        Game game = new Game();
        
        game.setAnswer("fancy");
        game.setFinished(false);
        gameDao.add(game);
        
        Game fromDao = gameDao.getGameById(game.getGameId());
        
        assertEquals(game, fromDao);
    }

    /**
     * Test of getAllGames method, of class GameDBDao.
     */
    @Test
    public void testGetAllGames() {
        Game game1 = new Game();
        game1.setAnswer("fancy");
        game1.setFinished(false);
        gameDao.add(game1);
        
        Game game2 = new Game();
        game2.setAnswer("trees");
        game2.setFinished(false);
        gameDao.add(game2);
        
        List<Game> allGames = gameDao.getAllGames();
        
        assertEquals(2, allGames.size());
        assertTrue(allGames.contains(game2));
        assertTrue(allGames.contains(game1));
    }

    /**
     * Test of updateGame method, of class GameDBDao.
     */
    @Test
    public void testUpdateGame() {
        
        Game game = new Game();
        game.setAnswer("fancy");
        game.setFinished(false);
        gameDao.add(game);
        
        Game fromDao = gameDao.getGameById(game.getGameId());
        assertEquals(game, fromDao);
        
        game.setFinished(true);
        gameDao.updateGame(game);
        
        assertNotEquals(game, fromDao);
        
        fromDao = gameDao.getGameById(game.getGameId());
        assertEquals(game, fromDao);
    }

    /**
     * Test of deleteGameById method, of class GameDBDao.
     */
    @Test
    public void testDeleteGameById() {
        Game game = new Game();
        game.setAnswer("fancy");
        game.setFinished(false);
        gameDao.add(game);
        
        //create a round to check the full delete 
        Round round = new Round();
        
        round.setGameId(game.getGameId());
        round.setGuess("audio");
        round.setResult("e:-:p:0");
        roundDao.addRound(round);
        
        gameDao.deleteGameById(game.getGameId());
        
        Game fromDao = gameDao.getGameById(game.getGameId());
        assertNull(fromDao);
    }
    
}
