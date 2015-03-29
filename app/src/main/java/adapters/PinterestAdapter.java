package adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.jianyang.mypinterestapp.PinActivity;
import com.example.jianyang.mypinterestapp.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import models.PinterestModel;

/**
 * Created by jianyang on 3/28/15.
 */
public class PinterestAdapter extends BaseAdapter
{
	private Context              context;
	private List<PinterestModel> mPinterestModelList;

	public PinterestAdapter(Context context, List<PinterestModel> pinterestModels)
	{
		this.context = context;
		mPinterestModelList = pinterestModels;
	}

	@Override
	public int getCount()
	{
		return mPinterestModelList.size();
	}

	@Override
	public Object getItem(int position)
	{
		return position;
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	//The classic viewHolder pattern which enables smooth scrolling for gridView
	static class ViewHolderItem
	{
		ImageView imageView;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent)
	{
		// Get an inflater reference
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		ViewHolderItem viewHolder;
		// Pass imageView reference to viewHolder so that we could reuse it
		if (convertView == null)
		{
			convertView = inflater.inflate(R.layout.grid_item, null);
			viewHolder = new ViewHolderItem();
			viewHolder.imageView = (ImageView) convertView.findViewById(R.id.thumbnail);
			convertView.setTag(viewHolder);
		}
		else
		{
			viewHolder = (ViewHolderItem) convertView.getTag();
		}
		// Populate imageView with image url
		Picasso.with(context).load(mPinterestModelList.get(position).getUrl()).into(viewHolder.imageView);
		// Set click listener event
		viewHolder.imageView.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent(context, PinActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.putExtra("url", mPinterestModelList.get(position).getUrl());
				intent.putExtra("description", mPinterestModelList.get(position).getDescription());
				context.startActivity(intent);
			}
		});

		return convertView;
	}
}
