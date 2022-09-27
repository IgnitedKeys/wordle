/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.adrees.wordle.service;

import com.adrees.wordle.dao.GameDao;
import com.adrees.wordle.dao.RoundDao;
import com.adrees.wordle.dto.Game;
import com.adrees.wordle.dto.Round;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import org.springframework.stereotype.Service;

/**
 *
 * @author adrees
 */
//@Component
@Service
public class WordleServiceImpl implements WordleService {

    GameDao gameDao;
    RoundDao roundDao;

    List<String> answers = new ArrayList<>();
    List<String> allWords = new ArrayList<>();

    private final String ANSWER_FILE;
    private final String WORDS_FILE;
    

    public WordleServiceImpl(GameDao gameDao, RoundDao roundDao) {
        this.gameDao = gameDao;
        this.roundDao = roundDao;
        ANSWER_FILE = "/dictionaries/wordle-answer.txt";
        WORDS_FILE = "/dictionaries/wordle-dictionary.txt";
    }

    public WordleServiceImpl(GameDao gameDao, RoundDao roundDao, String answerFile, String dictionaryFile) {
        this.gameDao = gameDao;
        this.roundDao = roundDao;
        ANSWER_FILE = answerFile;
        WORDS_FILE = dictionaryFile;
    }

    //Make sure answer is hidden ;-)
    @Override
    public List<Game> getAllGames() {
        List<Game> games = gameDao.getAllGames();

        for (Game game : games) {
            if (game.isFinished() == false) {
                game.setAnswer("");
            }
        }

        return games;
    }

    //check user input-make sure error message if null!
    @Override
    public Game getGameById(int id) {
        return gameDao.getGameById(id);
    }

    //check try-catch
    @Override
    public Game createGame() {
        Game newGame = new Game();

        newGame.setFinished(false);
        String answer;
        try {
            answer = getAnswerFromDictionary();
            newGame.setAnswer(answer);
            gameDao.add(newGame);

        } catch (FileNotFoundException ex) {

        }

        return newGame;
    }

    //do I need to delete rounds here if it is in the gameDao?
    @Override
    public void deleteGame(int id) {
        List<Round> rounds = roundDao.roundsForGame(id);
        for (Round round : rounds) {
            roundDao.deleteRound(round);
        }

        gameDao.deleteGameById(id);
    }

    //error if game does not exist
    //error if game is already finished?
    //set user errors if guess length is invalid-check with 
    @Override
    public Round playRound(int gameId, String guess) {

        Game game = gameDao.getGameById(gameId);
        //check guess to dictionary
        try {
            //what if guess is not in dictionary?
            guess = verifyGuess(guess);
        } catch (FileNotFoundException ex) {

        }

        //get results of guess
        String result = getResult(game, guess);

        //check if game is finished
        checkIfGameOver(game, result);
        
        //add result to Round

        Round round = new Round();
        round.setGameId(gameId);
        round.setGuess(guess);
        round.setResult(result);

        roundDao.addRound(round);
        return round;
    }

    @Override
    public List<Round> getRoundsForGame(int id) {
        List<Round> results = roundDao.roundsForGame(id);

        //what if game doesn't exist?
        //Game game = gameDao.getGameById(id);
        //if no rounds in game yet
        if (results.isEmpty()) {
            results = new ArrayList<>();
        }
        return results;
    }

    //returns if Game is finished
    @Override
    public boolean checkIfGameIsFinished(int gameId) {
        try {
            Game game = gameDao.getGameById(gameId);
            return game.isFinished();
        } catch (NullPointerException ex) {
            return false;
        }
    }

    private String getAnswerFromDictionary() throws FileNotFoundException {
        //get random answer from dictionary
        //how to store dictionary and possible answers
        loadWords();
        //get a random index number

        //check range of random (do I need to -1?)
        Random randomIndexNumber = new Random();
        int index = randomIndexNumber.nextInt(answers.size());
        //get word from arrayList at index
        String answer = answers.get(index);

        return answer;
    }

    private void loadWords() throws FileNotFoundException {
        //scanner dictionary
        //add to ArrayList
        Scanner scanner = null;

        //check this try/catch-FileNotFoundException
        try {
            scanner = new Scanner(new BufferedReader(new FileReader(ANSWER_FILE)));
        } catch (FileNotFoundException e) {

        }

        String currentWord;
        while (scanner.hasNextLine()) {
            currentWord = scanner.nextLine();
            answers.add(currentWord);
        }
        scanner.close();
    }

    //HOW TO GET WORD FROM DICTIONARY!!!
    private String verifyGuess(String guess) throws FileNotFoundException {
        //check if guess is in dictionary
        //slow list and check for now
        boolean wordIsValid = false;
        
        loadAllWords();
        
        for(String word : allWords) {
            if(word.equals(guess)) {
                wordIsValid = true;
            }
        }
        
        //WHAT IF GUESS IS INVALID?
        if (!wordIsValid) {
            guess = "";
        }
        
        return guess;

    }

    private String getResult(Game game, String guess) {
        //exact (e) and partial (p)- show the index of each. otherwise - if none
        String e = "";
        String p = "";
        boolean found;
        String answer = game.getAnswer();

        for (int i = 0; i < guess.length(); i++) {
            if (answer.charAt(i) == guess.charAt(i)) {
                e += i;
            } else {
                found = false;
                for (int j = 0; j < guess.length() && !found; j++) {
                    if (answer.charAt(i) == guess.charAt(j)) {
                        found = true;
                        p += j;
                    }
                }
            }
        }

        if (e.isEmpty()) {
            e = "-";
        }
        if (p.isEmpty()) {
            p = "-";
        }

        String result = "e:" + e + ":p:" + p;
        return result;
    }

    private void loadAllWords() throws FileNotFoundException {
        Scanner scanner = null;
        
        try {
            scanner = new Scanner(new BufferedReader(new FileReader(WORDS_FILE)));
        } catch(FileNotFoundException e) {
            
        }
        
        String currentWord;
        while(scanner.hasNextLine()) {
            currentWord = scanner.nextLine();
            allWords.add(currentWord);
        }
        
        scanner.close();
    }

    private void checkIfGameOver(Game game, String result) {
        //if result has 5 exact indexes, game is won
        //e:01234:p:-;
        
        if(result.charAt(result.length()-1) == '-'){
            game.setFinished(true);
        }
    }

}
