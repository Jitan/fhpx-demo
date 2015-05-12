package me.jitan.fhpxdemo;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import me.jitan.fhpxdemo.model.FhpxPhoto;

public class ImageAdapter extends ArrayAdapter<FhpxPhoto>
{
    private LayoutInflater mInflater;
    private Fragment mFragment;

    public ImageAdapter(Fragment fragment)
    {
        super(fragment.getActivity(), 0);
        mInflater = LayoutInflater.from((fragment.getActivity()));
        mFragment = fragment;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        if (convertView == null)
        {
            convertView = mInflater.inflate(R.layout.grid_item, parent, false);
        }

        final ImageView imageView = (ImageView) convertView.findViewById(R.id.image);

        Glide.with(mFragment)
                .load(getItem(position).getThumbUrl())
                .crossFade()
                .into(imageView);

        return convertView;
    }

}