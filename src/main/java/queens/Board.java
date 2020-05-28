/* Board class. A board has a certain number of rows and columns 
 * filled with squares of a given size. 
 */
package queens;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;

/**
 * @author Sergio Vega
 */
public class Board extends JPanel{
    
    private final int SIZE;
    private final Square[] SQUARES;
    private final boolean[] RowHasQueen, ColumnHasQueen,
            AdditionDiagonalHasQueen, SubtractionDiagonalHasQueen;
    private final int SIDE;
    
    private int Speed;
    private boolean Solving;
    private final ArrayList<Board> SOLUTIONS = new ArrayList();          
    private int Iterations = 0;
    private int Solutions = 0;
    
    /**
     * Board constructor
     * @param size 
     */
    public Board(int size){
        if(size<=10){
            SIDE = 60;
        } else if(size<=20){
            SIDE = 50;
        } else if(size<=50){
            SIDE = 40;
        } else {
            SIDE = 15;
        }
        SIZE = size;
        SQUARES = new Square[SIZE*SIZE];
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                int position = (i*SIZE)+j;
                if((i%2==0 && j%2==0) || (i%2==1 && j%2==1))
                    SQUARES[position] = new Square(i*SIDE, j*SIDE, Color.WHITE, SIDE);
                else
                    SQUARES[position] = new Square(i*SIDE, j*SIDE, Color.BLACK, SIDE);
            }
        }
        Solving = false;
        RowHasQueen = new boolean[SIZE];
        ColumnHasQueen = new boolean[SIZE];
        AdditionDiagonalHasQueen = new boolean[2*SIZE];
        SubtractionDiagonalHasQueen = new boolean[2*SIZE];
    }
    
    /**
     * Returns the amount of squares that form the board
     * @return SquaresAmount
     */
    public int getSquaresAmount(){
        return SIZE*SIZE;
    }
    
    /**
     * Returns Boards width
     * @return int
     */
    @Override
    public int getWidth(){
        return SIZE*SIDE;
    }
    
    /**
     * Returns board height
     * @return int
     */
    @Override
    public int getHeight(){
        return SIZE*SIDE;
    }

    /**
     * Returns solving status
     * @return boolean
     */
    public boolean isSolving() {
        return Solving;
    }

    /**
     * Solving status setter 
     * @param Solving
     */
    public void setSolving(boolean Solving) {
        this.Solving = Solving;
    }
        
    /**
     * Board paint method
     * @param g 
     */
    @Override
    public void paint(Graphics g) {
        for (Square SQUARE1 : SQUARES) {
            SQUARE1.paint(g);
        }
    }
    
    /**
     * Solving speed setter
     * @param miliseconds 
     */
    public void setSpeed(int miliseconds){
        Speed = miliseconds;
    }
    
    /**
     * BackTracking recursive method to find the solutions
     * @param game
     * @param row
     * @param column
     * @param UsedQueens
     * @return 
     */
    public Board[] Solve(GUI game, int row, int column, int UsedQueens) {
        if(isSolving()){                                //If the board is still solving
            if(UsedQueens==SIZE){                       //If all the queens has been used
                Solutions++;                            //Updates solutions counter
                SOLUTIONS.add(cloneBoard());            //Adds the new solution to the solutions array
                if(Speed==0){                           //If OnlySolutions speed is selected, repaints the board
                    try {
                        game.repaint();                 //Repaints the board
                        Thread.sleep(1);                //Mandatory sleep to have an accurate representation of the solution, some visual bugs may still happen sometimes
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Board.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            } else {            //Not a solution
                while(row<SIZE){
                    if(!RowHasQueen[row]){
                        while(column<SIZE){
                            if(!ColumnHasQueen[column] && CheckDiagonals(row, column)){
                                Iterations++;

                                AddQueen(row, column);
                                UpdateArrays(row, column);
                                UsedQueens++;

                                if(Speed>0){                            //If the selected speed != OnlySolutions -> repaints the board
                                    try {
                                        Thread.sleep(Speed);
                                        game.repaint();                 //Repaints the board
                                    } catch (InterruptedException ex) {
                                        Logger.getLogger(Board.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                }

                                Solve(game, row, column, UsedQueens);

                                UsedQueens--;
                                OutdateArrays(row, column);
                                RemoveQueen(row, column);

                                if(Speed>0){                            //If the selected speed != OnlySolutions -> repaints the board
                                    try {
                                        Thread.sleep(Speed);
                                        game.repaint();                 //Repaints the board
                                    } catch (InterruptedException ex) {
                                        Logger.getLogger(Board.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                }
                            }
                            column++;
                        }
                    }
                    row++;
                    column=0;
                }
            }
        }
        if(UsedQueens==0){                                              //Once BackTracking is done
            Board[] solutions = new Board[SOLUTIONS.size()];            //Solutions array
            SOLUTIONS.toArray(solutions);
            System.out.println(Iterations+" iterations done.");         //Debugging data / useful info
            System.out.println(Solutions+" solution(s) found.");        //Debugging data / useful infosout
            return solutions;
        } else {
            return null;
        }
    }
    
    /**
     * Clones a board
     * @return
     */
    private Board cloneBoard(){
        Board clone = new Board(SIZE);
        for(int i=0; i<clone.getSquaresAmount(); i++){
            clone.SQUARES[i].setColor(SQUARES[i].getColor());
            clone.SQUARES[i].setBlocked(SQUARES[i].isBlocked());
            clone.SQUARES[i].setQueen(SQUARES[i].hasQueen());
        }
        return clone;
    }
    
    /**
     * Method to add a queen onto the board
     * @param row
     * @param column 
     */
    private void AddQueen(int row, int column){
        int pos = (row*SIZE)+column;
        SQUARES[pos].setQueen(true);
    }
    
    /**
     * Method to remove a queen from the board
     * @param row
     * @param column 
     */
    private void RemoveQueen(int row, int column){
        int pos = (row*SIZE)+column;
        SQUARES[pos].setQueen(false);
    }
    
    /**
     * Prune algorithm that checks if there is a queen in the desired diagonal
     * @param row
     * @param column
     * @return 
     */
    private boolean CheckDiagonals(int row, int column){
        int sub = row-column;
        if(sub<0){
            sub *= -1; sub += SIZE;
        }
        return !SubtractionDiagonalHasQueen[sub] && !AdditionDiagonalHasQueen[(row+column)];
    }
    
    /**
     * Updates the arrays used to solve the problem
     * @param row
     * @param column 
     */
    private void UpdateArrays(int row, int column){
        RowHasQueen[row] = true;
        ColumnHasQueen[column] = true;
        AdditionDiagonalHasQueen[(row+column)] = true;
        int sub = row-column;
        if(sub<0){
            sub *= -1; sub += SIZE;
        }
        SubtractionDiagonalHasQueen[sub] = true;
    }
    
    /**
     * Outdates the arrays used to solve the problem
     * @param row
     * @param column 
     */
    private void OutdateArrays(int row, int column){
        RowHasQueen[row] = false;
        ColumnHasQueen[column] = false;
        AdditionDiagonalHasQueen[(row+column)] = false;
        int sub = row-column;
        if(sub<0){
            sub *= -1; sub += SIZE;
        }
        SubtractionDiagonalHasQueen[sub] = false;
    }
}
