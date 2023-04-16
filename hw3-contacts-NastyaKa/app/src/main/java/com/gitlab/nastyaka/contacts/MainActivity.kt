package com.gitlab.nastyaka.contacts

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*

const val MY_PERMISSIONS_REQUEST_CODE: Int = 1
val PERMISSIONS_READ_CONTACT = arrayOf(Manifest.permission.READ_CONTACTS)

class MainActivity : AppCompatActivity() {

    private lateinit var recycle: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recycle = findViewById(R.id.recycler_view)
        checkPermission()
    }

    private fun checkPermission() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_CONTACTS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.READ_CONTACTS
                )
            ) {
                Snackbar.make(
                    contact_layout, R.string.permission_contacts_request,
                    Snackbar.LENGTH_INDEFINITE
                ).setAction(R.string.accept) {
                    ActivityCompat.requestPermissions(
                        this,
                        PERMISSIONS_READ_CONTACT,
                        MY_PERMISSIONS_REQUEST_CODE
                    )
                }.show()
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    PERMISSIONS_READ_CONTACT,
                    MY_PERMISSIONS_REQUEST_CODE
                )
            }
        } else {
            getContacts()
        }
    }

    private fun getContacts() {
        val contactsList: List<Contact> = fetchAllContacts()
        recycle.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = ContactsAdapter(contactsList) {
                startActivity(Intent(Intent.ACTION_DIAL).apply {
                    data = Uri.parse("tel:${it.phoneNumber}")
                })
            }
        }
        Toast.makeText(
            this@MainActivity,
            resources.getQuantityString(
                R.plurals.contacts_count_plurals,
                contactsList.size,
                contactsList.size
            ),
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_CODE -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    Snackbar.make(
                        contact_layout,
                        getString(R.string.permissions_granted),
                        Snackbar.LENGTH_INDEFINITE
                    ).setAction(getString(R.string.refresh)) {
                        getContacts()
                    }.show()
                } else {
                    Snackbar.make(
                        contact_layout, getString(R.string.permissions_not_granted),
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
                return
            }
            else -> {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            }
        }
    }
}