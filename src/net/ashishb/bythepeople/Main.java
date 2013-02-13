package net.ashishb.bythepeople;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Main extends Activity
{
    public static final int AUDIO = 3;  // For Future.
    public static final int NOTES = 2;
    public static final int PHOTO = 1;
    public static final int VIDEO = 0;
		public static final String REPORT_TYPE = "Report";
    public static final String TAG = "ByThePeople";
    public static final String JPEG_FILE_PREFIX = "img_";
    public static final String JPEG_FILE_SUFFIX = ".jpg";

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.main);
      GridView gridview = (GridView) findViewById(R.id.gridview);

      final ImageAdapter imageAdapter = new ImageAdapter(this);
      gridview.setAdapter(imageAdapter);
      gridview.setOnItemClickListener(new OnItemClickListener() {
          public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
            int action = imageAdapter.getAction(position);
            switch (action) {
              case VIDEO: TakeVideo(); break;
              case PHOTO: TakePhoto(); break;
              case NOTES: takeNotes(null, NOTES); break;
            }
          }
      });
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
      if (resultCode != Activity.RESULT_CANCELED) {
        if (requestCode == VIDEO) {
          Uri videoUri = data.getData();
          takeNotes(videoUri.toString(), VIDEO);
        } else if (requestCode == PHOTO) {
          Bitmap image = (Bitmap) data.getExtras().get("data");
          FileOutputStream out = null;
          try {
            String photoPath = createImageFile().getAbsolutePath();
            out = new FileOutputStream(photoPath);
            image.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            takeNotes(photoPath, PHOTO);
          } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(Main.this, R.string.photo_failed,
                           Toast.LENGTH_SHORT).show();
          } finally {
          }
        } else if (requestCode == NOTES) {
          // TODO
        } else {
          Log.e(TAG, "Unknown request code: " + requestCode);
        }
      } else {
        Toast.makeText(Main.this, R.string.action_canceled, Toast.LENGTH_SHORT).show();
      }
    }

    private void TakeVideo() {
      Log.d(TAG, "Starting video recording");
      if (!Util.isIntentAvailable(this, MediaStore.ACTION_VIDEO_CAPTURE)) {
        Toast.makeText(Main.this, R.string.video_not_supported,
            Toast.LENGTH_SHORT).show();
      } else {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        startActivityForResult(takeVideoIntent, VIDEO);
      }
    }

    private void TakePhoto() {
      Log.d(TAG, "Taking photo");
      if (!Util.isIntentAvailable(this, MediaStore.ACTION_IMAGE_CAPTURE)) {
          Toast.makeText(Main.this, R.string.photo_not_supported,
            Toast.LENGTH_SHORT).show();
      } else {
        // Reference: http://developer.android.com/training/camera/photobasics.html
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
//            Uri.fromFile(imageFile));
        startActivityForResult(takePictureIntent, PHOTO);
      }
    }

    private void takeNotes(String uri, int type) {
			// uri can be null.
      Intent i = new Intent(this, NotesActivity.class);
			if (uri != null) {
        String[] uris = new String[1];
        uris[0] = uri.toString();
				i.putExtra("" + type, uris);
			}
      startActivity(i);
    }

    private File createImageFile() throws IOException {
      // Create an image file name
      String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
      String imageFileName = JPEG_FILE_PREFIX + timeStamp + "_";
      File image = File.createTempFile(imageFileName, JPEG_FILE_SUFFIX, 
                      getFilesDir());
      return image;
    }
}
