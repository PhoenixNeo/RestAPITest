package accounts.test.org.restapitest;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.JsonReader;
import android.widget.ListView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class ApprovalRequestsActivity extends Activity {
    final Context context = this;
    ApprovalRequestDataAdaptor requestData;
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approval_requests);
            new RetreiveRequestData(context).execute(getIntent().getStringExtra("AUTHTOKEN"), getIntent().getStringExtra("username"));
    }

    protected void processDataToObjects(String obj){
        try{
            JSONObject temp = null;
            requestData =  new ApprovalRequestDataAdaptor(this, R.layout.activity_approval_list_items );
            ApprovalRequest reqTemp = null;
            JSONObject jsonObject = new JSONObject(obj);
            JSONArray jsonArray = jsonObject.getJSONArray("entries");
            for (int i=0; i < jsonArray.length();i++) {
                temp = jsonArray.getJSONObject(i).getJSONObject("values");
                reqTemp = new ApprovalRequest(temp.getString("Detail-Sig-Id"),
                        temp.getString("Approval Status"),
                        temp.getString("Approvers"),
                        temp.getString("Application"),
                        temp.getString("Process"),
                        temp.getString("Request"));
                requestData.add(reqTemp);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        listView = (ListView)findViewById(R.id.lvRequestsListView);
        listView.setAdapter(requestData);
    }

    private class RetreiveRequestData extends AsyncTask<String, Void, String> {
        Context context;
        private ARRestAPIHelper requestHandler;
        private String userName = "";
        private ProgressDialog progressDialog = new ProgressDialog(ApprovalRequestsActivity.this);
        String jsonResult = "";
        String responseCode = "";

        public RetreiveRequestData(Context c) {
            this.context = c;
        }

        @Override
        protected void onPreExecute() {
            requestHandler = new ARRestAPIHelper();
            progressDialog.setMessage("Retreiving Data.....");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                requestHandler.setServerURL("http://10.41.4.59:8008/api/arsys/v1/entry/AP:Detail-Signature");
                requestHandler.setRequestMethod("GET");
//                requestHandler.setContentType("application/x-www-form-urlencoded");
                requestHandler.setAuthToken(params[0]);
                Map<String, String> payload = new HashMap<String, String>();
                payload.put("fields", "values(Detail-Sig-Id, Approval Status, Approvers, Application, Process, Request)");
//                payload.put("q", "'Approvers' LIKE \"%".concat(params[1].concat("%\"")));//.concat("christiangray".concat("%\"")));//.concat(params[1].concat("%\"")));
                requestHandler.setParams(payload);
                requestHandler.sendRequest();
                responseCode = String.valueOf(requestHandler.getResponseCode());
                jsonResult = requestHandler.getResponseText().toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return jsonResult;
        }

        @Override
        protected void onPostExecute(final String result) {
            super.onPostExecute(result);
            processDataToObjects(result);
            progressDialog.dismiss();
/*
            String code = result.get(0);
            if(code.equalsIgnoreCase("200")) {
                final AlertDialog.Builder resultDialog = new AlertDialog.Builder(context);
                resultDialog.setCancelable(false);
                resultDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alert = resultDialog.create();
                alert.show();
            }else{

            }
*/
        }
    }
}
