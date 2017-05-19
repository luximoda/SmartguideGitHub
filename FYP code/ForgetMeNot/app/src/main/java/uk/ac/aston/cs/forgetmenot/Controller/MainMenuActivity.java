package uk.ac.aston.cs.forgetmenot.Controller;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import uk.ac.aston.cs.forgetmenot.Model.DatabaseHelper;
import uk.ac.aston.cs.forgetmenot.Model.Item;
import uk.ac.aston.cs.forgetmenot.R;

public class MainMenuActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private LocationListener locationListener;
    private Location mLastLocation = null;
    private LocationManager locationManager;
    private DatabaseHelper dbHelper;
    private List<Item> items;
    private List<Item> closestItemList;
    AlertDialog.Builder builder;
    private List<Item> Aitems;
    private NavigationView navigationView;
    private FragmentManager fragmentManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        createNav();
        setTitle("Forget Me Not");
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        MainMenuFragment mainMenuFragment = new MainMenuFragment();
        fragmentTransaction.replace(R.id.main_menu_container, mainMenuFragment);
        fragmentTransaction.commit();

        dbHelper = new DatabaseHelper(getApplicationContext());


        Aitems = dbHelper.getArchiveItem();


        builder = new AlertDialog.Builder(this);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                mLastLocation = location;


                if (returnClosestItem().size() == 0) {

                } else {
                    closestItemList = new ArrayList<>();
                    closestItemList = returnClosestItem();
                    setList(closestItemList);

                    fragmentManager = getSupportFragmentManager();

                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                    DialogAiRecyclerViewFragment dialogAiRecyclerViewFragment = new DialogAiRecyclerViewFragment();
                    dialogAiRecyclerViewFragment.setList(closestItemList);
                    fragmentTransaction.replace(R.id.main_menu_container, dialogAiRecyclerViewFragment);
                    fragmentTransaction.commit();
                    changeLocationListenerStatus(true);


                }


            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };


        changeLocationListenerStatus(false);


    }

    /**
     * Creates the navigational drawer and the toolbar
     */
    protected void createNav() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    /**
     * sets the list for the closest item
     * @param list list of item
     */
    public void setList(List list) {

    }

    /**
     * Returns the list of the closest item(s) to the user
     * @return the List of type Item
     */
    public List<Item> getList() {
        return closestItemList;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.record_button) {


            Intent r = new Intent(this, RecordActivity.class);
            startActivity(r);
        }
    }

    @Override
    public void onBackPressed() {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();


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


    @Override
    protected void onPause() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.removeUpdates(locationListener);
        super.onPause();

    }

    /**
     * Returns the closest item to the user
     * @return Arraylist of type Item
     */
    public ArrayList<Item> returnClosestItem() {

        items = dbHelper.getArchiveItem();
        ArrayList<Item> itemList = new ArrayList<>();
        double calculatedDistance;
        double previousItemDistance = 2;


        for (Item currentItem : items) {

            Location current = new Location("");
            current.setLatitude(currentItem.getLatitude());
            current.setLongitude(currentItem.getLongitude());


            calculatedDistance = mLastLocation.distanceTo(current);


            if (calculatedDistance < previousItemDistance && calculatedDistance <= 2 && currentItem.getCount() >= 3) {
                itemList.clear();

                itemList.add(currentItem);
                previousItemDistance = calculatedDistance;
            } else if (calculatedDistance == previousItemDistance && calculatedDistance <= 2 && currentItem.getCount() >= 3) {
                itemList.add(currentItem);
                previousItemDistance = calculatedDistance;
            }
        }

        return itemList;
    }


    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {


        return super.onCreateView(parent, name, context, attrs);
    }

    /**
     * Stops internet connection if an AI dialog has been created
     * @param dialogCreated Boolean of whether the AI screen has been created or not
     */
    public void changeLocationListenerStatus(Boolean dialogCreated) {

        if (dialogCreated) {

            if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            locationManager.removeUpdates(locationListener);

        } else {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locationManager.requestLocationUpdates("gps", 5000, 0, locationListener);
        }

    }

}

