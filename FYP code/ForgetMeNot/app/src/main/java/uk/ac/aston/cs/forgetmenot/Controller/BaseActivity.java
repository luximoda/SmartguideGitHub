package uk.ac.aston.cs.forgetmenot.Controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import uk.ac.aston.cs.forgetmenot.R;

/**
 * Created by Dhanishrao_2 on 02/04/2017.
 */

public class BaseActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private NavigationView navigationView;

    /**
     * Renders the app toolbar
     */
   public void displayToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    /**
     *  Renders the app navigation drawer and the toolbar
     */
    protected void displayDrawer() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer != null && drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * Makes the selected button in the drawer disabled and greyed and the other button enabled and not greyed out
     * @param record boolean to know whether the record button has been pressed or not
     * @param find boolean to know whether the find button has been pressed or not
     */
    protected void handleDrawerItemSelection(boolean record, boolean find) {
        if (record) {
            navigationView.getMenu().getItem(0).setChecked(true);
            navigationView.getMenu().getItem(0).setEnabled(false);
        } else {
            navigationView.getMenu().getItem(0).setChecked(false);
            navigationView.getMenu().getItem(0).setEnabled(true);
        }

        if (find) {
            navigationView.getMenu().getItem(1).setChecked(true);
            navigationView.getMenu().getItem(1).setEnabled(false);
        } else {
            navigationView.getMenu().getItem(1).setChecked(false);
            navigationView.getMenu().getItem(1).setEnabled(true);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_record:
                Intent recordActivityIntent = new Intent(this, RecordActivity.class);

                startActivity(recordActivityIntent);
                break;

            case R.id.nav_find:

                Intent findActivityIntent = new Intent(this, FindActivity.class);

                startActivity(findActivityIntent);

                break;


            default:
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}