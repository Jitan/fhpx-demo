package me.jitan.fhpxdemo;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.koushikdutta.ion.Ion;


public class MainActivity extends AppCompatActivity
{
    private MenuItem mSearchAction;
    private Boolean mSearchOpened;
    private String mSearchQuery;
    private EditText mSearchField;
    private ActionBar mActionBar;
    private Drawable mIconCloseSearch, mIconOpenSearch;
    private GridFragment mGridFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Ion.getDefault(this).configure().setLogging("fhpx-ion", Log.DEBUG);

        // Need to disable Spdy to access 500px API with Ion, otherwise we get weird errors.
        Ion.getDefault(this).getHttpClient().getSSLSocketMiddleware().setSpdyEnabled(false);

        setupToolbar();

        setupFragments(savedInstanceState);

    }

    private void setupFragments(Bundle savedInstanceState)
    {
        if (savedInstanceState != null)
        {
            return;
        }

        mGridFragment = new GridFragment();

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, mGridFragment)
                .commit();
    }


    private void setupToolbar()
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null)
        {
            setSupportActionBar(toolbar);
        }
        mActionBar = getSupportActionBar();
        mSearchOpened = false;
        mSearchQuery = "";

        // Non-deprecated form of getDrawable only available in API-21
        mIconCloseSearch = getResources().getDrawable(R.drawable.ic_close);
        mIconOpenSearch = getResources().getDrawable(R.drawable.ic_search);
        mSearchField = (EditText) findViewById(R.id.editTextSearch);
        mSearchField.addTextChangedListener(new SearchWatcher());
        setKeyboardSearchListener();
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
                    hideKeyboard();
                }
                else
                {
                    openSearchBar(mSearchQuery);
                    showKeyboard();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
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

    private void openSearchBar(String queryText)
    {
        // Set custom view on action bar.
        if (mActionBar != null)
        {
            // Search edit text field setup.
            mSearchField.setVisibility(View.VISIBLE);
            mSearchField.setText(queryText);
            mSearchField.requestFocus();

            // Change search icon accordingly.
            mSearchAction.setIcon(mIconCloseSearch);
            mSearchOpened = true;
        }
    }

    public void hideKeyboard()
    {
        View view = getCurrentFocus();
        if (view != null)
        {
            InputMethodManager manager = (InputMethodManager) getSystemService(
                    Context.INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void showKeyboard()
    {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(
                Context.INPUT_METHOD_SERVICE);
        // only will trigger it if no physical keyboard is open
        inputMethodManager.showSoftInput(mSearchField, InputMethodManager.SHOW_IMPLICIT);
    }

    private void setKeyboardSearchListener()
    {
        mSearchField.setOnEditorActionListener(new TextView.OnEditorActionListener()
        {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
            {
                if (actionId == EditorInfo.IME_ACTION_SEARCH)
                {
                    mSearchQuery = mSearchField.getText().toString();
                    Toast.makeText(getApplicationContext(), "Searching for: " + mSearchQuery,
                            Toast.LENGTH_SHORT).show();
                    hideKeyboard();

                    mGridFragment.loadSearchResults(mSearchQuery);
                    return true;
                }
                return false;
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
        }

    }
}
