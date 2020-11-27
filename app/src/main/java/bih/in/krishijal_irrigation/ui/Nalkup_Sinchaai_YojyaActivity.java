package bih.in.krishijal_irrigation.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import bih.in.krishijal_irrigation.R;

public class Nalkup_Sinchaai_YojyaActivity extends AppCompatActivity {
    LinearLayout ll;
    Spinner sp_panchayat,sp_village,sp_option_power,sp_dist_khatakhesra;
    EditText edt_no_of_nalkup,edt_pole_length,edt_pipe_Perimeter_inch,edt_pipe_lingth_meter,edt_apporx_command_area_hec,edt_yojna_price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nalkup__sinchaai__yojya);
        Initialization();
    }


    private void Initialization(){
        sp_panchayat=(Spinner)findViewById(R.id.sp_panchayat);
        sp_village=(Spinner)findViewById(R.id.sp_village);
        sp_option_power=(Spinner)findViewById(R.id.sp_option_power);
        sp_dist_khatakhesra=(Spinner)findViewById(R.id.sp_dist_khatakhesra);
        edt_no_of_nalkup=(EditText)findViewById(R.id.edt_no_of_nalkup);
        edt_pole_length=(EditText)findViewById(R.id.edt_pole_length);
        edt_pipe_Perimeter_inch=(EditText)findViewById(R.id.edt_pipe_Perimeter_inch);
        edt_pipe_lingth_meter=(EditText)findViewById(R.id.edt_pipe_lingth_meter);
        edt_apporx_command_area_hec=(EditText)findViewById(R.id.edt_apporx_command_area_hec);
        edt_yojna_price=(EditText)findViewById(R.id.edt_yojna_price);
    }




}
