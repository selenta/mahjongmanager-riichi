package com.mahjongmanager.riichi.utils;

import com.mahjongmanager.riichi.common.Tile;

import java.util.Arrays;
import java.util.List;

/**
 * Example of data to test specific features
 */
public class SampleData {
    public static List<Tile> drawDiscardTestKans(){
        return Arrays.asList(
                // Starting hand
                new Tile(3, Tile.Suit.MANZU),
                new Tile(4, Tile.Suit.MANZU),
                new Tile(5, Tile.Suit.MANZU),
                new Tile(6, Tile.Suit.PINZU),
                new Tile(6, Tile.Suit.PINZU),
                new Tile(6, Tile.Suit.PINZU),
                new Tile(7, Tile.Suit.PINZU),
                new Tile(8, Tile.Suit.PINZU),
                new Tile(4, Tile.Suit.SOUZU),
                new Tile(5, Tile.Suit.SOUZU),
                new Tile(Tile.Wind.NORTH),
                new Tile(Tile.Wind.NORTH),
                new Tile(Tile.Dragon.RED),

                // Draws
                new Tile(Tile.Wind.NORTH),
                new Tile(Tile.Dragon.RED),
                new Tile(6, Tile.Suit.PINZU),
                new Tile(2, Tile.Suit.MANZU),
                new Tile(7, Tile.Suit.SOUZU),
                new Tile(Tile.Wind.SOUTH),
                new Tile(3, Tile.Suit.PINZU),
                new Tile(Tile.Dragon.RED),
                new Tile(2, Tile.Suit.SOUZU),
                new Tile(2, Tile.Suit.SOUZU),
                new Tile(Tile.Wind.NORTH),
                new Tile(2, Tile.Suit.PINZU),
                new Tile(8, Tile.Suit.PINZU),
                new Tile(Tile.Dragon.WHITE),
                new Tile(7, Tile.Suit.PINZU),
                new Tile(8, Tile.Suit.SOUZU),
                new Tile(Tile.Dragon.RED),
                new Tile(7, Tile.Suit.MANZU) );
    }
}
