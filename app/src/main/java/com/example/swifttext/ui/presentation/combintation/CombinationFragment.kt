package com.example.swifttext.ui.presentation.combintation

import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import com.example.swifttext.R
import com.example.swifttext.databinding.FragmentCombinationBinding
import com.example.swifttext.ui.presentation.base.BaseFragment
import com.example.swifttext.ui.presentation.combintation.viewModel.CombinationViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CombinationFragment : BaseFragment<FragmentCombinationBinding>() {

    override val viewModel: CombinationViewModel by viewModels()
    override fun getLayoutResource() = R.layout.fragment_combination

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindView(view: View, savedInstanceState: Bundle?) {
        super.onBindView(view, savedInstanceState)

        binding?.run {
            btnDelete.setIconResource(R.drawable.close)
            btnConfirm.setOnClickListener {
                val textIncludes = etTextIncludes.text
                val reply = etReply.text
                val status = btnActivate.isChecked

                if(textIncludes.isEmpty() || reply.isEmpty()){
                    val error = Toast.makeText(requireContext(),"Please fill in all details", Toast.LENGTH_SHORT)
                    error.show()
                }else{
                    viewModel.addRules(textIncludes,reply,status)
                    navController.navigate(R.id.homeFragment)
                }
            }

            btnDelete.setOnClickListener {
                navController.navigate(R.id.homeFragment)
            }
        }
    }

    override fun onBindData(view: View) {
        super.onBindData(view)

    }

}