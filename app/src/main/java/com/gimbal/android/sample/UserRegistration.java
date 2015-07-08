package com.gimbal.android.sample;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

//User Registration
public class UserRegistration extends Activity {
    Button register;
    Button home;
    EditText firstname;
    EditText lastname;
    EditText email;
    EditText phonenumber;
    EditText username;
    EditText password;
    EditText address;
    String IMEI= "i9";
    private RadioGroup radioSexGroup;
    private RadioButton radioSexButton;

    //profile picture
    private Uri mImageCaptureUri;
    private ImageView mImageView;
    final ArrayList<CropOption> cropOptions = new ArrayList<CropOption>();

    private static final int PICK_FROM_CAMERA = 1;
    private static final int CROP_FROM_CAMERA = 2;
    private static final int PICK_FROM_FILE = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registration);
        Parse.initialize(this, "9k8UJH0jwIkxXLEWzDerAvcwdq7NmY0ch6FLXkoy", "Mnf3GCgMFEY83I5E67McRaO3776WrK1xv9f3YEne");
        // Getting the EditText Ids and assigning them to EditText objects.
        firstname = (EditText)findViewById(R.id.firstnameTxt);
        lastname =   (EditText)findViewById(R.id.lastNameTxt);
        email=(EditText)findViewById(R.id.emailIdTxt);
        phonenumber =(EditText)findViewById(R.id.phoneNoTxt);
        username =(EditText)findViewById(R.id.userNameTxt);
        password = (EditText)findViewById(R.id.passwordTxt);
        address =(EditText)findViewById(R.id.addressTxt);
        register  =(Button)findViewById(R.id.userRegister);
        home  =(Button)findViewById(R.id.homeBtn);
        radioSexGroup = (RadioGroup) findViewById(R.id.radioSex);
        //Onclick of User Registration button, the entered details will be stored in Parse DataStore.
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get selected radio button from radioGroup
                System.out.println("There-----");
                int selectedId = radioSexGroup.getCheckedRadioButtonId();
                System.out.println("Here-----");
                // find the radiobutton by returned id
                radioSexButton = (RadioButton) findViewById(selectedId);

                String role=radioSexButton.getText().toString();
                System.out.println("Role from registration"+role);
                //Create an object in Parse datastore
              ParseObject testing = ParseObject.create("Registration");
                // Retrieve the values from the EditView objects and assign them to registration attributes.
                testing.put("First_Name",firstname.getText().toString());
                testing.put("Last_Name",lastname.getText().toString());
                testing.put("Email_Id",email.getText().toString());
                testing.put("Phone_Number",phonenumber.getText().toString());
                testing.put("User_Name",username.getText().toString());
                testing.put("Password",password.getText().toString());
                testing.put("Address",address.getText().toString());
                testing.put("IMEI",IMEI);
                testing.put("Role",role);


                try {
                    testing.save();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                // saveBackground is used to store details to Parse.
                //After storing, clear the EditView controls, by calling clearForm.
                ViewGroup group = (ViewGroup) findViewById(R.id.userregId);
                clearForm(group);
                //A short toast message is displayed on successfull registration.
                Toast.makeText(getApplicationContext(), "Registered Successfully", Toast.LENGTH_SHORT).show();
                Intent i =new Intent(UserRegistration.this,OptInActivity.class);

                startActivity(i);


            }
        });
        //Onclick of Back button, the user is redirected to Registration home page.
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(UserRegistration.this,OptInActivity.class);

                startActivity(i);
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
// This function is used to clear the EditView controls
    private void clearForm(ViewGroup group)
    {
        for (int i = 0, count = group.getChildCount(); i < count; ++i) {
            View view = group.getChildAt(i);
            if (view instanceof EditText) {
                ((EditText)view).setText("");
            }

            if(view instanceof ViewGroup && (((ViewGroup)view).getChildCount() > 0))
                clearForm((ViewGroup)view);
        }
    }

}
