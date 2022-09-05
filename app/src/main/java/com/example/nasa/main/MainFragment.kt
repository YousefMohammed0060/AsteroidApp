package com.example.nasa.main

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.nasa.Asteroid
import com.example.nasa.R
import com.example.nasa.database.AsteroidDatabase
import com.example.nasa.database.DatabaseAsteroid
import com.example.nasa.databinding.FragmentMainBinding
import kotlin.collections.ArrayList as ArrayList

class MainFragment : Fragment() {
    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        var adapter = AsteroidAdapter(AsteroidListener {
            viewModel.onAsteroidClicked(it)
        })


        viewModel.list.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })
        binding.asteroidRecycler.adapter = adapter

        viewModel.navigateToAsteroidDetails.observe(viewLifecycleOwner, Observer {
            it?.let {
                findNavController().navigate(MainFragmentDirections.actionMainFragmentToDetailFragment(it))
                viewModel.onAsteroidDetailsNavigated()
            }
        })


        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        viewModel.updateFilter(
            when (item.itemId) {
                R.id.show_week_menu -> Filter.SHOW_WEEK
                R.id.show_today_menu -> Filter.SHOW_TODAY
                else -> Filter.SHOW_SAVED
            }
        )
        return true
    }
}