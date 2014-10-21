package com.example.johnpham.hcftw;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;


public class Login extends Activity {
        EditText username;
        EditText password;
        private String[] AccountPass={"foo@example.com:hello"};
        private String a="a";
        private String b="b";
        private String c="c";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username=(EditText)findViewById(R.id.editText);
        password=(EditText)findViewById(R.id.editText3);
        password.setOnEditorActionListener(new TextView.OnEditorActionListener(){

        @Override
            public boolean onEditorAction(TextView v, int actionId,KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (username.getText().toString().equals((a))) {

                    if (password.getText().toString().equals(b)) {

                        //do something
                        startActivity(new Intent(getApplicationContext(), Home.class));
                    }

                }


            }
            return false;
        }

              });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.login, menu);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
