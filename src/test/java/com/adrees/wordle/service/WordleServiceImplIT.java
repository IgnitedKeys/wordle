/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package com.adrees.wordle.service;

import com.adrees.wordle.dao.GameDao;
import com.adrees.wordle.dao.RoundDao;
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
public class WordleServiceImplIT {

    @Autowired
    WordleService service;

    @Autowired
    GameDao gameDao;

    @Autowired
    RoundDao roundDao;

    public WordleServiceImplIT() {

    }

    @BeforeAll
    public static void setUpClass() {
    }

    @AfterAll
    public static void tearDownClass() {
    }

    @BeforeEach
    public void setUp() {
        List<Game> games = service.getAllGames();
        for (Game game : games) {
            service.deleteGame(game.getGameId());
        }
    }

    @AfterEach
    public void tearDown() {
    }

    /**
     * Test of getAllGames method, of class WordleServiceImpl.
     */
    @Test
    public void testGetAllGames() {
        //check if empty list returns 0
        assertEquals(service.getAllGames().size(), 0);

        Game game = new Game();
        game.setAnswer("three");
        game.setFinished(false);

        Game testGame = gameDao.add(game);

        Game game2 = new Game();
        game2.setAnswer("Froze");
        game2.setFinished(true);

        Game testGame2 = gameDao.add(game2);

        assertEquals(service.getAllGames().size(), 2);
        assertTrue(service.getAllGames().contains(testGame));
        assertTrue(service.getAllGames().contains(testGame2));

    }

    /**
     * Test of getGameById method, of class WordleServiceImpl.
     */
    @Test
    public void testGetGameById() {
        //check if game doesn't not exist returns null
        assertNull(service.getGameById(1));

        Game game = new Game();
        game.setAnswer("grape");
        game.setFinished(false);

        gameDao.add(game);

        Game testGame = service.getGameById(game.getGameId());

        assertEquals(game, testGame);

    }

    /**
     * Test of createGame and getGameById method, of class WordleServiceImpl.
     */
    @Test
    public void testCreateGameAndGetGameById() {
        Game game = service.createGame();
        //check if game isFinished is set to false
        Game testGame = service.getGameById(game.getGameId());
        assertEquals(testGame.isFinished(), false);
        //make sure answer was set from list
        assertNotNull(testGame.getAnswer());
    }

    /**
     * Test of playRound method, of class WordleServiceImpl. Checks if
     * user guess works from both dictionaries
     */
    @Test
    public void testValidGuess() {
        Game game = service.createGame();

        Game testGame = service.getGameById(game.getGameId());

        Round round = service.playRound(testGame.getGameId(), "claim");

        //check a word in answers list
        assertEquals("claim", round.getGuess());

        Game game2 = service.createGame();
        Game testGame2 = service.getGameById(game2.getGameId());

        Round round2 = service.playRound(testGame2.getGameId(), "claps");

        //check a word in allwords list
        assertEquals("claps", round2.getGuess());

    }

    /**
     * Test of getRoundsForGame method, of class WordleServiceImpl.
     */
    @Test
    public void testGetRoundsForGame() {
        //returns null if game does not exist
        assertNull(service.getGameById(1));
        
        //check if game doesn't have any rounds returns empty Array
        Game game = service.createGame();
        List<Round> emptyRounds = service.getRoundsForGame(game.getGameId());
        assertEquals(emptyRounds.size(), 0);
        
        Round round = service.playRound(game.getGameId(), "claim");
        Round round2 = service.playRound(game.getGameId(), "claps");

        List<Round> rounds = service.getRoundsForGame(game.getGameId());

        assertEquals(rounds.size(), 2);
        assertTrue(rounds.contains(round));
        assertTrue(rounds.contains(round2));
    }

    /**
     * Test of checkIfGameIsFinished method, of class WordleServiceImpl.
     */
    @Test
    public void testCheckIfGameFinished() {
        Game game = new Game();
        game.setAnswer("claps");
        game.setFinished(false);

        Game fromDao = gameDao.add(game);
        assertEquals(fromDao.isFinished(), false);

        service.playRound(game.getGameId(), "claps");
        Game testGame = service.getGameById(game.getGameId());
        assertTrue(testGame.isFinished());
    }

    /**
     * Test of deleteGame method, of class WordleServiceImpl.
     */
    @Test
    public void testDeleteGame() {
        Game game = service.createGame();

        //create a round to check the full delete 
        Round round = new Round();

        round.setGameId(game.getGameId());
        round.setGuess("audio");
        round.setResult("e:-:p:0");
        roundDao.addRound(round);

        service.deleteGame(game.getGameId());

        Game fromDao = service.getGameById(game.getGameId());
        assertNull(fromDao);

    }

    /**
     * Test if deleteGame method works if game does not have rounds
     */
    @Test
    public void testDeleteGameWithNoRounds() {

        Game game = service.createGame();

        service.deleteGame(game.getGameId());

        Game fromDao = service.getGameById(game.getGameId());

        assertNull(fromDao);

    }

    /**
     * Test if helper method verifyGuess in playRound method
     * when user word is invalid- should return an empty string
     */
    @Test
    public void testInvalidGuess() {
        Game game = service.createGame();

        Game testGame = service.getGameById(game.getGameId());

        Round round = service.playRound(testGame.getGameId(), "aaaaa");

        assertEquals("", round.getGuess());
    }
    
    /**
     * Test getResult method as a part of playRound method. Check  result
     * on exact match, partials, and duplicate letters
     */
    @Test
    public void testGetResult() {
        Game game = new Game();
        game.setAnswer("claim");
        game.setFinished(false);
        Game testGame = gameDao.add(game);
        
        //partial
        Round round1 = service.playRound(testGame.getGameId(), "blink");
        assertEquals(round1.getResult(), "e:1:p:2");
        //none
        Round round2 = service.playRound(testGame.getGameId(), "sheep");
        assertEquals(round2.getResult(), "e:-:p:-");
        //all
        Round round3 = service.playRound(testGame.getGameId(), "claim");
        assertEquals(round3.getResult(), "e:01234:p:-");
        
        Game game2 = new Game();
        game2.setAnswer("shell");
        game2.setFinished(false);
        Game testGame2 = gameDao.add(game2);
        
        //duplicate letter
        Round round4 = service.playRound(testGame2.getGameId(), "learn");
        assertEquals(round4.getResult(), "e:-:p:01");
        
        Round round5 = service.playRound(testGame2.getGameId(), "shush");
        assertEquals(round5.getResult(), "e:01:p:-");
    }
    
    /**
     * check if finished games can't be played
     */
    @Test
    public void testPlayingFinishedGames() {
        Game game = new Game();
        game.setAnswer("learn");
        game.setFinished(false);
        gameDao.add(game);
        
        service.playRound(game.getGameId(), "learn");
        //isFinished should be true
        assertTrue(service.checkIfGameIsFinished(game.getGameId()));
    }


}
