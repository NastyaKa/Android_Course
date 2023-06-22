package com.gitlab.nastyaka.chat

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.gitlab.nastyaka.chat.databinding.FragmentMainBinding
import com.gitlab.nastyaka.chat.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    private val viewModel: MainViewModel by viewModels()
    private var name: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        name = arguments?.getString("name")
        if (name != null) {
            setter()
        }
    }

    private fun setter() {
        val recycle = binding.layoutRecycle
        val sendButton = binding.sendingButton
        val sendImgButton = binding.prishebochka
        val backButton = binding.imgBack

        val adapter = MessagesListAdapter(requireContext())
        viewModel.msgs(name!!).observe(viewLifecycleOwner) {
            adapter.submitList(it) {
                activity?.reportFullyDrawn()
            }
        }
        recycle.adapter = adapter
        recycle.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        sendButton.setOnClickListener {
            viewModel.sendTxt(this, binding, name!!)
        }
        sendImgButton.setOnClickListener {
            val sendingIntent = Intent()
            sendingIntent.apply {
                type = "image/*"
                action = Intent.ACTION_GET_CONTENT
            }
            launchPicture.launch(sendingIntent)
        }
        backButton?.setOnClickListener {
            try {
                parentFragmentManager.popBackStackImmediate(
                    "chat",
                    FragmentManager.POP_BACK_STACK_INCLUSIVE
                )
            } catch (e: NullPointerException) {
                Log.e(e.javaClass.simpleName, e.message.toString())
            }
        }
    }

    private val launchPicture =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val selectedImageUri: Uri = it.data!!.data!!
                viewModel.sendPhoto(this, selectedImageUri, name!!)
            }
        }
}