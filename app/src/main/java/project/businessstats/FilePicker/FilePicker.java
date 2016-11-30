package project.businessstats.FilePicker;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import com.nononsenseapps.filepicker.FilePickerActivity;

public class FilePicker {

    //A different StartActivityForResult will have a different requestCode (FILE_CODE in this case) number in order to identify which onActivityResult to goto.
    static final int FILE_CODE = 1;

    String[] SelectedFiles = new String[20];
    public static Context context;

    public FilePicker(Context con) {
        context = con;
    }

    public Intent FilePickerIntent() {

        Intent i = new Intent(context, FilePickerActivity.class);

        i.putExtra(FilePickerActivity.EXTRA_ALLOW_MULTIPLE, true);
        i.putExtra(FilePickerActivity.EXTRA_ALLOW_CREATE_DIR, false);
        i.putExtra(FilePickerActivity.EXTRA_MODE, FilePickerActivity.MODE_FILE);

        // Configure initial directory by specifying a String.
        // You could specify a String like "/storage/emulated/0/", but that can
        // dangerous. Always use Android's API calls to get paths to the SD-card or
        // internal memory.
        i.putExtra(FilePickerActivity.EXTRA_START_PATH, Environment.getExternalStorageDirectory().getPath());
        return i;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == FILE_CODE && resultCode == Activity.RESULT_OK) {

            //returns false if FilePickerActivity.EXTRA_ALLOW_MULTIPLE is false
            //SELECT MULTIPLE FILES
            if (data.getBooleanExtra(FilePickerActivity.EXTRA_ALLOW_MULTIPLE, false)) {

                //Selected files are copied to clipboard. Line below gets items from clipboard.
                ClipData clip = data.getClipData();

                if (clip != null) {
                    for (int i=0; i<clip.getItemCount(); i++) {
                        Uri uri = clip.getItemAt(i).getUri();

                        SelectedFiles[i] = uri.toString();
                        Log.i("ASD", SelectedFiles[0]);
                    }
                }

                else
                    SelectedFiles = null;
            }
        }
    }
}
