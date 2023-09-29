package edu.byuh.cis.cs203.tapthechips2;

import android.graphics.*;

public class Chip {

    private static final Paint lightC, darkC, gold, red, haloPaint;

    // Initialize colors
    static {
        lightC = new Paint();
        lightC.setColor(Color.rgb(247, 199, 255));
        darkC = new Paint();
        darkC.setColor(Color.rgb(130, 199, 255));
        gold = new Paint();
        gold.setColor(Color.rgb(202,192,6));
        red = new Paint();
        red.setColor(Color.rgb(255,0,0));
        haloPaint = new Paint();
        haloPaint.setColor(Color.rgb(0, 153, 76));
        haloPaint.setStyle(Paint.Style.STROKE);
        haloPaint.setStrokeWidth(5);
    }

    public final int colorNum;
    public Cell cell;
    public final boolean isPower;
    private boolean selected = false;
    private boolean OnCorner = false;

    private Chip(int colorNum, Cell cell, boolean isPower) {
        this.colorNum = colorNum;
        this.cell = cell;
        this.isPower = isPower;
    }

    // small factory pattern
    public static Chip normal(int colorNum, Cell cell) {
        return new Chip(colorNum, cell, false);
    }

    public static Chip power(int colorNum, Cell cell) {
        return new Chip(colorNum, cell, true);
    }

    // For onTouchEvent

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isSelected() {
        return selected;
    }

    // For calculate position
    public void beOnCorner(){
        OnCorner = true;
    }

    public boolean isOnCorner() {
        return OnCorner;
    }

    public RectF getAbsolute(){
        return cell.absolute;
    }

    // Return realative position
    public Point getRelative() {
        Point relative = new Point(cell.x, cell.y);
        return relative;
    }

    // Draw chips
    public void draw(Canvas c) {
        float cellW = c.getWidth() / 9;
        RectF absolute = cell.absolute;
        float radius = absolute.width() / 2 * 0.8f;
        float cx = absolute.left + (cellW / 2);
        float cy = absolute.top + (cellW / 2);
        if (colorNum == Team.light) {
            c.drawCircle(cx, cy, radius, lightC);
        } else if (colorNum == Team.dark) {
            c.drawCircle(cx, cy, radius, darkC);
        }
        c.drawCircle(cx, cy, radius, BoardView.boarder);

        // Draw power chip
        if (isPower) {
            c.drawCircle(cx, cy, radius * 0.4f, gold);
        }

        // Draw halo
        if (selected) {
            float haloRadius = radius + 10;
            c.drawCircle(cx, cy, haloRadius, haloPaint);
        }
    }
}
