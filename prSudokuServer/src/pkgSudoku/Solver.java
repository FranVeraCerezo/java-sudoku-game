/*
 * Copyright (c) 2015 Alexane Rose, Etienne Casanova, Ewen Fagon and Tristan Bourvon
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package pkgSudoku;

import java.util.HashSet;
import java.util.Set;
import java.util.ArrayList;

public class Solver {

    private ArrayList< ArrayList< Set<Integer> >> testedNumbers = new ArrayList< ArrayList< Set<Integer> >>(Board.SIZE);

    private Board board;

    private int current_x = 0;
    private int current_y = 0;

    private boolean lastBacktrack = false;

    public Solver(Board b) {
        for ( int i = 0; i< Board.SIZE; i++) {
            testedNumbers.add(i, new ArrayList < Set<Integer>>(Board.SIZE));
            for ( int j = 0; j < Board.SIZE; j++ ) {
                testedNumbers.get(i).add(j, new HashSet<Integer> ());
            }
        }

        board = b;
    }

    /**
     * Checks if a number can be added at a certain position in the board.
     * @param x The x coordinate of the cell to be checked.
     * @param y The y coordinate of the cell to be checked.
     * @param num The number to check in the cell.
     * @return If the cell can be set to this number.
     */
    private boolean isValid ( int x, int y, int num ) {
        // First we check in the column
        for (int i = 0; i < Board.SIZE; i++) {
            if (i == y)
                continue; // Jump to next iteration if we are on our cell, ignoring any further instruction

            if (board.getNumber(x, i) == num)
                return false;
        }


        // Then we check the rows
        for (int i = 0; i < Board.SIZE; i++) {
            if (i == x)
                continue; // Jump to next iteration if we are on our cell, ignoring any further instruction

            if (board.getNumber(i, y) == num)
                return false;
        }

        // Finally, we check the squares
        final int square_x = x / (int)Math.sqrt(Board.SIZE);
        final int square_y = y / (int)Math.sqrt(Board.SIZE);

        final int start_x = square_x * (int)Math.sqrt(Board.SIZE);
        final int start_y = square_y * (int)Math.sqrt(Board.SIZE);

        final int end_x = (square_x + 1) * (int)Math.sqrt(Board.SIZE);
        final int end_y = (square_y + 1) * (int)Math.sqrt(Board.SIZE);

        for (int i = start_x; i < end_x; i++) {
            for (int j = start_y; j < end_y; j++) {
                if (x == i && y == j)
                    continue; // Jump to next iteration if we are one our cell, ignoring any further instruction

                if (board.getNumber(i, j) == num)
                    return false;
            }
        }

        return true; // We never returned false, hence we can safely return true
    }

    /**
     * Returns whether the specified cell is empty.
     * @param x The x coordinate of the cell to be checked.
     * @param y The y coordinate of the cell to be checked.
     * @return If the cell is empty or not.
     */
    private boolean isEmpty ( int x, int y ) {
        return (board.getNumber(x, y) == 0);
    }

    /**
     * Executes the next solving step in the solving process.
     * If we find a const, we either advance, or backtrack if the last action was a backtrack.
     * If we find an empty cell, we just fill it with a random number.
     * If we find an non-empty cell, we try to fill it with another random number.
     * In case there is no number left to try, we backtrack.
     * @return true if the sudoku is solved or if it is impossible, else returns false.
     */
    public boolean nextStep () {
        if (board.isConst(current_x, current_y)) {
            if (lastBacktrack)
                return backtrack();
            lastBacktrack = false;

            return advanceCursor();
        } else {
            if (board.getNumber(current_x, current_y) == 0) {
                int a;
                a = (int)(Math.random()*Board.SIZE+1);
                board.setNumber(current_x, current_y, a);
                testedNumbers.get(current_y).get(current_x).add (a);
                return false;
            }

            if (isValid(current_x, current_y, board.getNumber(current_x, current_y)) && !lastBacktrack)
                return advanceCursor();
            else {
                if (testedNumbers.get(current_y).get(current_x).size() == Board.SIZE) {
                    lastBacktrack = true;
                    board.setNumber(current_x, current_y, 0);
                    testedNumbers.get(current_y).get(current_x).clear();
                    return backtrack();
                } else {
                    lastBacktrack = false;
                    int a;
                    while (true){
                        a = (int)(Math.random()*Board.SIZE+1);
                        if (!testedNumbers.get(current_y).get(current_x).contains (a)){
                            break;
                        }
                    }
                    board.setNumber(current_x, current_y, a);
                    testedNumbers.get(current_y).get(current_x).add (a);
                }
            }
        }

        return false;
    }

    /**
     * Advances the cursor by one cell, taking into account rows and columns.
     * @eturns true if at the end, else false.
     */
    private boolean advanceCursor() {
        if (current_x == Board.SIZE - 1) {
            if (current_y == Board.SIZE - 1)
                return true;

            current_x = 0;
            current_y++;
        } else {
            current_x++;
        }

        return false;
    }

    /**
     * Backtracks the cursor by one cell, taking into account rows and columns.
     * @return true if at the beginning, else false.
     */
    private boolean backtrack() {
        if (current_x == 0) {
            if (current_y == 0)
                return true;

            current_x = Board.SIZE - 1;
            current_y--;
        } else {
            current_x--;
        }

        return false;
    }

    public Board getBoard () {
        return board;
    }

    /**
     * Helper function to execute all steps until one returns true.
     * Leaves the board in a solved, or any state if impossible.
     */
    public void solveBoard () {
        while (!nextStep());
    }


}