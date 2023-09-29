package edu.byuh.cis.cs203.tapthechips2;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    BoardView boardView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // make an instance from the view class.
        boardView = new BoardView(this);
        // set view class and show into activity.
        setContentView(boardView);
    }
}