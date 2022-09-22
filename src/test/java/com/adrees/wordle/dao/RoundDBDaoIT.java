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
import static org.junit.jupiter.api.Assertions.assertNotNull;
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

@RunWith(SpringRunner.class)
@SpringBootTest
public class RoundDBDaoIT {
    
    @Autowired
    RoundDao roundDao;
    
    @Autowired
    GameDao gameDao;
    
    public RoundDBDaoIT() {
    }
    
    @BeforeAll
    public static void setUpClass() {
    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    
    @BeforeEach
    public void setUp() {
        List<Game> games = gameDao.getAllGames();
        for(Game game : games) {
            gameDao.deleteGameById(game.getGameId());
        }
    }
    
    @AfterEach
    public void tearDown() {
        
    }

    /**
     * Test of addRound and getRoundById method, of class RoundDBDao.
     */
    @Test
    public void testAddAndGetRound() {
        Game game = new Game();
        game.setAnswer("fancy");
        game.setFinished(false);
        game = gameDao.add(game);
        
        Game gameFromDao = gameDao.getGameById(game.getGameId());
        
        Round round = new Round();
        round.setGameId(game.getGameId());
        round.setGuess("audio");
        round.setResult("e:-:p:0");
        round = roundDao.addRound(round);
        
        Round roundFromDao = roundDao.getRoundById(round.getRoundId());
        
        assertEquals(round, roundFromDao);
        assertEquals(roundFromDao.getGameId(), gameFromDao.getGameId());
    }

    /**
     * Test of roundsForGame method, of class RoundDBDao.
     */
    @Test
    public void testRoundsForGame() {
        Game game = new Game();
        game.setAnswer("fancy");
        game.setFinished(false);
        game = gameDao.add(game);
        
        //Game gameFromDao = gameDao.getGameById(game.getGameId());
        
        Round round = new Round();
        round.setGameId(game.getGameId());
        round.setGuess("audio");
        round.setResult("e:-:p:0");
        round = roundDao.addRound(round);
        
        Round round2 = new Round();
        round2.setGameId(game.getGameId());
        round2.setGuess("great");
        round2.setResult("e:-:p:1");
        round2 = roundDao.addRound(round);
        
        List<Round> rounds = roundDao.roundsForGame(game.getGameId());
        
        assertEquals(2, rounds.size());
        assertTrue(rounds.contains(round));
        assertTrue(rounds.contains(round2));
    }

    /**
     * Test of deleteRound method, of class RoundDBDao.
     */
    @Test
    public void testDeleteRound() {
        Game game = new Game();
        game.setAnswer("fancy");
        game.setFinished(false);
        game = gameDao.add(game);
        
        Game gameFromDao = gameDao.getGameById(game.getGameId());
        
        Round round = new Round();
        round.setGameId(game.getGameId());
        round.setGuess("audio");
        round.setResult("e:-:p:0");
        round = roundDao.addRound(round);
        
        Round round2 = new Round();
        round2.setGameId(game.getGameId());
        round2.setGuess("great");
        round2.setResult("e:-:p:3");
        round2 = roundDao.addRound(round2);
        
        roundDao.deleteRound(round);
        Round fromDao =roundDao.getRoundById(round.getRoundId());
        assertNull(fromDao);
        
        Round fromDao2 = roundDao.getRoundById(round2.getRoundId());
        assertNotNull(fromDao2);
    }
    
}
