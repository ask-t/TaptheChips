package edu.byuh.cis.cs203.tapthechips2;

import android.content.Context;
import android.graphics.*;
import android.view.MotionEvent;
import android.view.View;
import java.util.ArrayList;

public class BoardView extends View {
    // For Cells and Chips
    private Cell[][] board = new Cell[9][10];
    private ArrayList<Chip> chips = new ArrayList<>();

    // stock legal moves
    private ArrayList<Cell> legalMoves = new ArrayList<>();

    // For Paints
    public Paint darkB, lightB, neutral, markerPaint;
    public static Paint boarder;
    // For Cells
    private RectF rect;

    public BoardView(Context c) {
        super(c);
        initPaints();
        setArray();
    }
    // Initialize Paints
    private void initPaints() {
        darkB = new Paint();
        darkB.setColor(Color.rgb(32, 129, 247));
        lightB = new Paint();
        lightB.setColor(Color.rgb(255, 92, 92));
        neutral = new Paint();
        neutral.setColor(Color.rgb(255, 231, 135));
        boarder = new Paint();
        boarder.setColor(Color.BLACK);
        boarder.setStyle(Paint.Style.STROKE);
        boarder.setStrokeWidth(3);
        markerPaint = new Paint();
        markerPaint.setColor(Color.rgb(219, 123, 228));

    }
    // Initialize Cells
    private void setCell(){
        int w = getWidth();
        int h = w/9*10;
        float cellW = (float)w/9;
        float cellH = (float)h/10;
        int colorNum;
        for(int x=0; x<9; x++){
            for(int y=0; y<10;y++){
                if(5<x && y<3){
                    colorNum = Team.dark;
                } else if(x<3 && 6<y){
                    colorNum = Team.light;
                }
                else {
                    colorNum = Team.neutral;
                }
                rect = new RectF(cellW*x,cellH*y,cellW*(x+1), cellH*(y+1));
                board[x][y] =new Cell(x,y,rect,colorNum);
            }
        }
    }


    // Initialize Chips
    private void setChip(){
        chips.add(Chip.normal(Team.light,board[0][0]));
        chips.add(Chip.normal(Team.light,board[1][1]));
        chips.add(Chip.normal(Team.light,board[2][2]));
        chips.add(Chip.normal(Team.light,board[3][3]));
        chips.add(Chip.power(Team.light,board[4][4]));
        chips.add(Chip.normal(Team.light,board[5][5]));
        chips.add(Chip.normal(Team.light,board[6][6]));
        chips.add(Chip.normal(Team.light,board[7][7]));
        chips.add(Chip.normal(Team.light,board[8][8]));
        chips.add(Chip.normal(Team.dark,board[0][1]));
        chips.add(Chip.normal(Team.dark,board[1][2]));
        chips.add(Chip.normal(Team.dark,board[2][3]));
        chips.add(Chip.normal(Team.dark,board[3][4]));
        chips.add(Chip.power(Team.dark,board[4][5]));
        chips.add(Chip.normal(Team.dark,board[5][6]));
        chips.add(Chip.normal(Team.dark,board[6][7]));
        chips.add(Chip.normal(Team.dark,board[7][8]));
        chips.add(Chip.normal(Team.dark,board[8][9]));
    }

    // for test
    private void setChip2(){
        chips.add(Chip.normal(Team.light,board[1][8]));
        chips.add(Chip.normal(Team.dark,board[7][1]));
        chips.add(Chip.normal(Team.dark,board[0][1]));
        chips.add(Chip.normal(Team.dark,board[7][8]));
        chips.add(Chip.power(Team.light,board[1][2]));
        chips.add(Chip.power(Team.dark,board[7][8]));
        chips.add(Chip.power(Team.light,board[4][5]));

    }
    // Initialize Cells and Chips
    private void setArray() {
        setCell();
//        setChip();
        setChip2();
    }

