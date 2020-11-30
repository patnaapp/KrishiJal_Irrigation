package bih.in.krishijal_irrigation.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;

import bih.in.krishijal_irrigation.entity.InspectionDetailsModel;
import bih.in.krishijal_irrigation.entity.PanchayatData;
import bih.in.krishijal_irrigation.entity.UserDetails;
import bih.in.krishijal_irrigation.entity.VillageListEntity;

public class DataBaseHelper extends SQLiteOpenHelper {
    //private static String DB_PATH = "";
    private static String DB_PATH = "/data/data/app.bih.in.nic.epacsmgmt/databases/";
    //private static String DB_NAME = "eCountingAC.sqlite";
    //private static String DB_NAME = "PACSDB1";
    private static String DB_NAME = "krishijal.db";

    private SQLiteDatabase myDataBase;
    private final Context myContext;

    SQLiteDatabase db;

    public DataBaseHelper(Context context) {

        super(context, DB_NAME, null, 1);
        if (android.os.Build.VERSION.SDK_INT >= 4.2) {


            DB_PATH = context.getApplicationInfo().dataDir + "/databases/";
        } else {
            DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";
        }
        this.myContext = context;
    }



    /**
     * Creates a empty database on the system and rewrites it with your own
     * database.
     */
    public void createDataBase() throws IOException {

        boolean dbExist = checkDataBase();

        if (dbExist) {
            // do nothing - database already exist


        } else {

            // By calling this method and empty database will be created into
            // the default system path
            // of your application so we are gonna be able to overwrite that
            // database with our database.
            this.getReadableDatabase();

            try {

                copyDataBase();

            } catch (IOException e) {

                throw new Error("Error copying database");

            }
        }

    }

    /**
     * Check if the database already exist to avoid re-copying the file each
     * time you open the application.
     *
     * @return true if it exists, false if it doesn't
     */
    private boolean checkDataBase() {
        SQLiteDatabase checkDB = null;

        try {
            String myPath = DB_PATH + DB_NAME;
            //this.getReadableDatabase();

            checkDB = SQLiteDatabase.openDatabase(myPath, null,
                    SQLiteDatabase.NO_LOCALIZED_COLLATORS | SQLiteDatabase.OPEN_READWRITE);


        } catch (SQLiteException e) {

            // database does't exist yet.

        }

        if (checkDB != null) {

            checkDB.close();

        }

        return checkDB != null ? true : false;

    }

    public boolean databaseExist() {


        File dbFile = new File(DB_PATH + DB_NAME);

        return dbFile.exists();
    }

    /**
     * Copies your database from your local assets-folder to the just created
     * empty database in the system folder, from where it can be accessed and
     * handled. This is done by transfering bytestream.
     */
    private void copyDataBase() throws IOException {

        // Open your local db as the input stream
        InputStream myInput = myContext.getAssets().open(DB_NAME);

        // Path to the just created empty db
        String outFileName = DB_PATH + DB_NAME;

        // Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);
        this.getReadableDatabase().close();
        // transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }

        // Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();


    }

