package bih.in.krishijal_irrigation.ui;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import bih.in.krishijal_irrigation.R;
import bih.in.krishijal_irrigation.database.DataBaseHelper;
import bih.in.krishijal_irrigation.entity.GpsMasterModel;
import bih.in.krishijal_irrigation.entity.InspectionDetailsModel;
import bih.in.krishijal_irrigation.entity.PanchayatData;
import bih.in.krishijal_irrigation.entity.VillageListEntity;
import bih.in.krishijal_irrigation.utility.CommonPref;
import bih.in.krishijal_irrigation.utility.GpsTracker;
import bih.in.krishijal_irrigation.utility.Utiilties;
import bih.in.krishijal_irrigation.web_services.WebServiceHelper;

public class Nalkup_Sinchaai_YojyaActivity extends AppCompatActivity implements View.OnClickListener{
    LinearLayout ll;
    Spinner sp_panchayat,sp_village,sp_option_power,sp_motor_power;
    EditText edt_no_of_nalkup,edt_pole_length,edt_pipe_Perimeter_inch,edt_pipe_lingth_meter,edt_apporx_command_area_hec,edt_yojna_price;
    ArrayList<VillageListEntity>VillageList=new ArrayList<>();
    ArrayList<PanchayatData>PanchayatList=new ArrayList<>();
    ArrayList<GpsMasterModel>GpsMasterList=new ArrayList<>();
    DataBaseHelper dataBaseHelper;
    InspectionDetailsModel inspectionDetailsModel;
    String Dist_Id="",BlockId="";
    private GpsTracker gpsTracker;
    TextView txt_location_Agri,txt_location_nalkup,txt_location_dist_pipe,txt_location_command;
    Button btn_location_Agri,btn_location_nalkup,btn_location_dist_pipe,btn_location_command,save_basic_detail;
    LinearLayout lin_location;
    ImageView take_photo;
    private final static int CAMERA_PIC = 99;
    byte[] img,imgnew;
    Bitmap bmp,bmp1;
    String panchayat_Id="",panchayat_Name="",Vill_Id="",Vill_Name="";
    ArrayList<String>EneryType=new ArrayList<>();
    ArrayList<String>MotorPower=new ArrayList<>();
    ArrayList<String>Selectchannel=new ArrayList<>();
    ArrayList<String>Selectplot=new ArrayList<>();
    String str_Motor_power="",str_EneryType="",str_Motor_powerCode="";
    String str_no_nalkup="",str_polelength="",str_dist_pipe_inch="",str_dist_pipe_mtr="",str_aprox_command_area="",str_yojna_price="";
    Spinner sp_choose_channel,sp_choose_plot;
    Spinner sp_gpsType;
    Button take_loc;
    takegpsListAdaptor mAdapter;
    double take_latitude=0.00;
    double take_longitude=0.00;
    long id=0;
    String str_cap_gps_id="",str_cap_gps_name="";
    String str_chaneelNo="",str_EneryTypeCode="",str_plot_No="";
    RecyclerView recyclerView;
    LinearLayout lin_plotno,lin_chaneelname;
    Intent imageData1;
    boolean edit = false;
    String keyid="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nalkup__sinchaai__yojya);
        Initialization();
        dataBaseHelper=new DataBaseHelper(getApplicationContext());
        try {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        btn_location_Agri.setOnClickListener(this);
        btn_location_nalkup.setOnClickListener(this);
        btn_location_dist_pipe.setOnClickListener(this);
        btn_location_command.setOnClickListener(this);
        save_basic_detail.setOnClickListener(this);
        take_photo.setOnClickListener(this);
        lin_location.setVisibility(View.GONE);
        take_loc.setOnClickListener(this);

        BlockId= CommonPref.getUserDetails(Nalkup_Sinchaai_YojyaActivity.this).getBlockCode();
        setPanchayat(BlockId);
        setEneryType();
        setMotorPoewer();
        setChannelNo();
        setGpsMaster();
        setplotNo();
        /*try {
            if(keyid.equalsIgnoreCase("")) {
                keyid = getIntent().getExtras().getString("KeyId");
                String isEdit = "";
                isEdit = getIntent().getExtras().getString("isEdit");
                Log.d("kvfrgv", "" + keyid + "" + isEdit);
                if (Integer.parseInt(keyid) > 0 && isEdit.equals("Yes")) {
                    edit = true;
                    //    ShowEditEntry(keyid);
                    setPanchayat(BlockId);

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }*/
        lin_plotno.setVisibility(View.GONE);
        lin_chaneelname.setVisibility(View.GONE);
        sp_panchayat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                if (arg2 > 0) {

                    PanchayatData panchayatData = PanchayatList.get(arg2 - 1);
                    panchayat_Id = panchayatData.getPcode();
                    panchayat_Name = panchayatData.getPname();
                    setVillageSpinnerData(panchayat_Id);
                }else {
                    panchayat_Id = "";
                    panchayat_Name = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                panchayat_Id = "";
                panchayat_Name = "";
            }
        });
        sp_village.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                if (arg2 > 0) {

                    VillageListEntity villageListEntity = VillageList.get(arg2 - 1);
                    Vill_Id = villageListEntity.getVillCode();
                    Vill_Name = villageListEntity.getVillName();
                }else {
                    Vill_Id = "";
                    Vill_Name = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        sp_motor_power.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                if (arg2 > 0) {


                    str_Motor_power = MotorPower.get(arg2);
                    if(str_Motor_power.equalsIgnoreCase("1 HP")){
                        str_Motor_powerCode="1";
                    }else if(str_Motor_power.equalsIgnoreCase("2 HP")){
                        str_Motor_powerCode="2";
                    }
                    else if(str_Motor_power.equalsIgnoreCase("3 HP")){
                        str_Motor_powerCode="3";
                    }
                    else if(str_Motor_power.equalsIgnoreCase("4 HP")){
                        str_Motor_powerCode="4";
                    }
                    else if(str_Motor_power.equalsIgnoreCase("5 HP")){
                        str_Motor_powerCode="5";
                    }

                }else {
                    str_Motor_power = "";

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                str_Motor_power = "";
            }
        });
        sp_option_power.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                if (arg2 > 0) {


                    str_EneryType = EneryType.get(arg2);
                    if(str_EneryType.equalsIgnoreCase("विधुत")){
                        str_EneryTypeCode="1";
                    }else if(str_EneryType.equalsIgnoreCase("सोलर")){
                        str_EneryTypeCode="2";
                    }

                }else {
                    str_EneryType = "";

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                str_EneryType = "";
            }
        });
        sp_choose_channel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                if (arg2 > 0) {


                    str_chaneelNo = Selectchannel.get(arg2);


                }else {
                    str_chaneelNo = "";

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                str_chaneelNo = "";
            }
        });
        sp_choose_plot.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                if (arg2 > 0) {


                    str_plot_No = Selectplot.get(arg2);

                }else {
                    str_plot_No = "";

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                str_plot_No = "";
            }
        });
        sp_gpsType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                if (arg2 > 0) {
                    lin_chaneelname.setVisibility(View.GONE);
                    lin_plotno.setVisibility(View.GONE);

                    GpsMasterModel gpsMasterModel = GpsMasterList.get(arg2 - 1);
                    str_cap_gps_id = gpsMasterModel.getGpsTypeId();
                    str_cap_gps_name = gpsMasterModel.getGpsDesc();

                    if(str_cap_gps_name.equalsIgnoreCase("Distribution Pipe")){
                        lin_chaneelname.setVisibility(View.VISIBLE);

                    }else {

                    }
                    if(str_cap_gps_name.equalsIgnoreCase("Command Area Boundary")){
                        lin_plotno.setVisibility(View.VISIBLE);

                    }else {

                    }

                }else {
                    str_cap_gps_id = "";
                    str_cap_gps_name = "";
                    lin_chaneelname.setVisibility(View.GONE);
                    lin_plotno.setVisibility(View.GONE);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                str_cap_gps_id = "";
                str_cap_gps_name = "";
                lin_chaneelname.setVisibility(View.GONE);
                lin_plotno.setVisibility(View.GONE);
            }
        });



    }


    private void Initialization(){
        lin_plotno=(LinearLayout)findViewById(R.id.lin_plotno);
        lin_chaneelname=(LinearLayout)findViewById(R.id.lin_chaneelname);
        recyclerView=(RecyclerView)findViewById(R.id.list_gps_taken);
        sp_gpsType=(Spinner)findViewById(R.id.sp_gpsType);
        take_loc=(Button) findViewById(R.id.take_loc);
        take_photo=(ImageView)findViewById(R.id.take_photo);
        lin_location=(LinearLayout)findViewById(R.id.lin_location);
        btn_location_Agri=(Button)findViewById(R.id.btn_location_Agri);
        save_basic_detail=(Button)findViewById(R.id.save_basic_detail);
        btn_location_nalkup=(Button)findViewById(R.id.btn_location_nalkup);
        btn_location_dist_pipe=(Button)findViewById(R.id.btn_location_dist_pipe);
        btn_location_command=(Button)findViewById(R.id.btn_location_command);
        txt_location_Agri=(TextView) findViewById(R.id.txt_location_Agri);
        txt_location_nalkup=(TextView) findViewById(R.id.txt_location_nalkup);
        txt_location_dist_pipe=(TextView) findViewById(R.id.txt_location_dist_pipe);
        txt_location_command=(TextView) findViewById(R.id.txt_location_command);
        sp_panchayat=(Spinner)findViewById(R.id.sp_panchayat);
        sp_village=(Spinner)findViewById(R.id.sp_village);
        sp_option_power=(Spinner)findViewById(R.id.sp_option_power);
        sp_motor_power=(Spinner)findViewById(R.id.sp_motor_power);
        edt_no_of_nalkup=(EditText)findViewById(R.id.edt_no_of_nalkup);
        edt_pole_length=(EditText)findViewById(R.id.edt_pole_length);
        edt_pipe_Perimeter_inch=(EditText)findViewById(R.id.edt_pipe_Perimeter_inch);
        edt_pipe_lingth_meter=(EditText)findViewById(R.id.edt_pipe_lingth_meter);
        edt_apporx_command_area_hec=(EditText)findViewById(R.id.edt_apporx_command_area_hec);
        edt_yojna_price=(EditText)findViewById(R.id.edt_yojna_price);
        sp_choose_channel=(Spinner)findViewById(R.id.sp_choose_channel);
        sp_choose_plot=(Spinner)findViewById(R.id.sp_choose_plot);
    }

    private void SetValue(){
        str_no_nalkup=edt_no_of_nalkup.getText().toString();
        str_polelength=edt_pole_length.getText().toString();
        str_dist_pipe_inch=edt_pipe_Perimeter_inch.getText().toString();
        str_dist_pipe_mtr=edt_pipe_lingth_meter.getText().toString();
        str_aprox_command_area=edt_apporx_command_area_hec.getText().toString();
        str_yojna_price=edt_yojna_price.getText().toString();


    }


    private long InsertData(){
        SetValue();

        String userid= PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("USER_ID", "");
        inspectionDetailsModel=new InspectionDetailsModel();
        inspectionDetailsModel.setSchemeCode("1");
        inspectionDetailsModel.setDistCode(CommonPref.getUserDetails(Nalkup_Sinchaai_YojyaActivity.this).getDistrictCode());
        inspectionDetailsModel.setBlockCode(CommonPref.getUserDetails(Nalkup_Sinchaai_YojyaActivity.this).getBlockCode());
        inspectionDetailsModel.setPanchayatCode(panchayat_Id);
        inspectionDetailsModel.setVILLCODE(Vill_Id);

        inspectionDetailsModel.setEnergyTypeId(str_EneryTypeCode);
        inspectionDetailsModel.setNoofNalkup(str_no_nalkup);
        inspectionDetailsModel.setNoOfPole(str_polelength);
        inspectionDetailsModel.setMotor_Pump_Power(str_Motor_power);
        inspectionDetailsModel.setDistributionPipeDiamater(str_dist_pipe_inch);
        inspectionDetailsModel.setDistributionPipeLength(str_dist_pipe_mtr);
        inspectionDetailsModel.setApproxCommandArea(str_aprox_command_area);
        inspectionDetailsModel.setSchemeApproxAmt(str_yojna_price);
        inspectionDetailsModel.setEntry_By(CommonPref.getUserDetails(Nalkup_Sinchaai_YojyaActivity.this).getUserID());
        inspectionDetailsModel.setEntryDate(Utiilties.getCurrentDate());

        id = new DataBaseHelper(Nalkup_Sinchaai_YojyaActivity.this).InsertInspectionDetail(inspectionDetailsModel);


        if (id > 0) {
            Toast.makeText(getApplicationContext(), "डेटा सफलतापूर्वक सहेजा गया", Toast.LENGTH_LONG).show();

        } else {
            Toast.makeText(getApplicationContext(), "डेटा सहेजा नहीं गया", Toast.LENGTH_LONG).show();
        }
        return id;
    }






    //Gps
    public String getLocation(){
        gpsTracker = new GpsTracker(Nalkup_Sinchaai_YojyaActivity.this);
        String location="";
        double latitude=0.00;
        double longitude=0.00;
        if(gpsTracker.canGetLocation()){
             latitude = gpsTracker.getLatitude();
             longitude = gpsTracker.getLongitude();
        }else{
            gpsTracker.showSettingsAlert();
        }
        location= String.valueOf((latitude)+","+String.valueOf(longitude));
        take_latitude=latitude;
        take_longitude=longitude;


        return location;
    }

    @Override
    public void onClick(View view) {
        if(view.getId()== R.id.take_loc){
            getLocation();
            long idnew = 0;
            InspectionDetailsModel detailsModel=new InspectionDetailsModel();
            detailsModel.setInspectionId(String.valueOf(id));
            detailsModel.setSchemeCode("1");
            detailsModel.setGPSTypeId(str_cap_gps_id);
            detailsModel.setGPSTypeName(str_cap_gps_name);
            detailsModel.setLatitude(String.valueOf(take_latitude));
            detailsModel.setLongitude(String.valueOf(take_longitude));
            detailsModel.setChannelName(str_chaneelNo);
            detailsModel.setPlotNo(str_plot_No);
            idnew= dataBaseHelper.InsertGpsDetail(detailsModel);
            if (idnew > 0) {
                Toast.makeText(getApplicationContext(), "डेटा सफलतापूर्वक सहेजा गया", Toast.LENGTH_LONG).show();

            } else {
                Toast.makeText(getApplicationContext(), "डेटा सहेजा नहीं गया", Toast.LENGTH_LONG).show();
            }
            ArrayList<InspectionDetailsModel>listgps=new ArrayList<>();
            listgps=dataBaseHelper.getInsGpslocationList(String.valueOf(id),"1");
            mAdapter = new takegpsListAdaptor(Nalkup_Sinchaai_YojyaActivity.this,listgps);
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            //mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            recyclerView.setLayoutManager(mLayoutManager);
         //   recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(mAdapter);


        }
        if(view.getId()== R.id.save_basic_detail){
           InsertData();

            lin_location.setVisibility(View.VISIBLE);

        }
        if(view.getId()== R.id.take_photo){
            final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {


                buildAlertMessageNoGps();
            }
            if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER) == true) {

                Intent iCamera = new Intent(getApplicationContext(), CameraActivity.class);
                    iCamera.putExtra("KEY_PIC", "1");


                startActivityForResult(iCamera, CAMERA_PIC);

            }
        }
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.bihgov);
        builder.setTitle("GPS ?");
        builder.setMessage("(GPS)जीपीएस बंद है\nस्थान के अक्षांश और देशांतर प्राप्त करने के लिए कृपया जीपीएस चालू करें")
