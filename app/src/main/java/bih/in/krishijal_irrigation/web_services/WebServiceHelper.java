package bih.in.krishijal_irrigation.web_services;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.protocol.HTTP;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import bih.in.krishijal_irrigation.entity.InspectionDetailsModel;
import bih.in.krishijal_irrigation.entity.PanchayatData;
import bih.in.krishijal_irrigation.entity.UserDetails;
import bih.in.krishijal_irrigation.entity.Versioninfo;
import bih.in.krishijal_irrigation.entity.VillageListEntity;

import static org.apache.http.util.EntityUtils.getContentCharSet;

public class WebServiceHelper {

    //public static final String SERVICENAMESPACE = "http://minorirrigation.bihar.gov.in/";
    public static final String SERVICENAMESPACE = "http://10.133.20.159/";

    public static final String SERVICEURL1 = "http://10.133.20.159/testservice/kjalwebservice.asmx";


    public static final String APPVERSION_METHOD = "getAppLatest";
    public static final String AUTHENTICATE_METHOD = "Authenticate";
    public static final String INSERT_DETAIL = "InsertInspection";



    private static final String FIELD_METHOD = "getFieldInformation";
    private static final String SPINNER_METHOD = "getSpinnerInformation";
    //private static final String UPLOAD_METHOD = "InsertData";
    private static final String REGISTER_USER = "RegisterUser";

    private static final String BLOCK_METHOD = "getBlock";

    private static final String GETINITIALPLANTATIONDATA = "getInitialDetailRDDPlantation";
    private static final String PONDLAKEENCRCHMNTDATA = "getInitialDetailsPondLakeDataCoVerified";
    private static final String WELLNCRCHMNTDATA = "getInitialDetailsWellDataCoVerified";
    private static final String GETPLANTATIONINSPECTIONDETAIL = "getPlantationInspdetails";
    private static final String WELLINSPECTIONLIST = "getWellInspectionList";
    private static final String UPLOADPLANTATIONINSPECTIONDETAIL = "PlantationInspDetails";
    private static final String UPLOADSCHEMEINSPECTIONDETAIL = "Inspection_Insert";
    private static final String GETVILLAGELIST = "getVillageList";
    private static final String GETPLANATATIONSITELIST = "getPlantationSite";
    private static final String GETSANRACHNATYPELIST = "getTypesOfSanrchnaList";
    private static final String GETWARDLIST = "getWardList";
    private static final String GETPANCHAYATLIST = "getPanchayat";
    private static final String GETDISTRICTLIST = "Districts_Select";

    static String rest;

    public static Versioninfo CheckVersion(String version) {
        Versioninfo versioninfo;
        SoapObject res1;
        try {

            res1=getServerData(APPVERSION_METHOD, Versioninfo.Versioninfo_CLASS,"IMEI","Ver","0",version);
            SoapObject final_object = (SoapObject) res1.getProperty(0);

            versioninfo = new Versioninfo(final_object);

        } catch (Exception e) {

            return null;
        }
        return versioninfo;

    }




    public static String resizeBase64Image(String base64image){
        byte [] encodeByte= Base64.decode(base64image.getBytes(), Base64.DEFAULT);
        BitmapFactory.Options options=new BitmapFactory.Options();
        options.inPurgeable = true;
        Bitmap image = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length,options);


