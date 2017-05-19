package uk.ac.aston.cs.forgetmenot.Controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import uk.ac.aston.cs.forgetmenot.R;

/**
 * Created by Dhanishrao_2 on 29/01/2017.
 */

public class FindActivity extends BaseActivity {
    private FragmentManager fragmentManager;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setTitle("Find Item");
        setContentView(R.layout.find_activity_nav_bar);
        displayDrawer();
        handleDrawerItemSelection(false, true);
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        RecyclerViewFragment recyclerViewFragment = new RecyclerViewFragment();
        fragmentTransaction.replace(R.id.find_frame_container, recyclerViewFragment);
        fragmentTransaction.commit();


    }

    @Override
    public void onBackPressed() {
        Intent intentToLoadMainMenuActivity = new Intent(this, MainMenuActivity.class);


        startActivity(intentToLoadMainMenuActivity);

    }
}
