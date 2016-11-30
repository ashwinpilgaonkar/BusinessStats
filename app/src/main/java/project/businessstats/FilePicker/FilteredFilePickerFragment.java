package project.businessstats.FilePicker;

/**
 *  This class is used to filter files of a certain extension in the FilePicker Activity.
 *  In this case, it will filter out all .xls (Spreadsheet) files
 */

import android.support.annotation.NonNull;
import com.nononsenseapps.filepicker.FilePickerFragment;
import java.io.File;

public class FilteredFilePickerFragment extends FilePickerFragment {

    // File extension to filter on
    private static final String EXTENSION = ".xls";

    //return The file extension. If file has no extension, it returns null.
    private String getExtension(@NonNull File file) {
        String path = file.getPath();
        int i = path.lastIndexOf(".");
        if (i < 0)
            return null;
        else
            return path.substring(i);
    }

    @Override
    protected boolean isItemVisible(final File file) {
        boolean ret = super.isItemVisible(file);
        if (ret && !isDir(file) && (mode == MODE_FILE || mode == MODE_FILE_AND_DIR)) {
            String ext = getExtension(file);
            return ext != null && EXTENSION.equalsIgnoreCase(ext);
        }
        return ret;
    }
}