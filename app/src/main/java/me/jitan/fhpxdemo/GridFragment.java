package me.jitan.fhpxdemo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

public class GridFragment extends Fragment
{
    private ImageAdapter mImageAdapter;
    private GridView mGridView;


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        if (mImageAdapter == null)
        {
            mImageAdapter = new ImageAdapter(getActivity());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_grid_view, container, false);
        mGridView = (GridView) view.findViewById(R.id.gridview);
        mGridView.setAdapter(mImageAdapter);

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> parent, View v,
                    int position, long id)
            {
                Toast.makeText(getActivity(), "" + position, Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    public void loadSearchResults(String mSearchQuery)
    {
        IonClient.getInstance(getActivity()).loadSearchResults(mSearchQuery, mImageAdapter);
    }
}
