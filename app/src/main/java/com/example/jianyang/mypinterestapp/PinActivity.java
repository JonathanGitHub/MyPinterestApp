package com.example.jianyang.mypinterestapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by jianyang on 3/28/15.
 */
public class PinActivity extends Activity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pin);

		ImageView imageView = (ImageView)findViewById(R.id.pin_image);
		TextView textView = (TextView)findViewById(R.id.pin_text);
		// Get intent from MainActivity
		Intent intent = getIntent();
		if (intent != null)
		{
			// Populate imageView and textView
			Picasso.with(this).load(intent.getExtras().get(MainActivity.JSON_URL).toString()).into(imageView);
			textView.setText(intent.getExtras().get(MainActivity.JSON_DESCRIPTION).toString());
		}
	}
}
