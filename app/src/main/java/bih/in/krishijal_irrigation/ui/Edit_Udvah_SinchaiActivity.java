package bih.in.krishijal_irrigation.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import bih.in.krishijal_irrigation.R;
import bih.in.krishijal_irrigation.adapter.AaharEditAdapter;
import bih.in.krishijal_irrigation.adapter.UdvahEditAdapter;
import bih.in.krishijal_irrigation.database.DataBaseHelper;
import bih.in.krishijal_irrigation.entity.InspectionDetailsModel;
import bih.in.krishijal_irrigation.utility.CommonPref;

public class Edit_Udvah_SinchaiActivity extends Activity {
    RecyclerView rv_data;
    LinearLayout ll_panchayat;
    UdvahEditAdapter adaptor_showedit_listDetail;
    ProgressDialog dialog;
    DataBaseHelper dataBaseHelper;
    ArrayList<InspectionDetailsModel> data;
    String listid, version="";
    TextView tv_Norecord,tv_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit__aahar__udvah);
        tv_Norecord=(TextView) findViewById(R.id.tv_Norecord);
        tv_title=(TextView) findViewById(R.id.tv_title);

        rv_data= findViewById(R.id.rv_data);

        dataBaseHelper = new DataBaseHelper(this);

        loadAdapter();
    }

    public void loadAdapter(){
        data=dataBaseHelper.getUdvahDetail(CommonPref.getUserDetails(Edit_Udvah_SinchaiActivity.this).getUserID());

        if(data.size()> 0){
            tv_Norecord.setVisibility(View.GONE);
            rv_data.setVisibility(View.VISIBLE);

            rv_data.setLayoutManager(new LinearLayoutManager(this));
            adaptor_showedit_listDetail = new UdvahEditAdapter(this, data);
            rv_data.setAdapter(adaptor_showedit_listDetail);

        }else{
            rv_data.setVisibility(View.GONE);
            tv_Norecord.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        loadAdapter();
    }
}
