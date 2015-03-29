package models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jianyang on 3/28/15.
 */
public class PinterestModel implements Parcelable
{
	private String url;
	private String description;

	public String getUrl()
	{
		return url;
	}

	public void setUrl(String url)
	{
		this.url = url;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	// Default constructor
	public PinterestModel()
	{

	}

	public PinterestModel(String url, String description)
	{
		this.url = url;
		this.description = description;
	}

	@Override
	public String toString()
	{
		return "PinterestModel{"  +
			   ", url='" + url + '\'' +
			   ", description='" + description + '\'' +
			   '}';
	}

	// Parcelling part, since our PinterestModel is a customized class so if we wanna retain this object when user
	// rotates screen, we must implements Parcelable interface
	public PinterestModel(Parcel in)
	{
		String[] data = new String[2];
		in.readStringArray(data);
		this.url = data[0];
		this.description = data[1];
	}

	@Override
	public int describeContents()
	{
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags)
	{
		dest.writeStringArray(new String[]{
				this.url, this.description
		});
	}

	public static final Parcelable.Creator CREATOR = new Parcelable.Creator()
	{

		@Override
		public PinterestModel createFromParcel(Parcel source)
		{
			return new PinterestModel(source);
		}

		@Override
		public PinterestModel[] newArray(int size)
		{
			return new PinterestModel[size];
		}
	};
}
