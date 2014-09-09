package gntikos.plugin.bluetoothsco;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.media.AudioManager;

public class BluetoothSco extends CordovaPlugin {
	
	private final BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
	private AudioManager audioManager;
	
	private synchronized void startBtSco() {
		
		if (btAdapter == null)
			throw new RuntimeException("This device does not support Bluetooth.");
		
		if (!btAdapter.isEnabled())
			throw new RuntimeException("Bluetooth interface is not enabled.");
		
		audioManager = (AudioManager) this.cordova.getActivity().getSystemService(Context.AUDIO_SERVICE);
		
		if (!audioManager.isBluetoothScoAvailableOffCall()) 
			throw new RuntimeException("Your device does not support recording via Bluetooth.");
		
		if (!audioManager.isBluetoothScoOn()) {
			audioManager.setBluetoothScoOn(true);
			audioManager.startBluetoothSco();
		}
		
		// TODO: Somebody must listen for AudioManager.ACTION_SCO_AUDIO_STATE_CHANGED event
		// in order to return properly. For now we hope that by the time the recording starts
		// everything will fine.
	}
	
	private synchronized void stopBtSco() {
		if ((audioManager == null) || (! audioManager.isBluetoothScoOn()))
			return;
		
		audioManager.stopBluetoothSco();
		audioManager.setBluetoothScoOn(false);
	}
	
	@Override
	public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
		
		if (action.equals("start")) {
			try {
				startBtSco();
				callbackContext.success("Bluetooth SCO connection initialized.");
				return true;
			} catch (Exception e) {
				callbackContext.error(e.getMessage());
				return false;
			}
		}
		
		if (action.equals("stop")) {
			try {
				stopBtSco();
				callbackContext.success("Bluetooth SCO connection terminated.");
				return true;
			} catch (Exception e) {
				callbackContext.error(e.getMessage());
				return false;
			}
		}
		
		return true;
	}
}
