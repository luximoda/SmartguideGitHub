package uk.ac.aston.cs.forgetmenot.Model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Dhanishrao_2 on 29/01/2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DBNAME = "itemList";
    public static final String itemTable = "Item_Details";
    public static final String itemID = "itemID";
    public static final String itemName = "itemName";
    public static final String itemImage = "itemImage";
    public static final String itemNote = "itemNote";
    public static final String itemLatitude = "itemLatitude";
    public static final String itemLongitude = "itemLongitude";
    public static final String itemCount = "itemCount";
    public static final String itemBool = "itemBool";

    public static final String archiveItemTable = "Archive_Item_Details";
    public static final String archiveItemID = "archiveItemID";
    public static final String archiveItemName = "archiveItemName";
    public static final String archiveItemImage = "archiveItemImage";
    public static final String archiveItemNote = "archiveItemNote";
    public static final String archiveItemLatitude = "archiveItemLatitude";
    public static final String archiveItemLongitude = "archiveItemLongitude";
    public static final String archiveItemCount = "archiveItemCount";

    public static final String dateTimeOfItem = "dateTimeOfItem";
    public static final String archiveDateTimeOfItem = "archiveDateTimeOfItem";
    public static final String archiveItemBool = "archiveItemBool";


    public DatabaseHelper(Context context) {
        super(context, DBNAME, null, 3);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {


        // create the item table
        String CREATE_ITEM_TABLE = "CREATE TABLE " + itemTable + "("
                + itemID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + itemName + " TEXT, "
                + itemImage + " TEXT, "
                + itemNote + " TEXT, "
                + itemLatitude + " DOUBLE, "
                + itemLongitude + " DOUBLE, "
                + itemCount + " INTEGER, "
                + itemBool + " TEXT, "
                + dateTimeOfItem + " DATETIME "

                + ")";

        db.execSQL(CREATE_ITEM_TABLE);

        //creates the archive item table
        String CREATE_ARCHIVE_ITEM_TABLE = "CREATE TABLE " + archiveItemTable + "("
                + archiveItemID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + archiveItemName + " TEXT, "
                + archiveItemImage + " TEXT, "
                + archiveItemNote + " TEXT, "
                + archiveItemLatitude + " DOUBLE, "
                + archiveItemLongitude + " DOUBLE, "
                + archiveItemCount + " INTEGER, "

                + archiveItemBool + " TEXT, "
                + archiveDateTimeOfItem + " DATETIME "

                + ")";

        db.execSQL(CREATE_ARCHIVE_ITEM_TABLE);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + itemTable);
        db.execSQL("DROP TABLE IF EXISTS " + archiveItemTable);

        onCreate(db);
    }

    /**
     * Adds the item to the database
     * @param name the string name of the item
     * @param image the string uri of the picture taken by the user
     * @param note the string note written by the user
     * @param latitude latitude in double taken whem the user selects a location for the item
     * @param longitude longitude in double taken whem the user selects a location for the item
     * @param count the int amount of times the item has previously been recorded
     * @param bool the bool needed to know by the AI whether to show it again or not
     * @param date the string date of when the item had been recorded by the user
     */
    public void addItem(String name, String image, String note, Double latitude, Double longitude, Integer count, String bool, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(itemNote, note);
        values.put(itemImage, image);

        values.put(itemName, name);
        values.put(itemLatitude, latitude);
        values.put(itemLongitude, longitude);
        values.put(itemCount, count);
        values.put(itemBool, bool);
        values.put(dateTimeOfItem, date);

        db.insert(itemTable, null, values);
        db.close();

    }

    /**
     * Returns an item based on name provided
     * @param name
     * @return the item matching the string name
     */
    public Item getItemByName(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + itemTable + " WHERE " + itemName + " = ?", new String[]{String.valueOf(name)});
        Item item = null;

        cursor.moveToLast();

        while (!cursor.isBeforeFirst()) {
            item = cursorToItem(cursor);


            cursor.moveToPrevious();

        }

        cursor.close();
        db.close();
        return item;
    }

    /**
     * Returns a list of items ordered by date in ascending order
     * @return a List of type item
     */
    public List<Item> getItem() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + itemTable + " ORDER BY " + dateTimeOfItem + " ASC", null);
        List<Item> items = new ArrayList<Item>();

        cursor.moveToLast();

        while (!cursor.isBeforeFirst()) {
            Item item = cursorToItem(cursor);
            items.add(item);
            Log.i("date", "name " + item.getName() + " date of item " + item.getDate());

            cursor.moveToPrevious();

        }

        cursor.close();
        db.close();
        return items;
    }

    /**
     * Returns the size of the database
     * @return int value for the size of the database
     */
    public int getDatabaseSize() {
        int size = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + itemTable, null);
        List<Item> items = new ArrayList<Item>();

        cursor.moveToLast();

        while (!cursor.isBeforeFirst()) {
            size += 1;
            cursor.moveToPrevious();
        }

        cursor.close();
        db.close();
        return size;
    }

    /**
     * clears the content in the tables
     * @throws SQLException
     */
    public void clearContents() throws SQLException {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(itemTable, null, null);
        db.delete(itemTable, null, null);

    }

    /**
     * fetches the item fields with help of the cursor and assigns them to
     * @param cursor
     * @return an Item
     */
    private Item cursorToItem(Cursor cursor) {

        long id = cursor.getLong(0);
        String name = cursor.getString(1);
        String image = cursor.getString(2);
        String note = cursor.getString(3);
        Double latitude = cursor.getDouble(4);
        Double longitude = cursor.getDouble(5);
        Integer count = cursor.getInt(6);
        String bool = cursor.getString(7);
        String date = cursor.getString(8);

        Item item = new Item(id, name, image, note, latitude, longitude, count, bool, date);

        return item;
    }

    /**
     * Deletes the item from the id passed in
     * @param id the id of the item
     */
    public void deleteItem(long id) {

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(itemTable, itemID + " = ?",
                new String[]{String.valueOf(id)});
        db.close();
    }

    /**
     * Updates the item fields based on the item passed in
     * @param name the string name of the item
     * @param image the string uri of the picture taken by the user
     * @param note the string note written by the user
     * @param latitude latitude in double taken whem the user selects a location for the item
     * @param longitude longitude in double taken whem the user selects a location for the item
     * @param count the int amount of times the item has previously been recorded
     * @param bool the bool needed to know by the AI whether to show it again or not
     * @param date the string date of when the item had been recorded by the user
     * @param item the item whose fields has to be updated
     */
    public void updateItem(String name, String image, String note, Double latitude, Double longitude, Integer count, String bool, String date, Item item) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv1 = new ContentValues();
        cv1.put(itemName, name);
        cv1.put(itemImage, image);
        cv1.put(itemNote, note);
        cv1.put(itemLatitude, latitude);
        cv1.put(itemLongitude, longitude);
        cv1.put(itemCount, count);
        cv1.put(itemBool, bool);
        cv1.put(dateTimeOfItem, date);

        db.update(itemTable, cv1, itemID + " = ?",
                new String[]{String.valueOf(item.get_id())});

        db.close();
    }

    /**
     * Gets the current date and time formatted based on simpledateformat
     * @return a formatted string of the current date and time
     */
    public String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "dd-MM-yy, HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    /**
     * Gets the most recent item stored
     * @return item of type Item
     */
    public Item getRecentItem() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + itemTable + " ORDER BY " + dateTimeOfItem + " DESC", null);
        Item item = null;

        cursor.moveToLast();

        while (!cursor.isBeforeFirst()) {
            item = cursorToItem(cursor);


            cursor.moveToPrevious();

        }

        cursor.close();
        db.close();
        return item;
    }