    public void openDataBase() throws SQLException {

        // Open the database
        this.getReadableDatabase();
        String myPath = DB_PATH + DB_NAME;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null,
                SQLiteDatabase.OPEN_READONLY);

    }

    @Override
    public synchronized void close() {

        if (myDataBase != null)
            myDataBase.close();

        super.close();

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        db.execSQL("ALTER TABLE Ward ADD COLUMN AreaType TEXT");
//        db.execSQL("ALTER TABLE OtherDept_Of_Rural_Dev_Dept ADD COLUMN VillageID TEXT");
//        db.execSQL("ALTER TABLE Menrega_Rural_Dev_Dept ADD COLUMN Work_Structure_Type_Name TEXT");
//        db.execSQL("ALTER TABLE Menrega_Rural_Dev_Dept ADD COLUMN Work_Structure_Type_Other_Name TEXT");
//        db.execSQL("ALTER TABLE OtherDept_Of_Rural_Dev_Dept ADD COLUMN Work_Structure_Type_Name TEXT");
//        db.execSQL("ALTER TABLE OtherDept_Of_Rural_Dev_Dept ADD COLUMN Work_Structure_Type_Other_Name TEXT");

        //modifyTable();
    }

    public void modifyTable(){

        if(isColumnExists("Ward", "AreaType") == false){
            AlterTable("Ward", "AreaType");
        }

//        if(!isColumnExists("OtherDept_Of_Rural_Dev_Dept", "VillageID")){
//            AlterTable("OtherDept_Of_Rural_Dev_Dept", "VillageID");
//        }
//
//
//        if(!isColumnExists("OtherDept_Of_Rural_Dev_Dept", "Work_Structure_Type_Name")){
//            AlterTable("OtherDept_Of_Rural_Dev_Dept", "Work_Structure_Type_Name");
//        }
//
//        if(!isColumnExists("OtherDept_Of_Rural_Dev_Dept", "Work_Structure_Type_Other_Name")){
//            AlterTable("OtherDept_Of_Rural_Dev_Dept", "Work_Structure_Type_Other_Name");
//        }

//        AlterManregTable("Menrega_Rural_Dev_Dept");
//        AlterManregTable("OtherDept_Of_Rural_Dev_Dept");
    }


    public void AlterTable(String tableName, String columnName)
    {
        db = this.getReadableDatabase();

        try{

            db.execSQL("ALTER TABLE "+tableName+" ADD COLUMN "+columnName+" TEXT");
            Log.e("ALTER Done",tableName +"-"+ columnName);
        }
        catch (Exception e)
        {
            Log.e("ALTER Failed",tableName +"-"+ columnName);
        }
    }

    public boolean isColumnExists (String table, String column) {
        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("PRAGMA table_info("+ table +")", null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String name = cursor.getString(cursor.getColumnIndex("name"));
                if (column.equalsIgnoreCase(name)) {
                    return true;
                }
            }
        }
        cursor.close();
        return false;
    }
    public long getUserCount() {

        long x = 0;
        try {

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cur = db.rawQuery("Select * from UserDetail", null);

            x = cur.getCount();

            cur.close();
            db.close();
        } catch (Exception e) {
            // TODO: handle exception
        }
        return x;
    }

    public long insertUserDetails(UserDetails result) {

        long c = 0;
        try {

            SQLiteDatabase db = this.getReadableDatabase();

            ContentValues values = new ContentValues();

            values.put("IsMobileUpdated", result.getIsMobileUpdated());
            values.put("Password", result.getPassword());
            values.put("UserID", result.getUserID());
            values.put("DistrictCode", result.getDistrictCode());
            values.put("DistName", result.getDistName());
            values.put("BlockCode", result.getBlockCode());
            values.put("BlockName", result.getBlockName());
            values.put("PanchayatCode", result.getPanchayatCode());
            values.put("PanchayatName", result.getPanchayatName());
            values.put("Userrole", result.getUserrole());
            values.put("Name", result.getName());

            String[] whereArgs = new String[]{result.getUserID()};

            c = db.update("UserDetail", values, "UserID=? ", whereArgs);

            if (!(c > 0))
            {
                //c = db.insert("UserDetail", null, values);
                c = db.insertWithOnConflict("UserDetail", null, values,SQLiteDatabase.CONFLICT_REPLACE);
            }

            db.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            // TODO: handle exception
        }
        return c;
    }


    public UserDetails getSurfaceUserDetails(String userId, String pass) {

        UserDetails userInfo = null;

        try {

            SQLiteDatabase db = this.getReadableDatabase();

            String[] params = new String[]{userId, pass};

            Cursor cur = db.rawQuery(
                    "Select * from UserDetail WHERE UserID=? and UserPassword=?",
                    params);
            int x = cur.getCount();

            while (cur.moveToNext()) {


                userInfo = new UserDetails();
                userInfo.setUserID(cur.getString(cur.getColumnIndex("UserID")));
                userInfo.setName(cur.getString(cur.getColumnIndex("UserName")));
                userInfo.setPassword(cur.getString(cur.getColumnIndex("UserPassword")));

                userInfo.setMobileNo(cur.getString(cur.getColumnIndex("MobileNo")));
                userInfo.setEmail(cur.getString(cur.getColumnIndex("Email")));

                userInfo.setUserroleId(cur.getString(cur.getColumnIndex("RoleId")));
                userInfo.setUserrole(cur.getString(cur.getColumnIndex("Role")));
                userInfo.setAuthenticated(true);
       }

            cur.close();
            db.close();

        } catch (Exception e) {
            // TODO: handle exception
            userInfo = null;
        }
        return userInfo;
    }

    public UserDetails getUserDetails(String userId, String pass) {

        UserDetails userInfo = null;

        try {

            SQLiteDatabase db = this.getReadableDatabase();

            String[] params = new String[]{userId.trim(), pass};

            Cursor cur = db.rawQuery(
                    "Select * from UserDetail WHERE UserID=? and UserPassword=?",
                    params);
            int x = cur.getCount();

            while (cur.moveToNext()) {


                userInfo = new UserDetails();
//                userInfo.setUserId(cur.getString(cur.getColumnIndex("UserID")));
        }

            cur.close();
            db.close();

        } catch (Exception e) {
            // TODO: handle exception
            userInfo = null;
        }
        return userInfo;
    }

    public long deleteSchemeRecord(){

        long c = -1;

        try {

            SQLiteDatabase db = this.getWritableDatabase();
            db.execSQL("delete from SurfaceSchemeDetail");

            db.close();

        } catch (Exception e) {
            // TODO: handle exception
            return c;
        }
        return c;
    }

    public long ResetSchemeInspectionDetail(String schemeId) {

        long c = 0;
        try {

            SQLiteDatabase db = this.getReadableDatabase();

            ContentValues values = new ContentValues();

            values.put("Updated", "0");
            values.putNull("CrossVerification");
            values.putNull("InspectionBy");
            values.putNull("SurveyorName");
            values.putNull("SurvyorPhone");
            values.putNull("InspectionDate");
            values.putNull("WorkStatus");
            values.putNull("WorkCompletionPer");
            values.putNull("WorkDone");
            values.putNull("ObservationCategory");
            values.putNull("Latitude");
            values.putNull("Longitude");
            values.putNull("Photo1");
            values.putNull("Photo2");
            values.putNull("Photo3");
            values.putNull("photo4");
            values.putNull("Comment1");
            values.putNull("Comment2");
            values.putNull("Comment3");
            values.putNull("Comment4");
            values.putNull("AppVersion");

            String[] whereArgs = new String[]{schemeId};

            c = db.update("SurfaceSchemeDetail", values, "SCHEME_ID=? ", whereArgs);

            db.close();
        } catch (Exception e) {
            e.printStackTrace();
            // TODO: handle exception
            return 0;
        }
        return c;

    }




    public long setPanchayatDataToLocal(UserDetails userInfo, ArrayList<PanchayatData> list) {
        // String tableName = type == "pond" ? "PondInspectionDetail" : "WellInspectionDetail";

        long c = -1;

        DataBaseHelper dh = new DataBaseHelper(myContext);
        try {
            dh.createDataBase();


        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
            return -1;
        }

        ArrayList<PanchayatData> info = list;


        if (info != null) {
            try {
                SQLiteDatabase db = this.getReadableDatabase();

                ContentValues values = new ContentValues();

                for (int i = 0; i < info.size(); i++) {

                    values.put("PanchayatCode", info.get(i).getPcode());
                    values.put("PanchayatName", info.get(i).getPname());
                    values.put("PACName", info.get(i).getAreaType());

                    values.put("BlockCode", userInfo.getBlockCode());
                    values.put("DistrictCode", userInfo.getDistrictCode());
                    values.put("DistrictName", userInfo.getDistName());
                    values.put("PartNo", "2");

                    String[] whereArgs = new String[]{String.valueOf(info.get(i).getPcode())};

                    c = db.update("Panchayat", values, "PanchayatCode=?", whereArgs);

                    if(c != 1){
                        c = db.insert("Panchayat", null, values);
                    }



                }
                db.close();

            } catch (Exception e) {
                e.printStackTrace();
                return c;
            }
        }
        return c;
    }


    public static String getCurrentDate() {
        Calendar cal = Calendar.getInstance();
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int month = cal.get(Calendar.MONTH);
        int year = cal.get(Calendar.YEAR);

        int h = cal.get(Calendar.HOUR);
        int m = cal.get(Calendar.MINUTE);
        int s = cal.get(Calendar.SECOND);

        String date = day + "/" + month + "/" + year + "  " + h + ":" + m + ":" + s;
        return date;

    }

    public long deletePendingUpload2(String pid, String userId) {
        long c = -1;

        try {

            SQLiteDatabase db = this.getWritableDatabase();
            String[] DeleteWhere = {String.valueOf(pid), userId};
            c = db.delete("UploadDataforGps", "sl_no=? and userId=?", DeleteWhere);

            db.close();

        } catch (Exception e) {
            // TODO: handle exception
            return c;
        }
        return c;
    }




    public String getPanchayatName(String pcode, Context context) {
        DataBaseHelper helper = new DataBaseHelper(context);
        SQLiteDatabase db = helper.getReadableDatabase();

        String kname = "";

        String[] whereArgs = new String[]{pcode};
        Cursor c = db.rawQuery(
                "select * from Panchayat where Code=?",
                whereArgs);

        if (c.getCount() > 0) {
            c.moveToNext();
            kname = c.getString(c.getColumnIndex("Name"));

        }
        c.close();


        return kname;

    }





    public ArrayList<VillageListEntity> getVillageList(String Pan_Code) {
        ArrayList<VillageListEntity> deptList = new ArrayList<VillageListEntity>();
        try {

            SQLiteDatabase db = this.getReadableDatabase();
            String[] params = new String[] { Pan_Code };

            Cursor cur = db
                    .rawQuery(
                            "SELECT * from VillageList WHERE PanCode= ?", params);
            int x = cur.getCount();

            while (cur.moveToNext()) {

                VillageListEntity dept = new VillageListEntity();
                dept.setVillCode(cur.getString(cur.getColumnIndex("VillageCode")));
                dept.setVillName(cur.getString(cur.getColumnIndex("VillageName")));
                dept.setPanchayatCode(cur.getString(cur.getColumnIndex("PanCode")));
                //dept.setBlockCode(cur.getString(cur.getColumnIndex("BLOCKCODE")));

                deptList.add(dept);
            }

            cur.close();
            db.close();

        } catch (Exception e) {
            e.printStackTrace();
            // TODO: handle exception

        }
        return deptList;
    }

    public ArrayList<PanchayatData> getPanchayt(String blockCode) {
        ArrayList<PanchayatData> panchayatList = new ArrayList<PanchayatData>();
        try {

            SQLiteDatabase db = this.getReadableDatabase();
            String[] params = new String[] { blockCode };

            Cursor cur = db
                    .rawQuery(
                            "SELECT * from Panchayat WHERE BlockCode = ? ORDER BY PanchayatName",
                            params);
            int x = cur.getCount();

            while (cur.moveToNext()) {

                PanchayatData panchayat = new PanchayatData();
                panchayat.setPcode(cur.getString(cur.getColumnIndex("PanchayatCode")));
                panchayat.setPname(cur.getString(cur.getColumnIndex("PanchayatName")));
                panchayat.setBcode(cur.getString(cur.getColumnIndex("BlockCode")));
                //panchayat.setDcode(cur.getString(cur.getColumnIndex("DistrictCode")));
                //panchayat.setAreaType(cur.getString(cur.getColumnIndex("PACName")));

                panchayatList.add(panchayat);
            }

            cur.close();
            db.close();

        } catch (Exception e) {
            e.printStackTrace();
            // TODO: handle exception

        }
        return panchayatList;
    }



  /*  public String getPostWhereConditionForStudentListForAttendance1(String finyr, String schemeid, String fund_id, String scheme_type, String distid) {

        String subWhere=" ";

        if(!finyr.equalsIgnoreCase("0"))
        {
            subWhere += "AND FINANCIAL_YEAR='"+finyr+"'";
        }
        if(!schemeid.equalsIgnoreCase(""))
        {
            subWhere += " AND SCHEME_ID='"+ schemeid +"'";
        }
        if(!fund_id.equalsIgnoreCase("0"))
        {
            subWhere += " AND Fund_Type='"+ fund_id  +"'";
        }
        if(!scheme_type.equalsIgnoreCase("0"))
        {
            subWhere += " AND TYPE_OF_SCHEME='"+ scheme_type  +"'";
        }
        if(!distid.equalsIgnoreCase(""))
        {
            subWhere += " AND District='"+ distid  +"'";
        }

        Log.e("SUBQUERY",subWhere);
        return subWhere;
    }*/




  // new tables



    public long InsertInspectionDetail(InspectionDetailsModel vdata) {

        long c = -1;
        SQLiteDatabase db = this.getWritableDatabase();
        //c = db.delete("VillageList", null, null);
        ContentValues values = new ContentValues();
        try {

            values.put("InspectionId", vdata.getInspectionId());
            values.put("SchemeCode", vdata.getSchemeCode());
            values.put("DistCode", vdata.getDistCode());
            values.put("DistName", vdata.getDistName());
            values.put("BlockCode", vdata.getBlockCode());
            values.put("BlockName", vdata.getBlockName());
            values.put("PanchayatCode", vdata.getPanchayatCode());
            values.put("PanchayatName", vdata.getPanchayatName());
            values.put("VILLCODE", vdata.getVILLCODE());
            values.put("VillageName", vdata.getVillageName());
            values.put("WaterAvailable_Kharif", vdata.getWaterAvailable_Kharif());
            values.put("WaterAvailable_Rabi", vdata.getWaterAvailable_Rabi());
            values.put("WaterAvailable_Garma", vdata.getWaterAvailable_Garma());
            values.put("DistributionChannelLength", vdata.getDistributionChannelLength());
            values.put("DistributionPipeDiamater", vdata.getDistributionPipeDiamater());
            values.put("DistributionPipeLength", vdata.getDistributionPipeLength());
            values.put("ApproxCommandArea", vdata.getApproxCommandArea());
            values.put("SchemeApproxAmt", vdata.getSchemeApproxAmt());


            values.put("EnergyTypeId", vdata.getEnergyTypeId());
            values.put("EnergyTypeName", vdata.getEnergyTypeName());
            values.put("NoofNalkup", vdata.getNoofNalkup());
            values.put("NoOfPole", vdata.getNoOfPole());
            values.put("Motor_Pump_Power", vdata.getMotor_Pump_Power());
            values.put("WaterSourceId", vdata.getWaterSourceId());
            values.put("EnergyTypeName", vdata.getEnergyTypeName());
            values.put("Photo", vdata.getPhoto());

            String[] whereArgs = new String[]{vdata.getInspectionId()};
            c = db.update("tbl_InspectionDetails", values, "InspectionId=?", whereArgs);

            if (!(c > 0)) {

                c = db.insert("tbl_InspectionDetails", null, values);
            }
        }

        catch (Exception e) {
            e.printStackTrace();
            return c;
        }

        return c;
    }

    public long InsertInspectionDetailAahar(InspectionDetailsModel vdata) {

        long c = -1;
        SQLiteDatabase db = this.getWritableDatabase();
        //c = db.delete("VillageList", null, null);
        ContentValues values = new ContentValues();
        try {

            values.put("InspectionId", vdata.getInspectionId());
            values.put("SchemeCode", vdata.getSchemeCode());
            values.put("DistCode", vdata.getDistCode());
            values.put("DistName", vdata.getDistName());
            values.put("BlockCode", vdata.getBlockCode());
            values.put("BlockName", vdata.getBlockName());
            values.put("PanchayatCode", vdata.getPanchayatCode());
            values.put("PanchayatName", vdata.getPanchayatName());
            values.put("VILLCODE", vdata.getVILLCODE());
            values.put("VillageName", vdata.getVillageName());
            values.put("WaterAvailable_Kharif", vdata.getWaterAvailable_Kharif());
            values.put("WaterAvailable_Rabi", vdata.getWaterAvailable_Rabi());
            values.put("WaterAvailable_Garma", vdata.getWaterAvailable_Garma());
            values.put("DistributionChannelLength", vdata.getDistributionChannelLength());
            values.put("DistributionPipeDiamater", vdata.getDistributionPipeDiamater());
            values.put("DistributionPipeLength", vdata.getDistributionPipeLength());
            values.put("ApproxCommandArea", vdata.getApproxCommandArea());
            values.put("SchemeApproxAmt", vdata.getSchemeApproxAmt());


//            values.put("EnergyTypeId", vdata.getEnergyTypeId());
//            values.put("EnergyTypeName", vdata.getEnergyTypeName());
//            values.put("NoofNalkup", vdata.getNoofNalkup());
//            values.put("NoOfPole", vdata.getNoOfPole());
//            values.put("Motor_Pump_Power", vdata.getMotor_Pump_Power());
//            values.put("WaterSourceId", vdata.getWaterSourceId());
//            values.put("EnergyTypeName", vdata.getEnergyTypeName());
//            values.put("Photo", vdata.getPhoto());

            String[] whereArgs = new String[]{vdata.getInspectionId()};
            c = db.update("tbl_InspectionDetails", values, "InspectionId=?", whereArgs);

            if (!(c > 0)) {

                c = db.insert("tbl_InspectionDetails", null, values);
            }
        }

        catch (Exception e) {
            e.printStackTrace();
            return c;
        }

        return c;
    }

    public long InsertInspectionAaharDetail(InspectionDetailsModel vdata) {

        long c = -1;
        SQLiteDatabase db = this.getWritableDatabase();
        //c = db.delete("VillageList", null, null);
        ContentValues values = new ContentValues();
        try {

            values.put("InspectionId", vdata.getInspectionId());
            values.put("SchemeCode", vdata.getSchemeCode());
            values.put("DistCode", vdata.getDistCode());
            values.put("BlockCode", vdata.getBlockCode());
            values.put("PanchayatCode", vdata.getPanchayatCode());
            values.put("DistName", vdata.getDistName());
            values.put("BlockName", vdata.getBlockName());
            values.put("PanchayatName", vdata.getPanchayatName());
            values.put("VILLCODE", vdata.getVILLCODE());
            values.put("EnergyTypeId", vdata.getEnergyTypeId());
            values.put("EnergyTypeName", vdata.getEnergyTypeName());
            values.put("NoofNalkup", vdata.getNoofNalkup());
            values.put("NoOfPole", vdata.getNoOfPole());
            values.put("Motor_Pump_Power", vdata.getMotor_Pump_Power());
            values.put("DistributionChannelLength", vdata.getDistributionChannelLength());
            values.put("DistributionPipeDiamater", vdata.getDistributionpipelngth_inch());
            values.put("DistributionPipeLength", vdata.getDistributionChannelLength());
            values.put("ApproxCommandArea", vdata.getApproxCommandArea());
            values.put("SchemeApproxAmt", vdata.getSchemeApproxAmt());
            values.put("WaterSourceId", vdata.getWaterSourceId());
            values.put("WaterAvailable_Kharif", vdata.getWaterAvailable_Kharif());
            values.put("WaterAvailable_Rabi", vdata.getWaterAvailable_Rabi());
            values.put("WaterAvailable_Garma", vdata.getWaterAvailable_Garma());
            values.put("DistributionPaenLength", vdata.getDistributionPaenLength());
            values.put("EnergyTypeName", vdata.getEnergyTypeName());
            values.put("Photo", vdata.getPhoto());

            String[] whereArgs = new String[]{vdata.getInspectionId()};
            c = db.update("tbl_InspectionDetails", values, "InspectionId=?", whereArgs);

            if (!(c > 0)) {

                c = db.insert("tbl_InspectionDetails", null, values);
            }
        }

        catch (Exception e) {
            e.printStackTrace();
            return c;
        }

        return c;
    }

    public long InsertInspectionUdvahDetail(InspectionDetailsModel vdata) {

        long c = -1;
        SQLiteDatabase db = this.getWritableDatabase();
        //c = db.delete("VillageList", null, null);
        ContentValues values = new ContentValues();
        try {

            values.put("InspectionId", vdata.getInspectionId());
            values.put("SchemeCode", vdata.getSchemeCode());
            values.put("DistCode", vdata.getDistCode());
            values.put("BlockCode", vdata.getBlockCode());
            values.put("PanchayatCode", vdata.getPanchayatCode());
            values.put("DistName", vdata.getDistName());
            values.put("BlockName", vdata.getBlockName());
            values.put("PanchayatName", vdata.getPanchayatName());
            values.put("VILLCODE", vdata.getVILLCODE());
            values.put("EnergyTypeId", vdata.getEnergyTypeId());
            values.put("EnergyTypeName", vdata.getEnergyTypeName());
            values.put("NoofNalkup", vdata.getNoofNalkup());
            values.put("NoOfPole", vdata.getNoOfPole());
            values.put("Motor_Pump_Power", vdata.getMotor_Pump_Power());
            values.put("DistributionChannelLength", vdata.getDistributionChannelLength());
            values.put("DistributionPipeDiamater", vdata.getDistributionpipelngth_inch());
            values.put("DistributionPipeLength", vdata.getDistributionChannelLength());
            values.put("ApproxCommandArea", vdata.getApproxCommandArea());
            values.put("SchemeApproxAmt", vdata.getSchemeApproxAmt());
            values.put("WaterSourceId", vdata.getWaterSourceId());
            values.put("WaterAvailable_Kharif", vdata.getWaterAvailable_Kharif());
            values.put("WaterAvailable_Rabi", vdata.getWaterAvailable_Rabi());
            values.put("WaterAvailable_Garma", vdata.getWaterAvailable_Garma());
            values.put("DistributionPaenLength", vdata.getDistributionPaenLength());
            values.put("EnergyTypeName", vdata.getEnergyTypeName());
            values.put("Photo", vdata.getPhoto());

            String[] whereArgs = new String[]{vdata.getInspectionId()};
            c = db.update("tbl_InspectionDetails", values, "InspectionId=?", whereArgs);

            if (!(c > 0)) {

                c = db.insert("tbl_InspectionDetails", null, values);
            }
        }

        catch (Exception e) {
            e.printStackTrace();
            return c;
        }

        return c;
    }
    public long setPanchayatLocal(ArrayList<PanchayatData> list,String Blk_Code) {
        // String tableName = type == "pond" ? "PondInspectionDetail" : "WellInspectionDetail";

        long c = -1;

        DataBaseHelper dh = new DataBaseHelper(myContext);
        try {
            dh.createDataBase();


        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
            return -1;
        }

        ArrayList<PanchayatData> info = list;


        if (info != null) {
            try {
                SQLiteDatabase db = this.getReadableDatabase();

                ContentValues values = new ContentValues();

                for (int i = 0; i < info.size(); i++) {

                    values.put("PanchayatCode", info.get(i).getPcode());
                    values.put("PanchayatName", info.get(i).getPname());
                    values.put("PanchayatNameHnd", info.get(i).getAreaType());
                    values.put("BlockCode", Blk_Code);

                    String[] whereArgs = new String[]{String.valueOf(info.get(i).getPcode())};

                    c = db.update("Panchayat", values, "PanchayatCode=?", whereArgs);

                    if(c != 1){
                        c = db.insert("Panchayat", null, values);
                    }

                }
                db.close();

            } catch (Exception e) {
                e.printStackTrace();
                return c;
            }
        }
        return c;
    }
    public long setVillageDataToLocal(ArrayList<VillageListEntity> list,String Pan_Code) {
        // String tableName = type == "pond" ? "PondInspectionDetail" : "WellInspectionDetail";

        long c = -1;

        DataBaseHelper dh = new DataBaseHelper(myContext);
        try {
            dh.createDataBase();


        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
            return -1;
        }

        ArrayList<VillageListEntity> info = list;


        if (info != null) {
            try {
                SQLiteDatabase db = this.getReadableDatabase();

                ContentValues values = new ContentValues();

                for (int i = 0; i < info.size(); i++) {

                    values.put("VillageCode", info.get(i).getVillCode());
                    values.put("VillageName", info.get(i).getVillName());
                    values.put("PanCode", Pan_Code);
                    //values.put("BLOCKCODE", info.get(i).getBlockCode());

                    String[] whereArgs = new String[]{String.valueOf(info.get(i).getVillCode())};

                    c = db.update("VillageList", values, "VillageCode=?", whereArgs);

                    if(c != 1){
                        c = db.insert("VillageList", null, values);
                    }

                    //Log.e("Panchayat", info.get(i).getPanchayatCode());
                    //Log.e("VillageCode", info.get(i).getVillCode());
                    Log.e("C", String.valueOf(c));

                }
                db.close();

            } catch (Exception e) {
                e.printStackTrace();
                return c;
            }
        }
        return c;
    }
}