package com.example.yp_playlist_maker.sharing.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.example.yp_playlist_maker.databinding.FragmentWebBinding

const val URI_LINK = "uri_link"

class WebFragment : Fragment() {
    private var uriLink: String? = null
    private lateinit var binding: FragmentWebBinding


   companion object{
       const val TAG = "WebFragment"
        fun newInstance(uri: String): Fragment {
            return WebFragment().apply {
                arguments = bundleOf(
                    URI_LINK to uri
                )
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWebBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.webView.loadUrl(requireArguments().getString(URI_LINK)!!)

        binding.buttonBackAgreement.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}