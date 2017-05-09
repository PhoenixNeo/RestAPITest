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
        String result = "";
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
                result = requestHandler.getResponseText().toString();
            } catch (Exception e) {
                Log.d("RESTTEST", e.getMessage());
            }
            return result;
        }

        @Override
        protected void onPostExecute(final String result){
            super.onPostExecute(result);
            progressDialog.dismiss();
            final AlertDialog.Builder resultDialog = new AlertDialog.Builder(context);
            resultDialog.setMessage(result);
            resultDialog.setCancelable(false);
            resultDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    Intent i = new Intent(getBaseContext(), ApprovalRequestsActivity.class);
                    i.putExtra("AUTHTOKEN",result);
                    i.putExtra("username", userName);
                    context.startActivity(i);
                }
            });
            AlertDialog alert = resultDialog.create();
            alert.show();
        }
    }
}