//		builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("[(GPS) जीपीएस चालू करे ]", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("[ बंद करें ]", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }




    private void loadVillageSpinnerdata()
    {
        VillageList = dataBaseHelper.getVillageList(panchayat_Id);
        ArrayList<String> VillageListString = new ArrayList<>();
        ArrayList<String> VillageListint = new ArrayList<>();

        if (VillageList.size() > 0)
        {
            VillageListString.add("--चयन करे--");
            VillageListint.add("--चयन करे--");
        }

        for (int i = 0; i < VillageList.size(); i++)
        {
            VillageListString.add(VillageList.get(i).getVillName());
            VillageListint.add(VillageList.get(i).getVillCode());
        }

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, VillageListString);
        ArrayAdapter<String> spinnerAdapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, VillageListint);
        sp_village.setAdapter(spinnerAdapter);
        String Village_code = "";
        Village_code = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("Spin_Vill_Code", "");

        if (!Village_code.equalsIgnoreCase(""))
        {

            sp_village.setSelection(((ArrayAdapter<String>)spinnerAdapter1).getPosition(Village_code));

        }

    }
    private void setPanchayat(String blockcode)
    {
        PanchayatList = dataBaseHelper.getPanchayt(blockcode);
        ArrayList<String> PanchayatListString = new ArrayList<>();
        ArrayList<String> PanchayatListint = new ArrayList<>();

        if (PanchayatList.size() > 0)
        {
            PanchayatListString.add("--चयन करे--");
            PanchayatListint.add("--चयन करे--");
        }

        for (int i = 0; i < PanchayatList.size(); i++)
        {
            PanchayatListString.add(PanchayatList.get(i).getPname());
            PanchayatListint.add(PanchayatList.get(i).getPcode());
        }

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, PanchayatListString);
        ArrayAdapter<String> spinnerAdapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, PanchayatListint);
        sp_panchayat.setAdapter(spinnerAdapter);
        String Village_code = "";
        Village_code = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("Spin_Vill_Code", "");

        if (!Village_code.equalsIgnoreCase(""))
        {

            sp_panchayat.setSelection(((ArrayAdapter<String>)spinnerAdapter1).getPosition(Village_code));

        }

    }
    private void setEneryType()
    {
        EneryType.add("--चयन करे--");
      EneryType.add("विधुत");
      EneryType.add("सोलर");

//        if (EneryType.size() > 0)
//        {
//
//
//        }

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, EneryType);

        sp_option_power.setAdapter(spinnerAdapter);
        String Village_code = "";
        Village_code = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("Spin_Vill_Code", "");

        if (!Village_code.equalsIgnoreCase(""))
        {

            sp_option_power.setSelection(((ArrayAdapter<String>)spinnerAdapter).getPosition(Village_code));

        }

    }
    private void setMotorPoewer()
    {
        MotorPower.add("--चयन करे--");
        MotorPower.add("1 HP");
        MotorPower.add("2 HP");
        MotorPower.add("3 HP");
        MotorPower.add("4 HP");
        MotorPower.add("5 HP");


        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, MotorPower);

        sp_motor_power.setAdapter(spinnerAdapter);
        String Village_code = "";
        Village_code = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("Spin_Vill_Code", "");

        if (!Village_code.equalsIgnoreCase(""))
        {

            sp_motor_power.setSelection(((ArrayAdapter<String>)spinnerAdapter).getPosition(Village_code));

        }

    }
    private void setChannelNo()
    {
        Selectchannel.add("--चयन करे--");
        Selectchannel.add("1");
        Selectchannel.add("2");
        Selectchannel.add("3");
        Selectchannel.add("4");
        Selectchannel.add("6");

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, Selectchannel);

        sp_choose_channel.setAdapter(spinnerAdapter);
        String Village_code = "";
        Village_code = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("Spin_Vill_Code", "");

        if (!Village_code.equalsIgnoreCase(""))
        {

            sp_choose_channel.setSelection(((ArrayAdapter<String>)spinnerAdapter).getPosition(Village_code)); }
    }
    private void setplotNo()
    {
        Selectplot.add("--चयन करे--");
        Selectplot.add("1");
        Selectplot.add("2");
        Selectplot.add("3");
        Selectplot.add("4");
        Selectplot.add("6");

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, Selectplot);

        sp_choose_plot.setAdapter(spinnerAdapter);
        String Village_code = "";
        Village_code = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("Spin_Vill_Code", "");

        if (!Village_code.equalsIgnoreCase(""))
        {

            sp_choose_plot.setSelection(((ArrayAdapter<String>)spinnerAdapter).getPosition(Village_code)); }
    }


    public void setVillageSpinnerData(String Pancode) {
        DataBaseHelper placeData = new DataBaseHelper(Nalkup_Sinchaai_YojyaActivity.this);
        VillageList = dataBaseHelper.getVillageList(Pancode);
        if(VillageList.size()<=0)
        {
            new SyncVillageData(Pancode).execute();
        }
        else {
            loadVillageSpinnerdata();//loadSHGSpinnerData(SHGList);
        }

    }
   private class SyncVillageData extends AsyncTask<String, Void, ArrayList<VillageListEntity>> {
    String PanCode="";
    public SyncVillageData(String panCode) {

        this.PanCode = panCode;

    }
    private final ProgressDialog dialog = new ProgressDialog(Nalkup_Sinchaai_YojyaActivity.this);

    @Override
    protected void onPreExecute() {
        //dialog.setCanceledOnTouchOutside(false);
        dialog.setMessage("ग्राम लोड हो रहा है...");
        this.dialog.show();
    }

    @Override
    protected ArrayList<VillageListEntity> doInBackground(String...arg) {

        return WebServiceHelper.getVillageListData(panchayat_Id);
    }

    @Override
    protected void onPostExecute(ArrayList<VillageListEntity> result) {
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
        DataBaseHelper helper=new DataBaseHelper(getApplicationContext());

        long i= helper.setVillageDataToLocal(result,panchayat_Id);

        if(i>0)
        {
            loadVillageSpinnerdata();
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Failed to update village",Toast.LENGTH_SHORT).show();
        }
    }
  }

  private void setLocalData(){
        dataBaseHelper=new DataBaseHelper(Nalkup_Sinchaai_YojyaActivity.this);
       // dataBaseHelper.getNalkupDetails("");

  }


  //gps
  private void setGpsMaster()
  {
      GpsMasterList = dataBaseHelper.getGpsList("1");
      ArrayList<String> GpsMasterListString = new ArrayList<>();
      ArrayList<String> GpsMasterListint = new ArrayList<>();

      if (GpsMasterList.size() > 0)
      {
          GpsMasterListString.add("--चयन करे--");
          GpsMasterListint.add("--चयन करे--");
      }

      for (int i = 0; i < GpsMasterList.size(); i++)
      {
          GpsMasterListString.add(GpsMasterList.get(i).getGpsDesc());
          GpsMasterListint.add(GpsMasterList.get(i).getGpsTypeId());
      }

      ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, GpsMasterListString);
      ArrayAdapter<String> spinnerAdapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, GpsMasterListint);
      sp_gpsType.setAdapter(spinnerAdapter);
      String Village_code = "";
      Village_code = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("Spin_Vill_Code", "");

      if (!Village_code.equalsIgnoreCase(""))
      {

          sp_gpsType.setSelection(((ArrayAdapter<String>)spinnerAdapter1).getPosition(Village_code));

      }

  }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CAMERA_PIC:
                if (resultCode == RESULT_OK) {
                    byte[] imgData = data.getByteArrayExtra("CapturedImage");
                    // byte[] imgData = data.getByteArrayExtra("CapturedImage");
                    DataBaseHelper placeData = new DataBaseHelper(getApplicationContext());

                    switch (data.getIntExtra("KEY_PIC", 0)) {
                        case 1:
                            imageData1=data;
                            bmp = BitmapFactory.decodeByteArray(imgData, 0,
                                    imgData.length);
                            take_photo.setScaleType(ImageView.ScaleType.FIT_XY);
                            take_photo.setImageBitmap(Utiilties.GenerateThumbnail(bmp,
                                    500, 500));
                            saveImage();

                            break;
                    }
                }
        }
    }

    private void saveImage() {

        int i = 0;
        DataBaseHelper placeData = new DataBaseHelper(
                Nalkup_Sinchaai_YojyaActivity.this);
        SQLiteDatabase db = placeData.getWritableDatabase();
        for (i = 0; i < 5; i++) {
            ContentValues values = new ContentValues();
            String[] whereArgs;
            byte[] imgData;
            switch (i) {
                case 0:
                    //GPSTime
                    if (imageData1!=null) {

                        imgData = imageData1.getByteArrayExtra("CapturedImage");
                        values.put("photo", imgData);
                       /* values.put("EntryDate",
                                imageData1.getStringExtra("CapturedTime"));
                        values.put("Latitude",
                                String.valueOf(imageData1.getStringExtra("Lat")));
                        values.put("Longitude",
                                String.valueOf(imageData1.getStringExtra("Lng")));
                        values.put("UploadDate",
                                String.valueOf(imageData1.getStringExtra("GPSTime")));*/
                        if (edit) {
                            whereArgs = new String[]{String.valueOf(keyid)};
                        } else {
                            whereArgs = new String[]{String.valueOf(id)};
                        }


                        db.update("InsetionTable", values, "InspectionId=?", whereArgs);
                    }
                    break;
            }
        }

        db.close();
    }

    private void setImage(String insId){
        DataBaseHelper placeData = new DataBaseHelper(this);
        SQLiteDatabase db = placeData.getReadableDatabase();
        //int HIDID = getIntent().getIntExtra("KEY_HIDID", 0);

        Cursor cursor = db
                .rawQuery(
                        "SELECT photo FROM InsetionTable where InspectionId =?",
                        new String[]{String.valueOf(insId)});

        Log.e("PID  ", insId);


        if (cursor.moveToNext()) {

            if (!cursor.isNull(0)) {
                lin_location.setVisibility(View.VISIBLE);
                byte[] imgData = cursor.getBlob(cursor.getColumnIndex("photo"));
                // Log.d("vdhbhbh",imgData.toString());
                Bitmap bmp = BitmapFactory.decodeByteArray(imgData, 0,
                        imgData.length);
                take_photo.setScaleType(ImageView.ScaleType.FIT_XY);
                take_photo.setImageBitmap(Utiilties.GenerateThumbnail(bmp,
                        500, 500));
            }
        }
        cursor.close();
        db.close();

    }

    private void setrecyclerviewaction(String keyid){
        ArrayList<InspectionDetailsModel>listgps=new ArrayList<>();
        listgps=dataBaseHelper.getInsGpslocationList(String.valueOf(keyid),"2");
        mAdapter = new takegpsListAdaptor(Nalkup_Sinchaai_YojyaActivity.this,listgps);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());

        recyclerView.setLayoutManager(mLayoutManager);

        recyclerView.setAdapter(mAdapter);
    }

}
