package com.example.androidtest

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.androidtest.databinding.FragmentMenuBinding
import com.example.androidtest.navigation.navigator

class MenuFragment : Fragment() {

    private lateinit var binding: FragmentMenuBinding

    private var currentResult: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMenuBinding.inflate(inflater, container, false)

        currentResult = savedInstanceState?.getInt(STATE_KEY) ?: 0
        updateUI()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.aboutBtn.setOnClickListener {
            navigator().launchAboutScreen()
        }
        binding.gameStartBtn.setOnClickListener {
            val curResult = currentResult
            navigator().launchGameScreen(curResult)
        }
        binding.exitBtn.setOnClickListener {
            navigator().back()
        }
        parentFragmentManager.setFragmentResultListener(GameFragment.REQUEST_KEY, viewLifecycleOwner) { _, bundle ->
            currentResult = bundle.getInt(GameFragment.RESULT_KEY, 0)
            updateUI()
        }
    }

    override fun onDestroyView() {
        parentFragmentManager.clearFragmentResultListener(GameFragment.REQUEST_KEY)
        super.onDestroyView()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(STATE_KEY, currentResult)
    }

    private fun updateUI() {
        binding.textResult.text = currentResult.toString()
    }
    companion object {
        const val STATE_KEY = "KEY_STATE"
    }
}