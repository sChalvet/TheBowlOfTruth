package com.monyetmabuk.rajawali.tutorials;

import java.util.Random;

import android.graphics.Point;
import android.media.MediaPlayer;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * 
 * @author Dennis Ippel, modified by Jack Fortenbery and Samuel Chalvet.
 * 
 *  This Activity contains a button to start/stop the spinning of the bowl.
 *  There is also a button to start/stop the wave effect on touch.
 *  It calls RajawaliLoadModelRenderer to lead the bowl and background.
 *
 */
public class RajawaliLoadModelActivity extends RajawaliExampleActivity implements OnTouchListener, OnClickListener{
	private RajawaliLoadModelRenderer mRenderer;
	private Point mScreenSize;
	private Random generator = new Random();
	MediaPlayer mp;
	private LinearLayout mLinearLayout;
	Button btnChange;
	Button btnWave;
	boolean waves;
	
	//contains all of the proverbs
	private String[] fortuneCookie = {	"Confucius says: Go to bed with itchy bum,\nwake up with stinky finger!",
										"A new pair of shoes will do you a world of good!",
										"The end is near...\nAnd it's your fault...",
										"Ignore all previous proverbs.",
										"You are not illiterate.",
										"Don't panic.",
										"Don't poke the bowl again or disaster will befall you!",
										"He who laughs last is slowest to get the joke.",
										"Man who lives in glass house should change clothes in basement.",
										"Man who run in front of car get tired.\nMan who run behind car get exhausted.",
										"Man who put head on railroad track get splitting headache.",
										"The answer is 42!",
										"You are wasting your time poking me...",
										"The last guy got all the good proverbs... Sorry.",
										"Never stop wondering.\nNever stop wandering.",
										"Don't look behind you...",
										"Man who poke bowl waste time...",
										"In order to have magical bodies\nWe must have magical minds.",
										"After Tuesday,\neven the calender goes W T F",
										"Sometimes you sits and thinks, mostly you just sits.",
										"Did you know the word bed is actually shaped like a bed...",
										"You will live a long life, as long as you keep poking this bowl...",
										"Would'nt it be ironic to die in the living room?",
										"Borrow money from pessimists, they don't expect it back.",
										"Boats and water are in your future.",
										"Your problems just got bigger, think! What did you just do?",
										"Are your legs tired? You've been running through someones mind all day.",
										"Change is inevitable except for vending machines.",
										"I cannot help you for I am just a bowl.",
										"All grades are negotiable. Right?",
										"When you sqeeze an orange, orange juice comes out because that's what's inside."};
	
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRenderer = new RajawaliLoadModelRenderer(this);
        mRenderer.setSurfaceView(mSurfaceView);
        super.setRenderer(mRenderer);
        
        //gong sound is instantiated here
        mp= MediaPlayer.create(this, R.raw.gong);
        //creates a touch listener
        mSurfaceView.setOnTouchListener(this);
        
		Display display = getWindowManager().getDefaultDisplay();
		mScreenSize = new Point();
		mScreenSize.x = display.getWidth();
		mScreenSize.y = display.getHeight();
		
		//instantiates a linear layout, used to add the buttons
		mLinearLayout = new LinearLayout(this);
        mLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
        mLinearLayout.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
        mLinearLayout.setVisibility(LinearLayout.VISIBLE);
        
        //Toggle button to turn auto rotation on or off
        btnChange = new Button(this);
        btnChange.setId(0);
        btnChange.setOnClickListener(this);
        btnChange.setText("Manualy");
        btnChange.setTextSize(10);
        mLinearLayout.addView(btnChange);
        
        //toggle button to turn waves on or off
        btnWave = new Button(this);
        btnWave.setId(1);
        btnWave.setOnClickListener(this);
        btnWave.setText("Waves On");
        btnWave.setTextSize(10);
        mLinearLayout.addView(btnWave);
        
        //if this is true then when screen is poked waves will appear.
        waves = true;
        
        //adds the linear layout to the screen layout
        mLayout.addView(mLinearLayout);
		
        //warning text, program crashes if users modifies the object before is loads
        Toast.makeText(getApplicationContext(), "Please wait for object to load before clicking buttons.", Toast.LENGTH_LONG).show();
        
        //starts the loading process in RajawaliLoadModelRenderer
        initLoader();
    }
    
    //onTouch event that detects the screen being poked
    public boolean onTouch(View v, MotionEvent event) {
		
    	//generates a random number, used for the fortuneCookie array[]
    	int randomIndex = generator.nextInt( fortuneCookie.length );
    	
		if(event.getAction() == MotionEvent.ACTION_DOWN)
		{
			//plays the  gong sound
			mp.start();
			//displays the bowl of truth proverb
			Toast.makeText(getApplicationContext(), fortuneCookie[randomIndex], Toast.LENGTH_LONG).show();
			
			//if the wave option is true then a wave is created centered on the point where the screen was touched
			if(waves)
				mRenderer.setTouch(event.getX() / mScreenSize.x, 1.0f - (event.getY() / mScreenSize.y));  //Waves!
		}
		return super.onTouchEvent(event);
	}
    
    //onTouchEvent used to detect a swiping action, used to rotate the bowl
	@Override public boolean onTouchEvent(MotionEvent e) {
		float x = e.getX();
		float y = e.getY();
	
		
		switch (e.getAction()) {

		case MotionEvent.ACTION_MOVE:						// rotation
			
			//sends the before and after x and y coord to RajawaliLoadModelRenderer to rotate the bowl
			mRenderer.manualRotation(mPreviousX, x, mPreviousY, y);
			break;
		}
		mPreviousX = x;
		mPreviousY = y;
		return true;
	}
    
	//on click event for the two buttons
    public void onClick(View v) {
    	
    	//if btnChange was  touched then toggle it, and execute the corresponding code
    	if(v.getId() == btnChange.getId()){
	    	if(this.btnChange.getText().toString().equalsIgnoreCase("Manually")){
	    		this.btnChange.setText("Automatic");
	    		mRenderer.stopRotation();
	    	}else{
	    		this.btnChange.setText("Manually");
	    		mRenderer.startRotation();		
	    	}
    	}else{ //else btnWave was  touched, toggle it, and execute the corresponding code
	    	if(this.btnWave.getText().toString().equalsIgnoreCase("Waves On")){
	    		this.btnWave.setText("Waves Off");
	    		waves = false;
	    	}else{
	    		this.btnWave.setText("Waves On");
	    		waves = true;		
	    	}	
    	}
	}
    
    /********************************
	 * PROPERTIES
	 *********************************/

	// rotation
	private float mPreviousX;
	private float mPreviousY;


	// pinch to zoom
	float oldDist = 100.0f;
	float newDist;

	int mode = 0;
    
    
    
}