    // check which location the chip can move
    // For right
    public int legalRight(Chip c){
        // get the location of the chip
        int x = c.getRelative().x;
        int y = c.getRelative().y;
        // candidate is the location where the chip can move
        Point candidate = new Point();
        candidate.y = c.getRelative().y;

        //coner is 8
        candidate.x = 8;
        // if the chip is near the home conor.
        boolean isLegalFinal = false;
        if (c.colorNum == Team.light && y >= 0 && y < 3) {
            candidate.x = 5;
        }
        //if the chip is in the home conor.
        if (c.colorNum == Team.light && y > 6  && x <3){
            candidate.x = 2;
            legalMoves.add(board[candidate.x][candidate.y]);
            return 0;
        }
        else if(c.colorNum == Team.dark && y <3  && x > 5){
            candidate.x = 8;
            legalMoves.add(board[candidate.x][candidate.y]);
            return 0;
        }
        // check if there are other chips
        for (int i = x + 1; i < 9; i++) {
            for (Chip chip : chips) {
                // if there are other chips
                if (i == chip.getRelative().x && y == chip.getRelative().y) {
                    // if there is a near chip
                    if (candidate.x > chip.getRelative().x) {
                        candidate.x = chip.getRelative().x - 1;
//                        System.out.println("change value");
//                        System.out.println("Now candidate is "+ candidate.x + " "+ candidate.y);
                    }
                }
                if(c.getRelative().x != 8 && candidate.x != c.getRelative().x) {
                    if(board[i][y].isLegalMove(c)){
                        isLegalFinal = true;
                    }
                }
            }

            // if there is no problem, the position add in the legalMoves
            if(isLegalFinal) {
                if (c.isPower) {
                    for (int j = x; j < candidate.x + 1; j++) {
                        legalMoves.add(board[j][candidate.y]);
                    }
                } else {
//                    System.out.println("Final candidate is " + candidate.x + " " + candidate.y);
                    legalMoves.add(board[candidate.x][candidate.y]);
                }
            }

        }
        for(Chip c1 : chips){
            int xx = c1.getRelative().x;
            int yy = c1.getRelative().y;
            legalMoves.remove(board[xx][yy]);
        }
        return 0;
    }

    // For left
    public int legalLeft(Chip c) {
        int x = c.getRelative().x;
        int y = c.getRelative().y;
        Point candidate = new Point();
        candidate.y = c.getRelative().y;
        candidate.x = 0;
        boolean isLegalFianal = false;
        if (c.colorNum == Team.dark && y >= 7 && y < 10) {
            candidate.x = 3;
        }
        if (c.colorNum == Team.light && y > 6  && x <3){
            candidate.x = 0;
            legalMoves.add(board[candidate.x][candidate.y]);
            return 0;
        }
        else if(c.colorNum == Team.dark && y <3  && x > 5){
            candidate.x = 6;
            legalMoves.add(board[candidate.x][candidate.y]);
            return 0;
        }
        for (int i = x - 1; i >= 0; i--) {
            for (Chip chip : chips) {
                if (i == chip.getRelative().x && y == chip.getRelative().y) {
                    if (candidate.x < chip.getRelative().x) {
                        candidate.x = chip.getRelative().x + 1;
                    }
                }
                    if (chip.getRelative().x != 0 && c.getRelative().x != 0 && candidate.x != c.getRelative().x) {
                    if(board[i][y].isLegalMove(c)){
                        isLegalFianal = true;
                    }
                }
            }
            if(isLegalFianal) {
                if (c.isPower) {
                    for (int j = x; j > candidate.x-1; j--) {
//                        System.out.println("j is " + i);
                        System.out.println("Final candidate is " + candidate.x + " " + candidate.y);
                        legalMoves.add(board[j][candidate.y]);
                    }
                } else {
//                            System.out.println(candidate.x + " " + candidate.y);
                    legalMoves.add(board[candidate.x][candidate.y]);
                }
            }
        }
        for(Chip c1 : chips){
            int xx = c1.getRelative().x;
            int yy = c1.getRelative().y;
            legalMoves.remove(board[xx][yy]);
        }
        return 0;
    }

