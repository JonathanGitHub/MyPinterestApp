package com.example.jianyang.mypinterestapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;

import adapters.PinterestAdapter;
import models.PinterestModel;


public class MainActivity extends ActionBarActivity
{
	// Save URL header and footer in static variables for easy maintenance
	public static String BASE_URL_HEADER  = "http://widgets.pinterest.com/v3/pidgets/users/";
	public static String BASE_URL_FOOTER  = "/pins/";
	// Save those string values as static variables so that it is less likely that we will make typo mistakes when parse json
	public static String JSON_URL         = "url";
	public static String JSON_IMAGES      = "images";
	public static String JSON_DESCRIPTION = "description";
	public static String JSON_237X        = "237x";
	public static String JSON_DATA        = "data";
	public static String JSON_PINS        = "pins";

	private GridView    mGridView;
	private EditText    mUserInputEditText;
	private Button      mSearchButton;
	private ProgressBar mProgressBar;
	private ArrayList<PinterestModel> mPinterestModelList = new ArrayList<>();

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mGridView = (GridView)findViewById(R.id.gridView);
		mUserInputEditText = (EditText)findViewById(R.id.inputSearch);
		mSearchButton = (Button)findViewById(R.id.confirmButton);
		mProgressBar = (ProgressBar)findViewById(R.id.progresBar);

		// If this comes from a screen rotation, restore the previous data
		if (savedInstanceState != null)
		{
			mPinterestModelList = savedInstanceState.getParcelableArrayList("dataKey");
			PinterestAdapter adapter = new PinterestAdapter(getApplicationContext(), mPinterestModelList);
			mGridView.setAdapter(adapter);
		}

		mSearchButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{

				// Hide keyboard when press mSearchButton

				InputMethodManager imm = (InputMethodManager)getSystemService(
						Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(mUserInputEditText.getWindowToken(), 0);

				// Show mProgressBar when fetching data
				mProgressBar.setVisibility(View.VISIBLE);

				getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

				String userName = mUserInputEditText.getText().toString();
				String url = BASE_URL_HEADER + userName + BASE_URL_FOOTER;

				// Clear previous data if there is any
				mPinterestModelList.clear();
				mGridView.setAdapter(null);

				// Make http call and process response
				Ion.with(getApplicationContext())
				   .load(url)
				   .asJsonObject()
				   .setCallback(new FutureCallback<JsonObject>()
				   {
					   @Override
					   public void onCompleted(Exception e, JsonObject result)
					   {
						   // do stuff with the result or error
						   if (result != null)
						   {
							   // Remove mProgressBar after network callback
							   mProgressBar.setVisibility(View.INVISIBLE);
							   try
							   {
								   // find the results and populate
								   if (result.getAsJsonObject(JSON_DATA) != null)
								   {
									   JsonArray results = result.getAsJsonObject(JSON_DATA).getAsJsonArray(JSON_PINS);

									   // find the results and populate
									   for (int i = 0; i < results.size(); i++)
									   {
										   PinterestModel pinterestModel = new PinterestModel();

										   String description = results.get(i).getAsJsonObject().get(JSON_DESCRIPTION).getAsString();
										   pinterestModel.setDescription(description);

										   JsonObject images = results.get(i).getAsJsonObject().get(JSON_IMAGES).getAsJsonObject();
										   JsonObject image = images.getAsJsonObject().get(JSON_237X).getAsJsonObject();

										   if (image.get(JSON_URL) != null)
										   {
											   pinterestModel.setUrl(image.get(JSON_URL).getAsString());
										   }

										   mPinterestModelList.add(pinterestModel);
									   }
									   // If there is any pins
									   if (mPinterestModelList.size() > 0)
									   {
										   // Adding items to gridView
										   PinterestAdapter adapter = new PinterestAdapter(getApplicationContext(), mPinterestModelList);
										   mGridView.setAdapter(adapter);
									   }
									   // Display empty message toast
									   else
									   {
										   Toast.makeText(getApplicationContext(), getString(R.string.no_data_message), Toast.LENGTH_LONG).show();
									   }
								   }
								   else
								   {
									   Toast.makeText(getApplicationContext(), getString(R.string.no_data_message), Toast.LENGTH_LONG).show();
								   }
							   }
							   catch (Exception exception)
							   {
								   exception.printStackTrace();
								   Toast.makeText(getApplicationContext(), getString(R.string.server_error_message), Toast.LENGTH_LONG).show();
							   }
						   }
						   else
						   {
							   // Remove mProgressBar
							   mProgressBar.setVisibility(View.INVISIBLE);
							   Toast.makeText(getApplicationContext(), getString(R.string.no_data_message), Toast.LENGTH_LONG).show();
						   }
					   }
				   });
			}
		});
	}


	@Override
	protected void onSaveInstanceState(Bundle outState)
	{
		// Save the data when configuration changes
		outState.putParcelableArrayList("dataKey", mPinterestModelList);
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState)
	{
		super.onRestoreInstanceState(savedInstanceState);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_settings)
		{
			return true;
		}

		return super.onOptionsItemSelected(item);
	}
}
