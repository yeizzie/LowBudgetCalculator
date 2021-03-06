package com.example.yeizz.intergercalculator;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.util.*;


public class MainActivity extends AppCompatActivity {

    private TextView display;
    private boolean curOpeartion;
    private Button  btnEqual, btnClear;
    private boolean errorState;
    private boolean firstDigit;
    private boolean equalSign;
    private String number1;
    private String number2;
    private String opeartor;
    private int[] numericButtons = {R.id.button0, R.id.button1, R.id.button2, R.id.button3, R.id.button4, R.id.button5, R.id.button6, R.id.button7, R.id.button8, R.id.button9};
    private int[] signButtons = {R.id.buttonPlus, R.id.buttonDivison, R.id.buttonMinus, R.id.buttonMultiply};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialUI();
        setNumericOnClickListener();
        setSignOnClickListener();


    }
    //number buttons
    private void setNumericOnClickListener() {
        // Create a common OnClickListener
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Just append/set the text of clicked button
                Button button = (Button) v;
                if(equalSign){
                    reset();
                }
                // If current state is Error, replace the error message
                String curDisplay = display.getText().toString();

                // if current display is zero
                boolean leadingZero = curDisplay.equals("0");
                //reset display if there is error, leadingnumber, previous is sign
                if(!curOpeartion && !errorState && !leadingZero){
                    number2 = curDisplay + button.getText().toString();
                }else{
                    number2 = button.getText().toString();
                }

                if(isOverFlow(number2)){
                    setOverflow();
                }else{
                    display.setText(number2);
                    errorState = false;
                    firstDigit = false;
                    curOpeartion = false;
                }

            }
        };
        // Assign the listener to all the numeric buttons
        for (int id : numericButtons) {
            findViewById(id).setOnClickListener(listener);
        }
    }

    //sign buttons
    private void setSignOnClickListener() {
        // Create a common OnClickListener
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button button = (Button) v;
                equalSign = false;
                if(firstDigit){
                    curOpeartion = true;
                    opeartor = button.getText().toString();
                    return;
                }else{
                    if(!curOpeartion){
                        String tmp = calculate(number1, number2, opeartor);
                        if(tmp == null){
                            setError();
                        }else if(isOverFlow(tmp)){
                            setOverflow();
                        }else{
                            display.setText(tmp);
                            number1 = tmp;
                        }

                    }


                    curOpeartion = true;
                    opeartor = button.getText().toString();

                }



            }
        };
        // Assign the listener to all the numeric buttons
        for (int id : signButtons) {
            findViewById(id).setOnClickListener(listener);
        }
    }

    private void initialUI(){ // initial all the button views
        errorState = false;
        curOpeartion = false;
        firstDigit = true;
        opeartor = null;
        number1 = null;
        number2 = null;
        equalSign = false;


        display = (TextView)findViewById(R.id.textDisplay);
        btnClear = (Button)findViewById(R.id.buttonClear);
        btnEqual = (Button)findViewById(R.id.buttonEqual);


        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reset();
            }
        });

        btnEqual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(number2 == null || curOpeartion){
                    setError();
                }else{
                    String tmp = calculate(number1, number2, opeartor);
                    if(tmp == null){
                        setError();
                        return;
                    } else if(isOverFlow(tmp)){
                        setOverflow();
                        return;
                    }else{
                        number1 = tmp;
                        number2 = null;
                        curOpeartion = true;
                        opeartor = null;
                        equalSign = true;
                        display.setText(number1);

                    }
                }

            }
        });


    }

    private String calculate(String value1, String value2, String sign){
        String result = null;
        if( value2 == null){
            setError();
            return null;
        }
        else{
            if(value1 == null && (sign == "*" || sign == "/")){
                setError();
                return null;
            }
            int val1 = value1 == null? 0 : toInt(value1);
            int val2 = toInt(value2);
            if(sign == null || sign.equals("+")){
                result =  Integer.toString(val1 + val2);
            }else if(sign.equals("-")){
                result =  Integer.toString(val1 - val2);
            }else if(sign.equals("*")){
                result =  Integer.toString(val1 * val2);
            }else {
                if (val2 == 0){
                    //setError();
                    return null;
                }else{
                    int intResult = val1 / val2;
                    double doubleResult = val1 / (val2 + 0.0);
                    double difference = Math.abs(doubleResult - intResult);
                    if(difference >= 0.5){
                        if(doubleResult > 0 ){
                            intResult++;
                        }else{
                            intResult--;
                        }
                    }
                    result =  Integer.toString(intResult);
                }

            }
        }

        return result;

    }

    private void setError(){
        errorState = true;
        curOpeartion = false;
        firstDigit = true;
        opeartor = null;
        number1 = null;
        number2 = null;
        equalSign = false;


        display.setText("ERROR!");

    }

    private void setOverflow(){

        errorState = true;
        curOpeartion = false;
        firstDigit = true;
        opeartor = null;
        number1 = null;
        number2 = null;
        equalSign = false;


        display.setText("OVERFLOW!");


    }

    private boolean isOverFlow(String number){
        if(toInt(number) > 9999999 || toInt(number) < -9999999){
            return true;
        }
        return false;
    }

    private int toInt(String str){
        return Integer.parseInt(str);
    }

    private void reset(){
        errorState = false;
        curOpeartion = false;
        firstDigit = true;
        opeartor = null;
        number1 = null;
        number2 = null;
        equalSign = false;
        display.setText("");


    }
}