    // For up
    public int legalUp(Chip c){
            int x = c.getRelative().x;
            int y = c.getRelative().y;
            Point candidate = new Point();
            candidate.x = c.getRelative().x;
            candidate.y = 0;
            boolean isRegalFinal = false;
            if (c.colorNum == Team.light && x > 5 && y < 9) {
                candidate.y = 3;
            }
        if (c.colorNum == Team.light && y > 6  && x <3){
            candidate.y = 7;
            legalMoves.add(board[candidate.x][candidate.y]);
            return 0;
        }
        else if(c.colorNum == Team.dark && y <3  && x > 5){
            candidate.y = 0;
            legalMoves.add(board[candidate.x][candidate.y]);
            return 0;
        }
            for (int i = y - 1; i >= 0; i--) {
                for (Chip chip : chips) {
                    if (i == chip.getRelative().y && x == chip.getRelative().x) {
                        if (candidate.y <= chip.getRelative().y) {
                            candidate.y = chip.getRelative().y + 1;
                        }
                    }
                        if (chip.getRelative().y != 0 && c.getRelative().y != 0 && candidate.y != c.getRelative().y) {
                            if (board[x][i].isLegalMove(c)){
                                isRegalFinal = true;
                            }
                        }
                }
//                System.out.println("isR is "+ isRegalFinal);
                if(isRegalFinal){
                    if(c.isPower){
                        for(int j =y; j>candidate.y-1;j--){
                            legalMoves.add(board[candidate.x][j]);
                        }
                    }else{
                        System.out.println("Final candidate is " + candidate.x + " " + candidate.y);
                        legalMoves.add(board[candidate.x][candidate.y]);
                    }
                }
            }
        for(Chip c1 : chips){
            int xx = c1.getRelative().x;
            int yy = c1.getRelative().y;
            legalMoves.remove(board[xx][yy]);
        }
            return 0;
    }

    // For down
    public int legalDown(Chip c){
            int x = c.getRelative().x;
            int y = c.getRelative().y;
            Point candidate = new Point();
            candidate.x = c.getRelative().x;
            candidate.y = 9;
            boolean isLegalFinal = false;
            if (c.colorNum == Team.dark && x >= 0 && x < 3) {
                candidate.y = 6;
            }
        if (c.colorNum == Team.light && y > 6  && x <3){
            candidate.y = 9;
            legalMoves.add(board[candidate.x][candidate.y]);
            for(Chip c1 : chips){
                int xx = c1.getRelative().x;
                int yy = c1.getRelative().y;
                legalMoves.remove(board[xx][yy]);
            }
            return 0;
        }
        else if(c.colorNum == Team.dark && y <3  && x > 5){
            candidate.y = 2;
            legalMoves.add(board[candidate.x][candidate.y]);
            for(Chip c1 : chips){
                int xx = c1.getRelative().x;
                int yy = c1.getRelative().y;
                legalMoves.remove(board[xx][yy]);
            }
            return 0;
        }
            for (int i = y + 1; i < 10; i++) {
                for (Chip chip : chips) {
                    if (i == chip.getRelative().y && x == chip.getRelative().x) {
                        if (candidate.y > chip.getRelative().y) {
                            candidate.y = chip.getRelative().y - 1;
                        }
                    }
                        if (chip.getRelative().y != 9 && c.getRelative().y != 9 && candidate.y != c.getRelative().y) {
                            if (board[x][i].isLegalMove(c)) {
                                isLegalFinal = true;
                            }
                        }
                }
                if(isLegalFinal){
                    if(c.isPower){
                        for(int j =y; j<candidate.y+1;j++){
                            legalMoves.add(board[candidate.x][j]);
                        }
                    }else{
//                        System.out.println(candidate.x + " " + candidate.y);
                        legalMoves.add(board[candidate.x][candidate.y]);
                    }
                }
            }
        for(Chip c1 : chips){
            int xx = c1.getRelative().x;
            int yy = c1.getRelative().y;
            legalMoves.remove(board[xx][yy]);
        }

        return 0;
    }

