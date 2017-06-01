package accounts.test.org.restapitest;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by adarshs on 01-06-2017.
 */

public class ApprovalRequestDataAdaptor extends ArrayAdapter {
    List data = new ArrayList();

    public ApprovalRequestDataAdaptor(@NonNull Context context, @LayoutRes int resource) {
        super(context, resource);
    }

    public void add(ApprovalRequest object) {
        super.add(object);
        data.add(object);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Nullable
    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem;
        listItem = convertView;
        RequestHolder requestHolder;
        if(listItem == null){
            LayoutInflater inflater = (LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            listItem = inflater.inflate(R.layout.activity_approval_list_items, parent,false);
            requestHolder = new RequestHolder();
            requestHolder.tvTitle = (TextView)listItem.findViewById(R.id.requestTitleName);
            requestHolder.tvSummary = (TextView)listItem.findViewById(R.id.requestSummaryLine);
            listItem.setTag(requestHolder);
        }else{
            requestHolder = (RequestHolder)listItem.getTag();
        }
        ApprovalRequest request = (ApprovalRequest)this.getItem(position);
        requestHolder.tvTitle.setText(request.getRequest());
        requestHolder.tvSummary.setText(request.getProcess().concat(" - ").concat(request.getApplication()));
        return listItem;
    }

    static class RequestHolder{
        TextView tvTitle, tvSummary;
    }

}
