package com.black.cat.system.demo.view

import android.content.Context
import android.content.pm.ApplicationInfo
import androidx.lifecycle.MutableLiveData

class PhoneInstalledListViewModel : BaseViewModel() {

  val installedApps = MutableLiveData<List<ApplicationInfo>>()

  fun getPhoneInstalledApps(context: Context) {
    launchOnUI {
      val applications =
        context.packageManager
          .getInstalledPackages(0)
          .filter { it.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM == 0 }
          .map { it.applicationInfo }
      installedApps.postValue(applications)
    }
  }
}
