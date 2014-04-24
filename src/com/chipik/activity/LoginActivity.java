package com.chipik.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.baasbox.android.BaasClientException;
import com.baasbox.android.BaasException;
import com.baasbox.android.BaasResult;
import com.baasbox.android.BaasServerException;
import com.baasbox.android.BaasUser;
import com.chipik.R;
import com.chipik.util.AlertUtils;

/**
 * 
 * @author anil sharma
 *
 */
public class LoginActivity extends Activity {

	private EditText usernameEditText;
	private EditText passwordEditText;
	private Button loginButton;
	private Button signupButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		setTitle("Login");
		setContentView(R.layout.activity_login);

		usernameEditText = (EditText) findViewById(R.id.username);
		passwordEditText = (EditText) findViewById(R.id.password);

		loginButton = (Button) findViewById(R.id.loginButton);
		loginButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String username = usernameEditText.getText().toString();
				String password = passwordEditText.getText().toString();
				onClickLogin(username, password);
			}
		});
		
		signupButton = (Button) findViewById(R.id.signupButton);
		signupButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),SignupActivity.class);
		        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		        startActivity(intent);
		        finish();
			}
		});

	}

	protected void onClickLogin(String username, String password) {

		BaasUser user = BaasUser.withUserName(username);
		user.setPassword(password);
		
		LoginTask loginTask = new LoginTask();
		loginTask.execute(user);
	}

	public class LoginTask extends AsyncTask<BaasUser, Void, BaasResult<BaasUser>> {
		@Override
		protected void onPreExecute() {
			loginButton.setEnabled(false);
		}
		@Override
		protected BaasResult<BaasUser> doInBackground(BaasUser... params) {
			return params[0].loginSync();
		}
		@Override
		protected void onPostExecute(BaasResult<BaasUser> result) {
			loginButton.setEnabled(true);
			onLogin(result);
		}
	}
	
	protected void onLogin(BaasResult<BaasUser> result) {
		try {
			result.get();
			onUserLoggedIn();
		} catch (BaasClientException e) {
			if (e.httpStatus == 400) {
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setCancelable(true);
				builder.setTitle("Login failed");
				builder.setMessage("Insert username and password");
				builder.setNegativeButton("Ok", null);
				builder.create().show();
			} else {
				AlertUtils.showErrorAlert(this, e);
			}
		} catch (BaasServerException e) {
			AlertUtils.showErrorAlert(this, e);
		} catch (BaasException e) {
			AlertUtils.showErrorAlert(this, e);
		}
	}
	
	private void onUserLoggedIn() {
		Intent intent = new Intent(this, MainActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);

		finish();
	}
}