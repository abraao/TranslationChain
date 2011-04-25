package com.guaranacode.android.translationchain.beta;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.feedbackrepo.android.FeedbackRepoClient;
import com.guaranacode.android.translationchain.beta.FeedbackActivity;
import com.guaranacode.android.translationchain.beta.MainActivity;
import com.guaranacode.android.translationchain.beta.R;

public class FeedbackActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedback);

        final Button sendFeebackButton = (Button) findViewById(R.id.send_feedback_button);
        
        sendFeebackButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final EditText sourceEditText = (EditText) findViewById(R.id.feedback_text);
                final String feedback = sourceEditText.getText().toString();
                final ProgressDialog dialog = ProgressDialog.show(FeedbackActivity.this, "", getString(R.string.loading_message), true);
                
                FeedbackRepoClient.sendFeedbackAsync(getApplicationContext(), feedback, dialog);
                
                FeedbackActivity fa = FeedbackActivity.this;
                
                Toast.makeText(fa.getApplicationContext(), fa.getString(R.string.feedback_sent), Toast.LENGTH_SHORT);
                FeedbackActivity.this.startActivity(new Intent(fa.getApplicationContext(), MainActivity.class));
            }
        });
    }
}
