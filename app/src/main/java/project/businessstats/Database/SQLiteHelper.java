package project.businessstats.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/*
 * This class deals with reading/writing values to the local SQLite Database
 */

public class SQLiteHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "tour.sqlite";
    private String Agent="";
    
    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public SQLiteHelper(Context context, String Agent) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.Agent = Agent;
    }
    
    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public List<Count> getCount() {
        List<Count> BookingCount = new ArrayList<Count>();

        QueryBuilder queryBuilder;

        if(Agent.equals("Agent"))
            queryBuilder = new QueryBuilder(Agent);

        else
            queryBuilder = new QueryBuilder();

        String selectQuery = queryBuilder.buildQuery();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Count count = new Count(cursor.getString(0), cursor.getString(1), cursor.getString(2));
                count.setYear(cursor.getString(0));
                count.setCategory(cursor.getString(1));
                count.setCountrec(cursor.getString(2));

                // Adding contact to list
                BookingCount.add(count);
            } while (cursor.moveToNext());
        }
        return BookingCount;
    }
}