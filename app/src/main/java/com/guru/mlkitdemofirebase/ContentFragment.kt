package com.guru.mlkitdemofirebase


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_content.view.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class ContentFragment : Fragment() {
    var fragmentType : String? = null
    companion object {

        var INSTANCE: ContentFragment = ContentFragment()

        fun get(type : String) : ContentFragment {
            val bun = Bundle()
            bun.putString("type", type)
            INSTANCE.arguments = bun
            return INSTANCE
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentType = arguments?.getString("type")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_content, container, false)
        view.textview.text =   when (fragmentType) {
            "one" -> "one"
            "two" -> "two"
            "three" -> "three"
            else -> "Null"
        }

        return view
    }


}
