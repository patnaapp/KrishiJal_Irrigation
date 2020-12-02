package bih.in.krishijal_irrigation.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
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
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

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

public class Udvah_Sinchaai_YojnaActivity extends Activity implements View.OnClickListener{
    Spinner sp_panchayat,sp_village,spn_jalshrot,spn_jalshrot_available,spn_motarpump_sanchalan,spn_motarpump_power;
    EditText edt_pump_loc_dist,edt_motor_power_hp,edt_distribution_length,edt_distribution_pipe_inch,edt_distribution_pipe_meter,edt_apporx_command_area_hec,edt_yojna_lagat;
    ArrayList<VillageListEntity> VillageList=new ArrayList<>();
    ArrayList<PanchayatData>PanchayatList=new ArrayList<>();
    DataBaseHelper dataBaseHelper;
    ArrayList<InspectionDetailsModel>EntryList=new ArrayList<>();
    ArrayList<GpsMasterModel>GpsMasterList=new ArrayList<>();
    InspectionDetailsModel inspectionDetailsModel;
    String jalshrot[] = {"--चयन करे--","नदी","तालाब","आहर","झील","अन्य"};
    String water_facility[] = {"--चयन करे--","खरीफ","रबी","गरमा"};
    String motarpump[] = {"--चयन करे--","बिद्युत","सोलर"};
    String motarpump_hp[] = {"--चयन करे--","1 HP","2 HP","3 HP","4 HP","5 HP"};
    String panchayat_Id="",panchayat_Name="",Vill_Id="",Vill_Name="",Dist_Id="",BlockId="",Jalshrot_id,Jalshrot_name,water_facility_Code="",motarpump_sanchalan_code="",motarpump_sanchalan_Name="";
    String _edt_pum_loc_distnce="",_edt_motor_power_hp="",_edt_distribution_length="",_edt_distribution_pipe_inch="",_edt_distribution_pipe_meter="",_edt_apporx_command_area_hec="",_edt_yojna_lagat="";
    Button save_basic_detail_udvah;
    String keyid="";
    boolean edit = false;
    CheckBox chk_kharif,chk_rabi,chk_garma;
    String userid="",dist_id="",dist_name="",blk_id="",block_name="";
    String motarpump_power_code="",motarpump_power_name="",_water_avlbl_kharif="N",_water_avlbl_rabi="N",_water_avlbl_garma="N",chk_string="N";
    String str_cap_gps_id="",str_cap_gps_name="";
    String str_chaneelNo="",str_plot_No="";
    RecyclerView recyclerView;
    LinearLayout lin_plotno,lin_chaneelname;
    Spinner sp_choose_channel,sp_choose_plot;
    Spinner sp_gpsType;
    Button take_loc;
    ArrayList<String>Selectchannel=new ArrayList<>();
    ArrayList<String>Selectplot=new ArrayList<>();
    private final static int CAMERA_PIC = 99;
    byte[] img,imgnew;
    Bitmap bmp,bmp1;
    ImageView take_photo;
    private GpsTracker gpsTracker;
    double take_latitude=0.00;
    double take_longitude=0.00;
    long id=0;
    takegpsListAdaptor mAdapter;
    LinearLayout ll_lat_long;
    Intent imageData1;
    int ThumbnailSize = 500;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_udvah__sinchaai__yojna);
        dataBaseHelper= new DataBaseHelper(Udvah_Sinchaai_YojnaActivity.this);

        initialization();
        setChannelNo();
        setGpsMaster();
        setplotNo();
        try {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        userid=CommonPref.getUserDetails(Udvah_Sinchaai_YojnaActivity.this).getUserID();
        dist_id=CommonPref.getUserDetails(Udvah_Sinchaai_YojnaActivity.this).getDistrictCode();
        dist_name=CommonPref.getUserDetails(Udvah_Sinchaai_YojnaActivity.this).getDistName();
        blk_id=CommonPref.getUserDetails(Udvah_Sinchaai_YojnaActivity.this).getBlockCode();
        block_name=CommonPref.getUserDetails(Udvah_Sinchaai_YojnaActivity.this).getBlockName();

        take_loc.setOnClickListener(this);
        take_photo.setOnClickListener(this);

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
    public void initialization()
    {
        ll_lat_long=(LinearLayout)findViewById(R.id.ll_lat_long);
        take_photo=(ImageView)findViewById(R.id.take_photo);
        sp_choose_channel=(Spinner)findViewById(R.id.sp_choose_channel);
        sp_choose_plot=(Spinner)findViewById(R.id.sp_choose_plot);
        lin_plotno=(LinearLayout)findViewById(R.id.lin_plotno);
        lin_chaneelname=(LinearLayout)findViewById(R.id.lin_chaneelname);
        recyclerView=(RecyclerView)findViewById(R.id.list_gps_taken);
        sp_gpsType=(Spinner)findViewById(R.id.sp_gpsType);
        take_loc=(Button) findViewById(R.id.take_loc);

        sp_panchayat=(Spinner)findViewById(R.id.sp_panchayat);
        sp_village=(Spinner)findViewById(R.id.sp_village);
        spn_jalshrot=(Spinner)findViewById(R.id.spn_jalshrot);
        spn_jalshrot_available=(Spinner)findViewById(R.id.spn_jalshrot_available);
        spn_motarpump_sanchalan=(Spinner)findViewById(R.id.spn_motarpump_sanchalan);
        spn_motarpump_power=(Spinner)findViewById(R.id.spn_motarpump_power);

        edt_pump_loc_dist=(EditText) findViewById(R.id.edt_pump_loc_dist);
        //edt_motor_power_hp=(EditText) findViewById(R.id.edt_motor_power_hp);
        edt_distribution_length=(EditText) findViewById(R.id.edt_distribution_length);
        edt_distribution_pipe_inch=(EditText) findViewById(R.id.edt_distribution_pipe_inch);
        edt_distribution_pipe_meter=(EditText) findViewById(R.id.edt_distribution_pipe_meter);
        edt_apporx_command_area_hec=(EditText) findViewById(R.id.edt_apporx_command_area_hec);
        edt_yojna_lagat=(EditText) findViewById(R.id.edt_yojna_lagat);
        save_basic_detail_udvah=(Button) findViewById(R.id.save_basic_detail_udvah);
        chk_kharif=(CheckBox) findViewById(R.id.chk_kharif);
        chk_rabi=(CheckBox) findViewById(R.id.chk_rabi);
        chk_garma=(CheckBox) findViewById(R.id.chk_garma);

        BlockId= CommonPref.getUserDetails(Udvah_Sinchaai_YojnaActivity.this).getBlockCode();
        setPanchayat(BlockId);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, jalshrot);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn_jalshrot.setAdapter(adapter);

        ArrayAdapter adapter1 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, water_facility);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn_jalshrot_available.setAdapter(adapter1);

        ArrayAdapter adapter2 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, motarpump);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn_motarpump_sanchalan.setAdapter(adapter2);

        ArrayAdapter adapter3 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, motarpump_hp);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn_motarpump_power.setAdapter(adapter3);

        ll_lat_long.setVisibility(View.GONE);

        try {
            if(keyid.equalsIgnoreCase("")) {
                keyid = getIntent().getExtras().getString("KeyId");
                String isEdit = "";
                isEdit = getIntent().getExtras().getString("isEdit");
                Log.d("kvfrgv", "" + keyid + "" + isEdit);
                if (Integer.parseInt(keyid) > 0 && isEdit.equals("Yes")) {
                    edit = true;
                    ShowEditEntry(keyid);
                    setPanchayat(BlockId);

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        sp_panchayat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3)
            {
                if (arg2 > 0)
                {
                    PanchayatData panchayatData = PanchayatList.get(arg2 - 1);
                    panchayat_Id = panchayatData.getPcode();
                    panchayat_Name = panchayatData.getPname();
                    setVillageSpinnerData(panchayat_Id);
                }
                else
                {
                    panchayat_Id = "";
                    panchayat_Name = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
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
            public void onNothingSelected(AdapterView<?> arg0)
            {

            }
        });
        spn_jalshrot.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                if (arg2 > 0) {

                    Jalshrot_name = jalshrot[arg2].toString();
                    if (Jalshrot_name.equalsIgnoreCase("नदी")) {
                        Jalshrot_id = "1";
                    }
                    else if (Jalshrot_name.equalsIgnoreCase("तालाब")) {
                        Jalshrot_id = "2";
                    }
                    else if (Jalshrot_name.equalsIgnoreCase("आहर")) {
                        Jalshrot_id = "3";
                    }
                    else if (Jalshrot_name.equalsIgnoreCase("झील")) {
                        Jalshrot_id = "4";
                    }
                    else if (Jalshrot_name.equalsIgnoreCase("अन्य")) {
                        Jalshrot_id = "5";
                    }
                }else {
                    Jalshrot_id = "";
                    Jalshrot_name = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        spn_motarpump_sanchalan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                if (arg2 > 0) {

                    motarpump_sanchalan_Name = water_facility[arg2].toString();
                    if (motarpump_sanchalan_Name.equalsIgnoreCase("बिद्युत")) {
                        motarpump_sanchalan_code = "1";
                    }
                    else if (motarpump_sanchalan_Name.equalsIgnoreCase("सोलर")) {
                        motarpump_sanchalan_code = "2";
                    }
                }else {
                    water_facility_Code = "";
                    motarpump_sanchalan_code = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        spn_motarpump_power.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                if (arg2 > 0) {

                    motarpump_power_name = motarpump_hp[arg2].toString();
                    if (motarpump_power_name.equalsIgnoreCase("1 HP")) {
                        motarpump_power_code = "1";
                    }
                    else if (motarpump_power_name.equalsIgnoreCase("2 HP")) {
                        motarpump_power_code = "2";
                    }
                    else if (motarpump_power_name.equalsIgnoreCase("3 HP")) {
                        motarpump_power_code = "3";
                    }
                    else if (motarpump_power_name.equalsIgnoreCase("4 HP")) {
                        motarpump_power_code = "4";
                    }
                    else if (motarpump_power_name.equalsIgnoreCase("5 HP")) {
                        motarpump_power_code = "5";
                    }
                }else {
                    motarpump_power_code = "";
                    motarpump_power_name = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        save_basic_detail_udvah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String isvalid = validateRecordBeforeSaving();
                if (isvalid.equalsIgnoreCase("yes")) {
                    InsertData();
                }
            }
        });

        chk_kharif.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                {
                    _water_avlbl_kharif="Y";
                    chk_string="Y";

                }
            }
        });

        chk_rabi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                {
                    _water_avlbl_rabi="Y";
                    chk_string="Y";
                }
            }
        });

        chk_garma.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                {
                    _water_avlbl_garma="Y";
                    chk_string="Y";
                }
            }
        });
    }
    private void InsertData()
    {


        setvalue();
        InspectionDetailsModel inspectionDetailsModel=new InspectionDetailsModel();
        inspectionDetailsModel.setSchemeCode("2");
        inspectionDetailsModel.setDistCode(dist_id);
        inspectionDetailsModel.setDistName(dist_name);
        inspectionDetailsModel.setBlockCode(blk_id);
        inspectionDetailsModel.setBlockName(blk_id);
        inspectionDetailsModel.setPanchayatCode(panchayat_Id);
        inspectionDetailsModel.setPanchayatName(panchayat_Name);
        inspectionDetailsModel.setVILLCODE(Vill_Id);
        inspectionDetailsModel.setVillageName(Vill_Name);
        inspectionDetailsModel.setWaterSourceId(Jalshrot_id);
        inspectionDetailsModel.setWaterSourceName(Jalshrot_name);
        inspectionDetailsModel.setWaterAvailable_Kharif(_water_avlbl_kharif);
        inspectionDetailsModel.setWaterAvailable_Rabi(_water_avlbl_rabi);
        inspectionDetailsModel.setWaterAvailable_Garma(_water_avlbl_garma);
        inspectionDetailsModel.setEnergyTypeId(motarpump_sanchalan_code);
        inspectionDetailsModel.setEnergyTypeName(motarpump_sanchalan_Name);
        inspectionDetailsModel.setPumplocation_distance(_edt_pum_loc_distnce);
        //inspectionDetailsModel.setEnergyTypeName(motarpump_sanchalan_Name);
        inspectionDetailsModel.setMotor_Pump_Power(motarpump_power_code);
        inspectionDetailsModel.setMotor_Pump_PowerName(motarpump_power_name);
        inspectionDetailsModel.setDistributionChannelLength(_edt_distribution_length);
        inspectionDetailsModel.setDistributionpipelngth_inch(_edt_distribution_pipe_inch);
        inspectionDetailsModel.setDistributionpipelngth_mtr(_edt_distribution_pipe_meter);
        inspectionDetailsModel.setApproxCommandArea(_edt_apporx_command_area_hec);
        inspectionDetailsModel.setSchemeApproxAmt(_edt_yojna_lagat);
        inspectionDetailsModel.setEntry_By(userid);
        id = new DataBaseHelper(Udvah_Sinchaai_YojnaActivity.this).InsertInspectionUdvahDetail(inspectionDetailsModel);

        if (id > 0)
        {
            ll_lat_long.setVisibility(View.VISIBLE);
           // Intent intent = new Intent(getApplicationContext(), MainActivity.class);
           // startActivity(intent);
          //  finish();
            Toast.makeText(getApplicationContext(), "डेटा सफलतापूर्वक सहेजा गया", Toast.LENGTH_LONG).show();
        }
        else
        {
            Toast.makeText(getApplicationContext(), "डेटा सहेजा नहीं गया", Toast.LENGTH_LONG).show();
        }
    }



    private void setVillage(String blockcode)
    {
        VillageList = dataBaseHelper.getVillageList(blockcode);
        ArrayList<String> VillageListString = new ArrayList<>();
        ArrayList<String> VillageListint = new ArrayList<>();

        if (VillageList.size() > 0)
        {
            VillageListString.add("--Choose--");
            VillageListint.add("--Choose--");
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

    public void setvalue()
    {
        _edt_pum_loc_distnce=edt_pump_loc_dist.getText().toString();
        //_edt_motor_power_hp=edt_motor_power_hp.getText().toString();
        _edt_distribution_length=edt_distribution_length.getText().toString();
        _edt_distribution_pipe_inch=edt_distribution_pipe_inch.getText().toString();
        _edt_distribution_pipe_meter=edt_distribution_pipe_meter.getText().toString();
        _edt_apporx_command_area_hec=edt_apporx_command_area_hec.getText().toString();
        _edt_yojna_lagat=edt_yojna_lagat.getText().toString();
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
        if(getIntent().hasExtra("KeyId"))
        {
            sp_village.setSelection(((ArrayAdapter<String>) sp_village.getAdapter()).getPosition(Vill_Name));
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
        if(getIntent().hasExtra("KeyId"))
        {
            sp_panchayat.setSelection(((ArrayAdapter<String>) sp_panchayat.getAdapter()).getPosition(panchayat_Name));
        }

    }


    public void setVillageSpinnerData(String Pancode) {
        DataBaseHelper placeData = new DataBaseHelper(Udvah_Sinchaai_YojnaActivity.this);
        VillageList = dataBaseHelper.getVillageList(Pancode);
        if(VillageList.size()<=0)
        {
            new SyncVillageData(Pancode).execute();
        }
        else {
            loadVillageSpinnerdata();//loadSHGSpinnerData(SHGList);
        }

    }


    private class SyncVillageData extends AsyncTask<String, Void, ArrayList<VillageListEntity>>
    {
        String PanCode="";
        public SyncVillageData(String panCode)
        {
            this.PanCode = panCode;
        }
        private final ProgressDialog dialog = new ProgressDialog(Udvah_Sinchaai_YojnaActivity.this);

        @Override
        protected void onPreExecute()
        {
            //dialog.setCanceledOnTouchOutside(false);
            dialog.setMessage("ग्राम लोड हो रहा है...");
            this.dialog.show();
        }

        @Override
        protected ArrayList<VillageListEntity> doInBackground(String...arg)
        {
            return WebServiceHelper.getVillageListData(panchayat_Id);
        }

        @Override
        protected void onPostExecute(ArrayList<VillageListEntity> result)
        {
            if (dialog.isShowing())
            {
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


    private String validateRecordBeforeSaving() {
        String isvalid = "no";

        if ((sp_panchayat != null && sp_panchayat.getSelectedItem() != null)) {
            if ((String) sp_panchayat.getSelectedItem() != "--चयन करे--") {
                isvalid = "yes";
            } else {
                Toast.makeText(Udvah_Sinchaai_YojnaActivity.this, "कृपया पंचायत का चयन करे", Toast.LENGTH_LONG).show();

                sp_panchayat.requestFocus();
                return "no";
            }
        }
        if ((sp_village != null && sp_village.getSelectedItem() != null)) {
            if ((String) sp_village.getSelectedItem() != "--चयन करे--") {
                isvalid = "yes";
            } else {
                Toast.makeText(Udvah_Sinchaai_YojnaActivity.this, "कृपया गाँव का चयन करे", Toast.LENGTH_LONG).show();

                sp_village.requestFocus();
                return "no";
            }
        }
        if ((spn_jalshrot != null && sp_village.getSelectedItem() != null)) {
            if ((String) spn_jalshrot.getSelectedItem() != "--चयन करे--") {
                isvalid = "yes";
            } else {
                Toast.makeText(Udvah_Sinchaai_YojnaActivity.this, "कृपया जलश्रोत का चयन करे", Toast.LENGTH_LONG).show();

                spn_jalshrot.requestFocus();
                return "no";
            }
        }

        if (!(chk_kharif.isChecked()|| chk_rabi.isChecked()|| chk_garma.isChecked())) {

            Toast.makeText(Udvah_Sinchaai_YojnaActivity.this, "कृपया जलश्रोत में जल की उपलब्धता का चयन करे", Toast.LENGTH_LONG).show();
            //et_habitation_name.requestFocus();
            return "no";
        }
        if ((spn_motarpump_sanchalan != null && spn_motarpump_sanchalan.getSelectedItem() != null)) {
            if ((String) spn_jalshrot.getSelectedItem() != "--चयन करे--") {
                isvalid = "yes";
            } else {
                Toast.makeText(Udvah_Sinchaai_YojnaActivity.this, "कृपया मोटर / पंप के संचालन हेतु उर्जा का विकल्प का चयन करे", Toast.LENGTH_LONG).show();

                spn_motarpump_sanchalan.requestFocus();
                return "no";
            }
        }


        if (edt_pump_loc_dist.getText().toString().trim().length() <= 0) {
            Toast.makeText(Udvah_Sinchaai_YojnaActivity.this, "कृपया Agriculture feeder/Electric Supply Line के अंतिम बिंदु से प्रस्तावित पम्प के लोकेशन की दुरी (Pole Length) में डाले", Toast.LENGTH_LONG).show();
            edt_pump_loc_dist.requestFocus();
            return "no";
        }
        if ((spn_motarpump_power != null && spn_motarpump_power.getSelectedItem() != null)) {
            if ((String) spn_jalshrot.getSelectedItem() != "--चयन करे--") {
                isvalid = "yes";
            } else {
                Toast.makeText(Udvah_Sinchaai_YojnaActivity.this, "कृपया मोटर / पंप का पावर (HP) का चयन करे", Toast.LENGTH_LONG).show();

                spn_motarpump_power.requestFocus();
                return "no";
            }
        }
        if (edt_distribution_length.getText().toString().trim().length() <= 0) {
            Toast.makeText(Udvah_Sinchaai_YojnaActivity.this, "कृपया डिस्ट्रीब्यूशन चैनल की लम्बाई (मी०) डाले", Toast.LENGTH_LONG).show();
            edt_distribution_length.requestFocus();
            return "no";
        }
        if (edt_distribution_pipe_inch.getText().toString().trim().length() <= 0) {
            Toast.makeText(Udvah_Sinchaai_YojnaActivity.this, "डिस्ट्रीब्यूटशन  पाइप का व्यास (इंच  में )", Toast.LENGTH_LONG).show();
            edt_distribution_pipe_inch.requestFocus();
            return "no";
        }
        if (edt_distribution_pipe_meter.getText().toString().trim().length() <= 0) {
            Toast.makeText(Udvah_Sinchaai_YojnaActivity.this, "कृपया डिस्ट्रीब्यूटशन  पाइप का लम्बाई (मी ० ) में डाले", Toast.LENGTH_LONG).show();
            edt_distribution_pipe_meter.requestFocus();
            return "no";
        }
        if (edt_apporx_command_area_hec.getText().toString().trim().length() <= 0) {
            Toast.makeText(Udvah_Sinchaai_YojnaActivity.this, "कृपया अनुमानित कमाण्ड एरिया (हेक्टेर में ) डाले", Toast.LENGTH_LONG).show();
            edt_apporx_command_area_hec.requestFocus();
            return "no";
        }
        if (edt_yojna_lagat.getText().toString().trim().length() <= 0) {
            Toast.makeText(Udvah_Sinchaai_YojnaActivity.this, "कृपया योजना की अनुमानित लागत (लाख ₹) में डाले", Toast.LENGTH_LONG).show();
            edt_yojna_lagat.requestFocus();
            return "no";
        }




        return isvalid;
    }

    public void ShowEditEntry(String keyid) {
        DataBaseHelper helper = new DataBaseHelper(getApplicationContext());
        userid = (CommonPref.getUserDetails(this).getUserID());
        EntryList=helper.getAllEntryUdvah(userid,keyid,"2");
        for (InspectionDetailsModel goatSurveyEntity : EntryList)
        {

            //next_button.setText("अपडेट करे");
            panchayat_Name=goatSurveyEntity.getPanchayatName();
            Vill_Name=goatSurveyEntity.getVillageName();
            Jalshrot_name=goatSurveyEntity.getWaterSourceName();
            spn_jalshrot.setSelection(((ArrayAdapter<String>) spn_jalshrot.getAdapter()).getPosition(Jalshrot_name));
            String WaterAvailable_Kharif = goatSurveyEntity.getWaterAvailable_Kharif();
            if (WaterAvailable_Kharif.equalsIgnoreCase("Y")) {
                chk_kharif.setChecked(true);
                _water_avlbl_kharif = "Y";
            } else {
                chk_kharif.setChecked(false);
                _water_avlbl_kharif = "N";
            }
            String WaterAvailable_Rabi = goatSurveyEntity.getWaterAvailable_Rabi();
            if (WaterAvailable_Rabi.equalsIgnoreCase("Y")) {
                chk_rabi.setChecked(true);
                WaterAvailable_Rabi = "Y";
            } else {
                chk_rabi.setChecked(false);
                WaterAvailable_Rabi = "N";
            }
            String WaterAvailable_Garma = goatSurveyEntity.getWaterAvailable_Garma();
            if (WaterAvailable_Garma.equalsIgnoreCase("Y")) {
                chk_garma.setChecked(true);
                WaterAvailable_Garma = "Y";
            } else {
                chk_garma.setChecked(false);
                WaterAvailable_Garma = "N";
            }
            Jalshrot_name=goatSurveyEntity.getWaterSourceName();
            spn_jalshrot.setSelection(((ArrayAdapter<String>) spn_jalshrot.getAdapter()).getPosition(Jalshrot_name));

            motarpump_sanchalan_Name=goatSurveyEntity.getEnergyTypeName();
            spn_motarpump_sanchalan.setSelection(((ArrayAdapter<String>) spn_motarpump_sanchalan.getAdapter()).getPosition(motarpump_sanchalan_Name));

            edt_pump_loc_dist.setText(goatSurveyEntity.getPumplocation_distance());

            motarpump_power_name=goatSurveyEntity.getMotor_Pump_PowerName();
            spn_motarpump_power.setSelection(((ArrayAdapter<String>) spn_motarpump_power.getAdapter()).getPosition(motarpump_power_name));



            edt_distribution_length.setText(goatSurveyEntity.getDistributionChannelLength());
            edt_distribution_pipe_inch.setText(goatSurveyEntity.getDistributionpipelngth_inch());
            edt_distribution_pipe_meter.setText(goatSurveyEntity.getDistributionpipelngth_mtr());
            edt_apporx_command_area_hec.setText(goatSurveyEntity.getApproxCommandArea());
            edt_yojna_lagat.setText(goatSurveyEntity.getSchemeApproxAmt());

            setImage(keyid);
            setrecyclerviewaction(keyid);
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
    private void setGpsMaster()
    {
        GpsMasterList = dataBaseHelper.getGpsList("2");
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

                          /*  img = imgData;
                            Bitmap bmpImg = BitmapFactory.decodeByteArray(imgData, 0, imgData.length);
                            bmp = bmpImg;
                            take_photo.setImageBitmap(bmpImg);*/

                            break;
                    }
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
    @Override
    public void onClick(View view) {
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
        if(view.getId()== R.id.take_loc){
            getLocation();
            long idnew = 0;
            InspectionDetailsModel detailsModel=new InspectionDetailsModel();
            if(edit){
                detailsModel.setInspectionId(String.valueOf(keyid));
            }else {
                detailsModel.setInspectionId(String.valueOf(id));
            }

            detailsModel.setSchemeCode("2");
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
            listgps=dataBaseHelper.getInsGpslocationList(String.valueOf(id),"2");
            mAdapter = new takegpsListAdaptor(Udvah_Sinchaai_YojnaActivity.this,listgps);
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            //mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            recyclerView.setLayoutManager(mLayoutManager);
            //   recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(mAdapter);


        }

    }
    //Gps
    public String getLocation(){
        gpsTracker = new GpsTracker(Udvah_Sinchaai_YojnaActivity.this);
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
    private void saveImage() {

        int i = 0;
        DataBaseHelper placeData = new DataBaseHelper(
                Udvah_Sinchaai_YojnaActivity.this);
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


                        db.update("Inspection_Aahar", values, "InspectionId=?", whereArgs);
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
                        "SELECT photo FROM Inspection_Aahar where InspectionId =?",
                        new String[]{String.valueOf(insId)});

        Log.e("PID  ", insId);


        if (cursor.moveToNext()) {

            if (!cursor.isNull(0)) {
                ll_lat_long.setVisibility(View.VISIBLE);
                byte[] imgData = cursor.getBlob(cursor.getColumnIndex("photo"));
                Log.d("vdhbhbh",imgData.toString());
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
        mAdapter = new takegpsListAdaptor(Udvah_Sinchaai_YojnaActivity.this,listgps);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());

        recyclerView.setLayoutManager(mLayoutManager);

        recyclerView.setAdapter(mAdapter);
    }


}
