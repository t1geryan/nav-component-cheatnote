package com.example.androidtest

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.navArgs
import com.example.androidtest.databinding.FragmentGameBinding
import com.example.androidtest.navigation.navigator
import com.example.mycontacts.ui.details.Action
import com.example.mycontacts.ui.details.HasCustomActionToolbar
import kotlin.properties.Delegates

class GameFragment : Fragment(), HasCustomActionToolbar {

    private lateinit var binding: FragmentGameBinding

    private val args: GameFragmentArgs by navArgs()

    private var currentResult: Int by Delegates.notNull()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGameBinding.inflate(inflater, container, false)

        currentResult = savedInstanceState?.getInt(STATE_KEY) ?: args.startNumber
        updateUI()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.incrementBtn.setOnClickListener {
            incrementResult()
        }
        binding.closeBtn.setOnClickListener {
            setFragmentResult(REQUEST_KEY, bundleOf(RESULT_KEY to currentResult))
            navigator().back()
        }
    }

    private fun incrementResult() {
        ++currentResult
        updateUI()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(STATE_KEY, currentResult)
    }

    private fun updateUI() {
        binding.textResult.text = currentResult.toString()
    }

    override fun getCustomAction(): Action = Action(R.drawable.icon_increment, R.string.inc) {
        ++currentResult
        updateUI()
    }

    companion object {
        const val REQUEST_KEY = "REQUEST_KEY"
        const val RESULT_KEY = "RESULT_KEY"

        const val STATE_KEY = "STATE_KEY"
    }


}