package ba.ito.assistance.util;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import timber.log.Timber;

import static android.content.Context.MODE_APPEND;

public class FileLoggingTree extends Timber.DebugTree {
    private static final String TAG = FileLoggingTree.class.getSimpleName();
    private static final String FOLDER_NAME = "/logs/";
    private static final String FILE_NAME = "app_logs.txt";

    public FileLoggingTree() {
    }


    public void log(String text,String tag) throws Exception {
        FileOutputStream fOut=null;
        OutputStreamWriter myOutWriter=null;

        try {
            String logTimeStamp = new SimpleDateFormat("E MMM dd yyyy 'at' hh:mm:ss:SSS aaa", Locale.getDefault()).format(new Date());
            String dir = Environment.getExternalStorageDirectory() + File.separator + "auto_crash_detect"+tag;
            File folder = new File(dir);
            if(!folder.exists()) {
                boolean mkdirs = folder.mkdirs();
            }
//            String fileNameTimeStamp = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
//            String fileName = fileNameTimeStamp + ".html";

            File folderpath = new File(folder + File.separator + "auto_crash_detect"+tag+".html");
            if(!folderpath.exists()) {
                boolean newFile = folderpath.createNewFile();
            }

            fOut = new FileOutputStream(folderpath,true);
            myOutWriter = new OutputStreamWriter(fOut);

            String t = "<p style=\"background:lightgray;\"><strong style=\"background:lightblue;\">&nbsp&nbsp" + logTimeStamp + " :&nbsp&nbsp</strong>&nbsp&nbsp" + text + "</p>";
            myOutWriter.append(t);
            myOutWriter.close();
            fOut.close();
        }catch (Exception e){
            if (fOut != null) {
                fOut.close();
            }
            if (myOutWriter != null) {
                myOutWriter.close();
            }
        }
    }


    private boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    private boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
    }

    @Override
    protected void log(int priority, String tag, String message, Throwable t) {
        if(!tag.contains("DetectedActivitiesIntentService") && !tag.contains("HomeActivity"))
            return;

        try {
            log(message,tag);
        } catch (Exception e) {
            Log.e(TAG, "Error while logging into file : " + e);
        }

    }
}
