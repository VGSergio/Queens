/* Square class. A square is distinguished by its color, side, 
 * position in the board, determined by the row and column the square
 * is placed, and whether it has a quuen over it or not.
 */
package queens;

import java.awt.Color;
import java.awt.Graphics;

/**
 * @author Sergio Vega
 */
public class Square {
    
    private final int SIDE;
    private Color COLOR;
    private final int Row, Column;
    private boolean Blocked;
    private boolean Queen;
    
    /**
     * Square constructor
     * @param row
     * @param column 
     * @param color 
     * @param side 
     */
    public Square(int row, int column, Color color, int side){
        COLOR = color;
        Row = row;
        Column = column;
        SIDE = side;
        Blocked = false;
        Queen = false;
    }
    
    /**
     * Draws the square with its color and size(Determined by side)
     * @param g 
     */
    public void paint(Graphics g){
        g.setColor(Color.LIGHT_GRAY);                   //Border color
        g.drawRect(Column, Row, SIDE, SIDE);            //Border
        g.setColor(COLOR);                              //Square color
        g.fillRect(Column+1, Row+1, SIDE-1, SIDE-1);    //+-1 so the border can be seen
        if(Queen){
            g.setColor(Color.BLUE);
            g.fillOval(Column+5, Row+5, SIDE-10, SIDE-10);
        }
    }
    
    /**
     * Returns squares side value
     * @return int
     */
    public int getSide(){
        return SIDE;
    }
    
    /**
     * Squares color setter
     * @param color 
     */
    public void setColor(Color color){
        COLOR = color;
    }
    
    /**
     * Squares color getter
     * @return 
     */
    public Color getColor(){
        return COLOR;
    }
    
    /**
     * Squares blocked status getter
     * @return boolean
     */
    public boolean isBlocked(){
        return Blocked;
    }
    
    /**
     * Blocked status setter
     * @param status 
     */
    public void setBlocked(boolean status){
        Blocked = status;
    }
    
    /**
     * Queen setter
     * @param status 
     */
    public void setQueen(boolean status){
        Queen = status;
    }
    
    /**
     * Queen getter
     * @return 
     */
    public boolean hasQueen(){
        return Queen;
    }
}