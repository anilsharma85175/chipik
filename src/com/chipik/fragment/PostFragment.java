package com.chipik.fragment;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.baasbox.android.BaasFile;
import com.baasbox.android.BaasHandler;
import com.baasbox.android.BaasResult;
import com.baasbox.android.json.JsonObject;
import com.chipik.R;


public class PostFragment extends Fragment {
	public static final String TAG = "Chipik/PostFragment";
	
	OnPostUploadCallback mCallback;
	// Container Activity must implement this interface
    public interface OnPostUploadCallback {
    	public Location getLocation();
        public void onPhotoUploaded();
    }	

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallback = (OnPostUploadCallback) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnPostUploadCallback");
        }
    }
    
	public static byte[] imageData = null;
	
	EditText captionEditText;
	
	ImageView photoImageView;
	Button uploadPhotoButton;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		View rootView = inflater.inflate(R.layout.fragment_post, container, false);
    
        captionEditText = (EditText) rootView.findViewById(R.id.captionEditText);
        
        photoImageView = (ImageView) rootView.findViewById(R.id.photoImageView);
        if (imageData != null){
        	Bitmap bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
        	photoImageView.setImageBitmap(bitmap);
        }

        uploadPhotoButton= (Button) rootView.findViewById(R.id.uploadPhotoButton);
        uploadPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            	InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            	inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                uploadPhoto();
            }
        });
        
		return(rootView);
	}

	//-------- File Upload Functions
	public void uploadPhoto() {
    	try {
			BaasFile file = new BaasFile();
			JsonObject attachedData = file.getAttachedData();
			attachedData.putString("caption", captionEditText.getText().toString().trim());
			Location location = mCallback.getLocation();
			if (location != null){
				attachedData.putDouble("lattitude", location.getLatitude());
				attachedData.putDouble("longitude", location.getLongitude());
			}
			//async upload
			file.upload(imageData, handler);		
		} catch (Exception e) {

		}
    }
	
	final BaasHandler<BaasFile> handler = new BaasHandler<BaasFile>() {				
		@Override
		public void handle(BaasResult<BaasFile> result) {
			if( result.isSuccess() ) {
				Toast.makeText(getActivity(), "Photo upload successfull", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(getActivity(), "Error : Photo upload failed", Toast.LENGTH_SHORT).show();					      
			}
			//whatever happens, go to photos fragment
			mCallback.onPhotoUploaded();
		}
	};
	
}