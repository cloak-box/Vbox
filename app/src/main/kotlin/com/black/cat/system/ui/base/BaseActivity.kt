package com.black.cat.system.ui.base

import android.app.Dialog
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NavUtils
import com.black.cat.system.demo.R
import com.black.cat.system.demo.databinding.CommonProgressDialogBinding

open class BaseActivity : AppCompatActivity() {

  val progressDialog: Dialog by lazy {
    val binding = CommonProgressDialogBinding.inflate(layoutInflater)
    val p =
      AlertDialog.Builder(this, R.style.progress_dialog)
        .setView(binding.root)
        .setCancelable(false)
        .create()
    p.setCanceledOnTouchOutside(false)
    return@lazy p
  }

  protected fun initToolbar(title: Int, showBack: Boolean = false) {
    supportActionBar?.let {
      it.setTitle(title)
      it.setDisplayHomeAsUpEnabled(showBack)
    }
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    if (item.itemId == android.R.id.home) {
      NavUtils.navigateUpFromSameTask(this)
      return true
    }
    return super.onOptionsItemSelected(item)
  }

  fun showLoading() {
    if (!progressDialog.isShowing) progressDialog.show()
  }
  fun hideLoading() {
    if (progressDialog.isShowing) progressDialog.dismiss()
  }
}
