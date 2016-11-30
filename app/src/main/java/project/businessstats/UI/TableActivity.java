package project.businessstats.UI;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;
import project.businessstats.Database.Count;
import project.businessstats.Database.QueryBuilder;
import project.businessstats.Database.SQLiteHelper;
import project.businessstats.R;

public class TableActivity extends AppCompatActivity {

    private List<String> bookingYear;
    private List<String> Category;
    private List<String> CountRec;

    public TableActivity(){
        bookingYear = new ArrayList<String>();
        Category = new ArrayList<String>();
        CountRec = new ArrayList<String>();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        getDatafromDB();
        showTable();

    }

    private void getDatafromDB(){
        SQLiteHelper db = new SQLiteHelper(this);
        List<Count> count = db.getCount();

        for (Count count1 : count) {
            String log = "BookingYear: " + count1.getYear() + ", Country: " + count1.getCategory() + ", CountRec: " + count1.getCountrec();
            Log.d("Items: : ", log);

            bookingYear.add(count1.getYear());
            Category.add(count1.getCategory());
            CountRec.add(count1.getCountrec());
        }
    }

    private void showTable(){
        TableView<String[]> tableView = (TableView<String[]>) findViewById(R.id.tableView);

        String[][] data = new String[50][50];
        String[] headers = new String[4];

        headers[0] = "Booking Year";
        headers[1] = QueryBuilder.category;
        headers[2] = "No. of Bookings";

        for(int i=0; i<bookingYear.size(); i++) {

            String[] row = new String[4];
            row[0] = bookingYear.get(i);
            row[1] = Category.get(i);
            row[2] = CountRec.get(i);

            data[i] = row;
        }
        tableView.setColumnCount(3);
        tableView.setDataAdapter(new SimpleTableDataAdapter(this, data));
        tableView.setHeaderAdapter(new SimpleTableHeaderAdapter(this, headers));
        //tableView.setHeaderBackground(R.drawable.linear_gradient);
        tableView.setHeaderElevation(9);
    }
}
