package net.ashishb.bythepeople;

import android.app.Activity;
import android.os.Bundle;

public class NotesActivity extends Activity {

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      // TODO: pass a Report object which might contain Photo/Video.
      setContentView(R.layout.notes_activity);
    }
}
