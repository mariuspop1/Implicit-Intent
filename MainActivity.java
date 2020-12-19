package fr.eurecom.implicitintent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.CalendarContract;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.GregorianCalendar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    private static String[] PERMISSIONS_STORAGE = {Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE,};
    public static void verifyStoragePermissions(Activity activity) {
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {ActivityCompat.requestPermissions(activity,PERMISSIONS_STORAGE,1);}}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            String filename="test" ;
            File outputFile =new File(Environment.getExternalStorageDirectory(),"photo_"+filename+".png");
            try {FileOutputStream out =new FileOutputStream(outputFile);
                imageBitmap.compress(Bitmap.CompressFormat.PNG,100,out);
                out.flush();out.close();
            }
            catch (FileNotFoundException e) {e.printStackTrace();
            }
            catch (IOException e) {e.printStackTrace();}}
        super.onActivityResult(requestCode, resultCode, data);}


    public void callIntent(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.button1:
                EditText textBrow = (EditText) findViewById(R.id.editTextTextPersonName);
                String url = textBrow.getText().toString().trim();
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://" + url));
                Intent chooser = Intent.createChooser(intent, "Select Application");
                startActivity(chooser);
                    break;
            case R.id.button2:
                EditText textBrow1 = (EditText) findViewById(R.id.editTextTextPersonName2);
                String number = textBrow1.getText().toString().trim();
                intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" +number )); // does not work with ACTION_DIAL(ACTION_VIEW does the same thing) neither ACTION_CALL
                startActivity(intent);
                break;
            case R.id.button3:
                //intent = new Intent(Intent.ACTION_VIEW, Uri.parse("content://contacts/people/1")); // DIAL FIRST CONTACT
                intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"));
                startActivity(intent);
                break;
            case R.id.button4:
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:43.614177,7.071390"));
                intent.setPackage("com.google.android.apps.maps");
                startActivity(intent);
                break;
            case R.id.button5:
                EditText textBrow2 = (EditText) findViewById(R.id.editTextTextPersonName3);
                String url2 = textBrow2.getText().toString().trim();
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?daddr="+ url2));
                intent.setPackage("com.google.android.apps.maps");
                startActivity(intent);
                break;
            case R.id.button6:
                verifyStoragePermissions(this);
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 2);}
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePictureIntent,1);
                break;
            case R.id.button7:
                Intent i = new Intent();
                i.setAction(Intent.ACTION_VIEW);
                i.setData(Uri.parse("content://contacts/people/"));
                startActivity(i);
                break;
            case R.id.button8:
                intent = new Intent(Intent.ACTION_EDIT, Uri.parse("content://contacts/people/1")); // DOES NOT WORK
                //intent = new Intent(ContactsContract.Intents.Insert.ACTION); // create a contact
                //intent.setType(ContactsContract.Contacts.CONTENT_TYPE); // create a contact
                startActivity(intent);
                break;
            case R.id.button9:
                intent = new Intent(Intent.ACTION_INSERT);
                intent.setData(CalendarContract.Events.CONTENT_URI);
                GregorianCalendar calDate = new GregorianCalendar();
                intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME,
                        calDate.getTimeInMillis());
                intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME,
                        calDate.getTimeInMillis());
                startActivity(intent);
                break;

        }
}
    }