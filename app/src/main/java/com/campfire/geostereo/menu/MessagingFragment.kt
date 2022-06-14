package com.campfire.geostereo.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.campfire.geostereo.databinding.FragmentMessagingBinding


// TODO: -implement messaging between users

/**
 *  Fragment to handle all messaging interactions (display conversations, search conversations,
 *  select conversation, create new message, delete conversation)
 */
class MessagingFragment : Fragment() {
    private var _binding: FragmentMessagingBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMessagingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}