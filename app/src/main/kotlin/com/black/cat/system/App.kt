package com.black.cat.system

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import com.black.cat.vsystem.api.Vsystem
import com.black.cat.vsystem.core.utils.Vlog

class App : Application() {

  override fun attachBaseContext(base: Context?) {
    super.attachBaseContext(base)
    Vsystem.doAttachBaseContext(this)
  }

  override fun onCreate() {
    super.onCreate()
    registerActivityLifecycleCallbacks(
      object : ActivityLifecycleCallbacks {
        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
          Vlog.d("App", "onActivityCreated " + activity.componentName)
        }

        override fun onActivityStarted(activity: Activity) {
          Vlog.d("App", "onActivityStarted " + activity.componentName)
        }

        override fun onActivityResumed(activity: Activity) {
          Vlog.d("App", "onActivityResumed " + activity.componentName)
        }

        override fun onActivityPaused(activity: Activity) {
          Vlog.d("App", "onActivityPaused " + activity.componentName)
        }

        override fun onActivityStopped(activity: Activity) {
          Vlog.d("App", "onActivityStopped " + activity.componentName)
        }

        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
          Vlog.d("App", "onActivitySaveInstanceState " + activity.componentName)
        }

        override fun onActivityDestroyed(activity: Activity) {
          Vlog.d("App", "onActivityDestroyed " + activity.componentName)
        }
      }
    )
  }
}
