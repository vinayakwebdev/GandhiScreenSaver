package com.awesome.shukla.gandhiscreensaver;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.graphics.Color;
import android.graphics.Point;
import android.service.dreams.DreamService;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;

import java.util.Random;


public class GandhiDayDream extends DreamService implements OnClickListener {

    //UI and animation variables
    private Button dismissBtn;
    private ImageView[] robotImgs;
    private AnimatorSet[] robotSets;

    //number or rows, total robots
    private final int ROWS_COLS=5;
    private final int NUM_ROBOTS=ROWS_COLS*ROWS_COLS;

    //dismiss button at random position
    private int randPosn;

    @Override
    public void onDreamingStarted() {
        //daydream started
        super.onDreamingStarted();

        //begin animations
        for(int r=0; r<NUM_ROBOTS; r++){
            if(r!=randPosn)
                robotSets[r].start();
        }
    }

    @Override
    public void onDreamingStopped(){
        //daydream stopped

        //stop animating
        for(int r=0; r<NUM_ROBOTS; r++){
            if(r!=randPosn)
                robotSets[r].cancel();
        }
        super.onDreamingStopped();
    }

    @Override
    public void onAttachedToWindow() {
        //setup daydream
        super.onAttachedToWindow();

        //setup daydream

        //interactive and fullscreen
        setInteractive(true);
        setFullscreen(true);//hides status bar

        //get random position for button within robot range
        Random rand = new Random();
        randPosn = rand.nextInt(NUM_ROBOTS);

        //grid layout
        GridLayout ddLayout = new GridLayout(this);
        ddLayout.setColumnCount(ROWS_COLS);
        ddLayout.setRowCount(ROWS_COLS);

        //initialise variables
        robotSets = new AnimatorSet[NUM_ROBOTS];
        robotImgs = new ImageView[NUM_ROBOTS];

        //get width and height for each item in the grid
        Point screenSize = new Point();
        getWindowManager().getDefaultDisplay().getSize(screenSize);
        int robotWidth = screenSize.x/ROWS_COLS;
        int robotHeight = screenSize.y/ROWS_COLS;

        //loop through robots, adding each to grid
        for(int r=0; r<NUM_ROBOTS; r++){
            //add to grid

            //layout params
            GridLayout.LayoutParams ddP = new GridLayout.LayoutParams();
            ddP.width=robotWidth;
            ddP.height=robotHeight;

            //check for random position - add the dismiss button here
            if(r==randPosn){
                //setup and listen for clicks
                //stop button
                dismissBtn = new Button(this);
                dismissBtn.setText("stop");
                dismissBtn.setBackgroundColor(Color.WHITE);
                dismissBtn.setTextColor(Color.RED);
                dismissBtn.setOnClickListener(this);
                dismissBtn.setLayoutParams(ddP);
                ddLayout.addView(dismissBtn);
            }
            else{
                //robot image view

                //add a robot to array
                robotImgs[r] = new ImageView(this);
                //use launcher image
                robotImgs[r].setImageResource(R.drawable.ic_launcher);
                //add to layout
                ddLayout.addView(robotImgs[r], ddP);

                //add to animator set array
                robotSets[r] = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.android_spin);
                //set to animate
                robotSets[r].setTarget(robotImgs[r]);
                //listen for clicks
                robotImgs[r].setOnClickListener(this);
            }
        }
        //set view
        setContentView(ddLayout);
    }

    @Override
    public void onDetachedFromWindow() {
        //tidy up

        //stop listening for clicks
        for(int r=0; r<NUM_ROBOTS; r++){
            if(r!=randPosn)
                robotImgs[r].setOnClickListener(null);
        }
        super.onDetachedFromWindow();
    }

    public void onClick(View v){
        //handle clicks on robots and dismiss button

        //dismiss button - finish
        if(v instanceof Button && (Button)v==dismissBtn){
            //stop button
            this.finish();
        }
        else {
            //robot - stop or start animating
            for(int r=0; r<NUM_ROBOTS; r++){
                //check array
                if(r!=randPosn){
                    //check image view
                    if((ImageView)v==robotImgs[r]){
                        //is the current view
                        if(robotSets[r].isStarted()) robotSets[r].cancel();
                        else robotSets[r].start();
                        break;
                    }
                }
            }
        }
    }

}
