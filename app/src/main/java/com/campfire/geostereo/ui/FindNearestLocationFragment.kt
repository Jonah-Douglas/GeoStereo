package com.campfire.geostereo.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.campfire.geostereo.databinding.FragmentFindNearestLocationBinding


class FindNearestLocationFragment : Fragment() {

    private var _binding: FragmentFindNearestLocationBinding? = null
    // Property only valid between onCreateView and onDestroyView, so !! ok
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)

        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFindNearestLocationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.findNearestLocation.setOnClickListener {
            // TODO: Google Maps search here (create coroutine call to do searches so it's nonblocking
            // TODO: On Result, navigate to new fragment
                // shows nearest location
                // if nothing nearby, query a weather API
            // TODO: Use final result to query Spotify API (again coroutine call)
                // show song playing in app, have some features such as pause song, skip, etc.
                // if I can't implement spotify in my app, use implicit/explicit Intent to pull up Spotify playlist on their phone and begin playing it
            Toast.makeText(context, "Implement Google Maps search here, navigate to new fragment showing nearest location and song playing.", Toast.LENGTH_SHORT).show()
        }
    }
}