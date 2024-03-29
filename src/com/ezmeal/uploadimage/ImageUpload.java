package com.ezmeal.uploadimage;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.ezmeal.main.R;

public class ImageUpload extends Activity {
	private static final int PICK_IMAGE = 1;
	private ImageView imgView;
	private Button upload;
	private Button select;
	private Bitmap bitmap;
	private int id;
	private Activity activity;
	private ProgressDialog dialog;
	private boolean flag;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.imageupload);
		
		activity = this;
		flag = false;
		id = getIntent().getExtras().getInt("id");

		imgView = (ImageView) findViewById(R.id.ImageView);
		upload = (Button) findViewById(R.id.Upload);
		upload.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				if (bitmap == null) {
					Toast.makeText(getApplicationContext(),
							"Please select image", Toast.LENGTH_SHORT).show();
				} else {
					dialog = ProgressDialog.show(ImageUpload.this, "Uploading",
							"Please wait...", true);
					new ImageUploadTask().execute();
				}
			}
		});
		select = (Button) findViewById(R.id.Select);
		select.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				try {
					Intent intent = new Intent();
					intent.setType("image/*");
					intent.setAction(Intent.ACTION_GET_CONTENT);
					startActivityForResult(
							Intent.createChooser(intent, "Select Picture"),
							PICK_IMAGE);
				} catch (Exception e) {
					Toast.makeText(getApplicationContext(),
							e.getMessage(),
							Toast.LENGTH_LONG).show();
					Log.e(e.getClass().getName(), e.getMessage(), e);
				}
			}
		});

	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case PICK_IMAGE:
			if (resultCode == Activity.RESULT_OK) {
				Uri selectedImageUri = data.getData();
				String filePath = null;

				try {
					// OI FILE Manager
					String filemanagerstring = selectedImageUri.getPath();

					// MEDIA GALLERY
					String selectedImagePath = getPath(selectedImageUri);

					if (selectedImagePath != null) {
						filePath = selectedImagePath;
					} else if (filemanagerstring != null) {
						filePath = filemanagerstring;
					} else {
						Toast.makeText(getApplicationContext(), "Unknown path",
								Toast.LENGTH_LONG).show();
						Log.e("Bitmap", "Unknown path");
					}

					if (filePath != null) {
						decodeFile(filePath);
					} else {
						bitmap = null;
					}
				} catch (Exception e) {
					Toast.makeText(getApplicationContext(), "Internal error",
							Toast.LENGTH_LONG).show();
					Log.e(e.getClass().getName(), e.getMessage(), e);
				}
			}
			break;
		default:
		}
	}

	class ImageUploadTask extends AsyncTask<Void, Void, String> {
		@Override
		protected String doInBackground(Void... unsued) {
			try {
				Log.e("Uploader","doInBackGround");
				HttpClient httpClient = new DefaultHttpClient();
				HttpContext localContext = new BasicHttpContext();
				HttpPost httpPost = new HttpPost(
						getString(R.string.WebServiceURL)
								+ "/upload_image.php");

				//MultipartEntity entity = new MultipartEntity(
				//		HttpMultipartMode.BROWSER_COMPATIBLE);
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				bitmap.compress(CompressFormat.JPEG, 100, bos);
				byte[] data = bos.toByteArray();
				String image_str = Base64.encodeBytes(data);
//				entity.addPart("returnformat", new StringBody("json"));
				ArrayList<NameValuePair> entity = new  ArrayList<NameValuePair>();				 
	            entity.add(new BasicNameValuePair("image",image_str));
	            entity.add(new BasicNameValuePair("id",Integer.toString(id)));

//				entity.addPart("image", new ByteArrayBody(data,
//						"uploaded_image.jpg"));
//				entity.addPart("photoCaption", new StringBody(caption.getText()
//						.toString()));
				httpPost.setEntity(new UrlEncodedFormEntity(entity));
				HttpResponse response = httpClient.execute(httpPost,
						localContext);
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(
								response.getEntity().getContent(), "UTF-8"));

				String sResponse = reader.readLine();
				sResponse = "success";
				return sResponse;
			} catch (Exception e) {
				if (dialog.isShowing())
					dialog.dismiss();
/*				Toast.makeText(getApplicationContext(),
						e.getMessage(),
						Toast.LENGTH_LONG).show();*/
				Log.e(e.getClass().getName(), e.getMessage(), e);
				return null;
			}

			// (null);
		}

		@Override
		protected void onProgressUpdate(Void... unsued) {

		}

		@Override
		protected void onPostExecute(String sResponse) {
			try {
				Log.e("Uploader","onPost");
				if (dialog.isShowing())
					dialog.dismiss();

				if (sResponse != null) {
					//JSONObject JResponse = new JSONObject(sResponse);
					//int success = JResponse.getInt("SUCCESS");
					//if (success == 0) {
					//	Log.e("Uploader","success=0");
					//} else {
						Toast.makeText(getApplicationContext(),
								"Photo uploaded successfully",
								Toast.LENGTH_SHORT).show();
						flag =true;
						//caption.setText("");
					//}
				}
				else{
					Toast.makeText(getApplicationContext(),
							"Failed. Try it later. ",
							Toast.LENGTH_SHORT).show();					
				}
			} catch (Exception e) {
				Toast.makeText(getApplicationContext(),
						e.getMessage(),
						Toast.LENGTH_LONG).show();
				Log.e(e.getClass().getName(), e.getMessage(), e);
			}
		}
	}

	public String getPath(Uri uri) {
		String[] projection = { MediaStore.Images.Media.DATA };
		Cursor cursor = managedQuery(uri, projection, null, null, null);
		if (cursor != null) {
			// HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
			// THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
			int column_index = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			return cursor.getString(column_index);
		} else
			return null;
	}

	public void decodeFile(String filePath) {
		// Decode image size
		BitmapFactory.Options o = new BitmapFactory.Options();
		o.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, o);

		// The new size we want to scale to
		final int REQUIRED_SIZE = 100;

		// Find the correct scale value. It should be the power of 2.
		int width_tmp = o.outWidth, height_tmp = o.outHeight;
		int scale = 1;
/*		while (true) {
			if (width_tmp < REQUIRED_SIZE && height_tmp < REQUIRED_SIZE)
				break;
			width_tmp /= 2;
			height_tmp /= 2;
			scale *= 2;
		}
*/
		// Decode with inSampleSize
		BitmapFactory.Options o2 = new BitmapFactory.Options();
		o2.inSampleSize = scale;
		bitmap = BitmapFactory.decodeFile(filePath, o2);

		if (bitmap.getHeight() <= bitmap.getWidth()) {
			bitmap = Bitmap.createBitmap(
					   bitmap, 
					   bitmap.getWidth()/2 - bitmap.getHeight()/2,
					   0,
					   bitmap.getWidth()/2 + bitmap.getHeight()/2, 
					   bitmap.getHeight()
					   );
			} else {
				bitmap = Bitmap.createBitmap(
						   bitmap,
						   0, 
						   bitmap.getHeight()/2 - bitmap.getWidth()/2,
						   bitmap.getWidth(),
						   bitmap.getHeight()/2 + bitmap.getWidth()/2 
						   );
				}
		
		bitmap = Bitmap.createScaledBitmap(bitmap, 144, 144, true);
		imgView.setImageBitmap(bitmap);

	}

	@Override
	public void onBackPressed() {
			Log.e("Uploader","doInBackGround");
		
				Intent i = new Intent();
				Bundle b = new Bundle();
				b.putBoolean("Result",flag);
				i.putExtras(b);
				activity.setResult(RESULT_OK,i);
    		finish();
		return;
	}

}