/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package com.adrees.wordle.service;

import com.adrees.wordle.dao.GameDao;
import com.adrees.wordle.dao.RoundDao;
import com.adrees.wordle.dto.Game;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.bind.annotation.RequestBody;

/**
 *
 * @author adrees
 */

@RunWith(SpringRunner.class)
@SpringBootTest
public class WordleServiceImplIT {
    
    @Autowired
    WordleService service;
    
    @Autowired
    GameDao gameDao;
    
    @Autowired
    RoundDao roundDao;
    
    
    public WordleServiceImplIT() {

    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        List<Game> games = service.getAllGames();
        for(Game game : games) {
            service.deleteGame(game.getGameId());
        }
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getAllGames method, of class WordleServiceImpl.
     */
    @Test
    public void testGetAllGames() {
        Game game = new Game();
        game.setAnswer("three");
        game.setFinished(false);
        
        Game testGame = gameDao.add(game);
        
        assertEquals(service.getAllGames().size(), 1);
        assertTrue(service.getAllGames().contains(testGame));
        
        
    }

    /**
     * Test of getGameById method, of class WordleServiceImpl.
     */
    @Test
    public void testGetGameById() {
    }

    /**
     * Test of createGame method, of class WordleServiceImpl.
     */
    @Test
    public void testCreateGame() {
    }

    /**
     * Test of deleteGame method, of class WordleServiceImpl.
     */
    @Test
    public void testDeleteGame() {
    }

    /**
     * Test of makeGuess method, of class WordleServiceImpl.
     */
    @Test
    public void testMakeGuess() {
    }

    /**
     * Test of getRoundsForGame method, of class WordleServiceImpl.
     */
    @Test
    public void testGetRoundsForGame() {
    }

    /**
     * Test of checkIfGameIsFinished method, of class WordleServiceImpl.
     */
    @Test
    public void testCheckIfGameFinished() {
    }
    
}
