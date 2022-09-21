/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.adrees.wordle.dao;

import com.adrees.wordle.dto.Round;
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
public class RoundDBDao implements RoundDao {

    private final JdbcTemplate jdbc;

    @Autowired
    public RoundDBDao(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public Round addRound(Round round) {
        final String INSERT_ROUND = "INSERT INTO round(gameId, guess, result) VALUES(?,?,?);";
        jdbc.update(INSERT_ROUND,
                round.getGameId(),
                round.getGuess(),
                round.getResult()
        );

        int newId = jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        round.setRoundId(newId);

        return round;
    }

    @Override
    public Round getRoundById(int id) {
        try {
            final String GET_ROUND = "SELECT roundId, gameId, guess, result FROM round WHERE roundId=?;";
            Round round = jdbc.queryForObject(GET_ROUND, new RoundMapper(), id);
            return round;
        } catch (DataAccessException ex) {
            return null;
        }
    }

    @Override
    public List<Round> roundsForGame(int gameId) {
        final String GET_ROUNDS = "SELECT * FROM round WHERE gameId=?;";
        return jdbc.query(GET_ROUNDS, new RoundMapper(), gameId);
    }

    //should I return the round? or void?
    @Override
    public Round deleteRound(Round round) {
        final String DELETE_ROUND = "DELETE FROM round WHERE roundId=?;";
        jdbc.update(DELETE_ROUND, round.getRoundId());
        return round;
    }

    private static final class RoundMapper implements RowMapper<Round> {

        @Override
        public Round mapRow(ResultSet rs, int Index) throws SQLException {
            Round round = new Round();
            round.setRoundId(rs.getInt("roundId"));
            round.setGuess(rs.getString("guess"));
            round.setResult(rs.getString("result"));
            round.setGameId(rs.getInt("gameId"));

            return round;

        }
    }

}
