package ua.hanasaka.tasksmilemolodykh;

import android.content.Context;
import android.database.Cursor;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import ua.hanasaka.tasksmilemolodykh.database.DB;

import static org.junit.Assert.*;

/**
 * Instrumentation test of some function DB class
 *
 */
@RunWith(AndroidJUnit4.class)
public class DBInstrumentedTest {
    private final Context appContext = InstrumentationRegistry.getTargetContext();

    /**
     * for checking if DB instance correctly created
     *
     * @throws Exception
     */
    @Test
    public void isCreateingDB() throws Exception {
        DB db = DB.getInstance(appContext);
        assertTrue(db!=null);
    }

    /**
     * for checking if db contains 3 users
     *
     * @throws Exception
     */
    @Test
    public void isGettingDataFromDB() throws Exception {
        DB db = DB.getInstance(appContext);
        db.open();
        Cursor c = db.getAllUsersData();
        assertTrue(c.getCount()==3);
    }
}
