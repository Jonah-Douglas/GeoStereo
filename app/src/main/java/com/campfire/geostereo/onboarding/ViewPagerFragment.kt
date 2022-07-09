package com.campfire.geostereo.onboarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.campfire.geostereo.R
import com.campfire.geostereo.databinding.FragmentViewPagerBinding
import com.campfire.geostereo.onboarding.screens.FirstScreen
import com.campfire.geostereo.onboarding.screens.SecondScreen
import com.campfire.geostereo.onboarding.screens.ThirdScreen


/**
 *  Fragment that contains the ViewPager for the onboarding screen.
 */
class ViewPagerFragment : Fragment() {
    private var _binding: FragmentViewPagerBinding? = null
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentViewPagerBinding.inflate(inflater, container, false)

        val fragmentList = arrayListOf<Fragment>(
            FirstScreen(),
            SecondScreen(),
            ThirdScreen()
        )

        val adapter = ViewPagerAdapter(
            fragmentList,
            requireActivity().supportFragmentManager,
            lifecycle
        )

        binding?.onboardingViewPager?.adapter = adapter

        return binding?.root
    }
}