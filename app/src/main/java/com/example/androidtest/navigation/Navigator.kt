package com.example.androidtest.navigation

import androidx.fragment.app.Fragment

fun Fragment.navigator() : Navigator = requireActivity() as Navigator

interface Navigator {

    fun launchGameScreen(startNumber: Int = 0)

    fun launchAboutScreen()

    fun back()
}