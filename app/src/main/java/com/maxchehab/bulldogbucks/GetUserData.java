package com.maxchehab.bulldogbucks;

import android.os.AsyncTask;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.webkit.ConsoleMessage.MessageLevel.LOG;

/**
 * Created by maxchehab on 8/28/17.
 */

public class GetUserData extends AsyncTask<Credential, Void, Boolean> {

    private static String TAG = "GetUserData";
    private static String ssid;
    private OnUserDataListener onUserDataListener;

    public GetUserData(OnUserDataListener onUserDataListener){
        this.onUserDataListener = onUserDataListener;
    }

    protected Boolean doInBackground(Credential... credentials){
        Credential credential = credentials[0];
        ssid = getSSID(credential);

        if(ssid != null){
            UserData userData = getUserData();
            logout(ssid);
            onUserDataListener.onSuccess(userData);
            return true;
        }
        onUserDataListener.onFailure("SSID is null");
        return false;
    }

    private UserData getUserData(){
        if(ssid == null){
            return null;
        }

        UserData userData = new UserData();
        try{
            URL url = new URL("https://zagweb.gonzaga.edu/pls/gonz/hwgwcard.transactions");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Cookie", "SESSID=" + ssid);

            //Get new ssid
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
                        ssid = fields[1];
                    }
                }
            }


            BufferedReader reader = new BufferedReader(new InputStreamReader((connection.getInputStream())));
            StringBuilder builder = new StringBuilder();
            String aux = "";
            while ((aux = reader.readLine()) != null) {
                builder.append(aux);
            }

            String html = builder.toString();

            /*int index = 0;
            while (index < html.length()) {
                Log.d(TAG,html.substring(index, Math.min(index + 1000,html.length())));
                index += 1000;
            }*/


            Document doc = Jsoup.parse(html);

            Elements tableRows = doc.select("tr");
            for(Element tableRow : tableRows){
                if(tableRow.html().contains("Semester Remaining")){
                    String mealSwipes = tableRow.select("td").get(1).html();
                    userData.setSwipes(mealSwipes);
                }
                if(tableRow.html().contains("Meal Plan</td>")){
                    Log.d(TAG, tableRow.html());
                    String swipeType = tableRow.select("td").get(1).html();
                    userData.setSwipeType(swipeType);
                }
            }
            Log.d(TAG, userData.getSwipeType() + " : " + userData.getSwipes());



            Elements tables = doc.select(".plaintable");

            for (Element table : tables) {
                if(table.html().contains("Transaction Date")){
                    Elements rows = table.select("tr");
                    rows.remove(0);
                    rows.remove(rows.size() - 1);
                    for (Element row : rows) {
                        Elements properties = row.select(".pllabel");

                        Transaction transaction = new Transaction(
                                                                    properties.get(1).html(),
                                                                    Double.parseDouble(properties.get(3).html().replaceAll("[^\\d.]", "")),
                                                                    properties.get(6).html()
                                                                );

                        userData.addTransactions(transaction);
                    }
                }
            }




            String re1=".*?";
            String re2="(\\$[0-9]+(?:\\.[0-9][0-9])?)(?![\\d])";

            Pattern p = Pattern.compile(re1+re2,Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
            Matcher m = p.matcher(html);
            if (m.find()){
                userData.setBalance(Double.parseDouble(m.group(1).replace("$", "")));
                Log.d(TAG, "balance : " + userData.getBalance());
            }
            userData.setFrozen(html.contains("Unfreeze my card now"));
            Log.d(TAG, "frozen : " + userData.getFrozen());


        }catch(MalformedURLException ex){
            Log.d(TAG, ex.getMessage());
        }catch(IOException ex){
            Log.d(TAG, ex.getMessage());
        }


        return userData;
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
