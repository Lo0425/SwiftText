package com.example.swifttext.ui.presentation.editCombination

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.swifttext.R
import com.example.swifttext.databinding.FragmentEditCombinationBinding
import com.example.swifttext.ui.presentation.base.BaseFragment
import com.example.swifttext.ui.presentation.base.viewModel.BaseViewModel
import com.example.swifttext.ui.presentation.editCombination.viewModel.EditCombinationViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditCombinationFragment : BaseFragment<FragmentEditCombinationBinding>() {

    override val viewModel: EditCombinationViewModel by viewModels()
    override fun getLayoutResource() = R.layout.fragment_edit_combination
    val navArgs: EditCombinationFragmentArgs by navArgs()

    override fun onBindView(view: View, savedInstanceState: Bundle?) {
        super.onBindView(view, savedInstanceState)
        binding?.run {

            btnConfirm.setOnClickListener {
                val textIncludes = etTextIncludes.text
                val reply = etReply.text
                val status = btnActivate.isChecked

                if(textIncludes.isEmpty() || reply.isEmpty()){
                    val error = Toast.makeText(requireContext(),"Please fill in all details", Toast.LENGTH_SHORT)
                    error.show()
                }else{
                    viewModel.editRules(textIncludes,reply,status)
                    navController.navigate(R.id.homeFragment)
                }
            }

            btnDelete.setOnClickListener {
                viewModel.deleteRule(navArgs.id)
                navController.navigate(R.id.homeFragment)
            }
        }
    }
    override fun onBindData(view: View) {
        super.onBindData(view)

        viewModel.getProductById(navArgs.id)
        viewModel.rules.observe(viewLifecycleOwner){
            binding?.run {
                etTextIncludes.setText(it.textIncludes)
                etReply.setText(it.reply)
                btnActivate.isChecked = it.status
            }
        }
    }

}