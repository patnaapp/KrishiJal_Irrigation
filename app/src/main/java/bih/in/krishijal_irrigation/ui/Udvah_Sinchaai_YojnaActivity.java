package bih.in.krishijal_irrigation.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
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
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

import bih.in.krishijal_irrigation.R;
import bih.in.krishijal_irrigation.database.DataBaseHelper;
import bih.in.krishijal_irrigation.entity.InspectionDetailsModel;
import bih.in.krishijal_irrigation.entity.PanchayatData;
import bih.in.krishijal_irrigation.entity.VillageListEntity;
import bih.in.krishijal_irrigation.utility.CommonPref;
import bih.in.krishijal_irrigation.web_services.WebServiceHelper;

public class Udvah_Sinchaai_YojnaActivity extends Activity {
    Spinner sp_panchayat,sp_village,spn_jalshrot,spn_jalshrot_available,spn_motarpump_sanchalan,spn_motarpump_power;
    EditText edt_pump_loc_dist,edt_motor_power_hp,edt_distribution_length,edt_distribution_pipe_inch,edt_distribution_pipe_meter,edt_apporx_command_area_hec,edt_yojna_lagat;
    ArrayList<VillageListEntity> VillageList=new ArrayList<>();
    ArrayList<PanchayatData>PanchayatList=new ArrayList<>();
    DataBaseHelper dataBaseHelper;
    ArrayList<InspectionDetailsModel>EntryList=new ArrayList<>();
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


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_udvah__sinchaai__yojna);
        dataBaseHelper= new DataBaseHelper(Udvah_Sinchaai_YojnaActivity.this);

        initialization();
        userid=CommonPref.getUserDetails(Udvah_Sinchaai_YojnaActivity.this).getUserID();
        dist_id=CommonPref.getUserDetails(Udvah_Sinchaai_YojnaActivity.this).getDistrictCode();
        dist_name=CommonPref.getUserDetails(Udvah_Sinchaai_YojnaActivity.this).getDistName();
        blk_id=CommonPref.getUserDetails(Udvah_Sinchaai_YojnaActivity.this).getBlockCode();
        block_name=CommonPref.getUserDetails(Udvah_Sinchaai_YojnaActivity.this).getBlockName();
    }
    public void initialization()
    {
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
//        String userid= PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("uid", "");
//        String dist_id= PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("dist_id", "");
//        String blk_id= PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("blk_id", "");
        long id = 0;
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
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
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


        }
    }

}