//-------------------------------------------------------------------------------------------------------------------------------------------------

    /**
     * Adds an item to the archive table
     *  @param name the string name of the item
     * @param image the string uri of the picture taken by the user
     * @param note the string note written by the user
     * @param latitude latitude in double taken whem the user selects a location for the item
     * @param longitude longitude in double taken whem the user selects a location for the item
     * @param count the int amount of times the item has previously been recorded
     * @param bool the bool needed to know by the AI whether to show it again or not
     * @param date the string date of when the item had been recorded by the user
     */
    public void addArchiveItem(String name, String image, String note, Double latitude, Double longitude, Integer count, String bool, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(archiveItemNote, note);
        values.put(archiveItemImage, image);

        values.put(archiveItemName, name);
        values.put(archiveItemLatitude, latitude);
        values.put(archiveItemLongitude, longitude);
        values.put(archiveItemCount, count);
        values.put(archiveItemBool, bool);
        values.put(archiveDateTimeOfItem, date);

        db.insert(archiveItemTable, null, values);

        db.close();

    }

    /**
     * Updates an item in the archive database
     * @param image the string uri of the picture taken by the user
     * @param note the string note written by the user
     * @param count the int amount of times the item has previously been recorded
     * @param id the long id of the item
     */
    public void updateArchiveItem(String image, String note, Integer count, long id) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv1 = new ContentValues();

        if (image == null) {

        } else {
            cv1.put(archiveItemImage, image);
        }
        if (note.equals("Undefined notes")) {

        } else {
            cv1.put(archiveItemNote, note);
        }

        cv1.put(archiveItemCount, count);


        db.update(archiveItemTable, cv1, archiveItemID + " = ?", new String[]{String.valueOf(id)});

        db.close();
    }

    /**
     * Returns a List of items from the archive table
     * @return List of type Item
     */
    public List<Item> getArchiveItem() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + archiveItemTable, null);
        List<Item> items = new ArrayList<Item>();

        cursor.moveToLast();

        while (!cursor.isBeforeFirst()) {
            Item item = cursorToItem(cursor);
            items.add(item);
            cursor.moveToPrevious();
        }

        cursor.close();
        db.close();
        return items;
    }

}
