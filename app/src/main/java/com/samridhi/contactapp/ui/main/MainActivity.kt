package com.samridhi.contactapp.ui.main

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.samridhi.contactapp.R
import com.samridhi.contactapp.databinding.ActivityMainBinding
import com.samridhi.contactapp.ui.main.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var binding: ActivityMainBinding
    private val CONTACTS_PERMISSION_CODE = 100
    lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        initListener()
    }

    private fun initListener() {
        binding.syncContact.setOnClickListener(this)
    }
    @SuppressLint("SetTextI18n")
    private fun startSyncingContacts() {
        if (checkAndRequestPermissions()) {
            lifecycleScope.launch(Dispatchers.IO) {
                AddContacts.addMultipleContacts(
                    viewModel.list,
                    this@MainActivity.contentResolver
                ) { message ->
                    binding.tvProgress.text = "progress - $message"
                }
            }
        } else {
        }
    }

    private fun checkAndRequestPermissions(): Boolean {
        val permissionsNeeded = mutableListOf<String>()
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_CONTACTS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            permissionsNeeded.add(Manifest.permission.WRITE_CONTACTS)
        }
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_CONTACTS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            permissionsNeeded.add(Manifest.permission.READ_CONTACTS)
        }
        if (permissionsNeeded.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                this,
                permissionsNeeded.toTypedArray(),
                CONTACTS_PERMISSION_CODE
            )
            return false
        }
        return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CONTACTS_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {

                startSyncingContacts()
            } else {

                Toast.makeText(
                    this,
                    "Contacts permissions are required to sync contacts.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.syncContact -> {
                startSyncingContacts()
            }
        }
    }
}