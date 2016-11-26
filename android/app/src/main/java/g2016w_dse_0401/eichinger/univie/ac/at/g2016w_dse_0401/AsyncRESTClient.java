package g2016w_dse_0401.eichinger.univie.ac.at.g2016w_dse_0401;

import android.os.AsyncTask;
import android.util.JsonReader;
import android.util.Log;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import g2016w_dse_0401.eichinger.univie.ac.at.g2016w_dse_0401.model.AlarmItem;

import static java.lang.System.in;

/**
 * Created by penderiko on 25.11.16.
 */

public class AsyncRESTClient {

    public static abstract class ResultHandler {
        public abstract void onResult(String result, Integer statusCode);
        public void onIOException(IOException e) {};
    };

    public interface StatusHandler {
        public void handleStatus(int statusCode);
    }

    public class Result {
        public String response;
        public Integer statusCode;
    }

    public static final String GET = "GET";
    public static final String POST = "POST";
    public static final String DELETE = "DELETE";

    public String urlBase;

    public AsyncRESTClient(String urlBase) {
        this.urlBase = "http://" + urlBase;
    }

    public void request(final String url, final String method, final String msg, final ResultHandler handler) {

        new AsyncTask<Void,Void,Result>()  {

            @Override
            protected Result doInBackground(Void... x) {
                String fullUrl = urlBase + url;
                StringBuffer resbuf = new StringBuffer();
                Result result = new Result();

                result.statusCode=-999;

                try {
                    HttpURLConnection connection = (HttpURLConnection) new URL(fullUrl).openConnection();

                    connection.setRequestMethod(method);

                    if (msg != null && !msg.isEmpty()) {
                        connection.setDoOutput(true);
                        OutputStream os = connection.getOutputStream();
                        OutputStreamWriter ow = new OutputStreamWriter(os);
                        ow.write(msg);
                        ow.flush();
                        ow.close();

                        os.close();
                    }

                    try {
                        InputStream in = connection.getInputStream();
                        BufferedReader br = new BufferedReader(new InputStreamReader(in));
                        String line;
                        while ((line = br.readLine()) != null) {
                            resbuf.append(line);
                        }
                    } catch (IOException e) {

                    }

                    result.statusCode = connection.getResponseCode();
                    connection.disconnect();

                } catch (MalformedURLException e) {
                    Log.e("AsyncRESTClient::get", "Malformed URL: " + fullUrl);
                } catch (IOException e) {
                    Log.e("AsyncRESTClient::get", e.getMessage());
                    if (handler!=null) handler.onIOException(e);
                }

                result.response=new String(resbuf);
                return result;
            }

            @Override
            protected void onPostExecute(Result result) {
                if (handler!=null) handler.onResult(result.response,result.statusCode);
            }


        }.execute();
    }


    public void get(String url, final ResultHandler handler) {
        request(url,GET,null,handler);
    }


    public void post(String url, final String msg, final ResultHandler handler) {
        request(url,POST,msg,handler);
    }

    public void delete(String url, final String msg, final ResultHandler handler) {
        request(url,DELETE,msg,handler);
    }
}
