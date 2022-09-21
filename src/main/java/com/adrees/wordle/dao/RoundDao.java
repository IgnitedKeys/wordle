/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.adrees.wordle.dao;

import com.adrees.wordle.dto.Round;
import java.util.List;

/**
 *
 * @author adrees
 */
public interface RoundDao {

    Round addRound(Round round);
    
    Round getRoundById(int id);
    
    List<Round> roundsForGame(int gameId);
    
    //void or return Round?
    Round deleteRound(Round round);

}
