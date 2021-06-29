package edu.unsa.exam2.model;

import android.util.Log;

public class FallDetector {

    final static float maxVariation=(float) 10;

    public static boolean isDeviceFalling(float [] previousAceleration, float[] actualAceleration){
        boolean testAxisX=actualAceleration[0]>1||actualAceleration[0]<-1;
        boolean testAxisY=actualAceleration[1]>2.5||actualAceleration[1]<-0.5;
        boolean testAxisZ=actualAceleration[0]>2.5||actualAceleration[2]<1.5;
        float acelerationDifference;
        if(testAxisX){
            acelerationDifference=Math.abs(actualAceleration[0]-previousAceleration[0]);
            if(acelerationDifference>maxVariation){
                Log.e("Diferencia en X:",""+acelerationDifference);
                return true;
            }
        }
        if(testAxisY){
            acelerationDifference=Math.abs(actualAceleration[1]-previousAceleration[1]);
            if(acelerationDifference>maxVariation){
                Log.e("Diferencia en Y:",""+acelerationDifference);
                return true;
            }
        }
        if(testAxisZ){
            acelerationDifference=Math.abs(actualAceleration[2]-previousAceleration[2]);
            if(acelerationDifference>maxVariation*1.3){
                Log.e("Diferencia en Z:",""+acelerationDifference);
                return true;
            }
        }
        return false;
    }
    public static String getVariationLog(float[] previousAceleration, float[] actualAceleration){
        return "X:"+previousAceleration[0]+"Y:"+previousAceleration[1]+"Z:"+previousAceleration[2]+"VS"+
                "X:"+actualAceleration[0]+"Y:"+actualAceleration[1]+"Z:"+actualAceleration[2];
    }
}
