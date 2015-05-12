package me.jitan.fhpxdemo.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import de.greenrobot.event.EventBus;
import me.jitan.fhpxdemo.ImageAdapter;
import me.jitan.fhpxdemo.R;
import me.jitan.fhpxdemo.event.AddPhotoSetToGridEvent;
import me.jitan.fhpxdemo.event.AddPhotoToGridEvent;
import me.jitan.fhpxdemo.event.LoadPhotoDetailEvent;
import me.jitan.fhpxdemo.event.SearchEvent;
import me.jitan.fhpxdemo.model.FhpxPhoto;

public class GridFragment extends Fragment
{
    private ImageAdapter mImageAdapter;
    private GridView mGridView;
    private static final EventBus eventBus = EventBus.getDefault();


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
        mImageAdapter = new ImageAdapter(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_grid, container, false);
        mGridView = (GridView) view.findViewById(R.id.gridview);
        mGridView.setAdapter(mImageAdapter);

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> parent, View v,
                    int position, long id)
            {
                FhpxPhoto photoForDetailView = mImageAdapter.getItem(position);
                eventBus.post(new LoadPhotoDetailEvent(photoForDetailView));
            }
        });

        return view;
    }

    public void onEventMainThread(AddPhotoToGridEvent event)
    {
        mImageAdapter.add(event.getFhpxPhoto());
    }

    public void onEventMainThread(SearchEvent event)
    {
        mImageAdapter.clear();
    }

    public void onEventMainThread(AddPhotoSetToGridEvent event)
    {
        mImageAdapter.addAll(event.getFhpxPhotoList());
    }

    @Override
    public void onStart()
    {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop()
    {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }


}
