package ru.ifmo.mobdev.androidenglishlearning;

import java.net.URL;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.content.Context;
import android.content.Intent;
import android.text.Layout;
import android.content.Context;
import android.content.Intent;
import android.text.Layout;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	String query;
	volatile boolean translated, urlsDownloaded;
	volatile boolean needTranslate, needUrlDownload;
	volatile boolean translationFail, urlFail;
	Context context;
	EditText inputField;
	ProgressBar progressBar, progressTranslation, progressPictures;
	Button translateButton, showPictureButton;
	TextView translationText;
	String translation;
	URL[] urls;
	Thread downloadTranslation, downloadPictures;
	Layout picturesLayout;
	Intent intent;

	void hideAllProgresses() {
		progressBar.setVisibility(4);
		progressPictures.setVisibility(4);
		progressTranslation.setVisibility(4);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		context = this;
		translateButton = (Button) findViewById(R.id.translateButton);
		showPictureButton = (Button) findViewById(R.id.buttonShowPictures);
		inputField = (EditText) findViewById(R.id.inputField);
		progressBar = (ProgressBar) findViewById(R.id.progressBar1);
		progressTranslation = (ProgressBar) findViewById(R.id.progressBarTranslaion);
		progressPictures = (ProgressBar) findViewById(R.id.progressBarPictures);
		translationText = (TextView) findViewById(R.id.translationText);

		downloadTranslation = new Thread(new Runnable() {
			public void run() {
				while (true) {
					while (!needTranslate) {
						Thread.yield();
					}
					translation = WordTranslate.translate(query);
					if (translation == null) {
						translationFail = true;

					}
					translated = true;
					needTranslate = false;
				}
			}
		});

		downloadPictures = new Thread(new Runnable() {
			public void run() {
				while (true) {
					while (!needUrlDownload) {
						Thread.yield();
					}
					urls = ImageURLSearcher.getURLs(query);
					if (urls == null) {
						urlFail = true;
					}
					urlsDownloaded = true;
					needUrlDownload = false;
				}
			}

		});

		downloadTranslation.start();
		downloadPictures.start();

		OnClickListener translateButtonClickListener = new OnClickListener() {
			public void onClick(View v) {
				query = inputField.getText().toString();
				if (query.length() == 0) {
					Toast.makeText(context, R.string.needToWriteSomething,
							Toast.LENGTH_SHORT).show();
				} else {
					translated = false;
					urlsDownloaded = false;
					translationFail = false;
					urlFail = false;
					translationText.setVisibility(4);
					showPictureButton.setVisibility(4);
					hideAllProgresses();
					progressBar.setVisibility(0);
					needTranslate = true;
					needUrlDownload = true;

					while (!translated || !urlsDownloaded) {
						if (translated && !translationFail) {
							translationText.setText(translation);
							translationText.setVisibility(0);
							progressBar.setVisibility(4);
							progressPictures.setVisibility(0);
						}
						if (urlsDownloaded && !urlFail) {
							showPictureButton.setVisibility(0);
							progressBar.setVisibility(4);
							progressTranslation.setVisibility(0);
						}
						Thread.yield();
					}

					if (translationFail) {
						Toast.makeText(context, R.string.translationError,
								Toast.LENGTH_SHORT).show();
					}
					if (urlFail) {
						Toast.makeText(context, R.string.picturesError,
								Toast.LENGTH_SHORT).show();
					}
					hideAllProgresses();
					if (!translationFail) {
						translationText.setText(translation);
						translationText.setVisibility(0);
					}
					if (!urlFail) {
						showPictureButton.setVisibility(0);
					}
				}
			}
		};
		translateButton.setOnClickListener(translateButtonClickListener);
		showPictureButton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View arg0) {
				 intent = new Intent(MainActivity.this, ShowPictures.class);
				 String[] putUrls = new String[urls.length];
				 for (int i = 0; i < urls.length; i++)
					 putUrls[i] = urls[i].toString();
				 intent.putExtra("urls", putUrls);
			     startActivity(intent);
			}
		});
		/*
		 * URL[] urls = ImageURLSearcher.getURLs("kittens"); if (urls == null ||
		 * urls.length == 0) { Toast.makeText(this,
		 * "fail while loadig pictures (Anton's problem, not mine)",
		 * Toast.LENGTH_LONG).show(); } else { Toast.makeText(this,
		 * urls[0].toString(), Toast.LENGTH_LONG).show(); }
		 */

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
}
