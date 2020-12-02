package bih.in.krishijal_irrigation.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
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
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;

import bih.in.krishijal_irrigation.R;
import bih.in.krishijal_irrigation.database.DataBaseHelper;
import bih.in.krishijal_irrigation.entity.GpsMasterModel;
import bih.in.krishijal_irrigation.entity.InspectionDetailsModel;
import bih.in.krishijal_irrigation.entity.PanchayatData;
import bih.in.krishijal_irrigation.entity.VillageListEntity;
import bih.in.krishijal_irrigation.utility.CommonPref;
import bih.in.krishijal_irrigation.utility.GlobalVariables;
import bih.in.krishijal_irrigation.utility.GpsTracker;
import bih.in.krishijal_irrigation.utility.Utiilties;
import bih.in.krishijal_irrigation.web_services.WebServiceHelper;

public class Aahar_Sinchaai_YojyaActivity extends Activity implements View.OnClickListener {
    Spinner sp_panchayat,sp_village;
    EditText edt_pipe_length,edt_distribution_pipe_inch,edt_distribution_pipe_lambai,edt_command_area,edt_yojna_lagat;
    Button btn_aahar_location;
    LocationManager mlocManager = null;
    static Location LastLocation = null;
    private final int UPDATE_LATLNG = 2;
    private final int UPDATE_ADDRESS = 1;
    private ProgressDialog dialog;
    String keyid="";
    String water_facility[] = {"-चयन करे-","खरीफ","रबी","गरमा"};
    ArrayList<VillageListEntity> VillageList=new ArrayList<>();
    ArrayList<PanchayatData>PanchayatList=new ArrayList<>();
    DataBaseHelper dataBaseHelper;
    RadioGroup rg_types_water;
    InspectionDetailsModel inspectionDetailsModel;
    String panchayat_Id="",panchayat_Name="",Vill_Id="",Vill_Name="",Dist_Id="",BlockId="",water_facility_Code="",water_facility_Name="";
    String _edt_pipe_length="",_edt_distribution_pipe_inch="",_edt_distribution_pipe_lambai="",_edt_command_area="",_edt_yojna_lagat;
    CheckBox chk_kharif,chk_rabi,chk_garma;
    String kharif="N",rabi="N",garma="N",Dist_Name="",BlockName="",userid="";
    Button btn_saveLocal;
    boolean edit = false;
    ArrayList<InspectionDetailsModel>EntryList=new ArrayList<>();

