package com.rasika.offlinewordle

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment

class HelpFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
      val view = inflater.inflate(R.layout.fragment_help,container,false)
        val backButton = view.findViewById<ImageView>(R.id.back_button)
        backButton.setOnClickListener {
            val fragmentManager = activity?.supportFragmentManager
            if (fragmentManager!=null){
                fragmentManager.beginTransaction().remove(this).commit()
            }
        }
             return view
        }
    }
