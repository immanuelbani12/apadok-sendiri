package com.example.emrpreventive.shorting;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.emrpreventive.MainActivity;
import com.example.emrpreventive.R;
import com.example.emrpreventive.shorting.stroke.ScreeningActivity;
import com.example.emrpreventive.shorting.stroke.StrokeFormActivity;
import com.example.emrpreventive.shorting.stroke.StrokeResultActivity;

import java.util.ArrayList;
import java.util.Arrays;

public class TestLogin extends Activity {
    Button mButton;
    EditText mEdit;
    TextView mText;
    String count;
    Integer id;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_login);
        mButton = (Button)findViewById(R.id.button1);

        mButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                mEdit = (EditText)findViewById(R.id.editText1);
                count = mEdit.getText().toString();
                id = 0;

                // Creating array of string length & convert to uppercase
                char[] ch = new char[count.length()];
                count.toUpperCase();

                // Copy character by character into array
                for (int i = 0; i < count.length(); i++) {
                    ch[i] = count.charAt(i);
                }

                for (int i = 0; i < count.length(); i++){
                    id = id + (i+1) * (ch[i] - 64);
                }

                mText = (TextView)findViewById(R.id.textView1);
                mText.setText("ID Pasien: "+ id.toString());

                Intent intent = new Intent(TestLogin.this, MainActivity.class);
                //Pass the User ID to next activity
                intent.putExtra("user", id);
                startActivity(intent);
                finish();
            }
        });
    }
}