    ArrayList<GpsMasterModel>GpsMasterList=new ArrayList<>();
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
    Button save_basic_detail_aahar;
    Intent imageData1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aahar__sinchaai__yojya);
        dataBaseHelper= new DataBaseHelper(Aahar_Sinchaai_YojyaActivity.this);

        initialization();
        setChannelNo();
        setGpsMaster();
        setplotNo();
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
    public  void initialization(){
        save_basic_detail_aahar=(Button)findViewById(R.id.save_basic_detail_aahar);
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
        edt_pipe_length=(EditText)findViewById(R.id.edt_pipe_length);
        edt_distribution_pipe_inch=(EditText)findViewById(R.id.edt_distribution_pipe_inch);
        edt_distribution_pipe_lambai=(EditText)findViewById(R.id.edt_distribution_pipe_lambai);
        edt_command_area=(EditText)findViewById(R.id.edt_command_area);
        edt_yojna_lagat=(EditText)findViewById(R.id.edt_yojna_lagat);
        btn_saveLocal=(Button) findViewById(R.id.btn_saveLocal);

        chk_kharif=(CheckBox) findViewById(R.id.chk_kharif);
        chk_rabi=(CheckBox) findViewById(R.id.chk_rabi);
        chk_garma=(CheckBox) findViewById(R.id.chk_garma);

        chk_kharif.setOnClickListener(this);
        chk_rabi.setOnClickListener(this);
        chk_garma.setOnClickListener(this);




        Dist_Id=CommonPref.getUserDetails(Aahar_Sinchaai_YojyaActivity.this).getDistrictCode();
        Dist_Name=CommonPref.getUserDetails(Aahar_Sinchaai_YojyaActivity.this).getDistName();
        BlockId=CommonPref.getUserDetails(Aahar_Sinchaai_YojyaActivity.this).getBlockCode();
        BlockName=CommonPref.getUserDetails(Aahar_Sinchaai_YojyaActivity.this).getBlockName();
        setPanchayat(BlockId);
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
        save_basic_detail_aahar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String isvalid = validateRecordBeforeSaving();
                if (isvalid.equalsIgnoreCase("yes")) {
                    InsertData();
                }
            }
        });


    }
    private void InsertData(){

        setvalue();
        InspectionDetailsModel inspectionDetailsModel=new InspectionDetailsModel();
        inspectionDetailsModel.setDistName(Dist_Name);
        inspectionDetailsModel.setDistCode(Dist_Id);
        inspectionDetailsModel.setBlockName(BlockName);
        inspectionDetailsModel.setBlockCode(BlockId);
        inspectionDetailsModel.setPanchayatName(panchayat_Name);
        inspectionDetailsModel.setPanchayatCode(panchayat_Id);
        inspectionDetailsModel.setVillageName(Vill_Name);
        inspectionDetailsModel.setVILLCODE(Vill_Id);
        inspectionDetailsModel.setWaterAvailable_Kharif(kharif);
        inspectionDetailsModel.setWaterAvailable_Rabi(rabi);
        inspectionDetailsModel.setWaterAvailable_Garma(garma);
        inspectionDetailsModel.setDistributionChannelLength(_edt_pipe_length);
        inspectionDetailsModel.setDistributionPipeDiamater(_edt_distribution_pipe_inch);
        inspectionDetailsModel.setDistributionPipeLength(_edt_distribution_pipe_lambai);
        inspectionDetailsModel.setApproxCommandArea(_edt_command_area);
        inspectionDetailsModel.setSchemeApproxAmt(_edt_yojna_lagat);
        inspectionDetailsModel.setEntry_By(CommonPref.getUserDetails(Aahar_Sinchaai_YojyaActivity.this).getUserID());
        inspectionDetailsModel.setSchemeCode("3");


        String userid= PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("USER_ID", "");


        id = new DataBaseHelper(Aahar_Sinchaai_YojyaActivity.this).InsertInspectionDetailAahar(inspectionDetailsModel);

        if (id > 0) {
            Toast.makeText(getApplicationContext(), "डेटा सफलतापूर्वक सहेजा गया", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();

        } else {
            Toast.makeText(getApplicationContext(), "डेटा सहेजा नहीं गया", Toast.LENGTH_LONG).show();
        }
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

    public void setvalue(){
        _edt_pipe_length=edt_pipe_length.getText().toString();
        _edt_distribution_pipe_inch=edt_distribution_pipe_inch.getText().toString();
        _edt_distribution_pipe_lambai=edt_distribution_pipe_lambai.getText().toString();
        _edt_command_area=edt_command_area.getText().toString();
        _edt_yojna_lagat=edt_yojna_lagat.getText().toString();
    }

    public void setVillageSpinnerData(String Pancode) {
        DataBaseHelper placeData = new DataBaseHelper(Aahar_Sinchaai_YojyaActivity.this);
        VillageList = dataBaseHelper.getVillageList(Pancode);
        if(VillageList.size()<=0)
        {
            new SyncVillageData(Pancode).execute();
        }
        else {
            loadVillageSpinnerdata();//loadSHGSpinnerData(SHGList);
        }

    }

    @Override
    public void onClick(View view) {
        /*boolean checked = ((CheckBox) view).isChecked();

        switch(view.getId()) {
            case R.id.chk_kharif:
                if (checked) {
                    // Do your coding
                    kharif = "Y";
                } else {
                    kharif = "N";
                }
                break;

            case R.id.chk_rabi:
                if (checked) {
                    // Do your coding
                    rabi = "Y";
                } else {
                    rabi = "N";
                }
                break;
            case R.id.chk_garma:
                if (checked) {
                    // Do your coding
                    garma = "Y";
                } else {
                    garma = "N";
                }
                break;
        }*/


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
            detailsModel.setInspectionId(String.valueOf(id));
            detailsModel.setSchemeCode("3");
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
            listgps=dataBaseHelper.getInsGpslocationList(String.valueOf(id),"3");
            mAdapter = new takegpsListAdaptor(Aahar_Sinchaai_YojyaActivity.this,listgps);
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            //mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            recyclerView.setLayoutManager(mLayoutManager);
            //   recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(mAdapter);


        }
    }

    private class SyncVillageData extends AsyncTask<String, Void, ArrayList<VillageListEntity>> {
        String PanCode="";
        public SyncVillageData(String panCode) {

            this.PanCode = panCode;

        }
        private final ProgressDialog dialog = new ProgressDialog(Aahar_Sinchaai_YojyaActivity.this);

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
    private String validateRecordBeforeSaving() {
        String isvalid = "no";

        if ((sp_panchayat != null && sp_panchayat.getSelectedItem() != null)) {
            if ((String) sp_panchayat.getSelectedItem() != "--चयन करे--") {
                isvalid = "yes";
            } else {
                Toast.makeText(Aahar_Sinchaai_YojyaActivity.this, "कृपया पंचायत का चयन करे", Toast.LENGTH_LONG).show();

                sp_panchayat.requestFocus();
                return "no";
            }
        }
            if ((sp_village != null && sp_village.getSelectedItem() != null)) {
                if ((String) sp_village.getSelectedItem() != "--चयन करे--") {
                    isvalid = "yes";
                } else {
                    Toast.makeText(Aahar_Sinchaai_YojyaActivity.this, "कृपया गाँव का चयन करे", Toast.LENGTH_LONG).show();

                    sp_village.requestFocus();
                    return "no";
                }
            }

        if (!(chk_kharif.isChecked()|| chk_rabi.isChecked()|| chk_garma.isChecked())) {

            Toast.makeText(Aahar_Sinchaai_YojyaActivity.this, "कृपया आहर में जल की उपलब्धता का चयन करे", Toast.LENGTH_LONG).show();
            //et_habitation_name.requestFocus();
            return "no";
        }


        if (edt_pipe_length.getText().toString().trim().length() <= 0) {
            Toast.makeText(Aahar_Sinchaai_YojyaActivity.this, "कृपया पूर्व में निर्मित डिस्ट्रीब्यूशन पाइन की लम्बाई(मी०) में डाले", Toast.LENGTH_LONG).show();
            edt_pipe_length.requestFocus();
            return "no";
        }
        if (edt_distribution_pipe_inch.getText().toString().trim().length() <= 0) {
            Toast.makeText(Aahar_Sinchaai_YojyaActivity.this, "कृपया डिस्ट्रीब्यूशन पाइप का ब्यास (इंच में) डाले", Toast.LENGTH_LONG).show();
            edt_distribution_pipe_inch.requestFocus();
            return "no";
        }
        if (edt_distribution_pipe_lambai.getText().toString().trim().length() <= 0) {
            Toast.makeText(Aahar_Sinchaai_YojyaActivity.this, "कृपया डिस्ट्रीब्यूशन पाइप का ब्यास (लम्बाई में) डाले", Toast.LENGTH_LONG).show();
            edt_distribution_pipe_lambai.requestFocus();
            return "no";
        }
        if (edt_command_area.getText().toString().trim().length() <= 0) {
            Toast.makeText(Aahar_Sinchaai_YojyaActivity.this, "कृपया अनुमानित कमांड एरिया (हे०) में डाले", Toast.LENGTH_LONG).show();
            edt_command_area.requestFocus();
            return "no";
        }
        if (edt_yojna_lagat.getText().toString().trim().length() <= 0) {
            Toast.makeText(Aahar_Sinchaai_YojyaActivity.this, "कृपया योजना की अनुमानित लागत (लाख ₹) में डाले", Toast.LENGTH_LONG).show();
            edt_yojna_lagat.requestFocus();
            return "no";
        }




        return isvalid;
    }
    public void ShowEditEntry(String keyid) {
        DataBaseHelper helper = new DataBaseHelper(getApplicationContext());
        userid = (CommonPref.getUserDetails(this).getUserID());
        EntryList=helper.getAllEntryById(userid,keyid,"3");
        for (InspectionDetailsModel goatSurveyEntity : EntryList)
        {

            //next_button.setText("अपडेट करे");
            panchayat_Name=goatSurveyEntity.getPanchayatName();
            Vill_Name=goatSurveyEntity.getVillageName();
            String WaterAvailable_Kharif = goatSurveyEntity.getWaterAvailable_Kharif();
            if (WaterAvailable_Kharif.equalsIgnoreCase("Y")) {
                chk_kharif.setChecked(true);
                kharif = "Y";
            } else {
                chk_kharif.setChecked(false);
                kharif = "N";
            }
            String WaterAvailable_Rabi = goatSurveyEntity.getWaterAvailable_Rabi();
            if (WaterAvailable_Rabi.equalsIgnoreCase("Y")) {
                chk_rabi.setChecked(true);
                rabi = "Y";
            } else {
                chk_rabi.setChecked(false);
                rabi = "N";
            }
            String WaterAvailable_Garma = goatSurveyEntity.getWaterAvailable_Garma();
            if (WaterAvailable_Garma.equalsIgnoreCase("Y")) {
                chk_garma.setChecked(true);
                garma = "Y";
            } else {
                chk_garma.setChecked(false);
                garma = "N";
            }
            edt_pipe_length.setText(goatSurveyEntity.getDistributionChannelLength());
            edt_distribution_pipe_inch.setText(goatSurveyEntity.getDistributionPipeDiamater());
            edt_distribution_pipe_lambai.setText(goatSurveyEntity.getDistributionPipeLength());
            edt_command_area.setText(goatSurveyEntity.getApproxCommandArea());
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

                            break;
                    }
                }
        }
    }



    public String getLocation(){
        gpsTracker = new GpsTracker(Aahar_Sinchaai_YojyaActivity.this);
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



    private void saveImage() {

        int i = 0;
        DataBaseHelper placeData = new DataBaseHelper(
                Aahar_Sinchaai_YojyaActivity.this);
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
        listgps=dataBaseHelper.getInsGpslocationList(String.valueOf(keyid),"3");
        mAdapter = new takegpsListAdaptor(Aahar_Sinchaai_YojyaActivity.this,listgps);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());

        recyclerView.setLayoutManager(mLayoutManager);

        recyclerView.setAdapter(mAdapter);
    }


}
