package ru.ifmo.mobdev.androidenglishlearning;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class ShowPictures extends Activity {

	Bitmap[] bm;
	String[] urls;
	ImageView iv;
	Button nextPictureButton, prevPictureButton;
	int nowImage = 0;


	void download(int i) {
		if (bm[i] == null) {
			try {
				bm[i] = BitmapFactory.decodeStream(new URL(urls[i])
						.openStream());
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	int show(int i) {
		if (urls.length == 0) return 0;
		if (i < 0) {
			i += urls.length;
		}
		if (i >= urls.length) {
			i %= urls.length;
		}
		download(i);
		iv.setImageBitmap(bm[i]);
		return i;
	}

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_pictures);

		urls = getIntent().getExtras().getStringArray("urls");
		if (urls.length == 0) {
			Toast.makeText(this, "No pictures was found", Toast.LENGTH_SHORT).show();
			return;
		}
		
		nextPictureButton = (Button) findViewById(R.id.nextPictureButton);
		prevPictureButton = (Button) findViewById(R.id.prevPictureButton);
		
		BitmapFactory.Options bmOptions;
		bmOptions = new BitmapFactory.Options();
		bmOptions.inSampleSize = 1;
		bm = new Bitmap[urls.length];
		iv = (ImageView) findViewById(R.id.imageView);
		show(0);
		nextPictureButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				nowImage = show(nowImage + 1);
			}
		});
		
		prevPictureButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				nowImage = show(nowImage - 1);
			}
		});
	}
}
