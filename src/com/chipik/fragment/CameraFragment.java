package com.chipik.fragment;

import java.io.IOException;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.chipik.R;
import com.commonsware.cwac.camera.CameraHost;
import com.commonsware.cwac.camera.CameraView;


public class CameraFragment extends Fragment {
	public static final String TAG = "Chipik/CameraFragment";

	private CameraView cameraView = null;
	private CameraHost host = null;
	
	private ImageButton capturePhotoButton;
    
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		View rootView = inflater.inflate(R.layout.fragment_camera, container, false);
    
		cameraView = (CameraView)rootView.findViewById(R.id.cameraView);
		cameraView.setHost(getHost());

		capturePhotoButton = (ImageButton) rootView.findViewById(R.id.capturePhotoButton);
        capturePhotoButton.setOnClickListener(new View.OnClickListener() {
     	   @Override
     	   public void onClick(View v) {
     		  cameraView.takePicture(false, true); //needBitmap false, needByteArray true
     	   }
        });
        
		return(rootView);
	}

	@Override
	public void onResume() {
		super.onResume();
		cameraView.onResume();
		cameraView.restartPreview();
	}

	@Override
	public void onPause() {
		if (cameraView.isRecording()) {
			try {
				cameraView.stopRecording();
			}
			catch (IOException e) {
				Log.e(getClass().getSimpleName(), "Exception stopping recording in onPause()", e);
			}
		}
		cameraView.onPause();
		super.onPause();
	}

	/**
	 * @return the CameraHost instance you want to use for this fragment, 
	 * where the default is an instance of the stock SimpleCameraHost.
	 */
	public CameraHost getHost() {
		if (host == null) {
			host = new CustomCameraHost(getActivity());
		}
		return host;
	}
}
