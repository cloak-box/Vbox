package com.black.cat.system.ui

import android.app.Activity
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewbinding.ViewBinding
import com.black.cat.system.demo.R
import com.black.cat.system.demo.databinding.ActivityPhoneInstalledListBinding
import com.black.cat.system.demo.databinding.PhoneInstalledListItemBinding
import com.black.cat.system.ui.base.BaseActivity
import com.black.cat.system.view.PhoneInstalledListViewModel
import com.black.cat.system.widget.adapter.BaseAdapter
import com.black.cat.system.widget.adapter.BaseHolder
import com.black.cat.system.widget.adapter.BaseItemClickListener

class PhoneInstalledListActivity : BaseActivity() {
  private val binding by lazy { ActivityPhoneInstalledListBinding.inflate(layoutInflater) }
  private val viewModel by lazy {
    ViewModelProvider(
        this,
        object : ViewModelProvider.Factory {
          override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return modelClass.newInstance() as T
          }
        }
      )
      .get(PhoneInstalledListViewModel::class.java)
  }
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(binding.root)
    initToolbar(R.string.install_app_list, true)
    binding.rvRecyclerView.layoutManager = LinearLayoutManager(this)
    showLoading()
    viewModel.getPhoneInstalledApps(this)
    viewModel.installedApps.observe(this) { applications ->
      val adapter = SystemInstallAppAdapter()
      adapter.addAll(applications)
      adapter.addAppOnClickListener =
        object : BaseItemClickListener<ApplicationInfo> {
          override fun onClick(binding: ViewBinding, positionData: ApplicationInfo, position: Int) {
            val intentData = Intent()
            intentData.putExtra(INTENT_KEY_APPLICATION_INFO, positionData)
            setResult(Activity.RESULT_OK, intentData)
            finish()
          }
        }
      binding.rvRecyclerView.adapter = adapter
      hideLoading()
    }
  }

  companion object {
    const val INTENT_KEY_APPLICATION_INFO = "intent_key_application_info"
  }
}

class SystemInstallAppAdapter : BaseAdapter<ApplicationInfo>() {
  var addAppOnClickListener: BaseItemClickListener<ApplicationInfo>? = null
  override fun onCreateViewHolder(
    parent: ViewGroup,
    viewType: Int
  ): BaseHolder<ApplicationInfo, ViewBinding> {
    val itemView =
      LayoutInflater.from(parent.context).inflate(R.layout.phone_installed_list_item, null)
    return SystemInstallAppHolder(itemView)
  }

  inner class SystemInstallAppHolder(itemView: View) :
    BaseHolder<ApplicationInfo, PhoneInstalledListItemBinding>(itemView) {
    override fun initBinding(itemView: View) = PhoneInstalledListItemBinding.bind(itemView)

    override fun bindView(positionData: ApplicationInfo, position: Int) {
      binding.imgAppIcon.setImageDrawable(positionData.loadIcon(itemView.context.packageManager))
      binding.tvAppName.text =
        "${positionData.loadLabel(itemView.context.packageManager)} ${positionData.targetSdkVersion}"
      binding.tvAppPackageName.text = positionData.packageName
      addAppOnClickListener?.let { tmpAddAppOnClickListener ->
        binding.btnAddApp.setOnClickListener {
          tmpAddAppOnClickListener.onClick(binding, positionData, position)
        }
      }
    }
  }
}
