package com.monyetmabuk.rajawali.tutorials;

import ie.cathalcoffey.android.rajawali.tutorials.FragmentsExampleActivity;

import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.util.Linkify;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.monyetmabuk.rajawali.tutorials.ui.ExamplesAdapter;

/**
 * 
 * @author Dennis Ippel, modified by Jack Fortenbery and Samuel Chalvet.
 * 
This is the main screen activity which contains one button to load the model Activity
 *
 */
public class RajawaliExamplesActivity extends Activity {
	Button btnFindBowl;

	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		//setListAdapter(new ExamplesAdapter(this, list));

		//sends user to the bowl of truth
		btnFindBowl = (Button) findViewById(R.id.button1);
		
		btnFindBowl.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(getApplicationContext(), RajawaliLoadModelActivity.class);
				startActivity(intent);
			}
		});
		
	}
}