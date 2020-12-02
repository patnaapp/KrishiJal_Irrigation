package bih.in.krishijal_irrigation.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import bih.in.krishijal_irrigation.R;

import bih.in.krishijal_irrigation.entity.InspectionDetailsModel;
import bih.in.krishijal_irrigation.utility.CommonPref;
import bih.in.krishijal_irrigation.web_services.WebServiceHelper;

public class MainActivity extends Activity implements View.OnClickListener {
    RelativeLayout rl_nalkup_new,rl_nalkup_edit,rl_nalkup_upload,rl_udvah_new,rl_udvah_edit,rl_udvah_upload,rl_aahar_new,rl_aahar_edit,rl_aahar_upload;
    TextView nalkup_edit,nalkup_upload,udvah_edit,udvah_upload,aahar_edit,aahar_upload,tv_username,tv_district,tv_block;
    String DistName="",BlockName="",UserName="";
    DataBaseHelper dataBaseHelper;
    long udhavCount,AaharCount;
    String version="",Userid="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dataBaseHelper= new DataBaseHelper(MainActivity.this);
        inilization();
        DistName= CommonPref.getUserDetails(MainActivity.this).getDistName();
        BlockName= CommonPref.getUserDetails(MainActivity.this).getBlockName();
        UserName= CommonPref.getUserDetails(MainActivity.this).getName();
        Userid= CommonPref.getUserDetails(MainActivity.this).getUserID();

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
        rl_nalkup_upload.setOnClickListener(this);
        rl_udvah_upload.setOnClickListener(this);
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
        else  if (view.getId()==R.id.rl_nalkup_upload){
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setIcon(R.drawable.upload_adaptor);
            builder.setTitle("Data upload");
            builder.setMessage("Are you sure want to Upload the Record");

            builder.setPositiveButton("[Yes]", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    DataBaseHelper dbHelper = new DataBaseHelper(MainActivity.this);
                    dialog.dismiss();
                    //ArrayList<InspectionDetailsModel> dataProgress = dbHelper.getAllProgressList(CommonPref.getUserDetails(getActivity()).getUserID());

                    InspectionDetailsModel NalkupDetails = dbHelper.getNalkupDetails(Userid,"1");
                    ArrayList<InspectionDetailsModel> gpsLoc = dbHelper.getInsGpslocationList(NalkupDetails.getInspectionId(),"1");
                    //ArrayList<InspectionDetailsModel> roadFacility = dbHelper.getRoadFacility(roadPhoto.getEntry_by(), roadEntity.getInspectionId());

                    new UploadRoadSurveyDetils(NalkupDetails, gpsLoc).execute();

                }

            });

            builder.setNegativeButton("[No]", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });

            AlertDialog dialog = builder.create();
            // Display the alert dialog on interface
            //  if(dialog.isFinishing()) {
            dialog.show();
        }
        else  if (view.getId()==R.id.rl_udvah_upload){
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setIcon(R.drawable.upload_adaptor);
            builder.setTitle("Data upload");
            builder.setMessage("Are you sure want to Upload the Record");

            builder.setPositiveButton("[Yes]", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    DataBaseHelper dbHelper = new DataBaseHelper(MainActivity.this);
                    dialog.dismiss();
                    //ArrayList<InspectionDetailsModel> dataProgress = dbHelper.getAllProgressList(CommonPref.getUserDetails(getActivity()).getUserID());

                    InspectionDetailsModel udvahDetails = dbHelper.getUdvahDetails(Userid,"2");
                    ArrayList<InspectionDetailsModel> gpsLoc = dbHelper.getInsGpslocationList(udvahDetails.getInspectionId(),"2");
                    //ArrayList<InspectionDetailsModel> roadFacility = dbHelper.getRoadFacility(roadPhoto.getEntry_by(), roadEntity.getInspectionId());

                    new UploadRoadSurveyDetils(udvahDetails, gpsLoc).execute();

                }

            });

            builder.setNegativeButton("[No]", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });

            AlertDialog dialog = builder.create();
            // Display the alert dialog on interface
            //  if(dialog.isFinishing()) {
            dialog.show();
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
    private class UploadRoadSurveyDetils extends AsyncTask<String, Void, String> {
        private final ProgressDialog dialog = new ProgressDialog(MainActivity.this);

        InspectionDetailsModel nalkup;
        ArrayList<InspectionDetailsModel> gps;


        public UploadRoadSurveyDetils(InspectionDetailsModel Nalkupdetails, ArrayList<InspectionDetailsModel> gpsLoc) {
            this.nalkup = Nalkupdetails;
            this.gps = gpsLoc;

        }

        @Override
        protected void onPreExecute()
        {
            this.dialog.setCanceledOnTouchOutside(false);
            this.dialog.setMessage(MainActivity.this.getResources().getString(R.string.uploading));
            this.dialog.show();
        }

        @Override
        protected String doInBackground(String... param)
        {

            String devicename=getDeviceName();
            String app_version=getAppVersion();
            boolean isTablet=isTablet(MainActivity.this);
            if(isTablet) {
                devicename="Tablet::"+devicename;
                Log.e("DEVICE_TYPE", "Tablet");
            } else {
                devicename="Mobile::"+devicename;
                Log.e("DEVICE_TYPE", "Mobile");
            }
            return WebServiceHelper.UploadNalkupDetails(MainActivity.this,nalkup,gps,devicename,app_version);
        }

        @Override
        protected void onPostExecute(String result)
        {
            if (this.dialog.isShowing())
            {
                this.dialog.dismiss();
            }

            if (result != null)
            {

                if(result.contains("1"))
                {

                    Toast.makeText(MainActivity.this, "Uploaded Successfully",Toast.LENGTH_SHORT).show();
//                    dataBaseHelper = new DataBaseHelper(MainActivity.this);
//                    long c = dataBaseHelper.deleteFromDB_RoadSurvey(workInfo.getSlno(), CommonPref.getUserDetails(MainActivity.this).getUserID().toLowerCase());
//
//                    if(c>0)
//                    {
//                        new AlertDialog.Builder(MainActivity.this)
//                                .setTitle("Message")
//                                .setMessage(result)
//                                .setCancelable(false)
//                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog, int id) {
//                                        MainActivity.this.finish();
//                                    }
//                                })
//                                .show();
//
//                    }
//                    else
//                    {
//                        Toast.makeText(MainActivity.this, "Failed to delete",Toast.LENGTH_SHORT).show();
//                    }

                }
                else
                {
                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle("Failed!!")
                            .setMessage(result)
                            .setCancelable(true)
                            .show();
                }


            }
            else
            {
                Toast.makeText(MainActivity.this,"Failed!! Null Response. Try again later",Toast.LENGTH_LONG).show();
            }


        }

    }
    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {

            return model.toUpperCase();
        } else {

            return manufacturer.toUpperCase() + " " + model;
        }
    }
    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }
    public String getAppVersion(){
        try {
            version = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
//                TextView tv = (TextView)getActivity().findViewById(R.id.txtVersion_1);
//                tv.setText(getActivity().getString(R.string.app_version) + version + " ");
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return version;
    }
}
