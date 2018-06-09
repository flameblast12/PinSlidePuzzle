package com.example.home.pinslidepuzzle;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.ViewTreeObserver;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private static final int COLUMNS = 3;
    private static final int DIMENTIONS = COLUMNS * COLUMNS;

    private String[] tileList;

    private GestureDetectGridView mGridView;

    private int mColumnWidth, mColumnHeight;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialize();

        scramble();
        
        setDimentions();
    }

    private void setDimentions() {
        ViewTreeObserver viewTreeObserver = mGridView.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mGridView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                int displayWidth = mGridView.getMeasuredWidth();
                int displayHeight = mGridView.getMeasuredHeight();

                int statusbarHeight = getStatusbarHeight(getApplicationContext());
                int requiredHeight = displayHeight -  statusbarHeight;

                 mColumnWidth = displayWidth / COLUMNS;
                 mColumnHeight = requiredHeight / COLUMNS;

                display();
            }
        });
    }

    private int getStatusbarHeight(Context context){
        int result = 0;
        int resourceID = context.getResources().getIdentifier("status_bar_height","dimen","android");

        if(resourceID>0){
            result = context.getResources().getDimensionPixelSize(resourceID);
        }

        return result;
    }

    private void display() {
        ArrayList<Button> buttons = new ArrayList<>();
        Button button;

        for(int i = 0; i < tileList.length; i++){
            button = new Button(this);


            if(tileList[i].equals("0")){
                button.setBackgroundResource(R.drawable.pigeon_piece1);
                button.setText("0");
            }else if(tileList[i].equals("1")){
                button.setBackgroundResource(R.drawable.pigeon_piece2);
                button.setText("1");
            }else if(tileList[i].equals("2")){
                button.setBackgroundResource(R.drawable.pigeon_piece3);
                button.setText("2");
            }else if(tileList[i].equals("3")){
                button.setBackgroundResource(R.drawable.pigeon_piece4);
                button.setText("3");
            }else if(tileList[i].equals("4")){
                button.setBackgroundResource(R.drawable.pigeon_piece5);
                button.setText("4");
            }else if(tileList[i].equals("5")){
                button.setBackgroundResource(R.drawable.pigeon_piece6);
                button.setText("5");
            }else if(tileList[i].equals("6")){
                button.setBackgroundResource(R.drawable.pigeon_piece7);
                button.setText("6");
            }else if(tileList[i].equals("7")){
                button.setBackgroundResource(R.drawable.pigeon_piece8);
                button.setText("7");
            }else if(tileList[i].equals("8")) {
                button.setBackgroundResource(R.drawable.pigeon_piece9);
                button.setText("8");
            }
            buttons.add(button);
        }
        mGridView.setAdapter(new CustomAdapter(buttons, mColumnWidth , mColumnWidth));
    }

    private void scramble(){
        int index;
        String temp;
        Random random = new Random();

        for (int i = tileList.length - 1; i > 0; i--){
            index = random.nextInt(i + 1);
            temp = tileList[index];
            tileList[index] = tileList[i];
            tileList[i] = temp;

        }
    }

    private void initialize(){
        mGridView = (GestureDetectGridView) findViewById(R.id.grid);
        mGridView.setNumColumns(COLUMNS);

        tileList =  new String[DIMENTIONS];
        for (int i = 0; i< DIMENTIONS; i++){
            tileList[i] = String.valueOf(i);
         }
    }



}
