package accounts.test.org.restapitest;

import android.net.Uri;

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

/**
 * Created by adarshs on 03-05-2017.
 */
public class ARRestAPIHelper {
    private String serverURL;
    private String contentType;
    private String requestMethod;
    private String authToken;
    private boolean isAuthenticated;
    private HttpURLConnection connection;
    private URL url;
    private Map<String, String> params;
    private StringBuffer responseText;

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
        connection = (HttpURLConnection)url.openConnection();
        connection.setReadTimeout(60000);
        connection.setConnectTimeout(60000);
        if(requestMethod == "POST")
            connection.setRequestProperty("Content-Type", contentType);
        if(requestMethod == "GET")
            connection.setRequestProperty("Authorization", "AR-JWT ".concat(authToken));
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.connect();
        Uri.Builder builder = new Uri.Builder();
        for (String key: params.keySet()) {
            builder.appendQueryParameter(key,params.get(key));
        }
        String postData = builder.build().getEncodedQuery();
        OutputStream os = connection.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
        writer.write(postData);
        writer.flush();
        writer.close();
        os.close();

        StringBuffer sb = new StringBuffer("");
        if(connection.getResponseCode() == HttpURLConnection.HTTP_OK){
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line = "";
            while((line = reader.readLine()) != null){
                sb.append(line);
            }
            reader.close();
        }else{
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
            String line = "";
            while((line = reader.readLine()) != null){
                sb.append(line);
            }
            reader.close();
        }
        responseText = sb;
        connection.disconnect();
}

}
