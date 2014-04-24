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
import com.baasbox.android.BaasUser.Scope;
import com.baasbox.android.json.JsonObject;
import com.chipik.R;
import com.chipik.util.AlertUtils;

/**
 * User representation
 * {"username":"{username}", "password":"{password}",
 *  "visibleByTheUser": {...}, "visibleByFriend": {...}, 
 *  "visibleByRegisteredUsers": {..}, "visibleByAnonymousUsers": {...}
 * }
 * rest api
 * curl http://localhost:9000/user -X POST 
 * -d '{"username": "cesare","password": "password","visibleByTheUser": {"email": "outerbound@gmail.com"} ,"" :{"name": "julius caesar"}}'  
 * -H Content-type:application/json 
 * -H X-BAASBOX-APPCODE:1234567890
 * @author anil sharma
 *
 */
public class SignupActivity extends Activity {

	private EditText nameEditText;
	private EditText emailEditText;
	private EditText usernameEditText;
	private EditText passwordEditText;
	private Button signupButton;
	private Button loginButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		setTitle("Signup");
		setContentView(R.layout.activity_signup);

		usernameEditText = (EditText) findViewById(R.id.username);
		passwordEditText = (EditText) findViewById(R.id.password);
		emailEditText = (EditText) findViewById(R.id.email);
		nameEditText = (EditText) findViewById(R.id.name);

		signupButton = (Button) findViewById(R.id.signupButton);
		signupButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String username = usernameEditText.getText().toString();
				String password = passwordEditText.getText().toString();
				String email = emailEditText.getText().toString();
				String name = nameEditText.getText().toString();

				onClickSignup(name, email, username, password);
			}
		});
		
		loginButton = (Button) findViewById(R.id.loginButton);
		loginButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
		        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		        startActivity(intent);
		        finish();
			}
		});
		
	}

	protected void onClickSignup(String name, String email, String username, String password) {

		BaasUser user = BaasUser.withUserName(username);
		user.setPassword(password);
		
		JsonObject privateData = user.getScope(Scope.PRIVATE);
		privateData.putString("email", email);
		
		JsonObject publicData = user.getScope(Scope.PUBLIC);
		publicData.putString("name", name);
		
		//user.signup(onComplete);
		
		SignupTask signupTask = new SignupTask();
		signupTask.execute(user);
	}

	public class SignupTask extends AsyncTask<BaasUser, Void, BaasResult<BaasUser>> {
		@Override
		protected void onPreExecute() {
			signupButton.setEnabled(false);
		}
		@Override
		protected BaasResult<BaasUser> doInBackground(BaasUser... params) {
			return params[0].signupSync();
		}
		@Override
		protected void onPostExecute(BaasResult<BaasUser> result) {
			signupButton.setEnabled(true);
			onSignup(result);
		}
	}
	
	protected void onSignup(BaasResult<BaasUser> result) {
		try {
			result.get();
			onUserSignedUp();
		} catch (BaasClientException e) {
			if (e.httpStatus == 400) {
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setCancelable(true);
				builder.setTitle("Signup failed");
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
	
	private void onUserSignedUp() {
		Intent intent = new Intent(this, MainActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);

		finish();
	}
	
	/*private final static String SIGNUP_TOKEN_KEY = "signup_token_key";
	private RequestToken requestToken;
	//todo 3.2
	final BaasHandler<BaasUser> onComplete = new BaasHandler<BaasUser>() {
		@Override
		public void handle(BaasResult<BaasUser> result) {
			requestToken = null;
			if (result.isFailed()){
				Log.d("ERROR","ERROR",result.error());
			}
			completeSignup(result.isSuccess());
		}
	};
    //signup   
	private void completeSignup(boolean success){
        if (success) {
        	//Intent intent = new Intent(this,PictureActivity.class);
        	Intent intent = new Intent(this,AddressBookActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        } else {
        	Toast.makeText(getApplicationContext(), "Error Signing up", Toast.LENGTH_SHORT).show();
        }
    }
	
	@Override
    protected void onPause() {
        super.onPause();
        if (requestToken != null){
        	requestToken.suspend();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (requestToken != null){
            requestToken.resume(onComplete);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if ( requestToken != null){
            outState.putParcelable(SIGNUP_TOKEN_KEY, requestToken);
        }
    }
*/

}