    // check which location the chip can move
    private void calculateLegalMoves(Chip chip) {

        // init the legalMoves
        legalMoves.clear();

        // check if there are other chips
        for(Chip c : chips){
            int x = c.getRelative().x;
            int y = c.getRelative().y;
            board[x][y].isOnChip = true;
            if((c.getRelative().x >5 && c.getRelative().y <3)||(c.getRelative().x <3 && c.getRelative().y >6)){
                System.out.println("This is on Coner "+c.getRelative());
                c.beOnCorner();
            }
        }

        // check if the chip is selected
        int x = chip.cell.x;
        int y = chip.cell.y;
        legalRight(chip);
        legalLeft(chip);
        legalUp(chip);
        legalDown(chip);
        for(Chip c : chips){
            int xx = c.getRelative().x;
            int yy = c.getRelative().y;
            legalMoves.remove(board[xx][yy]);
        }

        // Additional logic if chip is a power chip:
        if (chip.isPower) {
            // Check diagonal moves
            // for top right
            int i = x + 1;
            int j = y - 1;
            while (i < 9 && j >= 0) {

                if (board[i][j].isLegalMove(chip) && !board[i][j].isOnChip) {
                    legalMoves.add(board[i][j]);
                    i++;
                    j--;
                } else {
                    break;
                }
            }

            // for bottom right
            i = x + 1;
            j = y + 1;
            while (i < 9 && j < 10) {
                if (board[i][j].isLegalMove(chip)&& !board[i][j].isOnChip) {
                    legalMoves.add(board[i][j]);
                    i++;
                    j++;
                } else {
                    break;
                }
            }

            // for bottom left
            i = x - 1;
            j = y + 1;
            while (i >=0 && j < 10) {
                if (board[i][j].isLegalMove(chip)&& !board[i][j].isOnChip) {
                    legalMoves.add(board[i][j]);
                    i--;
                    j++;
                } else {
                    break;
                }
            }

            // for top left
            i = x - 1;
            j = y - 1;
            while (i >=0 && j >= 0) {
                if (board[i][j].isLegalMove(chip)&& !board[i][j].isOnChip) {
                    legalMoves.add(board[i][j]);
                    i--;
                    j--;
                } else {
                    break;
                }

            }

            // Remove any position if there are other chips
            for(Chip c: chips){
                int xx = c.getRelative().x;
                int yy = c.getRelative().y;
                legalMoves.remove(board[xx][yy]);

            }
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            for (Chip chip : chips) {
                // check the position people toched is the same with any chips
                if (chip.getAbsolute().contains(event.getX(), event.getY())) {
                    legalDown(chip);
                    chip.setSelected(!chip.isSelected()); // Toggle selection

                    calculateLegalMoves(chip);

                    // Unselect other chips
                    for (Chip otherChip : chips) {
                        if (otherChip != chip) {
                            otherChip.setSelected(false);
                        }
                    }
                    invalidate(); // Request a redraw to visually show the selection change
                    return true;
                } else{
                    chip.setSelected(false);
                    legalMoves.clear();
                    invalidate();
                }
            }
        }
        /*
        true: This indicates that the event was handled (or consumed) by the view.
        This means that the event won't be passed on to
        any other views beneath this one in the view hierarchy.
         */

        /*
        Chain of Responsibility:
        This pattern decouples the sender of a request from its receivers by allowing more
        than one object to handle the request. Essentially, it creates a chain of objects
        that attempt to process the request. Each object in the chain either handles the request
        or passes it to the next object in the chain.
         */
        return true;
    }


    @Override
    public void onDraw(Canvas c) {
        // ... (Draw cells and chips as before)
        // Draw Cells and Chips
        setArray();

        // If the cells are corner side, it will be dark or light color otherwise neutral color
        for(int x=0; x<9; x++){
            for(int y=0; y<10; y++){
                Cell cell = board[x][y];
                switch (cell.colorNum){
                    case Team.neutral:
                        c.drawRect(cell.absolute,neutral);
                        break;
                    case Team.light:
                        c.drawRect(cell.absolute,lightB);
                        break;
                    case Team.dark:
                        c.drawRect(cell.absolute,darkB);
                        break;
                }
                c.drawRect(cell.absolute,boarder);
            }
        }
        for (Chip chip: chips){
            chip.draw(c);
        }

        // Draw markers for legal moves
        Paint markerPaint = new Paint();
        markerPaint.setColor(Color.RED);
        float markerRadius = 20;  // Adjust size as needed
        for (Cell cell : legalMoves) {
            float cx = cell.absolute.centerX();
            float cy = cell.absolute.centerY();
            Paint paint = new Paint();
//            c.drawRect(cell.absolute,markerPaint);
            c.drawCircle(cx, cy, markerRadius, markerPaint);
        }
    }


}
