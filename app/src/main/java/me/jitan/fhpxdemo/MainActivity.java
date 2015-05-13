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

import de.greenrobot.event.EventBus;
import me.jitan.fhpxdemo.event.LoadPhotoDetailEvent;
import me.jitan.fhpxdemo.event.SearchEvent;
import me.jitan.fhpxdemo.helper.FragmentHelper;


public class MainActivity extends AppCompatActivity
{
    private MenuItem mSearchAction;
    private Boolean mSearchOpened;
    private String mSearchQuery, mSortOptions;
    private Drawable mIconCloseSearch, mIconOpenSearch;
    private IonClient mIonClient;
    private EditText mSearchField;
    private ActionBar mActionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get a reference to IonClient so it will listen to Eventbus events
        mIonClient = IonClient.getInstance(this);
        Ion.getDefault(this).configure().setLogging("fhpx-ion", Log.DEBUG);

        // Need to disable Spdy to access 500px API with Ion, otherwise we get weird errors.
        Ion.getDefault(this).getHttpClient().getSSLSocketMiddleware().setSpdyEnabled(false);

        setupToolbar();
        FragmentHelper.setupFragments(this, savedInstanceState);
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

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        outState.putString(FragmentHelper.ACTIVE_FRAGMENT_KEY, FragmentHelper
                .getActiveFragmentTag());

        super.onSaveInstanceState(outState);
    }

    private void loadSearchResults()
    {
        hideKeyboard();
        mSearchQuery = mSearchField.getText().toString().trim();

        if (!mSearchQuery.isEmpty())
        {
            EventBus.getDefault().post(
                    new SearchEvent(cleanSearchQuery(mSearchQuery), mSortOptions));
        }
    }

    public void onEventMainThread(LoadPhotoDetailEvent event)
    {
        FragmentHelper.showPhotoDetailFragment();
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

        // Non-deprecated form of getDrawable only available in API 21
        mIconCloseSearch = getResources().getDrawable(R.drawable.ic_close);
        mIconOpenSearch = getResources().getDrawable(R.drawable.ic_search);
        mSearchField = (EditText) findViewById(R.id.toolbar_edittext_search);
        mSearchField.addTextChangedListener(new SearchWatcher());
        setKeyboardSearchListener();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.action_search:
                toggleSearchFieldView();
                return true;

            case R.id.action_sort_score:
                Toast.makeText(this, "Sort on score", Toast.LENGTH_SHORT).show();
                mSortOptions = "_score";
                loadSearchResults();
                return true;

            case R.id.action_sort_date:
                Toast.makeText(this, "Sort on date", Toast.LENGTH_SHORT).show();
                mSortOptions = "created_at";
                loadSearchResults();
                return true;

            case R.id.action_sort_favorites:
                Toast.makeText(this, "Sort on favorites", Toast.LENGTH_SHORT).show();
                mSortOptions = "favorites_count";
                loadSearchResults();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void toggleSearchFieldView()
    {
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

    private String cleanSearchQuery(String searchQuery)
    {
        searchQuery = searchQuery.replaceAll("[^a-zA-Z0-9]+", "+").replaceAll("\\+$", "");
        return searchQuery;
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

    public void hideKeyboard()
    {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(
                Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(mSearchField.getWindowToken(), 0);
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
                    loadSearchResults();
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
