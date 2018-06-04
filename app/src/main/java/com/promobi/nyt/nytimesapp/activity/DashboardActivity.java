package com.promobi.nyt.nytimesapp.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;

import com.promobi.nyt.nytimesapp.R;
import com.promobi.nyt.nytimesapp.adapter.ArticleAdapter;
import com.promobi.nyt.nytimesapp.api.ApiServiceSingleton;
import com.promobi.nyt.nytimesapp.fragments.SettingsFragment;
import com.promobi.nyt.nytimesapp.listener.EndlessRecyclerViewScrollListener;
import com.promobi.nyt.nytimesapp.model.ApiResponse;
import com.promobi.nyt.nytimesapp.model.Doc;
import com.promobi.nyt.nytimesapp.model.SearchFilter;

import org.apache.commons.lang3.StringUtils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.util.TextUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SettingsFragment.OnSettingsChangeListener, DatePickerDialog.OnDateSetListener
{

    public static final String EXTRA_ARTICLE_URL = "ArticleUrl";
    public static final String TAG_FILTER_DIALOG = "filter";
    public static final String FILENAME = "searchFilter.txt";
    // General
    Activity mActivity;
    SharedPreferences mPref;
    // RecyclerView
    ArrayList<Doc> mArticles;
    ArticleAdapter mAdapter;
    ProgressDialog mProgressDialog;
    EndlessRecyclerViewScrollListener mScrollListener;
    @BindView(R.id.rv_dashboard)
    RecyclerView rv_dashboard;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    int page = 0;
    // API requests
    String mQuery = "";
    // UI
    MenuItem mFilterMenuItem;
    SearchFilter searchFilter;
    String searchQuery = null;
    String numSearchResults;
    SettingsFragment settingsDialog;
    SharedPreferences.Editor e;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        setSupportActionBar(toolbar);
        mActivity = this;
        mPref = getPreferences(MODE_PRIVATE);
        e = mPref.edit();
        searchFilter = loadSearchFilter();
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        mArticles = new ArrayList<>();
        mProgressDialog = setupProgressDialog();
        mAdapter = new ArticleAdapter(mArticles, this);


        // Set up RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mScrollListener = new EndlessRecyclerViewScrollListener(layoutManager)
        {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view)
            {
                loadData(page);
            }
        };
        rv_dashboard.setAdapter(mAdapter);
        rv_dashboard.setLayoutManager(layoutManager);
        // rv_dashboard.addItemDecoration(new SpacesItemDecoration(16));
        rv_dashboard.addOnScrollListener(mScrollListener);

        //loadData(0);
        loadData(page);

        mScrollListener = new EndlessRecyclerViewScrollListener(layoutManager)
        {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view)
            {
                loadData(page);
            }
        };

        e.clear();


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }


    // TODO: check if using ProgressBar is better
    private ProgressDialog setupProgressDialog()
    {
        ProgressDialog p = new ProgressDialog(mActivity);
        p.setIndeterminate(true);
        p.setMessage(getString(R.string.progress_loading));
        return p;
    }


    // Read filters from SharedPreferences, and construct and execute the API query
    private void loadData(int page)
    {
        // Read query parameter values from SharedPreferences
/*

        String beginDate = "";
        String endDate = "";
        String sortOrder = "";
        String newsDesk = "";
        String query = "";
*/


    /*    if (searchFilter.isFilterSet())
        {
            beginDate = nullify(mPref.getString(getString(R.string.pref_begin_date), ""));
            endDate = nullify(mPref.getString(getString(R.string.pref_end_date), ""));
            sortOrder = nullify(mPref.getString(getString(R.string.pref_sort_order), ""));
            newsDesk = nullify(mPref.getString(getString(R.string.pref_news_desk), ""));
            query = nullify(mQuery);

        }
        else
        {
            beginDate = nullify(mPref.getString(getString(R.string.pref_begin_date), ""));
            endDate = nullify(mPref.getString(getString(R.string.pref_end_date), ""));
            sortOrder = nullify(mPref.getString(getString(R.string.pref_sort_order), ""));
            newsDesk = nullify(mPref.getString(getString(R.string.pref_news_desk), ""));
            query = nullify(mQuery);


        }*/


        String beginDate = nullify(mPref.getString(getString(R.string.pref_begin_date), ""));
        String endDate = nullify(mPref.getString(getString(R.string.pref_end_date), ""));
        String sortOrder = nullify(mPref.getString(getString(R.string.pref_sort_order), ""));
        String newsDesk = nullify(mPref.getString(getString(R.string.pref_news_desk), ""));
        String query = nullify(mQuery);

        // The "fq" parameter uses Lucene query syntax:
        //     field:value
        // If the value contains whitespace, enclose it in double quotes:
        //     field:"my value"
        // For specifying multiple values for a field enclose them in parentheses:
        //     field:("value 1" "value 2")
        // The default operator for multiple values is OR. It's possible to specify AND:
        //     field:("value 1" AND "value 2")
        // Multiple fields can be specified by separating them with a space:
        //     field1:("value 1" "value_2") field2:"value 3"
        // The default operator for multiple fields is OR. It's possible to specify AND:
        //     field1:("value 1" "value 2") AND field2:"value 3"
        //
        // Notes:
        //   - If an invalid token is submitted (e.g. invalid field name or news_desk category),
        //     then it is just ignored (no error returned). An error is only returned if the
        //     Lucene query syntax is invalid.
        //   - In general, the "q" parameter is optional (might make sense if, for example,
        //     searching all the articles of today with begin_date=...&end_date=...)
        if (newsDesk != null)
        {
            ArrayList<String> a = new ArrayList<>(Arrays.asList(newsDesk.split(":")));
            Iterator itr = a.iterator();
            newsDesk = "news_desk:(";
            while (itr.hasNext())
            {
                newsDesk += "\"" + itr.next() + "\"";
                if (itr.hasNext())
                {
                    newsDesk += " ";
                }
                else
                {
                    newsDesk += ")";
                }
            }
        }

        query(query, newsDesk, beginDate, endDate, sortOrder, page);
    }


    private void query(String q, String fq, String beginDate, String endDate, String sort, Integer page)
    {
        mProgressDialog.show();
        Call<ApiResponse> call = ApiServiceSingleton.getInstance().query(q, fq, beginDate, endDate, sort, page);

        call.enqueue(new Callback<ApiResponse>()
        {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response)
            {

                if (response.code() == 429)
                {
                    Log.v("Dashboard", response.code() + ": rate limit exceeded");
                    return;
                }
                try
                {
                    ArrayList<Doc> articles = (ArrayList<Doc>) response.body().getResponse().getDocs();
                    if (articles.isEmpty())
                    {
                    }
                    //Util.toastLong(mActivity, getString(R.string.toast_no_results));
                    else
                    {
                        mAdapter.appendArticles(articles);
                    }
                }
                catch (NullPointerException e)
                {
                    System.out.print(e);
                }
                mProgressDialog.dismiss();

               /* ArrayList<Doc> articles = (ArrayList<Doc>) response.body().getResponse().getDocs();
                mAdapter.appendArticles(articles);
                mProgressDialog.dismiss();*/

            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t)
            {
                mProgressDialog.dismiss();
            }
        });

    }

    // Return null for an empty string, and the original string for an non-empty string
    private String nullify(String str)
    {
        return (str.isEmpty()) ? null : str;
    }


    @Override
    public void onBackPressed()
    {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);
        }
        else
        {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dashboard, menu);
        setupSearchView(menu);
        // Save reference to the "Filter" menu item and tint icon if any filters are set
        //  mFilterMenuItem = menu.findItem(R.id.action_settings);
        //    if (isAnyFilterSet()) tintFilterIcon(true);
        return super.onCreateOptionsMenu(menu);
    }

    private void setupSearchView(Menu menu)
    {
        MenuItem menuItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            // Called when query is submitted (by pressing "Search" button on keyboard)
            // Note: empty search queries detected by the SearchView itself and ignored
            @Override
            public boolean onQueryTextSubmit(String query)
            {
                mAdapter.clearArticles();
                mScrollListener.resetState();
                mQuery = query;
                loadData(0);
                loadData(1);
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText)
            {
                return false;
            }
        });
    }

    @Override
    protected void onPostResume()
    {
        super.onPostResume();
        mQuery = "";
        loadData(0);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            showSettingsDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void searchForArticles(String query)
    {
        searchQuery = query;


        String beginDateYYYYMMDD = searchFilter.getBeginDate(SearchFilter.FORMAT_YYYYMMDD);
        if (!TextUtils.isBlank(beginDateYYYYMMDD))
        {
            e.putString(getString(R.string.pref_begin_date), beginDateYYYYMMDD);
        }

        String sortOrder = searchFilter.getSortOrder();
        if (!TextUtils.isBlank(sortOrder))
        {
            e.putString(getString(R.string.pref_sort_order), sortOrder);
        }

        Set ndTopics = searchFilter.getNewsDeskTopics();
        if (ndTopics.size() > 0)
        {
            // &fq=news_desk:("Sports" "Foreign")
            e.putString(getString(R.string.pref_news_desk), "news_desk:(\"" + StringUtils.join(ndTopics.toArray(), "\" \"") + "\")");
        }

        e.apply();

        page=0;

        loadData(page);


    }


    private void showSettingsDialog()
    {
        FragmentManager fm = getSupportFragmentManager();
        settingsDialog = SettingsFragment.newInstance(searchFilter);
        settingsDialog.show(fm, "fragment_edit_name");
    }


    public void saveSearchFilterSettings(SearchFilter newSearchFilter)
    {
        searchFilter = newSearchFilter;
        saveSearchFilter(searchFilter);
        searchForArticles(searchQuery);
    }

    public void onDateSet(DatePicker view, int yy, int mm, int dd)
    {
        populateSetDate(yy, mm + 1, dd);
    }

    public void populateSetDate(int year, int month, int day)
    {
        if (settingsDialog != null)
        {
            settingsDialog.populateSetDate(year, month, day);
        }
    }


    private void saveSearchFilter(SearchFilter filter)
    {
        try
        {
            FileOutputStream fos = this.openFileOutput(FILENAME, Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(filter);
            os.close();
            fos.close();
        }
        catch (IOException e)
        {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    public SearchFilter loadSearchFilter()
    {
        SearchFilter filter = new SearchFilter();
        try
        {
            FileInputStream fis = this.openFileInput(FILENAME);
            ObjectInputStream is = new ObjectInputStream(fis);
            filter = (SearchFilter) is.readObject();
            is.close();
            fis.close();
            return (filter);
        }
        catch (ClassNotFoundException cnfe)
        {
            Log.e("Exception", "ClassNotFoundException: " + cnfe.toString());
        }
        catch (IOException e)
        {
            Log.e("Exception", "IOException: " + e.toString());
        }
        return filter;
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera)
        {
            // Handle the camera action
        }
        else if (id == R.id.nav_gallery)
        {

        }
        else if (id == R.id.nav_slideshow)
        {

        }
        else if (id == R.id.nav_manage)
        {

        }
        else if (id == R.id.nav_share)
        {

        }
        else if (id == R.id.nav_send)
        {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
