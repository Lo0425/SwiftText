package com.example.swifttext.ui.presentation.home

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.swifttext.R
import com.example.swifttext.databinding.FragmentHomeBinding
import com.example.swifttext.service.AuthService
import com.example.swifttext.ui.presentation.activatedRulesFragment.ActivatedRulesFragment
import com.example.swifttext.ui.presentation.adapter.PagerAdapter
import com.example.swifttext.ui.presentation.allRulesFragment.AllRulesFragment
import com.example.swifttext.ui.presentation.base.BaseFragment
import com.example.swifttext.ui.presentation.deactivatedRulesFragment.DeactivatedRulesFragment
import com.example.swifttext.ui.presentation.home.viewModel.HomeViewModel
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() {


//    private val allRulesFragment = AllRulesFragment.getInstance()
//    private val deactivatedRulesFragment = DeactivatedRulesFragment.getInstance()
//    private val activatedRulesFragment = ActivatedRulesFragment.getInstance()


    override val viewModel: HomeViewModel by viewModels()

    @Inject
    lateinit var authService: AuthService
    override fun getLayoutResource() = R.layout.fragment_home

    override fun onBindView(view: View, savedInstanceState: Bundle?) {
        super.onBindView(view, savedInstanceState)

        val actionBar = (activity as AppCompatActivity).supportActionBar
        actionBar?.show()



        val adapter = PagerAdapter(
            listOf(AllRulesFragment(), ActivatedRulesFragment(), DeactivatedRulesFragment()),
            requireActivity().supportFragmentManager,
            lifecycle
        )


        binding?.run {
            btnAddRules.setOnClickListener {
                navController.navigate(R.id.combinationFragment)
            }
        }


        binding?.viewPager?.adapter = adapter

        TabLayoutMediator(binding!!.tabLayout,binding!!.viewPager){ tab, pos ->
            tab.text = when(pos){
                0 -> "All"
                1 -> "Activated"
                2 -> "Deactivated"
                else -> "none"
            }
        }.attach()

    }

    override fun onBindData(view: View) {
        super.onBindData(view)
//        viewModel.users.observe(viewLifecycleOwner) {
//            adapter.setChats(it.toMutableList())
//        }
    }



}