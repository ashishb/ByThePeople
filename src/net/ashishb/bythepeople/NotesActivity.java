package net.ashishb.bythepeople;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class NotesActivity extends Activity {

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.notes_activity);

      final Button cancel_button = (Button) findViewById(R.id.cancel);
      // Destroy the activity if cancel button is clicked.
      cancel_button.setOnClickListener(
          new View.OnClickListener() {
            public void onClick(View v) {
              finish();
            }
          });

      final Button done_button = (Button) findViewById(R.id.done);
      done_button.setOnClickListener(
          new View.OnClickListener() {
            public void onClick(View v) {
              Toast.makeText(NotesActivity.this, R.string.report_submission_mocked,
                             Toast.LENGTH_SHORT).show();
              finish();
            }
          });

      // Now add custom audio/photo/video.
      // As of now, only one entry of each type is supported.
      TextView textView = (TextView) findViewById(R.id.notes_attachments);
      textView.setText(R.string.notes_attachment_info);
      Intent intent = getIntent();
      if (intent.hasExtra("" + Main.VIDEO)) {
        String[] uris = intent.getStringArrayExtra("" + Main.VIDEO);
        for (String uri: uris) {
          textView.append("\n" + uri);
          Log.d(Main.TAG, uri);
        }
      }
      if (intent.hasExtra("" + Main.PHOTO)) {
        String[] uris = intent.getStringArrayExtra("" + Main.PHOTO);
        for (String uri: uris) {
          textView.append("\n" + uri);
          Log.d(Main.TAG, uri);
        }
      }
      if (intent.hasExtra("" + Main.AUDIO)) {
        Toast.makeText(NotesActivity.this, R.string.audio_not_supported,
          Toast.LENGTH_SHORT).show();
      }
    }
}
