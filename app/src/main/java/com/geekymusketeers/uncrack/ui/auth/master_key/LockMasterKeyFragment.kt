package com.geekymusketeers.uncrack.ui.auth.master_key

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.geekymusketeers.uncrack.R
import com.geekymusketeers.uncrack.databinding.FragmentLockMasterKeyBinding
import com.geekymusketeers.uncrack.ui.MainActivity
import com.geekymusketeers.uncrack.ui.auth.MasterKeyActivity
import com.geekymusketeers.uncrack.util.Encryption
import com.geekymusketeers.uncrack.viewModel.KeyViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class LockMasterKeyFragment : Fragment() {

    private var _binding : FragmentLockMasterKeyBinding?= null
    private val binding get() = _binding!!
    private lateinit var buttonLayout: ConstraintLayout
    private lateinit var buttonText: TextView
    private lateinit var buttonProgress: ProgressBar
    private lateinit var checkKeyViewModel: KeyViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentLockMasterKeyBinding.inflate(inflater,container,false)

        initialization()
        clickHandlers()
        return binding.root
    }

    private fun clickHandlers() {
        buttonLayout.setOnClickListener {
            checkKeyViewModel.getMasterKey().observe(viewLifecycleOwner) { key ->
                val inputMasterKey = binding.inputMasterKey.text.toString()
                showProgress()
                val encryption = Encryption.getDefault("Key", "Salt", ByteArray(16))
                val correctMasterKey = encryption.decryptOrNull(key[0].password)
                if (inputMasterKey.isEmpty() || inputMasterKey.isBlank()){
                    binding.apply {
                        inputMasterKeyHelperTV.text = getString(R.string.master_key_cannot_be_blank)
                        inputMasterKeyHelperTV.visibility = View.VISIBLE
                    }
                    stopProgress()
                    return@observe
                } else {
                    if (inputMasterKey == correctMasterKey) {
                        lifecycleScope.launch(Dispatchers.Main) {
                            delay(800L)
                            goToMainActivity()
                        }
                    }
                    else {
                        binding.apply {
                            inputMasterKeyHelperTV.text = getString(R.string.incorrect_password)
                            inputMasterKeyHelperTV.visibility = View.VISIBLE
                        }
                        stopProgress()
                        return@observe
                    }
                }
            }
        }

    }

    private fun goToMainActivity() {
        val intent = Intent(requireContext(), MainActivity::class.java)
        startActivity(intent)
        (activity as MasterKeyActivity).finish()
    }

    private fun showProgress() {
        buttonText.visibility = View.GONE
        buttonProgress.visibility = View.VISIBLE
    }

    private fun stopProgress() {
        buttonText.visibility = View.VISIBLE
        buttonProgress.visibility = View.GONE
    }

    private fun initialization() {
        checkKeyViewModel = ViewModelProvider(this)[KeyViewModel::class.java]
        binding.apply {
            btnCheckMasterKey.apply {
                this@LockMasterKeyFragment.buttonLayout = this.progressButtonBg
                this@LockMasterKeyFragment.buttonText = this.buttonText
                this@LockMasterKeyFragment.buttonText.text = "Continue"
                this@LockMasterKeyFragment.buttonProgress = this.buttonProgress
            }
        }
    }

}