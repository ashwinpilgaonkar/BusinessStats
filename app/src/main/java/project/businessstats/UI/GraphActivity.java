package project.businessstats.UI;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import project.businessstats.Database.Count;
import project.businessstats.Database.QueryBuilder;
import project.businessstats.Database.SQLiteHelper;
import project.businessstats.R;

public class GraphActivity extends AppCompatActivity {

   private List<String> bookingYear;
   private List<String> Category;
   private List<String> CountRec;

    public GraphActivity(){
        bookingYear = new ArrayList<String>();
        Category = new ArrayList<String>();
        CountRec = new ArrayList<String>();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        getDatafromDB();
        showGraph();
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

    private void showGraph(){
        BarChart chart = (BarChart) findViewById(R.id.chart);

        BarData data = new BarData(getXAxisValues(), getDataSet());
        chart.setDescription(QueryBuilder.category);

        chart.setData(data);
        chart.animateXY(2000, 2000);
        chart.invalidate();
    }

    private int getYear(String year){
        int ctr=0;

        switch(Integer.parseInt(year)){

            case 2013 : ctr=0;
                        break;

            case 2014 : ctr=1;
                        break;

            case 2015 : ctr=2;
                        break;

            case 2016 : ctr=3;
                        break;
        }
        return ctr;
    }

    private void setColor(BarDataSet barDataSet){

        //Ra
        Random rnd = new Random();
        int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        barDataSet.setColor(color);
    }

    private ArrayList<BarDataSet> getDataSet() {
        ArrayList<BarDataSet> dataSets = null;
        dataSets = new ArrayList<>();
        int i=0, pos=0, flag=0;

        for(int j=0; j<bookingYear.size(); j++){
                ArrayList<BarEntry> valueSet = new ArrayList<>();

                for (i = pos; Category.get(pos).equals(Category.get(i));) {
                    Log.i(Category.get(i), CountRec.get(i));
                    BarEntry v1e1 = new BarEntry(Integer.parseInt(CountRec.get(i)), getYear(bookingYear.get(i)));
                    valueSet.add(v1e1);
                    i++;

                    if(i>=bookingYear.size()) {
                        Log.i("i: ", Integer.toString(i));
                        flag=1;
                        break;
                    }
                }
                pos = i;

            BarDataSet barDataSet = new BarDataSet(valueSet, Category.get(i-1));
            dataSets.add(barDataSet);
            setColor(barDataSet);

            if(flag==1)
                break;
        }
        return dataSets;
    }

    private ArrayList<String> getXAxisValues() {
        ArrayList<String> xAxis = new ArrayList<>();

        String startYear = bookingYear.get(0);
        String endYear="";

        for(int i=0; i<bookingYear.size(); i++){
            endYear = bookingYear.get(i);
        }

        for(int i = Integer.parseInt(startYear); i <= Integer.parseInt(endYear); i++){
            xAxis.add(Integer.toString(i));
        }
        return xAxis;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home)
            finish();

        return super.onOptionsItemSelected(item);
    }
}