        if(image.getHeight() <= 200 && image.getWidth() <= 200){
            return base64image;
        }
        image = Bitmap.createScaledBitmap(image, 100, 100, false);

        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG,100, baos);

        byte [] b=baos.toByteArray();
        System.gc();
        return Base64.encodeToString(b, Base64.NO_WRAP);

    }

    public static UserDetails Login(String User_ID, String Pwd) {
        try {
            SoapObject res1;
            res1=getServerData(AUTHENTICATE_METHOD, UserDetails.getUserClass(),"UserID","Password",User_ID,Pwd);
            if (res1 != null) {
                return new UserDetails(res1);
            } else
                return null;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }




    public static ArrayList<VillageListEntity> getVillageListData(String BlockCode) {

        SoapObject res1;
        res1=getServerData(GETVILLAGELIST, VillageListEntity.VillageList_CLASS,"PanchayatCode",BlockCode);
        int TotalProperty=0;
        if(res1!=null) TotalProperty= res1.getPropertyCount();

        ArrayList<VillageListEntity> fieldList = new ArrayList<VillageListEntity>();

        for (int i = 0; i < TotalProperty; i++) {
            if (res1.getProperty(i) != null) {
                Object property = res1.getProperty(i);
                if (property instanceof SoapObject) {
                    SoapObject final_object = (SoapObject) property;
                    VillageListEntity villageData= new VillageListEntity(final_object);
                    fieldList.add(villageData);
                }
            } else
                return fieldList;
        }

        return fieldList;
    }

    public static ArrayList<PanchayatData> getPanchayatList(String BlockCode) {

        SoapObject res1;
        res1=getServerData(GETPANCHAYATLIST, PanchayatData.PanchayatData_CLASS,"BlockCode",BlockCode);
        int TotalProperty=0;
        if(res1!=null) TotalProperty= res1.getPropertyCount();

        ArrayList<PanchayatData> fieldList = new ArrayList<PanchayatData>();

        for (int i = 0; i < TotalProperty; i++) {
            if (res1.getProperty(i) != null) {
                Object property = res1.getProperty(i);
                if (property instanceof SoapObject) {
                    SoapObject final_object = (SoapObject) property;
                    PanchayatData villageData= new PanchayatData(final_object);
                    fieldList.add(villageData);
                }
            } else
                return fieldList;
        }

        return fieldList;
    }






    public static SoapObject getServerData(String methodName, Class bindClass, String param, String value )
    {
        SoapObject res1;
        try {
            SoapObject request = new SoapObject(SERVICENAMESPACE,methodName);
            request.addProperty(param,value);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            envelope.addMapping(SERVICENAMESPACE,bindClass.getSimpleName(),bindClass);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(SERVICEURL1);
            androidHttpTransport.call(SERVICENAMESPACE + methodName,envelope);
            res1 = (SoapObject) envelope.getResponse();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return res1;
    }



    public static SoapObject getServerData(String methodName, Class bindClass, String param1, String param2, String value1, String value2 )
    {
        SoapObject res1;
        try {
            SoapObject request = new SoapObject(SERVICENAMESPACE,methodName);
            request.addProperty(param1,value1);
            request.addProperty(param2,value2);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            envelope.addMapping(SERVICENAMESPACE,bindClass.getSimpleName(),bindClass);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(SERVICEURL1);
            androidHttpTransport.call(SERVICENAMESPACE + methodName,envelope);
            res1 = (SoapObject) envelope.getResponse();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return res1;
    }

    public static SoapObject getServerData(String methodName, Class bindClass, String param1, String param2, String param3, String value1, String value2, String value3 )
    {
        SoapObject res1;
        try {
            SoapObject request = new SoapObject(SERVICENAMESPACE,methodName);
            request.addProperty(param1,value1);
            request.addProperty(param2,value2);
            request.addProperty(param3,value3);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            envelope.addMapping(SERVICENAMESPACE,bindClass.getSimpleName(),bindClass);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(SERVICEURL1);
            androidHttpTransport.call(SERVICENAMESPACE + methodName,envelope);
            res1 = (SoapObject) envelope.getResponse();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return res1;
    }
    public static ArrayList<PanchayatData> getPanchayat(String Block_Code) {

        SoapObject res1;
        res1=getServerData(GETPANCHAYATLIST,PanchayatData.PanchayatData_CLASS,"BlockCode",Block_Code);
        int TotalProperty=0;
        if(res1!=null) TotalProperty= res1.getPropertyCount();
        ArrayList<PanchayatData> fieldList = new ArrayList<PanchayatData>();

        for (int i = 0; i < TotalProperty; i++) {
            if (res1.getProperty(i) != null) {
                Object property = res1.getProperty(i);
                if (property instanceof SoapObject) {
                    SoapObject final_object = (SoapObject) property;
                    PanchayatData sm = new PanchayatData(final_object);
                    fieldList.add(sm);
                }
            } else
                return fieldList;
        }



        return fieldList;
    }
    public static String UploadNalkupDetails(Context context, InspectionDetailsModel workDetail, ArrayList<InspectionDetailsModel> requirements,String device_name,String App_ver) {

        context=context;
        DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
        dbfac.setNamespaceAware(true);
        DocumentBuilder docBuilder = null;

        try {
            docBuilder = dbfac.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "0, ParserConfigurationException!!";
        }
        DOMImplementation domImpl = docBuilder.getDOMImplementation();
        Document doc = domImpl.createDocument(SERVICENAMESPACE,INSERT_DETAIL, null);
        doc.setXmlVersion("1.0");
        doc.setXmlStandalone(true);

        Element poleElement = doc.getDocumentElement();

        poleElement.appendChild(getSoapPropert(doc, "DistCode", workDetail.getDistCode() == null ? "" : workDetail.getDistCode()));
        poleElement.appendChild(getSoapPropert(doc, "BlockCode", workDetail.getBlockCode()== null ? "" : workDetail.getBlockCode()));
        poleElement.appendChild(getSoapPropert(doc, "PanchayatCode", workDetail.getPanchayatCode()== null ? "" : workDetail.getPanchayatCode()));
        poleElement.appendChild(getSoapPropert(doc, "VILLCODE", workDetail.getVILLCODE()== null ? "" : workDetail.getVILLCODE()));
        poleElement.appendChild(getSoapPropert(doc, "SchemeCode",workDetail.getSchemeCode()== null ? "" : workDetail.getSchemeCode()));
        poleElement.appendChild(getSoapPropert(doc, "EnergyTypeId",workDetail.getEnergyTypeId()== null ? "" : workDetail.getEnergyTypeId()));
        poleElement.appendChild(getSoapPropert(doc, "NoofNalkup",workDetail.getNoofNalkup()== null ? "" : workDetail.getNoofNalkup()));
        poleElement.appendChild(getSoapPropert(doc, "NoOfPole",workDetail.getNoOfPole()== null ? "" : workDetail.getNoOfPole()));
        poleElement.appendChild(getSoapPropert(doc, "Motor_Pump_Power",workDetail.getMotor_Pump_Power()== null ? "" : workDetail.getMotor_Pump_Power()));
        poleElement.appendChild(getSoapPropert(doc, "DistributionChannelLength",workDetail.getDistributionChannelLength()== null ? "" : workDetail.getDistributionChannelLength()));
        poleElement.appendChild(getSoapPropert(doc, "DistributionPipeDiamater",workDetail.getDistributionPipeDiamater()== null ? "" : workDetail.getDistributionPipeDiamater()));
        poleElement.appendChild(getSoapPropert(doc, "DistributionPipeLength",workDetail.getDistributionPipeLength()== null ? "" : workDetail.getDistributionPipeLength()));
       // poleElement.appendChild(getSoapPropert(doc, "ApproxCommandArea",workDetail.getApproxCommandArea()== null ? "" : workDetail.getApproxCommandArea()));
        poleElement.appendChild(getSoapPropert(doc, "ApproxCommandArea","968"));
        poleElement.appendChild(getSoapPropert(doc, "SchemeApproxAmt",workDetail.getSchemeApproxAmt()== null ? "" : workDetail.getSchemeApproxAmt()));
        poleElement.appendChild(getSoapPropert(doc, "WaterSourceId",workDetail.getWaterSourceId()== null ? "" : workDetail.getWaterSourceId()));
        poleElement.appendChild(getSoapPropert(doc, "WaterAvailable_Kharif",workDetail.getWaterAvailable_Kharif()== null ? "" : workDetail.getWaterAvailable_Kharif()));
        poleElement.appendChild(getSoapPropert(doc, "WaterAvailable_Rabi",workDetail.getWaterAvailable_Rabi()== null ? "" : workDetail.getWaterAvailable_Rabi()));
        poleElement.appendChild(getSoapPropert(doc, "WaterAvailable_Garma",workDetail.getWaterAvailable_Garma()== null ? "" : workDetail.getWaterAvailable_Garma()));
        poleElement.appendChild(getSoapPropert(doc, "DistributionPaenLength",workDetail.getDistributionPaenLength()== null ? "" : workDetail.getDistributionPaenLength()));
        poleElement.appendChild(getSoapPropert(doc, "EntryBy",workDetail.getEntry_By()== null ? "" : workDetail.getEntry_By()));
        poleElement.appendChild(getSoapPropert(doc, "Photo",workDetail.getPhoto()== null ? "" : workDetail.getPhoto()));
        //poleElement.appendChild(getSoapPropert(doc, "EntryDate",workDetail.getEntryDate()== null ? "" : workDetail.getEntryDate()));
        poleElement.appendChild(getSoapPropert(doc, "AppVersion",App_ver));
        poleElement.appendChild(getSoapPropert(doc, "DeviceID",device_name));




        //--------------Array1-----------------//
        Element pdlsElement = doc.createElement("Agrilocationnew");
        ArrayList<InspectionDetailsModel> list = requirements;

        for(int x=0;x<list.size();x++)
        {
            Element pdElement = doc.createElement("Agriclocation");

//            Element fid = doc.createElement("s_aid");
//            fid.appendChild(doc.createTextNode(list.get(x).getAddPhoto_AId()));
//            pdElement.appendChild(fid);

            Element vLebel = doc.createElement("SchemeCode");
            vLebel.appendChild(doc.createTextNode(list.get(x).getSchemeCode()== null ? "" : list.get(x).getSchemeCode()));
            //vLebel.appendChild(doc.createTextNode("1234"));
            pdElement.appendChild(vLebel);

            Element vLebel2 = doc.createElement("GPSTypeId");
            vLebel2.appendChild(doc.createTextNode(list.get(x).getGPSTypeId()== null ? "" : list.get(x).getGPSTypeId()));
            pdElement.appendChild(vLebel2);

            Element vLebel3 = doc.createElement("Latitude");
            vLebel3.appendChild(doc.createTextNode(list.get(x).getLatitude()== null ? "" : list.get(x).getLatitude()));
            pdElement.appendChild(vLebel3);

            Element vLebel4 = doc.createElement("Longitude");
            vLebel4.appendChild(doc.createTextNode(list.get(x).getLongitude()== null ? "" : list.get(x).getLongitude()));
            pdElement.appendChild(vLebel4);

            Element vLebel6 = doc.createElement("PlotNo");
            vLebel6.appendChild(doc.createTextNode(list.get(x).getPlotNo()== null ? "" : list.get(x).getPlotNo()));
            pdElement.appendChild(vLebel6);

            Element vLebel5 = doc.createElement("ChannelNo");
            vLebel5.appendChild(doc.createTextNode(list.get(x).getChannelName()== null ? "" : list.get(x).getChannelName()));
            pdElement.appendChild(vLebel5);




            pdlsElement.appendChild(pdElement);
        }
        poleElement.appendChild(pdlsElement);
        //--------------Array2-----------------//

        TransformerFactory transfac = TransformerFactory.newInstance();
        Transformer trans = null;
        String res = "0";
        try {

            try {
                trans = transfac.newTransformer();
            } catch (TransformerConfigurationException e1) {

                // TODO Auto-generated catch block

                e1.printStackTrace();
                return "0, TransformerConfigurationException";
            }

            trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            trans.setOutputProperty(OutputKeys.INDENT, "yes");

            // create string from xml tree
            StringWriter sw = new StringWriter();
            StreamResult result = new StreamResult(sw);
            DOMSource source = new DOMSource(doc);

            BasicHttpResponse httpResponse = null;

            try {
                trans.transform(source, result);
            } catch (TransformerException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return "0, TransformerException";
            }

            String SOAPRequestXML = sw.toString();

            String startTag = "<?xml version=\"1.0\" encoding=\"utf-8\"?><soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" "
                    + "xmlns:xsd=\"http://www.w3.org/2001/XMLSchem\" "
                    + "xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" >  "
                    + "<soap:Body > ";
            String endTag = "</soap:Body > " + "</soap:Envelope>";

            try{


                HttpPost httppost = new HttpPost(SERVICEURL1);

                StringEntity sEntity = new StringEntity(startTag + SOAPRequestXML+ endTag, HTTP.UTF_8);

                sEntity.setContentType("text/xml;charset=UTF-8");
                httppost.setEntity(sEntity);
                HttpClient httpClient = new DefaultHttpClient();
                httpResponse = (BasicHttpResponse) httpClient.execute(httppost);

                HttpEntity entity = httpResponse.getEntity();

                if (httpResponse.getStatusLine().getStatusCode() == 200|| httpResponse.getStatusLine().getReasonPhrase().toString().equals("OK")) {
                    String output = _getResponseBody(entity);

                    res = parseRespnse(output);
                    Log.d("dfggdg",res);
                } else {
                    res = "0, Server no reponse";
                }
            }catch (Exception e){
                e.printStackTrace();
                return "0, Exception Caught";
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "0, Exception Caught";
        }

        // response.put("HTTPStatus",httpResponse.getStatusLine().toString());
        return res;
    }
    public static Element getSoapPropert(Document doc, String key, String value)
    {
        Element eid = doc.createElement(key);
        eid.appendChild(doc.createTextNode(value));
        return eid;
    }
    public static String _getResponseBody(final HttpEntity entity) throws IOException, ParseException {

        if (entity == null) { throw new IllegalArgumentException("HTTP entity may not be null"); }

        InputStream instream = entity.getContent();

        if (instream == null) { return ""; }

        if (entity.getContentLength() > Integer.MAX_VALUE) { throw new IllegalArgumentException(

                "HTTP entity too large to be buffered in memory"); }

        String charset = getContentCharSet(entity);

        if (charset == null)
        {

            charset = org.apache.http.protocol.HTTP.DEFAULT_CONTENT_CHARSET;

        }

        Reader reader = new InputStreamReader(instream, charset);

        StringBuilder buffer = new StringBuilder();

        try {

            char[] tmp = new char[1024];

            int l;

            while ((l = reader.read(tmp)) != -1) {

                buffer.append(tmp, 0, l);

            }

        }
        finally
        {

            reader.close();

        }

        return buffer.toString();

    }
    public static String parseRespnse(String xml){
        String result = "Failed to parse";
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        InputSource is;
        try {
            builder = factory.newDocumentBuilder();
            is = new InputSource(new StringReader(xml));
            Document doc = builder.parse(is);
            NodeList list = doc.getElementsByTagName("InsertInspectionResult");
            result = list.item(0).getTextContent();
            //System.out.println(list.item(0).getTextContent());
        } catch (ParserConfigurationException e) {
        } catch (SAXException e) {
        } catch (IOException e) {
        }

        return result;
    }
}
