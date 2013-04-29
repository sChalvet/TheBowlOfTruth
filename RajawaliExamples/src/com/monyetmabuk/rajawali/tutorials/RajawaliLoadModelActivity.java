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

public class RajawaliLoadModelActivity extends RajawaliExampleActivity implements OnTouchListener, OnClickListener{
	private RajawaliLoadModelRenderer mRenderer;
	private Point mScreenSize;
	private Random generator = new Random();
	MediaPlayer mp;
	private LinearLayout mLinearLayout;
	Button btnChange;
	Button btnWave;
	boolean waves;
	
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
        
        mp= MediaPlayer.create(this, R.raw.gong);
        mSurfaceView.setOnTouchListener(this);
        
		Display display = getWindowManager().getDefaultDisplay();
		mScreenSize = new Point();
		mScreenSize.x = display.getWidth();
		mScreenSize.y = display.getHeight();
		
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
        
        //if this is true then when screen is pocked waves will appear.
        waves = true;
        mLayout.addView(mLinearLayout);
		
        Toast.makeText(getApplicationContext(), "Please wait for object to load before clicking buttons.", Toast.LENGTH_LONG).show();
        initLoader();
    }
    
    public boolean onTouch(View v, MotionEvent event) {
		
    	int randomIndex = generator.nextInt( fortuneCookie.length );
    	
		if(event.getAction() == MotionEvent.ACTION_DOWN)
		{
			Log.d("ShaderActivity", "mode=DRAG" );
			mp.start();
			Toast.makeText(getApplicationContext(), fortuneCookie[randomIndex], Toast.LENGTH_LONG).show();
			
			if(waves)
				mRenderer.setTouch(event.getX() / mScreenSize.x, 1.0f - (event.getY() / mScreenSize.y));  //Waves!
		}
		return super.onTouchEvent(event);
	}
    

	@Override public boolean onTouchEvent(MotionEvent e) {
		float x = e.getX();
		float y = e.getY();
	
		
		switch (e.getAction()) {

		case MotionEvent.ACTION_MOVE:						// rotation
			Log.d("ShaderActivity", "Action move2??" );
			Log.d("ShaderActivity", "xP= "+mPreviousX+" xNow= "+ "+mPreviousX+"+" yP= "+mPreviousY+" Ynow=  "+y);
			mRenderer.manualRotation(mPreviousX, x, mPreviousY, y);
			break;
		}
		mPreviousX = x;
		mPreviousY = y;
		return true;
	}
    
    public void onClick(View v) {
    	//Toast.makeText(getApplicationContext(), "button", Toast.LENGTH_LONG).show();
    	if(v.getId() == btnChange.getId()){
	    	if(this.btnChange.getText().toString().equalsIgnoreCase("Manualy")){
	    		this.btnChange.setText("Automatic");
	    		mRenderer.stopRotation();
	    	}else{
	    		this.btnChange.setText("Manualy");
	    		mRenderer.startRotation();		
	    	}
    	}else{
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
	


	// touch events
	private final int NONE = 0;
	private final int DRAG = 0;

	// pinch to zoom
	float oldDist = 100.0f;
	float newDist;

	int mode = 0;
    
    
    
}
