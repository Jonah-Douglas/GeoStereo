package com.campfire.geostereo.onboarding.screens

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.campfire.geostereo.R
import com.campfire.geostereo.databinding.FragmentThirdScreenBinding


/**
 *  Third screen of onboarding.
 */
class ThirdScreen : Fragment() {
    private var _binding: FragmentThirdScreenBinding? = null
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentThirdScreenBinding.inflate(inflater, container, false)

        val viewPager = activity?.findViewById<ViewPager2>(R.id.onboardingViewPager)

        binding?.thirdScreenFinish?.setOnClickListener {
            findNavController().navigate(R.id.action_viewPagerFragment_to_findNearestLocationFragment)
            onBoardingFinished()
        }

        binding?.thirdScreenPrevious?.setOnClickListener {
            viewPager?.currentItem = 1
        }

        return binding?.root
    }

    // TODO: use Pref DataStore here instead of SharedPref
    private fun onBoardingFinished() {
        val sharedPref = requireActivity().getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putBoolean("Finished", true)
        editor.apply()
    }
}