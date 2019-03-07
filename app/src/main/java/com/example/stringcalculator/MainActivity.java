package com.example.stringcalculator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @SuppressLint("ClickableViewAccessibility")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EditText expressionText = findViewById(R.id.expressionField);
        //Слушатель, позволяющий работать с курсором, но блокирующий системную клавиатуру
        expressionText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.onTouchEvent(event);
                InputMethodManager imm = (InputMethodManager)v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
                return true;
            }
        });
    }

    public void onClick(View view) {
        Button button = (Button) view;
        EditText editText = findViewById(R.id.expressionField);
        editText.getText().insert(editText.getSelectionStart(),button.getText());
    }

    public void onAllDeleteClick(View view) {
        EditText editText = findViewById(R.id.expressionField);
        editText.setText("");
    }

    public void onShortDeleteClick(View view) {
        EditText editText = findViewById(R.id.expressionField);
        if(editText.length()!=0)editText.getText().delete(editText.getSelectionStart()-1,editText.getSelectionStart());

    }

    public void onResultClick(View view) {
        EditText resultText = findViewById(R.id.resultField);
        EditText expressionText = findViewById(R.id.expressionField);
        try {
            resultText.setText(Calculator.getResult(expressionText.getText().toString()));
        }
        catch (Exception ex){
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Ошибка!")
                    .setMessage("Проверьте корректность введенной строки.")
                    .setCancelable(false)
                    .setNegativeButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }
}
