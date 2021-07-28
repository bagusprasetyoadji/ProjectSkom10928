package com.bagusprasetyoadji.projectskom.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bagusprasetyoadji.projectskom.R
import com.bagusprasetyoadji.projectskom.data.Mahasiswa
import com.bagusprasetyoadji.projectskom.data.MahasiswaAdapter
import com.bagusprasetyoadji.projectskom.databinding.FragmentHomeBinding
import com.google.firebase.database.*

class HomeFragment : Fragment() {

    private lateinit var binding : FragmentHomeBinding
    private lateinit var ref : DatabaseReference
    private lateinit var mhsList : MutableList<Mahasiswa>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater,container,false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ref = FirebaseDatabase.getInstance().getReference("mahasiswa")

        binding.button.setOnClickListener {
            saveData()
        }

        mhsList = mutableListOf()
        ref.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){

                    mhsList.clear()
                    for (h in snapshot.children){
                        val mahasiswa = h.getValue(Mahasiswa::class.java)
                        if (mahasiswa != null) {
                            mhsList.add(mahasiswa)
                        }
                    }
                    val adapter = MahasiswaAdapter(requireActivity(),R.layout.item_mhs,mhsList)
                    binding.lvMhs.adapter = adapter
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun saveData() {
        val nama = binding.etNama.text.toString().trim()
        val alamat = binding.etAlamat.text.toString().trim()

        if (nama.isEmpty()){
            binding.etNama.error = "Isi Nama!"
            return
        }
        if (alamat.isEmpty()){
            binding.etAlamat.error = "Isi Alamat!"
            return
        }

        val mhsId = ref.push().key
        val mhs  = Mahasiswa(mhsId!!,nama, alamat)
        ref.child(mhsId).setValue(mhs).addOnCompleteListener {
            Toast.makeText(context,"Data Berhasil Ditambahkan",Toast.LENGTH_SHORT).show()
        }
    }

}