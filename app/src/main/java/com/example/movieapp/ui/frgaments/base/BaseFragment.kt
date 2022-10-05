package com.example.movieapp.ui.frgaments.base

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.movieapp.R

/**
 * Base {@link android.app.Fragment} class for every fragment in this application.
 */
abstract class BaseFragment : Fragment() {

    protected abstract fun observeViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
    }
}