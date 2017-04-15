package io.cordova.lysedebiyat.Helpers;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.widget.Toast;

import io.cordova.lysedebiyat.R;

public class NavigationHelper {

    private static final String APP_ID = "io.cordova.lysedebiyat";

    private Context context;

    public NavigationHelper(Context c) {
        context = c;
    }

    public void sendMail() {

        String deviceInfo = "Sistem bilgilerimi de buraya bırakıyorum:\n";
        deviceInfo += "App Version: " + context.getResources().getString(R.string.about_us_version_number) + "\n";
        deviceInfo += "DB Version: " + context.getResources().getString(R.string.application_database_version) + "\n";
        deviceInfo += "OS: " + System.getProperty("os.version") + "\n";
        deviceInfo += "SDK Version: " + Build.VERSION.SDK_INT + "\n";
        deviceInfo += "Device: " + Build.DEVICE + "\n";
        deviceInfo += "Model: " + Build.MODEL + "\n";
        deviceInfo += "Product: " + Build.PRODUCT + "\n";

        String mailContent = "Merhaba. Oyuna dair şöyle bir önerim var:\n\n\n\n";
        mailContent += deviceInfo;

        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL, new String[]{"burak.karakan@gmail.com", "frknhatipoglu@gmail.com"});
        i.putExtra(Intent.EXTRA_SUBJECT, "LYS Edebiyat Kitap Uygulaması - İletişim");
        i.putExtra(Intent.EXTRA_TEXT, mailContent);
        try {
            context.startActivity(Intent.createChooser(i, "Mail Gönder"));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(context, "Yüklenmiş bir e-mail uygulaması bulunamadı.", Toast.LENGTH_SHORT).show();
        }
    }

    public void navigateToStore() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("market://details?id=" + APP_ID));
        if (!storeNavigationSucceeded(intent)) {
            //Market (Google play) app seems not installed, let's try to open a webbrowser.
            intent.setData(Uri.parse("https://play.google.com/store/apps/details?" + APP_ID));
            if (!storeNavigationSucceeded(intent)) {
                Toast.makeText(context, R.string.play_store_not_opened, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean storeNavigationSucceeded(Intent aIntent) {
        try {
            context.startActivity(aIntent);
            return true;
        } catch (ActivityNotFoundException e) {
            return false;
        }
    }

    public void navigateToActivity(Class className) {
        Intent intent = new Intent(context, className);
        context.startActivity(intent);
    }

    public void shareIt() {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, R.string.app_name);
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, context.getString(R.string.share_text));
        context.startActivity(Intent.createChooser(sharingIntent, context.getString(R.string.share_dialog_title)));
    }

}
