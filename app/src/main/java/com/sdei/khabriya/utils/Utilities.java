package com.sdei.khabriya.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.Display;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.sdei.khabriya.R;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;

/**
 * Created by sandeep on 22/12/15.
 */
public class Utilities {


    public static final String BANNER_IMAGE = "http://www.pagalparrot.com/apps/banner.jpg";

    public static boolean isConnectingToInternet(Context context) {
        try {
            ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity != null) {
                NetworkInfo[] info = connectivity.getAllNetworkInfo();
                if (info != null)
                    for (int i = 0; i < info.length; i++)
                        if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                            return true;
                        }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public static String gettingCountryCode(Context context) {
        String cCode = "";
        TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        cCode = manager.getSimCountryIso();
        if (TextUtils.isEmpty(cCode)) {
            cCode = context.getResources().getConfiguration().locale.getCountry();
            if (TextUtils.isEmpty(cCode)) {
                return "nz";
            }
            return cCode;

        } else {
            return cCode;
        }

    }

    public static void openGmailClient(Context activity, String email, String subject) {

        if (email == null || email.equalsIgnoreCase("")) {
            Toast.makeText(activity, "No email found!", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
            intent.putExtra(Intent.EXTRA_SUBJECT, subject);
            intent.setType("message/rfc822");
            intent.setPackage("com.google.android.gm");
            activity.startActivity(intent);
        }
    }

    public static Typeface getThin(Context context) {
        return Typeface.createFromAsset(context.getAssets(), "fonts/Helvetica.otf");
    }

    public static Typeface getRegular(Context context) {
        return Typeface.createFromAsset(context.getAssets(), "Roboto-Regular_1.ttf");
    }

    public static Typeface getMedium(Context context) {
        return Typeface.createFromAsset(context.getAssets(), "fonts/Helvetica.otf");
    }

    public static void hideKeyboard(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    public static String getCurrentDate() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.format(c.getTime());

    }
    public static DisplayImageOptions setDisplayOptions() {
        return new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.drawable.back_place)
                .showImageOnFail(R.drawable.back_place)
                .showImageOnLoading(R.drawable.back_place)
                .resetViewBeforeLoading(true)
                .cacheOnDisk(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .bitmapConfig(Bitmap.Config.ARGB_8888)
                .build();
    }

    /**
     * get Image fromurl
     */

    public static final String getIamgeTagFromHTML(String html){
        String all = html; // shortened it
        String s = "<img src=\"";
        int ix = all.indexOf(s)+s.length();
        System.out.println("Image URL"+all.substring(ix, all.indexOf("\"", ix+1)));
        return all.substring(ix, all.indexOf("\"", ix+1));
    }


    /**
     * return urls
     */
    public static String extractLinks(String text) {
        List<String> links = new ArrayList<String>();
        Matcher m = Patterns.WEB_URL.matcher(text);
        while (m.find()) {
            String url = m.group();
            Log.d("Utilities", "URL extracted: " + url);
            links.add(url);
        }
        for (String url : links) {
            if(url.endsWith(".jpg")||url.contains(".jpg")||url.contains(".jpeg")||url.endsWith(".png")||url.endsWith(".jpeg")){
                Log.d("Image", "URL extracted: " + url);
                return url;
            }
        }

        return "";
    }



    /**
     * this will date in yyyy-MM-dd date format
     *
     * @param year
     * @param monthOfYear
     * @param dayOfMonth
     * @return
     */
    public static String formatDate(int year, int monthOfYear, int dayOfMonth) {
        String month, day;
        if (monthOfYear < 10) {
            month = "0" + (monthOfYear + 1);
        } else {
            month = "" + (monthOfYear + 1);
        }
        if (dayOfMonth < 10) {
            day = "0" + dayOfMonth;
        } else {
            day = "" + dayOfMonth;
        }

        return year + "-" + month + "-" + day;
    }

    public static int getPixelsFromDP(Context context, float dps){
        final float scale = context.getResources().getDisplayMetrics().density;
        return  (int) (dps * scale + 0.5f);
    }


    /**
     * Get the screen width.
     *
     * @param context
     * @return the screen width
     */
    @SuppressWarnings("deprecation")
    @SuppressLint("NewApi")
    public static int getScreenWidth(Activity context) {

        Display display = context.getWindowManager().getDefaultDisplay();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            Point size = new Point();
            display.getSize(size);
            return size.x;
        }
        return display.getWidth();
    }

    public static String requestWebService(String serviceUrl) {
        disableConnectionReuseIfNecessary();

        HttpURLConnection urlConnection = null;
        try {
            // create connection
            URL urlToRequest = new URL(serviceUrl);
            urlConnection = (HttpURLConnection)
                    urlToRequest.openConnection();
//            urlConnection.setConnectTimeout(CONNECTION_TIMEOUT);
//            urlConnection.setReadTimeout(DATARETRIEVAL_TIMEOUT);

            // handle issues
            int statusCode = urlConnection.getResponseCode();
            if (statusCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
                // handle unauthorized (if service requires user login)
            } else if (statusCode != HttpURLConnection.HTTP_OK) {
                // handle any other errors, like 404, 500,..
            }

            // create JSON object from content
            InputStream in = new BufferedInputStream(
                    urlConnection.getInputStream());
            return getResponseText(in);

        } catch (MalformedURLException e) {
            // URL is invalid
        } catch (SocketTimeoutException e) {
            // data retrieval or connection timed out
        } catch (IOException e) {
            // could not read response body
            // (could not create input stream)
        } catch (Exception e) {
            // response body is no valid JSON string
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        return null;
    }

    /**
     * required in order to prevent issues in earlier Android version.
     */
    private static void disableConnectionReuseIfNecessary() {
        // see HttpURLConnection API doc
        if (Integer.parseInt(Build.VERSION.SDK)
                < Build.VERSION_CODES.FROYO) {
            System.setProperty("http.keepAlive", "false");
        }
    }

    private static String getResponseText(InputStream inStream) {
        return new Scanner(inStream).useDelimiter("\\A").next();
    }
    public static final String EXTRA_COVER_IMAGE = "cover_image";
//    public static final String EXTRA_TITLE = "title";
//    public static final String EXTRA_AUTHOR_IMAGE = "author_image";
//    public static final String EXTRA_AUTHOR_NAME = "author_name";
//    public static final String EXTRA_AUTHOR_ID = "author_id";
//    public static final String EXTRA_AUTHOR_EMAIL = "author_email";
//    public static final String EXTRA_AUTHOR_DESC = "author_desc";
//    public static final String EXTRA_CONTENT = "content";
    public static final String EXTRA_LINK = "link";
    public static final String TITLE = "title";
    public static final String CONTENT = "content";
    public static final String DATE = "date";
    public static final String PREFS = "prefs";
    public static final String ENABLE_NOTI = "enable_noti";
    static ProgressDialog mDialog;

    public static void showProgress(String title, Context context) {
        try {
            mDialog = ProgressDialog.show(context, "", title, false, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void dismissProgress() {
        if (mDialog != null) {
            mDialog.dismiss();
            mDialog = null;
        }
    }
}
