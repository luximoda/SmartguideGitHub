package uk.ac.aston.cs.forgetmenot;

import android.content.Context;
import android.location.Location;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import uk.ac.aston.cs.forgetmenot.Model.DatabaseHelper;
import uk.ac.aston.cs.forgetmenot.Model.Item;

import static org.junit.Assert.assertEquals;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class AndroidTest {

    DatabaseHelper dbHelper;
    Item item;
    StringBuilder sb;

    @Before
    public void setUp() {
        Context appContext = InstrumentationRegistry.getTargetContext();
        dbHelper = new DatabaseHelper(appContext);
        sb = new StringBuilder("car,glasses,shoes");
        item = new Item();
    }


    @Test
    public void addingItemToDatabase() {

        dbHelper.addItem("Glasses", null, null, 52.082172,
                0.457693, 0, "no", dbHelper.getDateTime());
        assertEquals(1, dbHelper.getDatabaseSize());
    }

    @Test
    public void gettingItemFromDatabase() {
        addingItemToDatabase();
        assertEquals("Glasses", dbHelper.getRecentItem().getName());
    }


    @Test
    public void testingAIreturningClosestItem() {
        dbHelper.clearContents();
        Item item1 = new Item(111, "Glasses", null, null, 52.082160, 0.457701, 4, "no", dbHelper.getDateTime());
        Item item2 = new Item(112, "Book", null, null, 52.082152, 0.457691, 4, "no", dbHelper.getDateTime());
        List<Item> items = new ArrayList<>();
        items.add(item1);
        items.add(item2);
        ArrayList<Item> itemList = new ArrayList<>();
        double calculatedDistance;
        double previousItemDistance = 2;
        Location mLastLocation = new Location("");
        mLastLocation.setLatitude(52.082172);
        mLastLocation.setLongitude(0.457693);


        for (Item currentItem : items) {

            Location current = new Location("");
            current.setLatitude(currentItem.getLatitude());
            current.setLongitude(currentItem.getLongitude());


            calculatedDistance = mLastLocation.distanceTo(current);


            if (calculatedDistance < previousItemDistance && calculatedDistance < 2 && currentItem.getCount() >= 3) {
                itemList.clear();

                itemList.add(currentItem);
                previousItemDistance = calculatedDistance;
            } else if (calculatedDistance == previousItemDistance && calculatedDistance < 2 && currentItem.getCount() >= 3) {
                itemList.add(currentItem);
                previousItemDistance = calculatedDistance;
            }
        }

        assertEquals(1, itemList.size());
        assertEquals("Glasses", itemList.get(0).getName());
    }

    @Test
    public void testingAINotReturningClosestItem() {
        dbHelper.clearContents();
        Item item1 = new Item(111, "Glasses", null, null, 52.082160, 0.457701, 2, "no", dbHelper.getDateTime());
        Item item2 = new Item(112, "Book", null, null, 52.082152, 0.457691, 2, "no", dbHelper.getDateTime());
        List<Item> items = new ArrayList<>();
        items.add(item1);
        items.add(item2);
        ArrayList<Item> itemList = new ArrayList<>();
        double calculatedDistance;
        double previousItemDistance = 2;
        Location mLastLocation = new Location("");
        mLastLocation.setLatitude(52.082172);
        mLastLocation.setLongitude(0.457693);


        for (Item currentItem : items) {

            Location current = new Location("");
            current.setLatitude(currentItem.getLatitude());
            current.setLongitude(currentItem.getLongitude());


            calculatedDistance = mLastLocation.distanceTo(current);


            if (calculatedDistance < previousItemDistance && calculatedDistance < 2 && currentItem.getCount() >= 3) {
                itemList.clear();

                itemList.add(currentItem);
                previousItemDistance = calculatedDistance;
            } else if (calculatedDistance == previousItemDistance && calculatedDistance < 2 && currentItem.getCount() >= 3) {
                itemList.add(currentItem);
                previousItemDistance = calculatedDistance;
            }
        }

        assertEquals(0, itemList.size());

    }

    @Test
    public void testingAIreturningMultipleClosestItem() {
        dbHelper.clearContents();
        Item item1 = new Item(111, "Glasses", null, null, 52.082160, 0.457701, 4, "no", dbHelper.getDateTime());
        Item item2 = new Item(111, "Car", null, null, 52.082160, 0.457701, 4, "no", dbHelper.getDateTime());
        Item item3 = new Item(112, "Book", null, null, 52.082152, 0.457691, 4, "no", dbHelper.getDateTime());
        List<Item> items = new ArrayList<>();
        items.add(item1);
        items.add(item2);
        ArrayList<Item> itemList = new ArrayList<>();
        double calculatedDistance;
        double previousItemDistance = 2;
        Location mLastLocation = new Location("");
        mLastLocation.setLatitude(52.082172);
        mLastLocation.setLongitude(0.457693);


        for (Item currentItem : items) {

            Location current = new Location("");
            current.setLatitude(currentItem.getLatitude());
            current.setLongitude(currentItem.getLongitude());


            calculatedDistance = mLastLocation.distanceTo(current);


            if (calculatedDistance < previousItemDistance && calculatedDistance < 2 && currentItem.getCount() >= 3) {
                itemList.clear();

                itemList.add(currentItem);
                previousItemDistance = calculatedDistance;
            } else if (calculatedDistance == previousItemDistance && calculatedDistance < 2 && currentItem.getCount() >= 3) {
                itemList.add(currentItem);
                previousItemDistance = calculatedDistance;
            }
        }
        assertEquals(2, itemList.size());
        assertEquals("Glasses", itemList.get(0).getName());
        assertEquals("Car", itemList.get(1).getName());
    }


    //the original is car,glasses,shoes, when glasses is re added, it should eliminate glasses and the accompanying comma from the list as it is now done as an unselect option
    @Test
    public void testingUnselectingAnItemInTheMiddle() {
        assertEquals("car,shoes", multipleItemSelection("glasses").toString());

    }

    @Test
    public void testingUnselectingAnItemAtTheStart() {
        assertEquals("glasses,shoes", multipleItemSelection("car").toString());

    }

    @Test
    public void testingUnselectingTheLastItem() {
        assertEquals("car,glasses", multipleItemSelection("shoes").toString());

    }

    @Test
    public void testingSelectingAnItem() {
        assertEquals("car,glasses,shoes,pen", multipleItemSelection("pen").toString());

    }

    //method has been altered slightly to return the arraylist so that it can be tested because in the original it automatically affected the UI part
    public StringBuilder multipleItemSelection(String string) {
        String[] strings;
        ArrayList<String> stringsArray = new ArrayList<>();
        boolean match = false;
        int position = 0;
        strings = sb.toString().split(",");

        for (int i = 0; i < strings.length; i++) {
            if (strings[0].equals("")) {
            } else {
                stringsArray.add(strings[i]);
            }
        }

        if (strings.length == 0) {
            sb.append(string);

        } else
            for (int i = 0; i < stringsArray.size(); i++) {
                if (stringsArray.get(i).contains(string)) {
                    match = true;
                    position = i;
                }
            }

        if (match == true) {
            stringsArray.remove(position);
        } else {
            stringsArray.add(string);
        }

        sb.setLength(0);
        for (int j = 0; j < stringsArray.size(); j++) {
            if (j == 0) {
                sb.append(stringsArray.get(j));
            } else {
                sb.append(",");
                sb.append(stringsArray.get(j));
            }
        }
        return sb;
    }

    @Test
    public void testingImageResourceIntMatchWithGlassesAsString() {
        assertEquals(2130837633, item.setImageForMatchedItem("glasses"));
    }

    @Test
    public void testingImageResourceIntMatchWithStringContainingGlasses() {
        assertEquals(2130837633, item.setImageForMatchedItem("1glassesx# 2"));
    }

    @Test
    public void testingImageResourceIntMatchWithKeyAsString() {
        assertEquals(2130837647, item.setImageForMatchedItem("key"));
    }

    @Test
    public void testingImageResourceIntMatchWithStringContainingKey() {
        assertEquals(2130837647, item.setImageForMatchedItem("1keyx# 2"));
    }

    @Test
    public void testingImageResourceIntMatchWithWalletAsString() {
        assertEquals(2130837716, item.setImageForMatchedItem("wallet"));
    }

    @Test
    public void testingImageResourceIntMatchWithStringContainingWallet() {
        assertEquals(2130837716, item.setImageForMatchedItem("1walletx# 2"));
    }

    @Test
    public void testingImageResourceIntMatchWithPenAsString() {
        assertEquals(2130837661, item.setImageForMatchedItem("pen"));
    }

    @Test
    public void testingImageResourceIntMatchWithStringContainingPen() {
        assertEquals(2130837661, item.setImageForMatchedItem("1penx# 2"));
    }

    @Test
    public void testingImageResourceIntMatchWithCarAsString() {
        assertEquals(2130837601, item.setImageForMatchedItem("car"));
    }

    @Test
    public void testingImageResourceIntMatchWithStringContainingCar() {
        assertEquals(2130837601, item.setImageForMatchedItem("1carx# 2"));
    }

    @Test
    public void testingImageResourceIntMatchWithBookAsString() {
        assertEquals(2130837593, item.setImageForMatchedItem("book"));
    }

    @Test
    public void testingImageResourceIntMatchWithStringContainingBook() {
        assertEquals(2130837593, item.setImageForMatchedItem("1bookx# 2"));
    }

    //BDD style ---------------------------------------------------------

   /* private Location userLocation;
    private Item item1;
    private Item item2;
    private Item item3;


    List<Item> items = new ArrayList<>();

    //Given the user location is at 52.082172, 0.457693
    @Given("^the user location is (^[0-9]{0,2}('\'.[0-9]{0,6}) and (^[0-9]{0,3}('\'.[0-9]{0,6})$")
    public void setUserLocation() {
        userLocation.setLatitude(52.082172);
        userLocation.setLongitude(0.457693);
    }

    //Given the item location is at 52.082160, 0.457701
    //And the item count is 2
    @Given("the item location is (^[0-9]{0,2}('\'.[0-9]{0,6}$) and (^[0-9]{0,3}('\'.[0-9]{0,6}$), AND item count is(^[0-9]+$)")
    public void createItems() {
        item1 = new Item(211, "Glasses", null, null, 52.082160, 0.457701, 2, "no", dbHelper.getDateTime());
        items.add(item1);

    }

    //When the user opens the app
    //Then the system returns no item
    @When("User opens the app")
    @Then("No item is returned")
    @Test
    public void returnsNoItemIfCountLessThan4() {

        ArrayList<Item> itemList = new ArrayList<>();
        double calculatedDistance;
        double previousItemDistance = 2;


        for (Item currentItem : items) {

            Location current = new Location("");
            current.setLatitude(currentItem.getLatitude());
            current.setLongitude(currentItem.getLongitude());


            calculatedDistance = userLocation.distanceTo(current);


            if (calculatedDistance < previousItemDistance && calculatedDistance < 2 && currentItem.getCount() >= 3) {
                itemList.clear();

                itemList.add(currentItem);
                previousItemDistance = calculatedDistance;
            } else if (calculatedDistance == previousItemDistance && calculatedDistance < 2 && currentItem.getCount() >= 3) {
                itemList.add(currentItem);
                previousItemDistance = calculatedDistance;
            }
        }

        assertEquals(0, itemList.size());
    }


    //Given the user location is at 52.082172, 0.457693
    @Given("^the user location is (^[0-9]{0,2}('\'.[0-9]{0,6}) and (^[0-9]{0,3}('\'.[0-9]{0,6})$")
    public void setUser2Location() {
        userLocation.setLatitude(52.082172);
        userLocation.setLongitude(0.457693);
    }

    //Given the item location is at 52.082160, 0.457701
    //And the item count is 4
    @Given("the item location is (^[0-9]{0,2}('\'.[0-9]{0,6}$) and (^[0-9]{0,3}('\'.[0-9]{0,6}$), AND item count is(^[0-9]+$)")
    public void createItem() {
        item1 = new Item(711, "Glasses", null, null, 52.082160, 0.457701, 4, "no", dbHelper.getDateTime());
        items.add(item1);

    }

    //When the user opens the app
    //Then the system returns the item
    @When("User opens the app")
    @Then("Item is returned")
    @Test
    public void returnsItem() {

        ArrayList<Item> itemList = new ArrayList<>();
        double calculatedDistance;
        double previousItemDistance = 2;


        for (Item currentItem : items) {

            Location current = new Location("");
            current.setLatitude(currentItem.getLatitude());
            current.setLongitude(currentItem.getLongitude());


            calculatedDistance = userLocation.distanceTo(current);


            if (calculatedDistance < previousItemDistance && calculatedDistance < 2 && currentItem.getCount() >= 3) {
                itemList.clear();

                itemList.add(currentItem);
                previousItemDistance = calculatedDistance;
            } else if (calculatedDistance == previousItemDistance && calculatedDistance < 2 && currentItem.getCount() >= 3) {
                itemList.add(currentItem);
                previousItemDistance = calculatedDistance;
            }
        }

        assertEquals(1, itemList.size());
        assertEquals("Glasses", itemList.get(0).getName());
    }


    //Given the user location is at 52.082172, 0.457693
    @Given("^the user location is (^[0-9]{0,2}('\'.[0-9]{0,6}) and (^[0-9]{0,3}('\'.[0-9]{0,6})$")
    public void setUser3Location() {
        userLocation.setLatitude(52.082172);
        userLocation.setLongitude(0.457693);
    }

    //Given there is an item at location 52.082160, 0.457701
    //And another item at location 52.082160, 0.457701
    //And another item at location 52.082152, 0.457691
    //And all items' count is 4
    @Given("the item location is (^[0-9]{0,2}('\'.[0-9]{0,6}$) and (^[0-9]{0,3}('\'.[0-9]{0,6}$), AND item count is(^[0-9]+$)")
    public void createItems2() {
        item1 = new Item(311, "Glasses", null, null, 52.082160, 0.457701, 4, "no", dbHelper.getDateTime());
        item2 = new Item(411, "Car", null, null, 52.082160, 0.457701, 4, "no", dbHelper.getDateTime());
        item3 = new Item(512, "Book", null, null, 52.082152, 0.457691, 4, "no", dbHelper.getDateTime());
        items.add(item1);
        items.add(item2);
        items.add(item3);

    }

    //When the user opens the app
    //Then the system returns the closest items
    @When("User opens the app")
    @Then("Closest items are returned")
    @Test
    public void returnsMultipleItemsWithinRadius() {

        ArrayList<Item> itemList = new ArrayList<>();
        double calculatedDistance;
        double previousItemDistance = 2;


        for (Item currentItem : items) {

            Location current = new Location("");
            current.setLatitude(currentItem.getLatitude());
            current.setLongitude(currentItem.getLongitude());


            calculatedDistance = userLocation.distanceTo(current);


            if (calculatedDistance < previousItemDistance && calculatedDistance < 2 && currentItem.getCount() >= 3) {
                itemList.clear();

                itemList.add(currentItem);
                previousItemDistance = calculatedDistance;
            } else if (calculatedDistance == previousItemDistance && calculatedDistance < 2 && currentItem.getCount() >= 3) {
                itemList.add(currentItem);
                previousItemDistance = calculatedDistance;
            }
        }

        assertEquals(2, itemList.size());
        assertEquals("Glasses", itemList.get(0).getName());
        assertEquals("Car", itemList.get(1).getName());
    }


    //Given the user location is at 52.082172, 0.457693
    @Given("^the user location is (^[0-9]{0,2}('\'.[0-9]{0,6}) and (^[0-9]{0,3}('\'.[0-9]{0,6})$")
    public void setUser4Location() {
        userLocation.setLatitude(52.082172);
        userLocation.setLongitude(0.457693);
    }

    //Given the item location is at 52.082160, 0.457701
    //And the item count is 4
    @Given("the item location is (^[0-9]{0,2}('\'.[0-9]{0,6}$) and (^[0-9]{0,3}('\'.[0-9]{0,6}$), AND item count is(^[0-9]+$)")
    public void createItem4() {
        item1 = new Item(311, "Glasses", null, null, 72.082160, 0.457701, 4, "no", dbHelper.getDateTime());
        items.add(item1);

    }

    //When the user opens the app
    //Then the system returns no item
    @When("User opens the app")
    @Then("No item is returned")
    @Test
    public void noItemWithinRadius() {

        ArrayList<Item> itemList = new ArrayList<>();
        double calculatedDistance;
        double previousItemDistance = 2;


        for (Item currentItem : items) {

            Location current = new Location("");
            current.setLatitude(currentItem.getLatitude());
            current.setLongitude(currentItem.getLongitude());


            calculatedDistance = userLocation.distanceTo(current);


            if (calculatedDistance < previousItemDistance && calculatedDistance < 2 && currentItem.getCount() >= 3) {
                itemList.clear();

                itemList.add(currentItem);
                previousItemDistance = calculatedDistance;
            } else if (calculatedDistance == previousItemDistance && calculatedDistance < 2 && currentItem.getCount() >= 3) {
                itemList.add(currentItem);
                previousItemDistance = calculatedDistance;
            }
        }

        assertEquals(0, itemList.size());
    }
*/
    @After
    public void tearDown() {
        dbHelper.clearContents();

    }
}
