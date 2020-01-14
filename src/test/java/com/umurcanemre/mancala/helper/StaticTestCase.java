package com.umurcanemre.mancala.helper;

public class StaticTestCase {

	public static int[][] realGameMoves() {
		return new int[][] {
				{ 1, 3 }, { 2, 3 }, { 1, 2 }, { 2, 4 }, 
				{ 1, 1 }, { 2, 4 }, { 1, 2 }, { 2, 3 }, 
				{ 1, 3 }, { 2, 5 }, { 1, 1 }, { 1, 6 }, 
				{ 2, 3 }, { 1, 3 }, { 2, 1 }, { 1, 4 }, 
				{ 2, 1 }, { 1, 2 }, { 2, 2 }, { 1, 1 },
				{ 2, 5 }, { 2, 3 }, { 2, 1 }, { 1, 6 } 
				};
	}
}
