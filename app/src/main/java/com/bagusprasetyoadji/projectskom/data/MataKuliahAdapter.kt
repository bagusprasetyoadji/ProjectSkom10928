package com.bagusprasetyoadji.projectskom.data

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.bagusprasetyoadji.projectskom.R

class MataKuliahAdapter(val mCtx: Context, val layoutResId: Int, val matkulList: List<MataKuliah>) :
    ArrayAdapter<MataKuliah>(mCtx, layoutResId, matkulList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater: LayoutInflater = LayoutInflater.from(mCtx)
        val view: View = layoutInflater.inflate(layoutResId, null)
        val tvNamaMatkul = view.findViewById<TextView>(R.id.tv_matkul)

        val matkul = matkulList[position]
        tvNamaMatkul.text = matkul.nama
        return view
    }
}
