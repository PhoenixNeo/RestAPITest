package accounts.test.org.restapitest;

import android.net.Uri;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import okio.BufferedSink;

/**
 * Created by adarshs on 03-05-2017.
 */
public class ARRestAPIHelper {
    private String serverURL;
    private String contentType;
    private String requestMethod;
    private String authToken;
    private boolean isAuthenticated;
    private OkHttpClient connection;
    private URL url;
    private Map<String, String> params;
    private StringBuffer responseText;

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    private int responseCode;

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public boolean isAuthenticated() {
        return isAuthenticated;
    }

    public void setIsAuthenticated(boolean isAuthenticated) {
        this.isAuthenticated = isAuthenticated;
    }

    public String getServerURL() {
        return serverURL;
    }

    public void setServerURL(String serverURL) throws MalformedURLException {
        this.serverURL = serverURL;
        url = new URL(serverURL);
    }

    public StringBuffer getResponseText() {
        return responseText;
    }

    public void sendRequest() throws IOException {
        HttpUrl.Builder urlBuilder = null;
        Request request = null;
        OkHttpClient connClient = null;
        Response response = null;

        if(serverURL!=null){
            urlBuilder = HttpUrl.parse(serverURL).newBuilder();
        }
        request = new Request.Builder().url(serverURL).build();
        if(requestMethod.equalsIgnoreCase("get")) {
            if (!params.isEmpty()) {
                for (String key : params.keySet()) {
                    urlBuilder.addQueryParameter(key, params.get(key));
                }
            }
            request = new Request.Builder()
                    .url(urlBuilder.build().toString())
                    .addHeader("Authorization", "AR-JWT ".concat(authToken))
                    .build();
        }

        if(requestMethod.equalsIgnoreCase("post")) {
            FormEncodingBuilder postData = new FormEncodingBuilder();
            if (!params.isEmpty()) {
                for (String key : params.keySet()) {
                    postData.add(key, params.get(key));
                }
            }
            RequestBody postRequestBody = postData.build();
            request = new Request.Builder()
                    .url(urlBuilder.build().toString())
                    .addHeader("Content-Type", contentType)
                    .method("POST", postRequestBody)
                    .build();
        }

        connClient = new OkHttpClient();
        response = connClient.newCall(request).execute();
        responseCode = response.code();
        String responseBody = response.body().string();
        responseText =  new StringBuffer(responseBody);

/*        String postData = null;
        Uri.Builder builder;
        int responseCode;
        if(requestMethod == "POST") {
            connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod(requestMethod);
            connection.setRequestProperty("Content-Type", contentType);
            builder = new Uri.Builder();
            for (String key: params.keySet()) {
                builder.appendQueryParameter(key,params.get(key));
            }
            postData = builder.build().getEncodedQuery();
            connection.setDoOutput(true);
        }
        if(requestMethod == "GET") {
            builder = new Uri.Builder();
            for (String key: params.keySet()) {
                builder.appendQueryParameter(key,params.get(key));
            }
            setServerURL(getServerURL().toString().concat("?".concat(builder.build().getEncodedQuery())));
            connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod(requestMethod);
            connection.setRequestProperty("Authorization", "AR-JWT ".concat(authToken));
            connection.setRequestProperty("Content-Type", contentType);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            responseCode = connection.getResponseCode();
        }
        connection.setReadTimeout(60000);
        connection.setConnectTimeout(60000);
        connection.setRequestProperty("Connection","close");
        System.setProperty("http.keepAlive","false");
        connection.connect();
        if(requestMethod == "POST") {
            OutputStream os = connection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(postData);
            writer.flush();
            writer.close();
            os.close();
        }

        StringBuffer sb = new StringBuffer("");
        if(requestMethod == "GET"){
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line = "";
            while((line = reader.readLine()) != null){
                sb.append(line);
            }
            reader.close();
        }
        responseCode = connection.getResponseCode();
        if(requestMethod != "GET" && responseCode == HttpURLConnection.HTTP_OK){
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line = "";
            while((line = reader.readLine()) != null){
                sb.append(line);
            }
            reader.close();
        }
        if(responseCode != HttpURLConnection.HTTP_OK){
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
            String line = "";
            while((line = reader.readLine()) != null){
                sb.append(line);
            }
            reader.close();
        }
        responseText = sb;*/
}

}
