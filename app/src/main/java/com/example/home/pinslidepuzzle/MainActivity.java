package com.example.home.pinslidepuzzle;


import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private static GestureDetectGridView mGridView;

    private static final int COLUMNS = 5;
    private static final int DIMENSIONS = COLUMNS * COLUMNS;

    private static int mColumnWidth, mColumnHeight;

    public static final String up = "up";
    public static final String down = "down";
    public static final String left = "left";
    public static final String right = "right";



    private static String[][] tileList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        setDimensions();
    }

    private void init() {
        mGridView = (GestureDetectGridView) findViewById(R.id.grid);
        mGridView.setNumColumns(COLUMNS);
        Random random = new Random();

        tileList = new String[DIMENSIONS][2];
        for (int i = 0; i < DIMENSIONS; i++) {
            tileList[i][0] = String.valueOf(i);
            tileList[i][1] = String.valueOf(0);
        }
        tileList[7][1] = String.valueOf(random.nextInt(4)+1);
        tileList[11][1] = String.valueOf(random.nextInt(4)+1);
        tileList[13][1] = String.valueOf(random.nextInt(4)+1);
        tileList[17][1] = String.valueOf(random.nextInt(4)+1);
    }


    private void setDimensions() {
        ViewTreeObserver vto = mGridView.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mGridView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                int displayWidth = mGridView.getMeasuredWidth();
                int displayHeight = mGridView.getMeasuredHeight();

                int statusbarHeight = getStatusBarHeight(getApplicationContext());
                int requiredHeight = displayHeight - statusbarHeight;

                mColumnWidth = displayWidth / COLUMNS;
                mColumnHeight = requiredHeight / COLUMNS;

                display(getApplicationContext());
            }
        });
    }

    private int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen",
                "android");

        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }

        return result;
    }

    private static void display(Context context) {
        ArrayList<Button> buttons = new ArrayList<>();
        Button button;

        for (int i = 0; i < tileList.length; i++) {
            button = new Button(context);

            for(int j = 0; j<DIMENSIONS; j++){
                if(tileList[i][0].equals(Integer.toString(j))){
                    if(tileList[i][1] == String.valueOf(1)){
                        button.setBackgroundResource(R.drawable.redtile);
                    }else if(tileList[i][1] == String.valueOf(2)){
                        button.setBackgroundResource(R.drawable.bluetile);
                    }else if(tileList[i][1] ==String.valueOf(3)){
                        button.setBackgroundResource(R.drawable.greentile);
                    }else if(tileList[i][1] ==String.valueOf(4)){
                        button.setBackgroundResource(R.drawable.yellowtile);
                    }else{
                        button.setBackgroundResource(R.drawable.blanktile);
                    }
                }
            }

            buttons.add(button);
        }

        mGridView.setAdapter(new CustomAdapter(buttons, mColumnWidth, mColumnHeight));
    }

    private static void swap(int currentPosition, int swap) {
        String newColor = tileList[currentPosition + swap][1];
        tileList[currentPosition + swap][1] = tileList[currentPosition][1];
        tileList[currentPosition][1] = newColor;

    }

    private static void checkeverymove(Context context){
        destroySameColor();

        addNewColor();

        display(context);

        if (isSolved()) Toast.makeText(context, "GAME OVER!", Toast.LENGTH_SHORT).show();
    }

    private static void moveAll(Context context, String Direction, int pin){
        switch(Direction){
            case up:
                for(int i = 1; i < DIMENSIONS; i++){
                    if(i != pin && i != pin+COLUMNS){
                        if(i > 5) {
                            if(tileList[i-COLUMNS][1]==String.valueOf(0)) {
                                swap(i, -COLUMNS);
                            }
                        }
                    }
                }
                destroySameColor();
                break;
            case down:
                for(int i = DIMENSIONS-1; i >= 0; i--){
                    if(i != pin && i != pin-COLUMNS){
                        if(i<20){
                            if(tileList[i+COLUMNS][1]==String.valueOf(0)) {
                                swap(i,COLUMNS);
                            }
                        }
                    }
                }
                destroySameColor();
                break;
            case left:
                for(int i = 1; i<DIMENSIONS; i++){
                    if(i != pin && i != pin+1){
                        if(i%COLUMNS != 0){
                            if(tileList[i-1][1]==String.valueOf(0)) {

                                swap(i,-1);
                            }
                        }
                    }
                }
                destroySameColor();
                break;
            case right:
                for(int i = DIMENSIONS-1; i >= 0; i--){
                    if(i != pin && i != pin-1) {
                        if(i%COLUMNS != 4){
                            if (tileList[i + 1][1] == String.valueOf(0)) {

                                swap(i, +1);
                            }
                        }
                    }
                }
                destroySameColor();
                break;
            default:
                Toast.makeText(context,"Invalid Direction",Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private static void addNewColor() {
        int nextPlace;
        int nextColor;
        Random random = new Random();
        nextPlace = random.nextInt(DIMENSIONS);
        nextColor = random.nextInt(4)+1;

        if(!isSolved()) {
            if (tileList[nextPlace][1] != String.valueOf(0)) {
                addNewColor();
            } else {
                tileList[nextPlace][1] = String.valueOf(nextColor);
            }
        }
    }

    private static void destroySameColor() {
        for(int i =0; i < DIMENSIONS; i++){
            if(i%COLUMNS != COLUMNS-1 && i%COLUMNS != COLUMNS-2){
                if(tileList[i][1] == tileList[i+1][1] && tileList[i+1][1] == tileList[i+2][1]){
                    tileList[i][1] = String.valueOf(0);
                    tileList[i+1][1] = String.valueOf(0);
                    tileList[i+2][1] = String.valueOf(0);
                }
            }else if (i<15){
                if(tileList[i][1] == tileList[i+COLUMNS][1] && tileList[i+COLUMNS][1] == tileList[i+2*COLUMNS][1]) {
                    tileList[i][1] = String.valueOf(0);
                    tileList[i + COLUMNS][1] = String.valueOf(0);
                    tileList[i + 2 * COLUMNS][1] = String.valueOf(0);
                }
            }
        }
    }

    public static void moveTiles(Context context, String direction, int position) {

        if(direction.equals(up)){
            moveAll(context,up,position);

        }else if(direction.equals(down)){
            moveAll(context,down,position);

        }else if(direction.equals(left)){
            moveAll(context,left,position);

        }else{
            moveAll(context,right,position);
        }
        destroySameColor();
        checkeverymove(context);
    }

    private static boolean isSolved() {
        boolean solved = false;
        int check = 0;

        for (int i = 0; i < tileList.length; i++) {
            if(tileList[i][1] !=Integer.toString(0)){
                check ++;
            };
        }
        if(check >= 25){
            solved = true;
        }else{
            check = 0;
        }

        return solved;
    }

}