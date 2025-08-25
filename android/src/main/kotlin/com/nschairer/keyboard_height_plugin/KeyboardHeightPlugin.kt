package com.nschairer.keyboard_height_plugin
import android.graphics.Rect
import android.os.Build
import androidx.annotation.NonNull
import android.view.ViewTreeObserver
import androidx.annotation.RequiresApi
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.EventChannel
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding

class KeyboardHeightPlugin : FlutterPlugin, EventChannel.StreamHandler, ActivityAware {
    private val keyboardHeightEventChannelName = "keyboardHeightEventChannel"
    private var eventSink: EventChannel.EventSink? = null
    private var eventChannel: EventChannel? = null
    private var activityPluginBinding: ActivityPluginBinding? = null

    override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        eventChannel = EventChannel(flutterPluginBinding.binaryMessenger, keyboardHeightEventChannelName)
        eventChannel?.setStreamHandler(this)
    }

    override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
        eventChannel?.setStreamHandler(null)
    }

    override fun onListen(arguments: Any?, events: EventChannel.EventSink?) {
        eventSink = events
        val rootView = activityPluginBinding?.activity?.window?.decorView?.rootView
        if (rootView == null) return;
        ViewCompat.setOnApplyWindowInsetsListener(rootView) { _, insets ->
            val imeHeight = insets.getInsets(WindowInsetsCompat.Type.ime()).bottom
            val displayMetrics = activityPluginBinding?.activity?.resources?.displayMetrics
            val logicalKeypadHeight = imeHeight / (displayMetrics?.density ?: 1f)
            events?.success(logicalKeypadHeight.toDouble())
            insets
        }
    }

    override fun onCancel(arguments: Any?) {
        eventSink = null
    }

    override fun onAttachedToActivity(binding: ActivityPluginBinding) {
        activityPluginBinding = binding
    }

    override fun onDetachedFromActivityForConfigChanges() {
        activityPluginBinding = null
    }

    override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
        activityPluginBinding = binding
    }

    override fun onDetachedFromActivity() {
        activityPluginBinding = null
    }
}
