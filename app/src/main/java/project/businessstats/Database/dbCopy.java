package project.businessstats.Database;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

// This class deals with copying the SQLite Database from the app assets directory to the working data directory

public class dbCopy {

    private Context context;
    private int flag=0;

    public dbCopy(Context MyContext){
        context = MyContext;
    }

    public dbCopy(Context MyContext, int flag){
        context = MyContext;
        this.flag = flag;
    }

    public void copyFileOrDir(String path) {

        AssetManager assetManager = context.getAssets();
        String assets[] = null;

        try {

            assets = assetManager.list(path);

            if (assets.length == 0) {
                copyFile(path);
            }

            else {

                String fullPath;

                if(flag!=1)
                    fullPath = "/data/data/" + context.getPackageName() + "/" + path;

                else
                    fullPath = Environment.getExternalStorageDirectory() + "/" + path;

                File dir = new File(fullPath);

                if (!dir.exists())
                    dir.mkdir();

                for (int i=0; i<assets.length; ++i) {
                    copyFileOrDir(path + "/" + assets[i]);
                }
            }
        }
         catch (IOException ex) {
            Log.e("tag", "I/O Exception", ex);
        }
    }

    private void copyFile(String filename) {
        AssetManager assetManager = context.getAssets();

        InputStream in = null;
        OutputStream out = null;

        try {
            in = assetManager.open(filename);

            String newFileName;

            if(flag!=1)
                newFileName = "/data/data/" + context.getPackageName() + "/" + filename;

            else
                newFileName = Environment.getExternalStorageDirectory() + "/" + filename;


            out = new FileOutputStream(newFileName);

            byte[] buffer = new byte[1024];
            int read;

            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }

            in.close();
            in = null;
            out.flush();
            out.close();
            out = null;
        }

        catch (Exception e) {
            Log.e("tag", e.getMessage());
        }
    }
}