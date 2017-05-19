package uk.ac.aston.cs.forgetmenot.Controller;


import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;

import uk.ac.aston.cs.forgetmenot.Model.DatabaseHelper;
import uk.ac.aston.cs.forgetmenot.Model.Item;
import uk.ac.aston.cs.forgetmenot.R;


/**
 * Created by Dhanishrao_2 on 23/01/2017.
 */

public class RecordActivity extends BaseActivity implements NoteFragment.NoteFragmentInterface, CameraFragment.CameraFragmentInterface
        , MapFragment.MapFragmentInterface, PreviewFragment.PreviewFragmentInterface {

    private Button addNote;
    private Button addPicture;
    private Button addLocation;
    private Button preview;
    Drawable noteButtonBackground;
    Drawable pictureButtonBackground;
    Drawable locationButtonBackground;
    Drawable previewButtonBackground;
    private DatabaseHelper dbHelper;
    private String itemNameReceived = null;
    private String noteReceived = null;
    private String imageReceived = null;
    private Double latitudeReceived = 0.0;
    private Double longitudeReceived = 0.0;
    private FragmentManager fragmentManager;
    private boolean previewBooleanReceived = false;
    private boolean previewFragmentCreated = false;

    private List<Item> items;
    private boolean notEqualBool;


    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.record_activity_nav_bar);
        setTitle("Record Item");
        displayDrawer();
        handleDrawerItemSelection(true,false);
        dbHelper = new DatabaseHelper(this);

        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        NoteFragment noteFragment = new NoteFragment();
        fragmentTransaction.replace(R.id.frame_container, noteFragment);
        fragmentTransaction.commit();

        addNote = (Button) findViewById(R.id.note_button);
        noteButtonBackground = addNote.getBackground();
        addPicture = (Button) findViewById(R.id.camera_button);
        addLocation = (Button) findViewById(R.id.map_button);
        preview = (Button) findViewById(R.id.preview_button);


        pictureButtonBackground = addPicture.getBackground();
        locationButtonBackground = addLocation.getBackground();
        previewButtonBackground = preview.getBackground();




        addNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                NoteFragment noteFragment = new NoteFragment();
                fragmentTransaction.replace(R.id.frame_container, noteFragment);
                fragmentTransaction.commit();

            }
        });


        addPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                CameraFragment cameraFragment = new CameraFragment();
                fragmentTransaction.replace(R.id.frame_container, cameraFragment);
                fragmentTransaction.commit();

            }
        });


        addLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                MapFragment mapFragment = new MapFragment();
                fragmentTransaction.replace(R.id.frame_container, mapFragment);
                fragmentTransaction.commit();

            }
        });


        preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                PreviewFragment previewFragment = new PreviewFragment();
                fragmentTransaction.replace(R.id.frame_container, previewFragment);
                fragmentTransaction.commit();
            }
        });

        if (previewBooleanReceived) {
            addToDatabase();
            Toast.makeText(this, "Information saved", Toast.LENGTH_LONG).show();
            loadMainMenuActivity();

        }

        if (previewFragmentCreated) {

            PreviewFragment previewFragment = (PreviewFragment) getSupportFragmentManager().findFragmentById(R.id.frame_container);
            previewFragment.setAllTheValues(itemNameReceived, imageReceived, noteReceived, latitudeReceived, longitudeReceived);

        }

    }


    /**
     * Adds the item to the item table if it does not exists in the archive table. If it does exist in the latter, the count is updated in the archive table
     * and it is added to item table. Afterwards, the main menu is loaded
     */
    public void addToDatabase() {
        items = dbHelper.getItem();

        if (items.isEmpty()) {
            String[] itemNameSplit = itemNameReceived.split(",");

            for (int i = 0; i < itemNameSplit.length; i++) {

                dbHelper.addItem(itemNameSplit[i], imageReceived, noteReceived, latitudeReceived, longitudeReceived, 0, "no", dbHelper.getDateTime());
                compareItems(itemNameSplit[i], imageReceived, noteReceived, latitudeReceived, longitudeReceived, 0, "no", dbHelper.getDateTime());
            }
        } else {

            String[] itemNameSplit = itemNameReceived.split(",");

            for (int i = 0; i < itemNameSplit.length; i++) {
                notEqualBool = false;
                for (Item item : items) {

                    if ((itemNameSplit[i].toLowerCase()).equals(item.getName().toLowerCase())) {
                        notEqualBool = true;

                        int count = item.getCount();
                        count += 1;
                        dbHelper.updateItem(itemNameSplit[i], imageReceived,
                                noteReceived, latitudeReceived, longitudeReceived, count, "no", dbHelper.getDateTime(), item);
                           compareItems(itemNameSplit[i], imageReceived, noteReceived, latitudeReceived, longitudeReceived, 0, "no", dbHelper.getDateTime());
                    }

                }
                if (notEqualBool == false) {

                    dbHelper.addItem(itemNameSplit[i], imageReceived, noteReceived, latitudeReceived,
                            longitudeReceived, 0, "no", dbHelper.getDateTime());
                      compareItems(itemNameSplit[i], imageReceived, noteReceived, latitudeReceived, longitudeReceived, 0, "no", dbHelper.getDateTime());
                }
            }

        }

        loadMainMenuActivity();
    }

    /**
     * Checks if the item already exists in the archive table and adds the item if it does exist or updates the count if it already existsin the archive table
     * @param name the string name of the item
     * @param image the string uri of the picture taken by the user
     * @param note the string note written by the user
     * @param latitude latitude in double taken whem the user selects a location for the item
     * @param longitude longitude in double taken whem the user selects a location for the item
     * @param count the int amount of times the item has previously been recorded
     * @param bool the bool needed to know by the AI whether to show it again or not
     * @param date the string date of when the item had been recorded by the user
     */
    public void compareItems(String name, String image, String note, double latitude, double longitude, int count, String bool, String date) {

        List<Item> archiveItems = dbHelper.getArchiveItem();

        Location currentLocation = new Location("");
        currentLocation.setLatitude(latitude);
        currentLocation.setLongitude(longitude);


        if (archiveItems.size() == 0) {
            dbHelper.addArchiveItem(name, image, note, latitude, longitude, 0, "no", date);
        } else {
            boolean notEqualBool2 = false;
            for (int i = 0; i < archiveItems.size(); i++) {

                Location archiveLocation = new Location("");
                archiveLocation.setLatitude(archiveItems.get(i).getLatitude());
                archiveLocation.setLongitude(archiveItems.get(i).getLongitude());
                float calculatedDistance = currentLocation.distanceTo(archiveLocation);
                if (calculatedDistance < 5 && archiveItems.get(i).getName().equals(name)) {
                    notEqualBool2 = true;
                    int archiveCount = archiveItems.get(i).getCount() + 1;
                    dbHelper.updateArchiveItem(image, note, archiveCount, archiveItems.get(i).get_id());

                }
            }
            if (notEqualBool2 == false) {
                dbHelper.addArchiveItem(name, image, note, latitude, longitude, 0, "no", date);
            }
        }

    }


    @Override
    public void setNote(String note, String itemName) {
        if (note.isEmpty()) {
            note = "Undefined notes";
        }
        noteReceived = note;

        if (itemName.isEmpty()) {
            itemName = "Undefined name";
        }
        itemNameReceived = itemName;

    }

    @Override
    public void setCamera(String camera) {

        imageReceived = camera;
    }

    @Override
    public void setLocation(Location location) {

        if (!(location == null)) {
            latitudeReceived = location.getLatitude();
            longitudeReceived = location.getLongitude();
        }
    }

    @Override
    public void setPreviewBoolean(boolean boolValue) {
        previewBooleanReceived = boolValue;

    }

    @Override
    public void getAttentionOfCreation(boolean createdBool) {
        previewFragmentCreated = createdBool;
        PreviewFragment previewFragment = (PreviewFragment) getSupportFragmentManager().findFragmentById(R.id.frame_container);
        previewFragment.setAllTheValues(itemNameReceived, imageReceived, noteReceived, latitudeReceived, longitudeReceived);



    }

    /**
     * loads the main menu
     */
    public void loadMainMenuActivity() {
        Intent intentToLoadMainMenuActivity = new Intent(this, MainMenuActivity.class);
        startActivity(intentToLoadMainMenuActivity);
    }

    @Override
    public void onBackPressed() {

        finish();

    }

    /**
     * Disables and changes the colour if the icon in the Breadcrumb depending on which one has been chosen
     * @param note Boolean of whether the note icon has been pressed
     * @param camera Boolean of whether the camera icon has been pressed
     * @param map Boolean of whether the note map has been pressed
     * @param preview Boolean of whether the preview  icon has been pressed
     * @param noteInt Int that changes the alpha of the note icon
     * @param cameraInt Int that changes the alpha of the camera icon
     * @param mapInt Int that changes the alpha of the map icon
     * @param previewInt Int that changes the alpha of the preview icon
     */
    public void disableButton(boolean note, boolean camera, boolean map,
                              boolean preview, int noteInt, int cameraInt, int mapInt, int previewInt) {
        addNote.setEnabled(note);
        addPicture.setEnabled(camera);
        addLocation.setEnabled(map);
        this.preview.setEnabled(preview);
        addNote.getBackground().setAlpha(noteInt);
        addPicture.getBackground().setAlpha(cameraInt);
        addLocation.getBackground().setAlpha(mapInt);
        this.preview.getBackground().setAlpha(previewInt);
    }



}











