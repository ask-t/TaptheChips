package edu.byuh.cis.cs203.tapthechips2;

import android.graphics.RectF;

public class Cell {

    // x and y is the position in the board
    public int x, y;

    // absolute is the absolute position in the screen
    public RectF absolute;

    // which color is the cell.
    public int colorNum;

    public boolean isOnChip = false;

    // initialize the cell
    public Cell(int x, int y, RectF rect, int colorNum){
        this.x = x;
        this.y = y;
        this.absolute = rect;
        this.colorNum = colorNum;
    }

    public void setOnChip(boolean b){
        isOnChip = b;
    }


    /*
    No two chips may occupy the same cell.
    Once a chip is in its home corner, it can only move within its corner; it cannot move back to the neutral cells.
    A chip may move from one neutral cell to another.
    A chip may move from a neutral cell to its home corner.
    No chip may enter the home corner of the opposing team.


     */
    public boolean isLegalMove(Chip c) {
        if (c.colorNum == Team.dark || c.colorNum == Team.light) {
            return this.colorNum == c.colorNum || this.colorNum == Team.neutral;
        }
        if(c.isOnCorner() && c.colorNum == Team.dark){
            if(this.x >5 && this.y <3){
                return true;
            }
            else{
                return false;
            }
        }
        if(c.isOnCorner() && c.colorNum == Team.light){
            if(this.x <3 && this.y >6){
                return true;
            }
            else{
                return false;
            }
        }
        if(c.isOnCorner() && c.colorNum == Team.dark){
                    if(this.x >5 && this.y <3){
                        return true;
                    }
                    else{
                        return false;
                    }
                }
        return false;
    }
}
