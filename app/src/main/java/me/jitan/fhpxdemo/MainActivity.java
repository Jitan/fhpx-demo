package me.jitan.fhpxdemo;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity
{
    private ImageAdapter mImageAdapter;
    private MenuItem mSearchAction;
    private Boolean mSearchOpened;
    private String mSearchQuery;
    private EditText mSearchField;
    private Toolbar mToolbar;
    private ActionBar mActionBar;
    private GridView mGridView;
    private Drawable mIconCloseSearch, mIconOpenSearch;
    private SearchWatcher mSearchWatcher;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Toolbar
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null)
        {
            setSupportActionBar(mToolbar);
        }
        mActionBar = getSupportActionBar();
        mSearchOpened = false;
        mSearchQuery = "";
        mIconCloseSearch = getResources().getDrawable(R.drawable.ic_close);
        mIconOpenSearch = getResources().getDrawable(R.drawable.ic_search);
        mSearchWatcher = new SearchWatcher();


        // Gridview
        mGridView = (GridView) findViewById(R.id.gridview);
        mImageAdapter = new ImageAdapter(this);
        mGridView.setAdapter(mImageAdapter);

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> parent, View v,
                    int position, long id)
            {
                Toast.makeText(MainActivity.this, "" + position,
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu)
    {
        mSearchAction = menu.findItem(R.id.action_search);
        return super.onPrepareOptionsMenu(menu);
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
        switch (item.getItemId())
        {
            case R.id.action_settings:
                return true;

            case R.id.action_search:
                if (mSearchOpened)
                {
                    closeSearchBar();
                }
                else
                {
                    openSearchBar(mSearchQuery);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void openSearchBar(String queryText)
    {

        // Set custom view on action bar.
        if (mActionBar != null)
        {
            // Search edit text field setup.
            mSearchField = (EditText) findViewById(R.id.etSearch);
            mSearchField.setVisibility(View.VISIBLE);
            mSearchField.addTextChangedListener(mSearchWatcher);
            mSearchField.setText(queryText);
            mSearchField.requestFocus();

            // Change search icon accordingly.
            mSearchAction.setIcon(mIconCloseSearch);
            mSearchOpened = true;
        }
    }

    private void closeSearchBar()
    {
        // Remove custom view.
        if (mActionBar != null)
        {
            mSearchField.setVisibility(View.GONE);
        }

        // Change search icon accordingly.
        mSearchAction.setIcon(mIconOpenSearch);
        mSearchOpened = false;

    }

    private class SearchWatcher implements TextWatcher
    {
        @Override
        public void beforeTextChanged(CharSequence c, int i, int i2, int i3)
        {

        }

        @Override
        public void onTextChanged(CharSequence c, int i, int i2, int i3)
        {

        }

        @Override
        public void afterTextChanged(Editable editable)
        {
            mSearchQuery = mSearchField.getText().toString();
            Toast.makeText(getApplicationContext(), mSearchQuery, Toast.LENGTH_SHORT).show();
        }

    }
}
