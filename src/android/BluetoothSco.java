package gntikos.plugin.bluetoothsco;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;


public class BluetoothSco extends CordovaPlugin {
	
	private final BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
	
	@Override
	public boolean execute(String action, JSONArray args, CallbackContext callbackContext) {
		
		if(this.cordova.getActivity().isFinishing()) return true;
		
		if (action.equals("start")) {
			this.startScoConnection(callbackContext);
			return true;
		} 
		else if (action.equals("stop")) {
			this.stopScoConnection(callbackContext);
			return true;
		}
		
		return false;
	}
	

	private synchronized void startScoConnection(final CallbackContext callbackContext) {
		final CordovaInterface cordova = this.cordova;
		
		Runnable runnable = new Runnable() {

			@Override
			public void run() {
				if (btAdapter == null) {
					callbackContext.error("This device does not support Bluetooth");
					return;
				}
				else if (! btAdapter.isEnabled()) {
					callbackContext.error("Bluetooth is not enabled");
					return;
				} 
				
				cordova.getActivity().registerReceiver(new BroadcastReceiver() {

					@Override
					public void onReceive(Context context, Intent intent) {
						int state = intent.getIntExtra(AudioManager.EXTRA_SCO_AUDIO_STATE, -1);
						
						if (state == AudioManager.SCO_AUDIO_STATE_CONNECTED) {
							callbackContext.success();
							context.unregisterReceiver(this);
						}
//						else if (state == AudioManager.SCO_AUDIO_STATE_DISCONNECTED) {
//							callbackContext.error("Could not start Bluetooth SCO connection");
//							context.unregisterReceiver(this);
//						}
					}
					
				}, new IntentFilter(AudioManager.ACTION_SCO_AUDIO_STATE_UPDATED));
				
				AudioManager audioManager = (AudioManager) cordova.getActivity().getSystemService(Context.AUDIO_SERVICE);
				if (! audioManager.isBluetoothScoAvailableOffCall()) {
					callbackContext.error("Off-call Bluetooth audio not supported on this device.");
					return;
				}
				
				audioManager.setBluetoothScoOn(true);
				audioManager.startBluetoothSco();
			}
		};
		
		this.cordova.getActivity().runOnUiThread(runnable);
	}
	
	private synchronized void stopScoConnection(final CallbackContext callbackContext) {
		
		final CordovaInterface cordova = this.cordova;
		
		Runnable runnable = new Runnable() {

			@Override
			public void run() {
				
				AudioManager audioManager = (AudioManager) cordova.getActivity().getSystemService(Context.AUDIO_SERVICE);
				
				try {
					audioManager.stopBluetoothSco();
					audioManager.setBluetoothScoOn(false);
					callbackContext.success();
				} catch (Exception e) {
					callbackContext.error(e.getMessage());
				}
			}
		};
		
		this.cordova.getActivity().runOnUiThread(runnable);
	}

}
