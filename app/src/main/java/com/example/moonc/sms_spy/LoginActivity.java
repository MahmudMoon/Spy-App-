package com.example.moonc.sms_spy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {

     EditText user_name;
     Button button;
     String name ;
     String password;
    String login_approval = "Pushpo";
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

//        SharedPreferences sharedPreferences1 = getSharedPreferences("Login_details", MODE_PRIVATE);
//        String login_approval = sharedPreferences1.getString("name","NAME");

     //  Toast.makeText(getApplicationContext(),login_approval,Toast.LENGTH_SHORT).show();

//        if(!login_approval.equals("NAME") && !login_approval.isEmpty())
//        {
//            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
//            startActivity(intent);
//        }

        user_name = (EditText) findViewById(R.id.name);
        button = (Button) findViewById(R.id.button3);





//        if (!name.isEmpty()&& !password.isEmpty()) {
//            for (int i = 0; i < name.length(); i++) {
//                if (name.charAt(i) != '.' || name.charAt(i) != '*' || name.charAt(i) != '!' || name.charAt(i) != '/' || name.charAt(i) != '$'
//                        || name.charAt(i) != '%' || name.charAt(i) != '#' ||
//                        name.charAt(i) != ']' || name.charAt(i) != '[') {
//                    final_string = final_string + name.charAt(i);
//                }
//            }





        //}


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                name = user_name.getText().toString();
//                if(name.isEmpty()){
//                   Toast.makeText(getApplicationContext(),"Enter Name",Toast.LENGTH_SHORT).show();
//                    Vibrator vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
//                    vibrator.vibrate(500);
//                }

             //   else {
//                    String final_string = name;
//                    SharedPreferences sharedPreferences = getSharedPreferences("Login_details", MODE_PRIVATE);
//                    SharedPreferences.Editor editor = sharedPreferences.edit();
//                    editor.putString("name", final_string);
//                    editor.apply();
//                   Toast.makeText(getApplicationContext(), name, Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
              //  }
            }
        });

    }

}
