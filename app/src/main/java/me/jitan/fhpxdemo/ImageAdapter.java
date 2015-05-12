package me.jitan.fhpxdemo;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;

import me.jitan.fhpxdemo.model.FhpxPhoto;

public class ImageAdapter extends ArrayAdapter<FhpxPhoto>
{
    private LayoutInflater mInflater;
    private Fragment mFragment;
    private ImageView mImageView;

    public ImageAdapter(Fragment fragment)
    {
        super(fragment.getActivity(), 0);
        mInflater = LayoutInflater.from((fragment.getActivity()));
        mFragment = fragment;
//        new GlideBuilder(fragment.getActivity()).setBitmapPool(new LruBitmapPool());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        final FhpxPhoto fhpxPhoto = getItem(position);

        if (convertView == null)
        {
            convertView = mInflater.inflate(R.layout.grid_item, parent, false);
        }
        mImageView = (ImageView) convertView.findViewById(R.id.grid_item_image);

        Log.d("ImageAdapter", "---------");
        Log.d("ImageAdapter", "Loading thumb nr: " + position);
        if (fhpxPhoto.getThumb() == null)
        {
            Log.d("ImageAdapter", "FROM INTERNET");
            loadThumb(fhpxPhoto);
        }
        else
        {
            mImageView.setImageDrawable(fhpxPhoto.getThumb());
            Log.d("ImageAdapter", "FROM MEMORY");
        }
        Log.d("ImageAdapter", "---------");

        return convertView;
    }

    private void loadThumb(final FhpxPhoto fhpxPhoto)
    {
        Glide.with(mFragment)
                .load(fhpxPhoto.getThumbUrl())
                .crossFade()
                .into(new GlideDrawableImageViewTarget(mImageView)
                {
                    @Override
                    public void onResourceReady(GlideDrawable resource,
                            GlideAnimation<? super GlideDrawable> animation)
                    {
                        fhpxPhoto.setThumb(resource);
                        super.onResourceReady(resource, animation);
                    }
                });
    }

    private void preloadThumb(final FhpxPhoto fhpxPhoto)
    {
        Glide.with(mFragment)
                .load(fhpxPhoto.getThumbUrl())
                .asBitmap()
                .into(new SimpleTarget<Bitmap>(280, 280)
                {
                    @Override
                    public void onResourceReady(Bitmap resource,
                            GlideAnimation<? super Bitmap> glideAnimation)
                    {
                        fhpxPhoto.setThumb(new BitmapDrawable(mFragment.getResources(), resource));
                    }
                });
    }

    @Override
    public void add(FhpxPhoto fhpxPhoto)
    {
        super.add(fhpxPhoto);
//        preloadThumb(fhpxPhoto);
    }


}