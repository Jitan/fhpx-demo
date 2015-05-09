package me.jitan.fhpxdemo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.koushikdutta.ion.Ion;

public class ImageAdapter extends ArrayAdapter<String>
{
    private LayoutInflater mInflater;

    public ImageAdapter(Context context)
    {
        super(context, 0);
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        if(convertView == null)
        {
            convertView = mInflater.inflate(R.layout.square_gridview_image, parent, false);
        }

        final ImageView imageView = (ImageView) convertView.findViewById(R.id.image);

        Ion.with(imageView).load(getItem(position));

        return convertView;
    }

}