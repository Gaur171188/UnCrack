package com.geekymusketeers.uncrack.fragments

import android.os.Bundle
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.navigation.NavArgs
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.geekymusketeers.uncrack.R
import com.geekymusketeers.uncrack.databinding.FragmentEditBinding
import com.geekymusketeers.uncrack.databinding.OptionsModalBinding
import com.geekymusketeers.uncrack.helper.Util
import com.geekymusketeers.uncrack.helper.Util.Companion.createBottomSheet
import com.geekymusketeers.uncrack.helper.Util.Companion.setBottomSheet
import com.geekymusketeers.uncrack.model.Account
import com.geekymusketeers.uncrack.viewModel.AccountViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.chip.Chip


class EditFragment : Fragment() {


    private var _binding: FragmentEditBinding? = null
    private val binding get() = _binding!!


    private val args by navArgs<EditFragmentArgs>()

    private lateinit var accountViewModel: AccountViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentEditBinding.inflate(inflater,container,false)

        accountViewModel = ViewModelProvider(this)[AccountViewModel::class.java]


        // Fetching data from adapter
        val acc = arguments?.getString("company")
        binding.editAccType.setText(acc)
        val email = arguments?.getString("email")
        binding.editEmail.setText(email)
        val username = arguments?.getString("username")
        binding.editUsername.setText(username)
        val pass = arguments?.getString("password")
        binding.editPassword.setText(pass)

        // Setting logo according to the account type

        when(acc?.toLowerCase().toString()){
            "general account" -> setImageOnAccountNameChange(R.drawable.general_account)
            "paypal" -> setImageOnAccountNameChange(R.drawable.paypal)
            "instagram" -> setImageOnAccountNameChange(R.drawable.instagram)
            "facebook" -> setImageOnAccountNameChange(R.drawable.facebook)
            "linkedin" -> setImageOnAccountNameChange(R.drawable.linkedin)
            "snapchat" -> setImageOnAccountNameChange(R.drawable.snapchat)
            "gmail" -> setImageOnAccountNameChange(R.drawable.gmail)
            "twitter" -> setImageOnAccountNameChange(R.drawable.twitter)
            "google drive" -> setImageOnAccountNameChange(R.drawable.drive)
            "netflix" -> setImageOnAccountNameChange(R.drawable.netflix)
            "amazon prime" -> setImageOnAccountNameChange(R.drawable.amazon)
            "spotify" -> setImageOnAccountNameChange(R.drawable.spotify)
            "discord" -> setImageOnAccountNameChange(R.drawable.discord)
        }


        // Setting adapter for the companies list
        val accounts = resources.getStringArray(R.array.accounts)
        val arrayAdapter = ArrayAdapter(requireContext(),R.layout.list_items,accounts)
        binding.editAccType.setAdapter(arrayAdapter)

        requireActivity().findViewById<BottomNavigationView>(R.id.bottom_nav).visibility = View.GONE

        binding.btnEdit.setOnClickListener {
            updateDB()
        }

        binding.editBack.setOnClickListener {

            backButton()
        }
        return binding.root
    }

    private fun setImageOnAccountNameChange(imageID: Int) {
        binding.accountLogo.apply {
            setImageResource(imageID)
        }
    }

    private fun backButton() {

        val editAccountType = binding.editAccType.text.toString().trim()
        val editEmail = binding.editEmail.text.toString().trim()
        val editUserName = binding.editUsername.text.toString().trim()
        val editPassword = binding.editPassword.text.toString().trim()

        if (editAccountType.isNotEmpty() || editEmail.isNotEmpty() || editUserName.isNotEmpty() || editPassword.isNotEmpty()) {

            val dialog = OptionsModalBinding.inflate(layoutInflater)
            val bottomSheet = requireContext().createBottomSheet()
            dialog.apply {

                optionsHeading.text = "Discard changes"
                optionsContent.text = "Are you sure you discard changes?"
                positiveOption.text = "Discard"
                positiveOption.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.red
                    )
                )
                negativeOption.text = "Continue editing"
                negativeOption.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.black
                    )
                )
                positiveOption.setOnClickListener {
                    bottomSheet.dismiss()
                    val frag = HomeFragment()
                    val trans = fragmentManager?.beginTransaction()
                    trans?.replace(R.id.fragment,frag)?.commit()
                }
                negativeOption.setOnClickListener {
                    bottomSheet.dismiss()

                }
            }
            dialog.root.setBottomSheet(bottomSheet)
        }
        else {
            findNavController().navigate(R.id.action_editFragment_to_homeFragment)
        }
    }
    override fun onResume() {
        super.onResume()
        requireView().isFocusableInTouchMode = true
        requireView().requestFocus()
        requireView().setOnKeyListener { _, keyCode, event ->
            if (event.action === KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                backButton()
                true
            } else false
        }
    }

    private fun updateDB() {

        val accountName = binding.editAccType.text.toString()
        val email = binding.editEmail.text.toString()
        val category: String = (binding.editCategoryChipGroup.children.toList().filter {
            (it as Chip).isChecked
        }[0] as Chip).text.toString()
        val password = binding.editPassword.text.toString()
        val userName = binding.editUsername.text.toString()

        val updateAccount = Account(0,accountName, email, category,userName, password)

        accountViewModel.editAccount(updateAccount)
        Toast.makeText(requireContext(),"Successfully Edited",Toast.LENGTH_LONG).show()

        val frag = HomeFragment()
        val trans = fragmentManager?.beginTransaction()
        trans?.replace(R.id.fragment,frag)?.commit()

    }


}