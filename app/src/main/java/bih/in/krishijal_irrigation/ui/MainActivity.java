package bih.in.krishijal_irrigation.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import bih.in.krishijal_irrigation.R;
import bih.in.krishijal_irrigation.database.DataBaseHelper;
import bih.in.krishijal_irrigation.utility.CommonPref;

public class MainActivity extends Activity implements View.OnClickListener {
    RelativeLayout rl_nalkup_new,rl_nalkup_edit,rl_nalkup_upload,rl_udvah_new,rl_udvah_edit,rl_udvah_upload,rl_aahar_new,rl_aahar_edit,rl_aahar_upload;
    TextView nalkup_edit,nalkup_upload,udvah_edit,udvah_upload,aahar_edit,aahar_upload,tv_username,tv_district,tv_block;
    String DistName="",BlockName="",UserName="";
    DataBaseHelper dataBaseHelper;
    long udhavCount,AaharCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dataBaseHelper= new DataBaseHelper(MainActivity.this);
        inilization();
        DistName= CommonPref.getUserDetails(MainActivity.this).getDistName();
        BlockName= CommonPref.getUserDetails(MainActivity.this).getBlockName();
        UserName= CommonPref.getUserDetails(MainActivity.this).getName();

        tv_username.setText(UserName);
        tv_district.setText(DistName);
        tv_block.setText(BlockName);


    }
    public void inilization(){
        rl_nalkup_new=(RelativeLayout)findViewById(R.id.rl_nalkup_new);
        rl_nalkup_edit=(RelativeLayout)findViewById(R.id.rl_nalkup_edit);
        rl_nalkup_upload=(RelativeLayout)findViewById(R.id.rl_nalkup_upload);
        rl_udvah_new=(RelativeLayout)findViewById(R.id.rl_udvah_new);
        rl_udvah_edit=(RelativeLayout)findViewById(R.id.rl_udvah_edit);
        rl_udvah_upload=(RelativeLayout)findViewById(R.id.rl_udvah_upload);
        rl_aahar_new=(RelativeLayout)findViewById(R.id.rl_aahar_new);
        rl_aahar_edit=(RelativeLayout)findViewById(R.id.rl_aahar_edit);
        rl_aahar_upload=(RelativeLayout)findViewById(R.id.rl_aahar_upload);

        nalkup_edit=(TextView)findViewById(R.id.nalkup_edit);
        nalkup_upload=(TextView)findViewById(R.id.nalkup_upload);
        udvah_edit=(TextView)findViewById(R.id.udvah_edit);
        udvah_upload=(TextView)findViewById(R.id.udvah_upload);
        aahar_edit=(TextView)findViewById(R.id.aahar_edit);
        aahar_upload=(TextView)findViewById(R.id.aahar_upload);

        tv_username=(TextView)findViewById(R.id.tv_username);
        tv_district=(TextView)findViewById(R.id.tv_district);
        tv_block=(TextView)findViewById(R.id.tv_block);

        rl_nalkup_new.setOnClickListener(this);
        rl_udvah_new.setOnClickListener(this);
        rl_aahar_new.setOnClickListener(this);
        rl_aahar_edit.setOnClickListener(this);
        rl_udvah_edit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.rl_nalkup_new)
        {
            Intent intent=new Intent(getApplicationContext(),Nalkup_Sinchaai_YojyaActivity.class);
            startActivity(intent);
            //handle multiple view click events
        }else  if (view.getId()==R.id.rl_udvah_new){
            Intent intent=new Intent(getApplicationContext(),Udvah_Sinchaai_YojnaActivity.class);
            startActivity(intent);
        }else  if (view.getId()==R.id.rl_aahar_new){
            Intent intent=new Intent(getApplicationContext(),Aahar_Sinchaai_YojyaActivity.class);
            startActivity(intent);
        }else  if (view.getId()==R.id.rl_aahar_edit){
            Intent intent=new Intent(getApplicationContext(),Edit_Aahar_SinchaiActivity.class);
            startActivity(intent);
        }
        else  if (view.getId()==R.id.rl_udvah_edit){
            Intent intent=new Intent(getApplicationContext(),Edit_Udvah_SinchaiActivity.class);
            startActivity(intent);
        }
    }
    private void showPending() {
        udhavCount = dataBaseHelper.countUdvahDetail(MainActivity.this,CommonPref.getUserDetails(MainActivity.this).getUserID());
        AaharCount = dataBaseHelper.countAaharDetail(MainActivity.this,CommonPref.getUserDetails(MainActivity.this).getUserID());

        if (udhavCount > 0) {
            udvah_edit.setText(String.valueOf(udhavCount));
            udvah_upload.setText(String.valueOf(udhavCount));
        }else {
            udvah_edit.setText("0");
            udvah_upload.setText("0");
        }

        if (AaharCount > 0) {
            aahar_edit.setText(String.valueOf(AaharCount));
            aahar_upload.setText(String.valueOf(AaharCount));
        }else {
            aahar_edit.setText("0");
            aahar_upload.setText("0");
        }
    }
    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        showPending();
        super.onResume();


    }
}
