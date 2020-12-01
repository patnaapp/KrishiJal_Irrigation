package bih.in.krishijal_irrigation.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
    InspectionDetailsModel inspectionDetailsModel;
    String jalshrot[] = {"-चयन करे-","नदी","तालाब","आहर","झील","अन्य"};
    String water_facility[] = {"-चयन करे-","खरीफ","रबी","गरमा"};
    String motarpump[] = {"-चयन करे-","बिद्युत","सोलर"};
    String motarpump_hp[] = {"-चयन करे-","1 HP","2 HP","3 HP","4 HP","5 HP"};
    String panchayat_Id="",panchayat_Name="",Vill_Id="",Vill_Name="",Dist_Id="",BlockId="",Jalshrot_id,Jalshrot_name,water_facility_Code="",water_facility_Name="",motarpump_sanchalan_code="",motarpump_sanchalan_Name="";
    String _edt_pum_loc_distnce="",_edt_motor_power_hp="",_edt_distribution_length="",_edt_distribution_pipe_inch="",_edt_distribution_pipe_meter="",_edt_apporx_command_area_hec="",_edt_yojna_lagat="";
    Button save_basic_detail_udvah;
    CheckBox chk_kharif,chk_rabi,chk_garma;
    String motarpump_power_code="",motarpump_power_name="",_water_avlbl_kharif="N",_water_avlbl_rabi="N",_water_avlbl_garma="N",chk_string="N";


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_udvah__sinchaai__yojna);
        dataBaseHelper= new DataBaseHelper(Udvah_Sinchaai_YojnaActivity.this);

        initialization();
    }
    public void initialization()
    {
        sp_panchayat=(Spinner)findViewById(R.id.sp_panchayat);
        sp_village=(Spinner)findViewById(R.id.sp_village);
        spn_jalshrot=(Spinner)findViewById(R.id.spn_jalshrot);
        spn_jalshrot_available=(Spinner)findViewById(R.id.spn_jalshrot_available);
        spn_motarpump_sanchalan=(Spinner)findViewById(R.id.spn_motarpump_sanchalan);

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
        spn_jalshrot_available.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                if (arg2 > 0) {

                    water_facility_Name = water_facility[arg2].toString();
                    if (water_facility_Name.equalsIgnoreCase("खरीफ")) {
                        water_facility_Code = "1";
                    }
                    else if (water_facility_Name.equalsIgnoreCase("रबी")) {
                        water_facility_Code = "2";
                    }
                    else if (water_facility_Name.equalsIgnoreCase("गरमा")) {
                        water_facility_Code = "3";
                    }
                }else {
                    water_facility_Code = "";
                    water_facility_Name = "";
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
            public void onClick(View v) {
                if(validateData())
                {
                    setvalue();
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
        String userid= PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("uid", "");
        String dist_id= PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("dist_id", "");
        String blk_id= PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("blk_id", "");
        long id = 0;
        inspectionDetailsModel.setDistCode(dist_id);
        inspectionDetailsModel.setBlockCode(blk_id);
        inspectionDetailsModel.setPanchayatCode(panchayat_Id);
        inspectionDetailsModel.setVILLCODE(Vill_Id);
        inspectionDetailsModel.setWaterSourceId(Jalshrot_id);
        inspectionDetailsModel.setWaterSourceName(Jalshrot_name);
        inspectionDetailsModel.setWaterAvailable_Kharif(_water_avlbl_kharif);
        inspectionDetailsModel.setWaterAvailable_Rabi(_water_avlbl_rabi);
        inspectionDetailsModel.setWaterAvailable_Garma(_water_avlbl_garma);
        inspectionDetailsModel.setPumplocation_distance(_edt_pum_loc_distnce);
        inspectionDetailsModel.setMotor_Pump_Power(motarpump_power_code);
        inspectionDetailsModel.setDistributionChannelLength(_edt_distribution_length);
        inspectionDetailsModel.setDistributionpipelngth_inch(_edt_distribution_pipe_inch);
        inspectionDetailsModel.setDistributionpipelngth_mtr(_edt_distribution_pipe_meter);
        inspectionDetailsModel.setApproxCommandArea(_edt_apporx_command_area_hec);
        inspectionDetailsModel.setSchemeApproxAmt(_edt_yojna_lagat);

        // inspectionDetailsModel.setDistributionChannelLength(Flag_IsDataWrong);

        // inspectionDetailsModel.set_EntryBy(userid.toLowerCase());

        id = new DataBaseHelper(Udvah_Sinchaai_YojnaActivity.this).InsertInspectionUdvahDetail(inspectionDetailsModel);

        if (id > 0)
        {
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

    private boolean validateData() {
        View focusView = null;
        boolean validate = true;

        if(panchayat_Id.equalsIgnoreCase(""))
        {
            Toast.makeText(getApplicationContext(), "Please select Panchayat", Toast.LENGTH_LONG).show();
            validate = false;
        }


        if(Vill_Id.equalsIgnoreCase(""))
        {
            Toast.makeText(getApplicationContext(), "Please select village", Toast.LENGTH_LONG).show();
            validate = false;
        }

        if(Jalshrot_name.equalsIgnoreCase(""))
        {
            Toast.makeText(getApplicationContext(), "Please select jalshrot", Toast.LENGTH_LONG).show();
            validate = false;
        }

        if(chk_string.equalsIgnoreCase("N"))
        {
            Toast.makeText(getApplicationContext(), "Please select atleast one season for water availability", Toast.LENGTH_LONG).show();
            validate = false;
        }

        if(motarpump_sanchalan_Name.equalsIgnoreCase(""))
        {
            Toast.makeText(getApplicationContext(), "Please select motorpump energy type", Toast.LENGTH_LONG).show();
            validate = false;
        }

        if(_edt_pum_loc_distnce.equalsIgnoreCase(""))
        {
            edt_pump_loc_dist.setError("Please distance of pump location");
            Toast.makeText(getApplicationContext(), "Please distance of pump location", Toast.LENGTH_LONG).show();
            validate = false;
        }

//        if(_edt_motor_power_hp.equalsIgnoreCase("")){
//            edt_motor_power_hp.setError("Please enter motor power");
//            Toast.makeText(getApplicationContext(), "Please enter motor power", Toast.LENGTH_LONG).show();
//            validate = false;
//        }
//        if(Integer.parseInt(_edt_motor_power_hp)<1 && Integer.parseInt(_edt_motor_power_hp)>5)
//        {
//
//            Toast.makeText(getApplicationContext(), "Motor power should not be more than 5 hp", Toast.LENGTH_LONG).show();
//            validate = false;
//        }

        if(_edt_distribution_length.equalsIgnoreCase(""))
        {
            edt_distribution_length.setError("Please enter distribution channel length");
            Toast.makeText(getApplicationContext(), "Please enter distribution channel length", Toast.LENGTH_LONG).show();
            validate = false;
        }

        if(_edt_distribution_pipe_inch.equalsIgnoreCase(""))
        {
            edt_distribution_pipe_inch.setError("Please enter distribution channel length in inch");
            Toast.makeText(getApplicationContext(), "Please enter distributuion pipe length in inch", Toast.LENGTH_LONG).show();
            validate = false;
        }

//        if(_edt_distribution_pipe_inch.equalsIgnoreCase("")){
//            Toast.makeText(getApplicationContext(), "Please distributuion pipe length in inch", Toast.LENGTH_LONG).show();
//            validate = false;
//        }

        if(_edt_distribution_pipe_meter.equalsIgnoreCase(""))
        {
            edt_distribution_pipe_meter.setError("Please enter distribution channel length in meter");
            Toast.makeText(getApplicationContext(), "Please distributuion pipe length in meter", Toast.LENGTH_LONG).show();
            validate = false;
        }

        if(_edt_apporx_command_area_hec.equalsIgnoreCase(""))
        {
            edt_apporx_command_area_hec.setError("Please enter approx command area in heactare");
            Toast.makeText(getApplicationContext(), "Please enter approx command area in heactare", Toast.LENGTH_LONG).show();
            validate = false;
        }

        if(_edt_yojna_lagat.equalsIgnoreCase(""))
        {
            edt_yojna_lagat.setError("Please enter yojana lagat");
            Toast.makeText(getApplicationContext(), "Please enter yojana lagat", Toast.LENGTH_LONG).show();
            validate = false;
        }

        if(focusView != null && focusView.requestFocus())
        {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
        return validate;

    }

}
