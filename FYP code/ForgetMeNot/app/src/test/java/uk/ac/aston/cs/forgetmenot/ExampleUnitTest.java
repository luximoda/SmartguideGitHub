package uk.ac.aston.cs.forgetmenot;

import android.content.Context;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import uk.ac.aston.cs.forgetmenot.Model.DatabaseHelper;

import static org.junit.Assert.assertEquals;


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    DatabaseHelper dbHelper;
    Context context;

    @Before
    public void setUp(){

       // dbHelper = new DatabaseHelper(get);
    }


    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void addingItemToDatabase() throws Exception {

        dbHelper.addItem("Glasses", null, null, 52.082172,
                0.457693, 0, "no", dbHelper.getDateTime());
        assertEquals(1,dbHelper.getDatabaseSize());
    }
    //add item
    //retrieve item
    /**
     * Release all the resources used
     */
    @After
    public void tearDown(){

    }
}