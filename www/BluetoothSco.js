var exec = require('cordova/exec'),
    Plugin = function() {};
    
Plugin.prototype.start = function(onSuccess, onError) {
    return exec(onSuccess, onError, 'BluetoothSco', 'start', []);
};

Plugin.prototype.stop = function(onSuccess, onError) {
    return exec(onSuccess, onError, 'BluetoothSco', 'stop', []);
};

module.exports = new Plugin();
