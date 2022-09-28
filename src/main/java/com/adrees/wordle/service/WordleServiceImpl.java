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
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import org.springframework.stereotype.Component;

/**
 *
 * @author adrees
 */
@Component
//@Service
public class WordleServiceImpl implements WordleService {

    GameDao gameDao;
    RoundDao roundDao;

    List<String> answers = new ArrayList<>();
    List<String> allWords = new ArrayList<>();

    private final String ANSWER_FILE = "wordle-answer.txt";
    private final String WORDS_FILE = "wordle-dictionary.txt";

    public WordleServiceImpl(GameDao gameDao, RoundDao roundDao) {
        this.gameDao = gameDao;
        this.roundDao = roundDao;
    }

    //Make sure answer is hidden ;-)
    @Override
    public List<Game> getAllGames() {
        List<Game> games = gameDao.getAllGames();

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
        String verifiedGuess = "";
        //check guess to dictionary
        try {
            //what if guess is not in dictionary?
            verifiedGuess = verifyGuess(guess);
        } catch (FileNotFoundException ex) {

        }

        //get results of guess
        String result = getResult(game, verifiedGuess);

        //check if game is finished
        checkIfGameOver(game, result);

        //add result to Round
        Round round = new Round();
        round.setGameId(gameId);
        round.setGuess(verifiedGuess);
        round.setResult(result);

        roundDao.addRound(round);
        return round;
    }

    @Override
    public List<Round> getRoundsForGame(int id) {
        List<Round> results = roundDao.roundsForGame(id);

        if (results.isEmpty()) {
            results = new ArrayList<>();
        }
        return results;
    }

    //returns if Game is finished
    //check Null exception!
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
        loadWords();

//        if (answers.isEmpty()) {
//            return null;
//        }

        //check range of random (do I need to -1?)
        Random randomIndexNumber = new Random();
        int index = randomIndexNumber.nextInt(answers.size());
        //get word from arrayList at index
        String answer = answers.get(index);

        return answer;
    }

    private void loadWords() throws FileNotFoundException {
       
        Scanner scanner;

        //check this try/catch-FileNotFoundException
        try {
            scanner = new Scanner(new BufferedReader(new FileReader(ANSWER_FILE)));
            String currentWord;
            while (scanner.hasNextLine()) {
                currentWord = scanner.nextLine();
                answers.add(currentWord);
            }
            scanner.close();
        } catch (FileNotFoundException e) {

        }

    }

    private String verifyGuess(String guess) throws FileNotFoundException {
        boolean wordIsValid = false;

        loadAllWords();
        loadWords();

        if (allWords.isEmpty()) {
            return null;
        }
        if (answers.isEmpty()) {
            return null;
        }

        for (String word : allWords) {
            if (word.equals(guess)) {
                wordIsValid = true;
            }
        }

        if (!wordIsValid) {
            for (String answer : answers) {
                if (answer.equals(guess)) {
                    wordIsValid = true;
                }
            }
        }
        //if guess if invalid returns empty String
        if (!wordIsValid) {
            return "";
        }

        return guess;

    }

    private void loadAllWords() throws FileNotFoundException {
        Scanner scanner;

        try {
            scanner = new Scanner(new BufferedReader(new FileReader(WORDS_FILE)));
            String currentWord;
            while (scanner.hasNextLine()) {
                currentWord = scanner.nextLine();
                allWords.add(currentWord);
            }

            scanner.close();
        } catch (FileNotFoundException e) {

        }

    }

    private void checkIfGameOver(Game game, String result) {
        //if result has 5 exact indexes, game is won
        //e:01234:p:-;

        if (result.charAt(result.length() - 1) == '-') {
            game.setFinished(true);
            gameDao.updateGame(game);
        }
    }

    private String getResult(Game game, String guess) {
        //exact (e) and partial (p)- show the index of each. otherwise - if none
        Set<Integer> e = new HashSet<>();
        Set<Integer> p = new HashSet<>();
        String answer = game.getAnswer();

        List<Character> guessLetters = new ArrayList<>();
        List<Character> answerLetters = new ArrayList<>();

        for (int i = 0; i < guess.length(); i++) {
            guessLetters.add(guess.charAt(i));
        }

        for (int i = 0; i < answer.length(); i++) {
            answerLetters.add(answer.charAt(i));
        }

        for (Character g : guessLetters) {
            for (Character a : answerLetters) {
                if (g.equals(a)) {
                    //if exact match
                    if (guessLetters.indexOf(g) == answerLetters.indexOf(a)) {
                        //e += guessLetters.indexOf(g);
                        e.add(guessLetters.indexOf(g));
                    } else if (!e.contains(guessLetters.indexOf(g))) {
                        //p += guessLetters.indexOf(g);
                        p.add(guessLetters.indexOf(g));
                    }
                }
            }

        }
        String exact = "";
        for (Integer i : e) {
            exact += i;
        }
        if (exact.isEmpty()) {
            exact = "-";
        }
        String partial = "";
        for (Integer i : p) {
            partial += i;
        }
        if (partial.isEmpty()) {
            partial = "-";
        }

        String result = "e:" + exact + ":p:" + partial;
        return result;

    }

}
