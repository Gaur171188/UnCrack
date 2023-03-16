package com.geekymusketeers.uncrack.ui.fragments

import android.content.ClipData
import android.content.ClipboardManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.geekymusketeers.uncrack.R
import com.geekymusketeers.uncrack.databinding.FragmentGeneratePasswordBinding
import com.google.android.material.slider.LabelFormatter
import com.google.android.material.slider.Slider
import com.google.android.material.snackbar.Snackbar
import nu.aaro.gustav.passwordstrengthmeter.PasswordStrengthCalculator
import java.util.*


class GeneratePasswordFragment : Fragment(R.layout.fragment_generate_password) {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentGeneratePasswordBinding.bind(view)

        var passwordLength = 13

        binding.passwordInputMeter.setPasswordStrengthCalculator(object : PasswordStrengthCalculator{
            override fun calculatePasswordSecurityLevel(password: String?): Int {
                return if (password!=null){
                    getPasswordScope(password)
                }else{
                    0
                }
            }
            override fun getMinimumLength(): Int {
                return 1
            }
            override fun passwordAccepted(level: Int): Boolean {
                return true
            }
            override fun onPasswordAccepted(password: String?) {}
        })

        val generatedPassword = generatePassword(passwordLength,
            includeUpperCaseLetters = binding.cbUppercase.isChecked,
            includeLowerCaseLetters = binding.cbLowercase.isChecked,
            includeSymbols = binding.cbSymbols.isChecked,
            includeNumbers = binding.cbNumbers.isChecked)

        binding.passwordInputMeter.setEditText(binding.etGeneratedPassword)
        binding.etGeneratedPassword.setText(generatedPassword)

        binding.sliderPasswordStrength.addOnSliderTouchListener(object : Slider.OnSliderTouchListener{
            override fun onStartTrackingTouch(slider: Slider) {

            }

            override fun onStopTrackingTouch(slider: Slider) {
                passwordLength = slider.value.toInt()
            }
        })

        binding.sliderPasswordStrength.addOnChangeListener(object : Slider.OnChangeListener{
            override fun onValueChange(slider: Slider, value: Float, fromUser: Boolean) {
                slider.setLabelFormatter(LabelFormatter {
                    return@LabelFormatter "${value.toInt()} Letters"
                })
            }
        })

        binding.btnGeneratePassword.setOnClickListener {

            val generatedPassword = generatePassword(passwordLength,
                includeUpperCaseLetters = binding.cbUppercase.isChecked,
                includeLowerCaseLetters = binding.cbLowercase.isChecked,
                includeSymbols = binding.cbSymbols.isChecked,
                includeNumbers = binding.cbNumbers.isChecked)

            if (generatedPassword.isBlank()){
                if (passwordLength==0){
                    Toast.makeText(requireContext(),"Password length cannot be zero", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(),"Please check at least one item", Toast.LENGTH_SHORT).show()
                }
            }else{
                binding.etGeneratedPassword.setText(generatedPassword)
            }
        }

        binding.btnCopyPassword.setOnClickListener {
            val clipboard: ClipboardManager? = ContextCompat.getSystemService(
                requireContext(),
                ClipboardManager::class.java
            )
            val clip = ClipData.newPlainText("Generated Password", binding.etGeneratedPassword.text.toString())
            clipboard?.setPrimaryClip(clip)
            Toast.makeText(requireContext(),"Password Copied to Clipboard", Toast.LENGTH_SHORT).show()
        }
    }

    private fun<E> MutableList<E>.mRandom(): E? = if (size>0) get(Random().nextInt(size)) else null

    private fun generatePassword(length: Int, includeUpperCaseLetters: Boolean,includeLowerCaseLetters : Boolean,
                                includeSymbols : Boolean, includeNumbers : Boolean): String {

        var password = ""
        val list = mutableListOf<Int>()
        if (includeUpperCaseLetters)
            list.add(0)
        if (includeLowerCaseLetters)
            list.add(1)
        if (includeNumbers)
            list.add(2)
        if (includeSymbols)
            list.add(3)

        for(i in 1..length){
            when(list.toMutableList().mRandom()){
                0-> password += ('A'..'Z').toMutableList().mRandom().toString()
                1-> password += ('a'..'z').toMutableList().mRandom().toString()
                2-> password += ('0'..'9').toMutableList().mRandom().toString()
                3-> password += listOf('!','@','#','$','%','&','*','+','=','-','~','?','/','_').toMutableList().mRandom().toString()
            }
        }
        return password
    }

    private fun getPasswordScope(password: String): Int {
        if(password.isEmpty() || password.isBlank()) {
            return 0
        } else {
            return if(password.isEmpty()) {
                0
            } else if (password.length in 1..3) {
                1
            } else if (password.length in 4..6) {
                2
            } else if (password.length in 7..9) {
                3
            } else if (password.length in 10..12) {
                4
            } else {
                5
            }
        }
    }

}