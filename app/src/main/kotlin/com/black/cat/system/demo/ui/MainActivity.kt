package com.black.cat.system.demo.ui

import android.app.Activity
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.appcompat.widget.PopupMenu
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.viewbinding.ViewBinding
import com.black.cat.system.demo.R
import com.black.cat.system.demo.bean.AppInfo
import com.black.cat.system.demo.databinding.ActivityMainBinding
import com.black.cat.system.demo.databinding.ActivityMainItemBinding
import com.black.cat.system.demo.ui.base.BaseActivity
import com.black.cat.system.demo.view.MainActivityViewModel
import com.black.cat.system.demo.widget.adapter.BaseAdapter
import com.black.cat.system.demo.widget.adapter.BaseHolder
import com.black.cat.system.demo.widget.adapter.BaseItemClickListener
import com.black.cat.system.demo.widget.adapter.BaseItemLongClickListener
import com.black.cat.vsystem.api.VActivityManager

class MainActivity : BaseActivity() {
  private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
  private val viewModel by lazy {
    ViewModelProvider(
        this,
        object : ViewModelProvider.Factory {
          override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return modelClass.newInstance() as T
          }
        }
      )
      .get(MainActivityViewModel::class.java)
  }
  private val selectAppLauncher =
    registerForActivityResult(StartActivityForResult()) {
      if (it.resultCode == Activity.RESULT_OK) {
        val applicationInfo: ApplicationInfo? =
          it.data?.getParcelableExtra(PhoneInstalledListActivity.INTENT_KEY_APPLICATION_INFO)
        viewModel.installApp(applicationInfo!!)
        showLoading()
      }
    }
  private val installedPackageAdapter by lazy { initInstalledPackageAdapter() }
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(binding.root)
    initToolbar(R.string.app_name)
    viewModel.installResult.observe(this) {
      hideLoading()
      Log.d("MainActivity", "install ${it.packageName} success ${it.success} ${it.msg}")
      viewModel.getInstalledPackage(0)
    }
    binding.rvRecyclerView.layoutManager = GridLayoutManager(this, 4)
    viewModel.installedPackage.observe(this) {
      hideLoading()
      installedPackageAdapter.clear()
      installedPackageAdapter.add(AppInfo(true))
      installedPackageAdapter.addAll(it.map { applicationInfo -> AppInfo(false, applicationInfo) })
      binding.rvRecyclerView.adapter = installedPackageAdapter
    }
    viewModel.unInstallPackage.observe(this) { applicationInfo ->
      installedPackageAdapter
        .getData()
        .find { appInfo -> applicationInfo.packageName == appInfo.applicationInfo?.packageName }
        ?.let {
          installedPackageAdapter.remove(it)
          installedPackageAdapter.notifyDataSetChanged()
        }
    }
    viewModel.preloadAppObserver.observe(this) { intentTmp ->
      hideLoading()
      if (intentTmp != null) {
        VActivityManager.startActivity(intentTmp, 0)
      }
    }
    viewModel.getInstalledPackage(0)
  }

  private fun initInstalledPackageAdapter(): InstalledPackageAdapter {
    val adapter = InstalledPackageAdapter()
    adapter.itemClickListener =
      object : BaseItemClickListener<AppInfo> {
        override fun onClick(binding: ViewBinding, positionData: AppInfo, position: Int) {
          if (positionData.isDefault) {
            selectAppLauncher.launch(
              Intent(this@MainActivity, PhoneInstalledListActivity::class.java)
            )
          } else {
            showLoading()
            viewModel.preloadApp(positionData.applicationInfo!!.packageName)
          }
        }
      }
    adapter.itemLongClickListener =
      object : BaseItemLongClickListener<AppInfo> {
        override fun onLongClick(binding: ViewBinding, positionData: AppInfo, position: Int) {
          if (!positionData.isDefault) {
            showAppActions(binding.root, positionData.applicationInfo!!)
          }
        }
      }
    return adapter
  }

  private fun showAppActions(view: View, applicationInfo: ApplicationInfo) {
    val actionMenu = PopupMenu(this, view)
    actionMenu.inflate(R.menu.app_action_menu)
    actionMenu.setOnMenuItemClickListener {
      when (it.itemId) {
        R.id.app_clear -> viewModel.clearApk(applicationInfo)
        R.id.app_stop -> viewModel.forceStopApp(applicationInfo)
        R.id.app_remove -> viewModel.unInstallApk(applicationInfo)
      }
      return@setOnMenuItemClickListener true
    }
    actionMenu.show()
  }
}

class InstalledPackageAdapter : BaseAdapter<AppInfo>() {
  override fun onCreateViewHolder(
    parent: ViewGroup,
    viewType: Int
  ): BaseHolder<AppInfo, ViewBinding> {
    val itemView = LayoutInflater.from(parent.context).inflate(R.layout.activity_main_item, null)
    return InstalledPackageHolder(itemView)
  }
}

class InstalledPackageHolder(itemView: View) :
  BaseHolder<AppInfo, ActivityMainItemBinding>(itemView) {
  override fun initBinding(itemView: View) = ActivityMainItemBinding.bind(itemView)
  override fun bindView(positionData: AppInfo, position: Int) {
    if (positionData.isDefault) {
      binding.imgAppIcon.setImageResource(R.drawable.icon_add_app)
      binding.tvAppName.text = itemView.context.getString(R.string.install_app_list)
    } else {
      binding.tvAppName.text =
        positionData.applicationInfo?.loadLabel(itemView.context.packageManager)
      binding.imgAppIcon.setImageDrawable(
        positionData.applicationInfo?.loadIcon(itemView.context.packageManager)
      )
    }
  }
}
