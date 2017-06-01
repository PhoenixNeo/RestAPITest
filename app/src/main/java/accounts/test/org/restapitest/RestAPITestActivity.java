package accounts.test.org.restapitest;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Handler;

public class RestAPITestActivity extends AppCompatActivity {
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rest_apitest);
        context = this;
        final Button btnSignIn = (Button)findViewById(R.id.btnLogin);
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ARAuthenticate(context).execute(
                        ((EditText) findViewById(R.id.etUsername)).getText().toString(),
                        ((EditText) findViewById(R.id.etPassword)).getText().toString()
                );
            }
        });
    }

    private class ARAuthenticate extends AsyncTask<String, Void, String>{
        Context context;
        private ARRestAPIHelper requestHandler;
        private String userName = "";
        private ProgressDialog progressDialog = new ProgressDialog(RestAPITestActivity.this);
        public ARAuthenticate(Context c){
            this.context = c;
        }

        @Override
        protected void onPreExecute(){
            requestHandler = new ARRestAPIHelper();
            progressDialog.setMessage("Signing in.....");
            progressDialog.show();
        }
        @Override
        protected String doInBackground(String... params) {
            String result = null;
            try {
                requestHandler.setServerURL("http://10.41.4.59:8008/api/jwt/login");
                requestHandler.setContentType("application/x-www-form-urlencoded");
                requestHandler.setRequestMethod("POST");
                Map<String, String> payload = new HashMap<String, String>();
                userName = params[0];
                payload.put("username", userName);
                payload.put("password", params[1]);
                requestHandler.setParams(payload);
                requestHandler.sendRequest();
                result = String.valueOf(requestHandler.getResponseCode()).concat(";").concat(requestHandler.getResponseText().toString());
            } catch (Exception e) {
                Log.d("RESTTEST", e.getMessage());
            }
            return result;
        }

        @Override
        protected void onPostExecute(final String result){
            super.onPostExecute(result);
            progressDialog.dismiss();
            String[] code = result.split(";");
            if(code[0].equalsIgnoreCase("200")){
                progressDialog.setMessage("Signin Successful...Loading Requests");
                progressDialog.incrementProgressBy(5);
                progressDialog.show();
                Intent i = new Intent(getBaseContext(), ApprovalRequestsActivity.class);
                i.putExtra("AUTHTOKEN", code[1]);
                i.putExtra("username", userName);
                context.startActivity(i);
            }
            else{
                final AlertDialog.Builder resultDialog = new AlertDialog.Builder(context);
                resultDialog.setMessage("Authorization failed...");
                resultDialog.setCancelable(false);
                resultDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alert = resultDialog.create();
                alert.show();
            }
        }
    }
}
