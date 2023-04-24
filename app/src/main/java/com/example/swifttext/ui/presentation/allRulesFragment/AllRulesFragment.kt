package com.example.swifttext.ui.presentation.allRulesFragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.swifttext.R
import com.example.swifttext.databinding.FragmentAllRulesBinding
import com.example.swifttext.ui.presentation.adapter.RulesAdapter
import com.example.swifttext.ui.presentation.allRulesFragment.viewModel.AllRulesViewModel
import com.example.swifttext.ui.presentation.base.BaseFragment
import com.example.swifttext.ui.presentation.home.HomeFragmentDirections
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AllRulesFragment : BaseFragment<FragmentAllRulesBinding>() {

    override fun getLayoutResource() = R.layout.fragment_all_rules
    private lateinit var adapter: RulesAdapter
    override val viewModel: AllRulesViewModel by viewModels()



    override fun onBindView(view: View, savedInstanceState: Bundle?) {
        super.onBindView(view, savedInstanceState)
        setupAdapter()

        viewModel.rules.observe(viewLifecycleOwner){
            binding?.ivEmpty?.visibility = if(it.isEmpty()) View.VISIBLE else View.GONE
            binding?.tvEmpty?.visibility = if(it.isEmpty()) View.VISIBLE else View.GONE

            adapter.setRules(it)
        }


        binding?.refresh?.setOnRefreshListener {
            viewModel.rules.observe(viewLifecycleOwner){
               adapter.setRules(it)
            }
            binding?.refresh?.isRefreshing = false
        }
    }


    override fun onBindData(view: View) {
        super.onBindData(view)
        lifecycleScope.launch {
            viewModel.isLoading.collect() {
                if (it) {
                    binding?.isLoading?.visibility = View.VISIBLE
                } else {
                    binding?.isLoading?.visibility = View.GONE
                }
            }
        }

    }


    fun setupAdapter() {
        val layoutManager = LinearLayoutManager(requireContext())

        adapter = RulesAdapter(listOf()) { rule ->
                val action =
                    HomeFragmentDirections.actionHomeFragmentToEditCombinationFragment(
                        rule.id
                    )
                NavHostFragment.findNavController(this@AllRulesFragment).navigate(action)
            }
        binding?.rvRules?.adapter = adapter
        binding?.rvRules?.layoutManager = layoutManager
    }



}