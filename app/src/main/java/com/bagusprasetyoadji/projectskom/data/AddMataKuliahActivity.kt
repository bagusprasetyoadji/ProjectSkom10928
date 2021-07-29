package com.bagusprasetyoadji.projectskom.data

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bagusprasetyoadji.projectskom.R
import com.bagusprasetyoadji.projectskom.databinding.ActivityAddMataKuliahBinding
import com.google.firebase.database.*

class AddMataKuliahActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_NAMA = "extra_nama"
        const val EXTRA_ID = "extra_id"
    }

    private lateinit var binding: ActivityAddMataKuliahBinding
    private lateinit var matkulList: MutableList<MataKuliah>
    private lateinit var ref: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddMataKuliahBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val id = intent.getStringExtra(EXTRA_ID)
        val nama = intent.getStringExtra(EXTRA_NAMA)

        matkulList = mutableListOf()
        ref = FirebaseDatabase.getInstance().getReference("mata_kuliah").child(id.toString())

        binding.btnMatkul.setOnClickListener {
            saveMatkul()
        }
    }

    private fun saveMatkul() {
        val namaMatkul = binding.etMakul.text.toString().trim()
        if (namaMatkul.isEmpty()) {
            binding.etMakul.error = "Matkul harus diisi"
            return
        }
        val matkulId = ref.push().key
        val matkul = MataKuliah(matkulId!!, namaMatkul)
        if (matkulId != null) {
            ref.child(matkulId).setValue(matkul).addOnCompleteListener {
                Toast.makeText(
                    applicationContext,
                    "Mata Kuliah Berhasil Ditambahkan",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {

                    matkulList.clear()
                    for (h in snapshot.children) {
                        val mataKuliah = h.getValue(MataKuliah::class.java)
                        if (mataKuliah != null) {
                            matkulList.add(mataKuliah)
                        }
                    }
                    val adapter = MataKuliahAdapter(
                        this@AddMataKuliahActivity,
                        R.layout.item_matkul,
                        matkulList
                    )
                    binding.lvMatkul.adapter = adapter
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}