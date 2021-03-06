package com.campfire.geostereo.onboarding.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.widget.ViewPager2
import com.campfire.geostereo.R
import com.campfire.geostereo.data.PrimaryViewModel
import com.campfire.geostereo.databinding.FragmentSecondScreenBinding


/**
 *  Second screen of onboarding.
 */
class SecondScreen : Fragment() {
    private var _binding: FragmentSecondScreenBinding? = null
    private val binding get() = _binding!!

    private val sharedViewModel: PrimaryViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            viewModel = sharedViewModel
            secondScreen = this@SecondScreen
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentSecondScreenBinding.inflate(inflater, container, false)

        val viewPager = activity?.findViewById<ViewPager2>(R.id.onboardingViewPager)

        binding.secondScreenNext.setOnClickListener {
            viewPager?.currentItem = 2
        }

        binding.secondScreenPrevious.setOnClickListener {
            viewPager?.currentItem = 0
        }

        return binding.root
    }
}