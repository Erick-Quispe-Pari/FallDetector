package edu.unsa.exam2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import edu.unsa.exam2.model.AcelerationFilter;
import edu.unsa.exam2.model.FallDetector;

public class MainActivity extends AppCompatActivity {

    SensorManager sensor_manager;
    Sensor acelerometer;
    float []previousAcelerationValues;
    float []actualAcelerationValues;
    SensorEventListener stepDetector;

    TextView tv_message;
    Button start_stop;
    Button exit;
    boolean on,is_falling;
    ConstraintLayout main_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_message = (TextView) findViewById(R.id.label_test);
        start_stop = (Button) findViewById(R.id.bt_start_stop);
        exit = (Button) findViewById(R.id.bt_salir);
        main_layout = (ConstraintLayout) findViewById(R.id.main_layout);

        sensor_manager = (SensorManager) getSystemService(SENSOR_SERVICE);
        acelerometer = sensor_manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        stepDetector = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                if(event!=null){
                    actualAcelerationValues=AcelerationFilter.getGeneralAceleration(event);
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };
        sensor_manager.registerListener(stepDetector,acelerometer,SensorManager.SENSOR_DELAY_FASTEST);
        on=false;
        is_falling=false;
    }

    public void startStopFunction(View v){
        if(on){
            pauseFunction();
            start_stop.setText("Iniciar");
        }else{
            resumeFunction();
            start_stop.setText("Detener");
        }
    }

    AsyncTask tarea;

    public void closeApp(View v){
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void pauseFunction(){
        sensor_manager.unregisterListener(stepDetector);
        on=false;
        tarea.cancel(true);
        tarea=null;
    }

    public void resumeFunction(){
        sensor_manager.registerListener(stepDetector,acelerometer,SensorManager.SENSOR_DELAY_FASTEST);
        tarea = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                while(on){
                    publishProgress();
                    try {
                        Thread.sleep(100);
                        if(actualAcelerationValues!=null&&previousAcelerationValues!=null){
                            is_falling=FallDetector.isDeviceFalling(previousAcelerationValues,actualAcelerationValues);
                        }
                        previousAcelerationValues=actualAcelerationValues;
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                return null;
            }

            @Override
            protected void onProgressUpdate(Object[] values) {
                super.onProgressUpdate(values);
                if(is_falling){
                    main_layout.setBackgroundColor(Color.parseColor("#ff7073"));
                    tv_message.setText("Cayendo");
                }else{
                    main_layout.setBackgroundColor(Color.parseColor("#FFFFFFFF"));
                    tv_message.setText("Sin Peligro");
                }
            }
        };
        on=true;
        tarea.execute();
    }
}