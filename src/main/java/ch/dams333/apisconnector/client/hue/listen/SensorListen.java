package ch.dams333.apisconnector.client.hue.listen;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimerTask;

import org.json.JSONObject;

import ch.dams333.apisconnector.client.hue.Sensors;
import ch.dams333.apisconnector.client.hue.interfaces.SensorInterface;

public class SensorListen extends TimerTask{

    private int sensorID;
    private String bridgeIP;
    private String bridgeUsername;
    private Date lastInput;
    private SensorInterface<Integer> sensorInterface;


    public SensorListen(int sensorID, String bridgeIP, String bridgeUsername, SensorInterface<Integer> sensorInterface) {
        this.sensorID = sensorID;
        this.bridgeIP = bridgeIP;
        this.bridgeUsername = bridgeUsername;
        this.sensorInterface = sensorInterface;
        JSONObject sensor = Sensors.getSensor(bridgeIP, bridgeUsername, sensorID);
        String sensorLast = sensor.getJSONObject("state").getString("lastupdated");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        try {
            this.lastInput = format.parse(sensorLast);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void run() {
        JSONObject sensor = Sensors.getSensor(bridgeIP, bridgeUsername, sensorID);
        String sensorLast = sensor.getJSONObject("state").getString("lastupdated");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        try {
            Date sensorLastDate = format.parse(sensorLast);
            if(sensorLastDate.getTime() > lastInput.getTime()){
                System.out.println("Action détectée sur le capteur " + sensorID + " (" + sensor.getJSONObject("state").getInt("buttonevent") + ")");
                sensorInterface.active(sensor.getJSONObject("state").getInt("buttonevent"));
                lastInput = sensorLastDate;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
    
}
