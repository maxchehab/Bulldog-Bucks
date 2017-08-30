package com.maxchehab.bulldogbucks;

import android.os.AsyncTask;
import android.util.Log;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
/**
 * Created by maxchehab on 8/28/17.
 */

public class CheckLogin extends AsyncTask<Credential, Void, Boolean> {

    private static String TAG = "CheckLogin";
    private OnLoginListener httpListener;

    public CheckLogin(OnLoginListener httpListener){
        this.httpListener = httpListener;
    }

    protected Boolean doInBackground(Credential... credentials){
        Credential credential = credentials[0];
        String ssid = getSSID(credential);

        Log.d(TAG,ssid);

        if(ssid != null){
            logout(ssid);
            httpListener.onSuccess();
            return true;
        }
        httpListener.onFailure("SSID is null");
        return false;
    }

    private void logout(String ssid){
        try{
            URL url = new URL("https://zagweb.gonzaga.edu/pls/gonz/twbkwbis.P_Logout");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Cookie", "SESSID=" + ssid);

            if(connection.getResponseCode() >= HttpURLConnection.HTTP_BAD_REQUEST){
                Log.d(TAG, "Logout Error!");
            }
        } catch(MalformedURLException ex){
            Log.d(TAG, ex.getMessage());
        } catch(IOException ex){
            Log.d(TAG, ex.getMessage());
        }
    }

    private String getSSID(Credential credential){
        String postData = "sid=" + credential.getUserId() + "&PIN=" + credential.getPassword();
        try {
            URL url = new URL("https://zagweb.gonzaga.edu/pls/gonz/twbkwbis.P_ValLogin");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty( "Content-type", "application/x-www-form-urlencoded");
            connection.setRequestProperty( "Pragma", "no-cache");
            connection.setRequestProperty( "Origin", "https://zagweb.gonzaga.edu");
            connection.setRequestProperty( "Accept-Encoding", "gzip, deflate, br");
            connection.setRequestProperty( "Accept-Language", "en-US,en;q=0.8");
            connection.setRequestProperty( "Upgrade-Insecure-Requests", "1");
            connection.setRequestProperty( "User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.86 Safari/537.36");
            connection.setRequestProperty( "Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
            connection.setRequestProperty( "Cache-Control", "no-cache");
            connection.setRequestProperty( "Referer", "https://zagweb.gonzaga.edu/pls/gonz/twbkwbis.P_WWWLogin");
            connection.setRequestProperty( "Cookie", "TESTID=set; accessibility=false; _ga=GA1.2.125809831.1497317199; _gid=GA1.2.926823714.1497978173; __utmt=1; __utma=247906316.125809831.1497317199.1497977790.1498030046.2; __utmb=247906316.9.10.1498030046; __utmc=247906316; __utmz=247906316.1498030046.2.2.utmcsr=gonzaga.edu|utmccn=(referral)|utmcmd=referral|utmcct=/Campus-Resources/Offices-and-Services-A-Z/Student-Financial-Services/Student-Accounts/Zag-Card/bulldog-bucks-form.asp");
            connection.setRequestProperty( "Connection", "keep-alive");
            connection.setRequestProperty( "charset", "utf-8");
            connection.setUseCaches(false);
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setConnectTimeout(5000);

            OutputStream os = connection.getOutputStream();
            os.write(postData.getBytes("UTF-8"));
            os.close();


            for (int i = 0;; i++) {
                String headerName = connection.getHeaderFieldKey(i);
                String headerValue = connection.getHeaderField(i);

                if (headerName == null && headerValue == null) {
                    break;
                }
                if ("Set-Cookie".equalsIgnoreCase(headerName)) {
                    String[] fields = headerValue.split("=");
                    Log.d(TAG, "full header: " + headerValue);
                    if(fields[0].equals("SESSID")){
                        return fields[1];
                    }
                }
            }

        }catch(MalformedURLException ex){
            Log.d(TAG, ex.getMessage());
        } catch(IOException ex){
            Log.d(TAG, ex.getMessage());
        }
        return null;
    }

    protected void onPostExecute(Boolean result) {
        Log.d(TAG, result.toString());
    }

}
