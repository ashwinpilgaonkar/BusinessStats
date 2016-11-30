package project.businessstats.AutoEmail;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import project.businessstats.Database.Count;
import project.businessstats.Database.SQLiteHelper;

/**
 * This class will be used to generate an email on 1st January of every year.
 * The email will automatically be sent to the Travel Agent with maximum number of bookings.
 */

public class AutoEmailSender {

    private  Context context;
    private int bookings=0, year=0;
    private String agent;
    private List<String> bookingYear;
    private List<String> Category;
    private List<String> CountRec;

    public AutoEmailSender(Context mcontext){
        context = mcontext;
        bookingYear = new ArrayList<String>();
        Category = new ArrayList<String>();
        CountRec = new ArrayList<String>();
    }

    public void getSharedprefs(){
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        boolean reminder = sharedPref.getBoolean("reminderCheckbox", true);
        boolean emailSwitch = sharedPref.getBoolean("EmailSwitch", false);
    }

    private void getTourAgent(){
        SQLiteHelper db = new SQLiteHelper(context, "Agent");

        List<Count> count = db.getCount();

        for (Count count1 : count) {
            bookingYear.add(count1.getYear());
            Category.add(count1.getCategory());
            CountRec.add(count1.getCountrec());
        }

        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);

        for(int i=0; i<bookingYear.size(); i++){
            if(Integer.parseInt(CountRec.get(i)) > bookings && bookingYear.get(i).equals(Integer.toString(year-1)))
                bookings = Integer.parseInt(CountRec.get(i));
                agent = Category.get(i);
        }
    }

    public Intent sendEmail(){

        getTourAgent();

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/html");
        intent.putExtra(Intent.EXTRA_EMAIL, "emailaddress@emailaddress.com");
        intent.putExtra(Intent.EXTRA_SUBJECT, "Congratulations!");
        intent.putExtra(Intent.EXTRA_TEXT, "Hello, " + agent + "! \nWe from Odyssey Tours & Travels congratulate you for having "
                + Integer.toString(bookings) + " bookings under you for the year " + Integer.toString(year-1) + ", which is the highest in our records."
        + "\n\nRegards, \nOdyssey Tours & Travels.");

        return intent;
    }
}
