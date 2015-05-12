package me.jitan.fhpxdemo;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

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


        if (fhpxPhoto.getThumb() == null)
        {
            loadThumb(fhpxPhoto);
        }
        else
        {
            mImageView.setImageDrawable(fhpxPhoto.getThumb());
        }

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

}