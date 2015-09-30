package com.example.facebookexample;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

public class DetailAdapter extends ArrayAdapter<String> {
	private Context Context = null;
	private ImageLoader imageLoader;
	private DisplayImageOptions options;
	private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
	private View v;
	List<String> items;
	public DetailAdapter(Context c, int textViewResourceId,
			List<String> items) {
		super(c, textViewResourceId, items);
		Context = c;
		
		this.items = items;
		imageLoader = ImageLoader.getInstance();
		imageLoader.init(ImageLoaderConfiguration.createDefault(Context));
		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.ic_launcher)
				.showImageForEmptyUri(R.drawable.ic_launcher)
				.showImageOnFail(R.drawable.ic_launcher).cacheInMemory(true)
				.cacheOnDisk(true).considerExifParams(true).build();
		

	}

	@Override
	public  String getItem(int position) {
		return items.get(position);
	}

	@Override
	public int getCount() {
		return items.size();
	}

	@Override
	public long getItemId(int position) {

		return super.getItemId(position);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final RecordHolder holder;
		v = convertView;
		if (v == null) {
			LayoutInflater mInflater = ((Activity) Context).getLayoutInflater();
			v = mInflater.inflate(R.layout.grid_items, parent, false);
			holder = new RecordHolder();
			holder.imagev = (ImageView) v.findViewById(R.id.imag_v1);
			
			v.setTag(holder);

		} else {
			holder = (RecordHolder) v.getTag();

		}
		holder.imagev.setId(position);
	Log.e("LINK ADAPER=", items.get(position));
		imageLoader.displayImage(items.get(position),
				holder.imagev, options, animateFirstListener);
		return v;

	}



	static class RecordHolder {

		ImageView imagev;
	}

	private static class AnimateFirstDisplayListener extends
			SimpleImageLoadingListener {
		static final List<String> displayedImages = Collections
				.synchronizedList(new LinkedList<String>());

		@Override
		public void onLoadingComplete(String imageUri, View view,
				Bitmap loadedImage) {
			if (loadedImage != null) {
				ImageView imageView = (ImageView) view;
				boolean firstDisplay = !displayedImages.contains(imageUri);
				if (firstDisplay) {
					FadeInBitmapDisplayer.animate(imageView, 400);
					displayedImages.add(imageUri);
				}
			}
		}
	}

}
