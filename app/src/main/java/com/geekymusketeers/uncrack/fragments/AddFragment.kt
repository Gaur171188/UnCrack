package com.geekymusketeers.uncrack.fragments

import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.geekymusketeers.uncrack.R
import com.geekymusketeers.uncrack.model.Account
import com.geekymusketeers.uncrack.databinding.FragmentAddBinding
import com.geekymusketeers.uncrack.viewModel.AccountViewModel


class AddFragment : Fragment() {

    private  var _binding : FragmentAddBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel : AccountViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentAddBinding.inflate(inflater,container,false)
        viewModel = ViewModelProvider(this)[AccountViewModel::class.java]

        binding.btnSave.setOnClickListener {

            val company = binding.accType.text.toString()
            val email = binding.email.text.toString()
            val password = binding.password.text.toString()

            if (company.isEmpty()){
                Toast.makeText(requireContext(),"Enter select company",Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                Toast.makeText(requireContext(),"Please check your Email Id",Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }else if (!isValidPassword(password)){
                Toast.makeText(requireContext(),"Password is to weak",Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            insertDataToDB()

        }
        binding.backBtn.setOnClickListener {
            findNavController().navigate(R.id.action_addFragment_to_homeFragment)
        }

        // Account List
        val accounts = resources.getStringArray(R.array.accounts)
        val arrayAdapter = ArrayAdapter(requireContext(),R.layout.list_items,accounts)
        binding.accType.setAdapter(arrayAdapter)

        return binding.root
    }

    private fun isValidPassword(password: String): Boolean {
        if (password.length < 8) return false
        if (password.filter { it.isDigit() }.firstOrNull() == null) return false
        if (password.filter { it.isLetter() }.filter { it.isUpperCase() }.firstOrNull() == null) return false
        if (password.filter { it.isLetter() }.filter { it.isLowerCase() }.firstOrNull() == null) return false
        if (password.filter { !it.isLetterOrDigit() }.firstOrNull() == null) return false

        return true
    }


    private fun insertDataToDB() {
        val company = binding.accType.text.toString()
        val email = binding.email.text.toString()
        val password = binding.password.text.toString()

            val account = Account(0,company, email, password)

            viewModel.addAccount(account)
            Toast.makeText(requireContext(),"Successfully Saved",Toast.LENGTH_LONG).show()
            // Moving into HomeFragment after saving
            val frag = HomeFragment()
            val trans = fragmentManager?.beginTransaction()
            trans?.replace(R.id.fragment,frag)?.commit()
    }
}