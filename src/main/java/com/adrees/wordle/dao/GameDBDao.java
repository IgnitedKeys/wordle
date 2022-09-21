/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.adrees.wordle.dao;

import com.adrees.wordle.dto.Game;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

/**
 *
 * @author adrees
 */
@Repository
public class GameDBDao implements GameDao {

    private final JdbcTemplate jdbcTemplate;

    //constructor with jdbcTemplate
    @Autowired
    public GameDBDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Game add(Game game) {
        final String INSERT_GAME = "INSERT INTO game(answer, finished) VALUES(?, ?)";
        jdbcTemplate.update(INSERT_GAME,
                game.getAnswer(),
                game.isFinished()
        );
        int newId = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        game.setGameId(newId);
        
        return game;
    }

    @Override
    public List<Game> getAllGames() {
        final String sql = "SELECT gameId, answer, finished FROM game;";
        return jdbcTemplate.query(sql, new GameMapper());
    }

    @Override
    public Game getGameById(int id) {
        //try catch if the game returned is null
        try {
            final String SELECT_GAME = "SELECT gameId, answer, finished FROM game WHERE gameId=?;";
            Game game = jdbcTemplate.queryForObject(SELECT_GAME, new GameMapper(), id);
            return game;
        } catch (DataAccessException ex) {
            return null;
        }

    }

    @Override
    public Game updateGame(Game game) {
        //update game to finished
        game.setFinished(true);

        final String UPDATE_GAME = "UPDATE game SET finished = ? WHERE gameId=?";
        jdbcTemplate.update(UPDATE_GAME, game.isFinished(), game.getGameId());

        return game;
    }

    @Override
    public void deleteGameById(int id) {
        //need to delete rounds connected to the game first
        final String DELETE_ROUND = "DELETE FROM round WHERE gameId=?;";
        jdbcTemplate.update(DELETE_ROUND, id);

        final String DELETE_GAME = "DELETE FROM game WHERE gameId= ?;";
        jdbcTemplate.update(DELETE_GAME, id);
    }

    private static final class GameMapper implements RowMapper<Game> {

        @Override
        public Game mapRow(ResultSet rs, int index) throws SQLException {
            Game game = new Game();
            game.setGameId(rs.getInt("gameId"));
            game.setAnswer(rs.getString("answer"));
            game.setFinished(rs.getBoolean("finished"));
            return game;
        }
    }

}
