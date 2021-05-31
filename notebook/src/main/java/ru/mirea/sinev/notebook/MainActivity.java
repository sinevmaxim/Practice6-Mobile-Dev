package ru.mirea.sinev.notebook;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    final String LAST_TEXT = "last_text";
    private EditText fileName;
    private EditText fileText;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fileText = findViewById(R.id.editText);
        fileName = findViewById(R.id.editFileName);

        preferences = getPreferences(MODE_PRIVATE);
        if (!preferences.getString(LAST_TEXT, "Empty").equals("Empty"))
            fileName.setText(preferences.getString(LAST_TEXT, "Empty"));

        fileText.setText(getTextFromFile());
    }

    public void saveFile(View view) {
        FileOutputStream outputStream;
        try {
            outputStream = openFileOutput(fileName.getText().toString(), Context.MODE_PRIVATE);
            outputStream.write(fileText.getText().toString().getBytes());
            outputStream.close();

            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(LAST_TEXT, fileName.getText().toString());
            editor.apply();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getTextFromFile() {
        String fileName = preferences.getString(LAST_TEXT, "Empty");

        if (fileName.equals("Empty")) {
            return null;
        }

        FileInputStream fin = null;

        try {
            fin = openFileInput(fileName);
            byte[] bytes = new byte[fin.available()];
            fin.read(bytes);
            String text = new String(bytes);
            Log.d(LOG_TAG, text);
            return text;

        } catch (IOException ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();

        } finally {
            try {
                if (fin != null)
                    fin.close();
            } catch (IOException ex) {
                Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        return null;
    }
}