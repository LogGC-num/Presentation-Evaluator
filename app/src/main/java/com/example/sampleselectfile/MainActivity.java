package com.example.sampleselectfile;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {
    static final int REQUEST_OPEN_FILE = 1001;
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //CreateFile(null);
        openFile(null);


    }
    private void openFile(Uri pickerInitialUri) {

            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("audio/*");

            intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, pickerInitialUri);

                startActivity(intent);
                onActivityResult(REQUEST_OPEN_FILE, RESULT_OK, intent);

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // File load
        if (requestCode == REQUEST_OPEN_FILE) {
            if (resultCode == RESULT_OK && data != null) {
                Uri uri = data.getData();
                System.out.println(uri);
                if (uri != null) {
                    editText.setText(loadStrFromUri(uri));
                }
            }
        }
    }

    String loadStrFromUri(Uri uri) {
        String str = "";
        Boolean loop = true;
        try {
            if (uri.getScheme().equals("content")) {
                InputStream iStream = getContentResolver().openInputStream(uri);
                if (iStream == null) loop = false;
                ByteArrayOutputStream bout = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                while (loop) {
                    int len = iStream.read(buffer);
                    if (len < 0) break;
                    bout.write(buffer, 0, len);
                }
                str = bout.toString();
            } else {
                Toast.makeText(this, "Unknown scheme", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Cannot read the file:" + e.toString(), Toast.LENGTH_LONG).show();
        }
        return str;
    }

}