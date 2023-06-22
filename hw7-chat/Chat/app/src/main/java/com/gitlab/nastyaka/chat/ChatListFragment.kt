package com.gitlab.nastyaka.chat

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.gitlab.nastyaka.chat.databinding.FragmentChatlistBinding
import com.gitlab.nastyaka.chat.viewmodels.ChatListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatListFragment : Fragment() {

    private lateinit var binding: FragmentChatlistBinding
    private val viewModel: ChatListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatlistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = ChatsListAdapter(requireContext()) { name ->
            val fragmentManager = parentFragmentManager
            val dest =
                if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                    R.id.first_container
                } else {
                    R.id.second_container
                }
            val chatFragment = MainFragment().apply {
                arguments = Bundle().apply {
                    putString("name", name)
                }
            }
            val oldFragment = fragmentManager.findFragmentById(dest) ?: return@ChatsListAdapter
            try {
                fragmentManager.popBackStackImmediate(
                    "chat",
                    FragmentManager.POP_BACK_STACK_INCLUSIVE
                )
            } catch (e: NullPointerException) {
                Log.e(e.javaClass.simpleName, e.message.toString())
            }
            fragmentManager.executePendingTransactions()
            fragmentManager
                .beginTransaction()
                .add(dest, chatFragment)
                .hide(oldFragment)
                .addToBackStack("chat")
                .commitAllowingStateLoss()
        }
        binding.layoutRecycle.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.layoutRecycle.adapter = adapter

        viewModel.chatList.observe(viewLifecycleOwner) {
            adapter.submitList(it) {
                activity?.reportFullyDrawn()
            }
        }
    }
}