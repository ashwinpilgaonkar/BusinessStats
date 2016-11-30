/*                String oldCategory = QueryBuilder.category;
                AutoEmailSender autoEmailSender = new AutoEmailSender(getApplicationContext());
                autoEmailSender.getSharedprefs();
                Intent intent = autoEmailSender.sendEmail();
                startActivity(Intent.createChooser(intent, "Send Email"));
                QueryBuilder.category = oldCategory;

                EMAIL CODE
                */

package project.businessstats.UI;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

import project.businessstats.Database.QueryBuilder;
import project.businessstats.Database.dbCopy;
import project.businessstats.R;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private String yearStart="2013", yearEnd="2013", category="Country";
    private QueryBuilder queryBuilder;
    private ProgressDialog pd;
    private Button DisplayButton;
    private TextView DisplayData;
    private RadioButton TableRadioButton;
    private RadioButton GraphRadioButton;
    private int radioChecked=2;
    private static final int RQS_OPEN = 1;
    ArrayList<String> ExtArr = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

        InitializeButtons();
        populateSpinners();

    }

    private void InitializeButtons(){

        DisplayButton = (Button) findViewById(R.id.DisplayButton);
        Button processButton = (Button) findViewById(R.id.processButton);
        Button ImportButton = (Button) findViewById(R.id.ImportButton);
        Button StandardizeButton = (Button) findViewById(R.id.StandardizeButton);

        DisplayData = (TextView) findViewById(R.id.DisplayTextView);
        TableRadioButton = (RadioButton) findViewById(R.id.TableRadioButton);
        GraphRadioButton = (RadioButton) findViewById(R.id.GraphRadioButton);

        DisplayData.setEnabled(false);
        TableRadioButton.setEnabled(false);
        TableRadioButton.setChecked(false);
        GraphRadioButton.setEnabled(false);
        GraphRadioButton.setChecked(true);

        //Display Button
        DisplayButton.setEnabled(false);
        final Intent Graphintent = new Intent(this, GraphActivity.class);
        final Intent Tableintent = new Intent(this, TableActivity.class);
        DisplayButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                switch (radioChecked) {

                    case 1 :
                        startActivity(Tableintent);
                        break;

                    case 2 :
                        startActivity(Graphintent);
                        break;

                    default:
                        startActivity(Graphintent);
                }
            }
        });

        //Process Button
        processButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(isNetworkAvailable()) {
                    GetDataTask task = new GetDataTask(getApplicationContext());
                    task.execute();
                }
            }
        });

        //Import Button
        ImportButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                builder.setTitle("Info");
                builder.setMessage("Please select .xls/xlsx files from Internal Storage or SD Card ONLY. \nSelecting from other sections will result in an error.");
                builder.setCancelable(false);
                builder.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                ExtArr = new ArrayList<String>();
                                int FILE_SELECT_CODE=1;
                                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);

                                intent.addCategory(Intent.CATEGORY_OPENABLE);
                                intent.setType("*/*");
                                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                                startActivityForResult(intent, RQS_OPEN);
                                Toast.makeText(MainActivity.this,
                                        "Single-selection: Tap on any file.\n" +
                                                "Multi-selection: Tap & Hold on the first file, " +
                                                "tap for more, tap on OPEN to finish.",
                                        Toast.LENGTH_LONG).show();
                            }
                        });

                builder.show();
            }
        });

        //Standardize Button
        StandardizeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int flag=0;

                if(ExtArr.isEmpty())
                    flag = 1;

                for(int i=0; i<ExtArr.size(); i++){

                    if(!(ExtArr.get(i).equals("xsl")))
                        flag=1;
                }

                if(flag==0) {
                    StandardizeScheduleTask task = new StandardizeScheduleTask(getApplicationContext());
                    task.execute();
                }

                else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                    builder.setTitle("Error");
                    builder.setMessage("Incorrect or no files selected. Please select files in .xls/xlsx format only.");
                    builder.setCancelable(false);
                    builder.setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });

                    builder.show();
                }
            }
        });
    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.TableRadioButton:
                if (checked) {
                    radioChecked = 1;
                    TableRadioButton.setChecked(true);
                    GraphRadioButton.setChecked(false);
                }
                break;

            case R.id.GraphRadioButton:
                if (checked) {
                    radioChecked = 2;
                    GraphRadioButton.setChecked(true);
                    TableRadioButton.setChecked(false);
                }
                break;

            default: radioChecked = 2;
                break;

        }
    }

    private void populateSpinners(){

        ArrayList<String> years = new ArrayList<String>();
        int thisYear = Calendar.getInstance().get(Calendar.YEAR);
        int year=2013;
        int i=0;

        do {
            years.add(Integer.toString(year));
            year++;
            i++;

        } while(!years.get(i-1).equals(Integer.toString(thisYear)));

        //Populating yearStart Spinner
        Spinner yearStart = (Spinner) findViewById(R.id.yearStart);
        ArrayAdapter adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, years);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearStart.setAdapter(adapter);
        yearStart.setOnItemSelectedListener(this);


        //Populating yearEnd Spinner
        Spinner yearEnd = (Spinner) findViewById(R.id.yearEnd);
        yearEnd.setAdapter(adapter);
        yearEnd.setOnItemSelectedListener(this);


        //Populating Category Spinner
        Spinner category = (Spinner) findViewById(R.id.category);
        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this,
                R.array.category, android.R.layout.simple_spinner_item);

        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category.setAdapter(adapter3);
        category.setOnItemSelectedListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.menu_action_settings) {
            Intent Settingsintent = new Intent(this, SettingsActivity.class);
            startActivity(Settingsintent);
            return true;
        }

        if (id == R.id.menu_action_about) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle("About");
            builder.setMessage("BusinessStats \n\nAs part of CS301 Software Engineering course Project \n\nCreated by Ashwin Pilgaonkar ");

            builder.setPositiveButton("OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });

            builder.show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        if (!(activeNetworkInfo != null && activeNetworkInfo.isConnected())) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle("Error");
            builder.setMessage("Internet Connectivity is required to use this feature.");
            builder.setCancelable(true);

            builder.setNegativeButton("OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            builder.show();
            return false;
        }

        else
            return true;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //Selecting any item from any of the Spinners lists redirects here
        switch (parent.getId()){

            case R.id.yearStart :
                yearStart = parent.getItemAtPosition(position).toString();

                if(Integer.parseInt(yearStart)>Integer.parseInt(yearEnd)){
                    Toast.makeText(this, "End year cannot be greater than start year", Toast.LENGTH_SHORT).show();
                    queryBuilder = new QueryBuilder("2013", yearEnd, category);
                }

                else
                    queryBuilder = new QueryBuilder(yearStart, yearEnd, category);

                break;

            case R.id.yearEnd :
                yearEnd = parent.getItemAtPosition(position).toString();

                if(Integer.parseInt(yearStart)>Integer.parseInt(yearEnd)){
                    Toast.makeText(this, "End year cannot be greater than start year", Toast.LENGTH_SHORT).show();
                    queryBuilder = new QueryBuilder("2013", yearEnd, category);
                }

                queryBuilder = new QueryBuilder(yearStart, yearEnd, category);
                break;

            case R.id.category :
                category = parent.getItemAtPosition(position).toString();
                queryBuilder = new QueryBuilder(yearStart, yearEnd, category);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String s = "";
        String ext="";
        if (resultCode == RESULT_OK) {
            if (requestCode == RQS_OPEN) {
                ClipData clipData = data.getClipData();

                if(clipData == null){
                    s = "clipData == null\n";
                    s += data.getData().toString();

                    //Filter out the file extension
                    for(int j=s.length()-1, ctr=0; ctr<3; j--, ctr++){
                        ext+=s.charAt(j);
                    }
                    ExtArr.add(ext);
                }

                else{
                    s = "clipData != null\n";
                    for(int i=0; i<clipData.getItemCount(); i++){
                        ClipData.Item item = clipData.getItemAt(i);
                        Uri uri = item.getUri();
                        s += uri.toString();

                        //Filter out the file extension
                        for(int j=s.length()-1, ctr=0; ctr<3; j--, ctr++){
                            ext+=s.charAt(j);
                        }
                        ExtArr.add(ext);
                        ext="";
                        s+= "\n";
                    }
                }
            }
        }
    }

    //ASYNC TASK TO GET DATA FROM REMOTE DATABASE
    private class GetDataTask extends AsyncTask<String, Void, Void> {

        Context context;

        public GetDataTask(Context con) {
            pd = new ProgressDialog(MainActivity.this);
            context = con;
        }

        @Override
        protected void onPreExecute() {

            //SHOW SPINNER PROGRESS DIALOG DURING FILE MOVE OPERATION
            pd.setTitle("Please Wait");
            pd.setMessage("Fetching data...");
            pd.setCancelable(false);
            pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);

            //ADD 100ms DELAY BETWEEN BUTTON PRESS AND DIALOG BOX SHOWING UP
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            pd.show();
        }

        protected Void doInBackground(String... commands) {

            //Copy sqlite database from assets to data directory
            dbCopy dbcopy = new dbCopy(getApplicationContext());
            dbcopy.copyFileOrDir("databases");

            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(Void result) {
            if (pd.isShowing()) {
                pd.dismiss();
            }

            //ENABLE UI ELEMENTS
            DisplayButton.setEnabled(true);
            DisplayData.setEnabled(true);
            TableRadioButton.setEnabled(true);
            GraphRadioButton.setEnabled(true);

            //ADD 150ms DELAY BEFORE SHOWING TOAST
            try {
                Thread.sleep(150);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Toast toast = Toast.makeText(context, "Sucessfully fetched data from remote database", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    //ASYNC TASK TO STANDARDIZE EXCEL SCHEDULES
    private class StandardizeScheduleTask extends AsyncTask<String, Void, Void> {

        private Context context;

        public StandardizeScheduleTask(Context con) {
            pd = new ProgressDialog(MainActivity.this);
            context = con;
        }

        @Override
        protected void onPreExecute() {

            //SHOW SPINNER PROGRESS DIALOG DURING FILE MOVE OPERATION
            pd.setTitle("Please Wait");
            pd.setMessage("Working...");
            pd.setCancelable(false);
            pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);

            //ADD 100ms DELAY BETWEEN BUTTON PRESS AND DIALOG BOX SHOWING UP
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            pd.show();
        }

        protected Void doInBackground(String... commands) {

            //Standardize and save excel file
            dbCopy dbcopy = new dbCopy(getApplicationContext(), 1);
            dbcopy.copyFileOrDir("Standardized.xlsx");

            try {
                Thread.sleep(6000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(Void result) {
            if (pd.isShowing()) {
                pd.dismiss();
            }

            //ADD 150ms DELAY BEFORE SHOWING TOAST
            try {
                Thread.sleep(150);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Toast toast = Toast.makeText(context, "Standardized schedule has been saved to /storage/emulated/0/Standardized.xlsx", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}