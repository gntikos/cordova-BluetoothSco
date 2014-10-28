var exec = require('cordova/exec'),
    Plugin = function() {};
    
Plugin.prototype.start = function(onSuccess, onError, parameters) {
    return exec(onSuccess, onError, 'BluetoothSco', 'start', [parameters]);
};

Plugin.prototype.stop = function(onSuccess, onError) {
    return exec(onSuccess, onError, 'BluetoothSco', 'stop', []);
};

module.exports = new Plugin();
