package com.black.cat.system.view

import android.content.Intent
import android.content.pm.ApplicationInfo
import androidx.lifecycle.MutableLiveData
import com.black.cat.vsystem.api.InstallFrags
import com.black.cat.vsystem.api.VActivityManager
import com.black.cat.vsystem.api.VPackageManager
import com.black.cat.vsystem.api.pm.InstallResult

class MainActivityViewModel : BaseViewModel() {
  val preloadAppObserver = MutableLiveData<Intent?>()
  val installResult = MutableLiveData<InstallResult>()
  val installedPackage = MutableLiveData<List<ApplicationInfo>>()
  val unInstallPackage = MutableLiveData<ApplicationInfo>()

  fun forceStopApp(applicationInfo: ApplicationInfo) {
    launchOnUI { VPackageManager.forceStopApp(applicationInfo.packageName, 0) }
  }

  fun clearApk(applicationInfo: ApplicationInfo) {
    launchOnUI { VPackageManager.clearApk(applicationInfo.packageName, 0) }
  }
  fun unInstallApk(applicationInfo: ApplicationInfo) {
    launchOnUI {
      VPackageManager.unInstallApk(applicationInfo.packageName, 0)
      unInstallPackage.postValue(applicationInfo)
    }
  }

  fun preloadApp(packageName: String) {
    launchOnUI {
      val launchIntent = VPackageManager.getLaunchIntentForPackage(packageName, 0)
      if (launchIntent == null) {
        preloadAppObserver.postValue(null)
      } else {
        val result = VActivityManager.preloadAppByLaunchIntent(launchIntent, 0)
        if (result) {
          preloadAppObserver.postValue(launchIntent)
        } else {
          preloadAppObserver.postValue(null)
        }
      }
    }
  }

  fun installApp(applicationInfo: ApplicationInfo) {
    launchOnUI {
      val result =
        VPackageManager.installPackageAsUser(
          applicationInfo.sourceDir,
          applicationInfo.packageName,
          InstallFrags.INSTALL_FLAG_STORAGE,
          0
        )
      if (result != null) {
        installResult.postValue(result!!)
      } else {
        installResult.postValue(
          InstallResult(applicationInfo.packageName, "install error unknown", false)
        )
      }
    }
  }

  fun getInstalledPackage(userID: Int) {
    launchOnUI {
      VPackageManager.getInstalledApplications(0, userID)?.let { installedPackage.postValue(it) }
    }
  }
}
