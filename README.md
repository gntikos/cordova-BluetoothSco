cordova-BluetoothSco
====================
<i>Enable audio communication via a Bluetooth SCO link</i>

How to use
----------

####Create a SCO link
```js
BluetoothSco.start(function() {
    // success callback
}, function() {
    // error callback
}, {enablePlayback: true});
```

The last argument is optional. If ```enablePlayback``` is set ```false``` (or not at all), the link is used to route audio <b> from </b> the Bluetooth headset <b> to </b> the phone (one-way link). If set ```true``` as above, a two-way link is created.

####Remove SCO link

```js
BluetoothSco.start(function() {
    // success callback
}, function() {
    // error callback
});

```

Note
-----

The plugin will not switch device's Bluetooth on or off, or try to pair with a headset. You have to do that in some other part of your code.
