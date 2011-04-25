package com.guaranacode.android.translationchain.beta;

import java.util.Random;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.guaranacode.android.libs.google.translate.Language;
import com.guaranacode.android.libs.google.translate.LanguageTranslator;
import com.guaranacode.android.libs.string.StringUtil;
import com.guaranacode.android.translationchain.beta.FeedbackActivity;
import com.guaranacode.android.translationchain.beta.R;

public class MainActivity extends Activity {
    private static final LanguageTranslator translator = new LanguageTranslator("YOUR_API_KEY_HERE");
    private static final Random randGen = new Random(System.currentTimeMillis());
    private ProgressDialog mDialog;
    
    private String[] mStartTexts;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mStartTexts = new String[]{ getString(R.string.start_text_1), getString(R.string.start_text_2),
                getString(R.string.start_text_3), getString(R.string.start_text_4), getString(R.string.start_text_5),
                getString(R.string.start_text_6), getString(R.string.start_text_7), getString(R.string.start_text_8),
                getString(R.string.start_text_9), getString(R.string.start_text_10), getString(R.string.start_text_11),
                getString(R.string.start_text_12), getString(R.string.start_text_13), getString(R.string.start_text_14),
                getString(R.string.start_text_15), getString(R.string.start_text_16), getString(R.string.start_text_17),
                getString(R.string.start_text_18), getString(R.string.start_text_19), getString(R.string.start_text_20)};

        pickRandomSourceText(mStartTexts);
        
        final Button goButton = (Button) findViewById(R.id.button_num_lang);
        goButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final Editable sourceEditable = ((EditText) findViewById(R.id.input_text)).getText();
                final String sourceText = sourceEditable.toString();
                
                if(StringUtil.isNullOrEmpty(sourceText)) {
                    Toast.makeText(getApplicationContext(), getString(R.string.empty_input_text_msg), Toast.LENGTH_LONG).show();
                }
                else {
                    clearOutputText();
                    
                    mDialog = ProgressDialog.show(MainActivity.this, "", getString(R.string.dialog_loading_translations), true);
                    
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            String outputText = translator.translate(Language.English, Language.Afrikaans, sourceText);
                            outputText = translator.translate(Language.Afrikaans, Language.ChineseSimplified, outputText);
                            outputText = translator.translate(Language.ChineseSimplified, Language.Vietnamese, outputText);
                            outputText = translator.translate(Language.Vietnamese, Language.English, outputText);
                            
                            setOutputText(outputText);
                            
                            mDialog.dismiss();
                        }
                    }, 100);
                }
            }
        });
        
        final Button newTextButton = (Button) findViewById(R.id.button_new_text);
        newTextButton.setOnClickListener(new View.OnClickListener() {            
            @Override
            public void onClick(View v) {
                clearOutputText();
                pickRandomSourceText(mStartTexts);
            }
        });
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.app_menu, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.menu_send_feedback:
            this.startActivity(new Intent(this.getApplicationContext(), FeedbackActivity.class));
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }
    
    /**
     * Sets the output text (the result of the translation).
     * @param text
     */
    public void setOutputText(String outputText) {
        if(StringUtil.isNullOrEmpty(outputText)) {
            Toast.makeText(getApplicationContext(), getString(R.string.no_output), Toast.LENGTH_LONG);
        }
        else {
            final EditText outputEdit = (EditText) findViewById(R.id.output_text);
            outputEdit.setText(outputText);
        }
    }

    /**
     * Pick a random source text to translate.
     * @param startTexts
     */
    private void pickRandomSourceText(String[] startTexts) {
        if((null != startTexts) && startTexts.length > 0) {
            final String startText = startTexts[randGen.nextInt(startTexts.length - 1) + 1];
            
            final EditText sourceEdit = (EditText) findViewById(R.id.input_text);
            sourceEdit.setText(startText);
        }
    }
    
    /**
     * Clears the output text.
     */
    private void clearOutputText() {
        final EditText outputEdit = (EditText) findViewById(R.id.output_text);
        outputEdit.getEditableText().clear();
    }
}