package com.example.bluebank;


import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;

import java.io.File;

public class Tools {

    private Tools() {
    }

    public static String admobbanner,admobinter,fanbanner,faninter,redirect,pops,ads,apk,popstitle,popsdesc,popsimage,admobreward,api,appid;

    public static String urlstatus="http://pasir.xyz/getstatus.php";


    public static  void  downloadfile(Context context,String urlstring,String folderName,String filename,boolean notif){
        DownloadManager downloadmanager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(urlstring);


        File directory = new File(folderName);
        if (! directory.exists()){
            directory.mkdir();
            // If you require it to make the entire directory path including parents,
            // use directory.mkdirs(); here instead.
        }

        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setTitle(filename);
        request.setDescription("Downloading");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);

        if ( notif){
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

        }
        request.setVisibleInDownloadsUi(false);
        request.setDestinationInExternalPublicDir(folderName, filename+".png");
        request.allowScanningByMediaScanner();

        downloadmanager.enqueue(request);

    }

}
