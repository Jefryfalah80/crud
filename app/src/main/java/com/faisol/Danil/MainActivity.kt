package com.faisol.Danil

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import com.firebase.ui.auth.AuthUI
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private var auth: FirebaseAuth? = null
    private val RC_SIGN_IN = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_logout.setOnClickListener(this)
        btn_save.setOnClickListener(this)
        btn_show_data.setOnClickListener(this)

        auth = FirebaseAuth.getInstance()

    }

    private fun isEmpty(s: String): Boolean {
        return TextUtils.isEmpty(s)
    }

    override fun onClick(v: View) {
        when (v.getId()) {
            R.id.btn_save -> {
                val getUserID = auth!!.currentUser!!.uid
                val database = FirebaseDatabase.getInstance()

                val getNama: String = nama.getText().toString()
                val getAlamat: String = alamat.getText().toString()
                val getNoHp: String = no_hp.getText().toString()

                val getReference: DatabaseReference
                getReference = database.reference

                if (isEmpty(getNama) || isEmpty(getAlamat) || isEmpty(getNoHp)) {
                    Toast.makeText(this@MainActivity, "Data boleh kosong!!",
                        Toast.LENGTH_SHORT).show()
                } else {
                    getReference.child("Admin").child(getUserID).child("DataTeman").push()
                        .setValue(data_teman(getNama, getAlamat, getNoHp))
                        .addOnCompleteListener(this) {
                            nama.setText("")
                            alamat.setText("")
                            no_hp.setText("")
                            Toast.makeText(this@MainActivity, "Data tersimpan",
                                Toast.LENGTH_SHORT).show()
                        }
                }
            }
            R.id.btn_logout -> {
                AuthUI.getInstance()
                    .signOut(this)
                    .addOnCompleteListener(object : OnCompleteListener<Void> {
                        override fun onComplete(p0: Task<Void>) {
                            Toast.makeText(this@MainActivity, "Proses Logout Berhasil",
                                Toast.LENGTH_SHORT).show()
                            intent = Intent(applicationContext, Login::class.java)
                            startActivity(intent)
                            finish()
                        }
                    })
            }
            R.id.btn_show_data -> {
                startActivity(Intent(this@MainActivity, ListData::class.java))
            }
        }
    }
}