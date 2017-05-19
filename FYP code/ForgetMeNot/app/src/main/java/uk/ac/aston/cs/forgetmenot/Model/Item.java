package uk.ac.aston.cs.forgetmenot.Model;

import java.io.Serializable;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Locale;

import uk.ac.aston.cs.forgetmenot.R;

/**
 * Created by Dhanishrao_2 on 29/01/2017.
 */

public class Item implements Serializable{
    long _id;
    String name;
    String image;
    String note;
    Double latitude;
    Double longitude;
    Integer count;
    String bool;
    String date;

    /**
     * Constructor for item
     */
    public Item(){};

    /**
     * Constructor for item
     * @param _id long id of the item
     * @param name the string name of the item
     * @param image the string uri of the picture taken by the user
     * @param note the string note written by the user
     * @param latitude latitude in double taken whem the user selects a location for the item
     * @param longitude longitude in double taken whem the user selects a location for the item
     * @param count the int amount of times the item has previously been recorded
     * @param bool the bool needed to know by the AI whether to show it again or not
     * @param date the string date of when the item had been recorded by the user
     */
    public Item(long _id,String name,String image,String note, Double latitude, Double longitude, Integer count, String bool, String date){
        this._id=_id;
        this.name = name;
        this.image=image;
        this.note=note;
        this.latitude = latitude;
        this.longitude = longitude;
        this.count = count;
        this.bool = bool;
        this.date = date;
    }

    /**
     * Returns the id of the item
     * @return long id
     */
    public long get_id() {
        return _id;
    }

    /**
     * Sets the id for the item
     * @param _id Long Id
     */
    public void set_id(long _id) {
        this._id = _id;
    }

    /**
     * Returns the name of the item
     * @return string name of item
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the date the item was created
     * @return String date
     */
    public String getDate() {
        return date;
    }

    /**
     * Sets the date the item is recorded
     * @param date the String date
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * Sets the name of the item
     * @param name String name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the image of the item
     * @return the String uri of the image
     */
    public String getImage() {
        return image;
    }

    /**
     * Sets the image of the item
     * @param image the String uri of the image
     */
    public void setImage(String image) {
        this.image = image;
    }

    /**
     * Returns the note of the item
     * @return the String note
     */
    public String getNote() {
        return note;
    }

    /**
     * Sets the note of the item
     * @param note the String note
     */
    public void setNote(String note) {
        this.note = note;
    }

    /**
     * Returns the latitude of where the item has been recorded
     * @return the Double latitude
     */
    public Double getLatitude() {
        return latitude;
    }

    /**
     * Returns the longitude of where the item has been recorded
     * @return the Double longitude
     */
    public Double getLongitude() {
        return longitude;
    }

    /**
     * Sets the longitude of the item
     * @param longitude Double longitude
     */
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    /**
     * Sets the latitude of the item
     * @param latitude Double latitude
     */
    public void setLatitude(Double latitude) {
        this.latitude = latitude;}

    /**
     * Returns the amount of time the same item has been stored
     * @return the amoutn of times of type Integer the item has been stored
     */
    public Integer getCount() {
        return count;
    }

    /**
     * Sets the amount of times the same item has been stored
     * @param count Integer count
     */
    public void setCount(Integer count) {
        this.count = count;
    }

    /**
     * returns the bool of whether the item should be displayed by the AI or not
     * @return returns the boolean of type String
     */
    public String getBool() {
        return bool;
    }

    /**
     * Sets the bool of whether the item should be displayed by the AI or not
     * @param bool bool of type String
     */
    public void setBool(String bool) {
        this.bool = bool;
    }

    /**
     * Returns the date the item has been stored
     * @return Date of type string
     */
    public String getItemDate(){
        return date;
    }

    /**
     * Returns the formatted current date and time
     * @return the String date
     */
    public String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    /**
     *Sets the image of the item based on their name
     * @param item the String name of the item
     * @return image of the item in int
     */
    public int setImageForMatchedItem(String item) {
        int imageResource;
        item = item.toLowerCase();
        if(item.contains(",")){
            imageResource = R.drawable.cameranull;
        }else {
            if (item.contains("glasses")) {
                imageResource = R.drawable.glasses;

            } else if (item.contains("key")) {
                imageResource = R.drawable.keys;

            } else if (item.contains("wallet")) {
                imageResource = R.drawable.wallet;

            } else if (item.contains("pen")) {
                imageResource = R.drawable.pen;

            } else if (item.contains("car")) {
                imageResource = R.drawable.car;

            } else if (item.contains("book")) {
                imageResource = R.drawable.book;

            } else {
                imageResource = R.drawable.cameranull;
            }
        }

return imageResource;
    }
}