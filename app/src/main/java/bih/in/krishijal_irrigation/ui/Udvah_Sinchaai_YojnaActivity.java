package bih.in.krishijal_irrigation.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Spinner;

import bih.in.krishijal_irrigation.R;

public class Udvah_Sinchaai_YojnaActivity extends Activity {
    Spinner sp_panchayat,sp_village,spn_jalshrot,spn_jalshrot_available,spn_motarpump_sanchalan;
    EditText edt_sincht_totArea_dec,edt_motor_power,edt_distribution_length,edt_distribution_pipe_inch,edt_distribution_pipe_meter,edt_apporx_command_area_hec,edt_yojna_lagat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_udvah__sinchaai__yojna);

        initialization();
    }
    public void initialization(){
        sp_panchayat=(Spinner)findViewById(R.id.sp_panchayat);
        sp_village=(Spinner)findViewById(R.id.sp_village);
        spn_jalshrot=(Spinner)findViewById(R.id.spn_jalshrot);
        spn_jalshrot_available=(Spinner)findViewById(R.id.spn_jalshrot_available);
        spn_motarpump_sanchalan=(Spinner)findViewById(R.id.spn_motarpump_sanchalan);

        edt_sincht_totArea_dec=(EditText) findViewById(R.id.edt_sincht_totArea_dec);
        edt_motor_power=(EditText) findViewById(R.id.edt_motor_power);
        edt_distribution_length=(EditText) findViewById(R.id.edt_distribution_length);
        edt_distribution_pipe_inch=(EditText) findViewById(R.id.edt_distribution_pipe_inch);
        edt_distribution_pipe_meter=(EditText) findViewById(R.id.edt_distribution_pipe_meter);
        edt_apporx_command_area_hec=(EditText) findViewById(R.id.edt_apporx_command_area_hec);
        edt_yojna_lagat=(EditText) findViewById(R.id.edt_yojna_lagat);
    }
}
