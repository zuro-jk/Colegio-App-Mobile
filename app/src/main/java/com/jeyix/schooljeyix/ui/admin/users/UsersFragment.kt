package com.jeyix.schooljeyix.ui.admin.users

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import com.jeyix.schooljeyix.R
import com.jeyix.schooljeyix.databinding.FragmentAdminUsersBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UsersFragment : Fragment(R.layout.fragment_admin_users) {

    private var _binding: FragmentAdminUsersBinding? = null
    private val binding get() = _binding!!

    private var pagerAdapter: UsersPagerAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAdminUsersBinding.bind(view)

        pagerAdapter = UsersPagerAdapter(this)
        binding.viewPager.adapter = pagerAdapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Estudiantes"
                1 -> "Padres"
                2 -> "Administradores"
                else -> null
            }
        }.attach()

        binding.btnAddUser.setOnClickListener {
            val currentTabPosition = binding.tabLayout.selectedTabPosition

            when (currentTabPosition) {

                0 -> {
                    val action = UsersFragmentDirections.actionNavAdminUsersToAdminStudentFormFragment(null)
                    findNavController().navigate(action)
                }
                1 -> {
                    val action = UsersFragmentDirections.actionUsersFragmentToEditParentFragment(0L)
                    findNavController().navigate(action)
                }
                2 -> {
                    val action = UsersFragmentDirections.actionNavAdminUsersToAdminStaffFormFragment(0L)
                    findNavController().navigate(action)
                }
            }
        }

        setupSearchListeners()
    }

    private fun setupSearchListeners() {
        binding.etSearch.setOnEditorActionListener { textView, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val query = textView.text.toString()
                performSearch(query)
                hideKeyboard()
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }

        binding.tilSearch.setEndIconOnClickListener {
            binding.etSearch.setText("")
            performSearch("")
            hideKeyboard()
        }
    }

    /**
     * Pasa la consulta de búsqueda al fragmento hijo actual.
     */
    private fun performSearch(query: String) {
        val currentFragmentTag = "f${binding.viewPager.currentItem}"
        val currentFragment = childFragmentManager.findFragmentByTag(currentFragmentTag)

        (currentFragment as? SearchableFragment)?.onSearchQuery(query)
    }

    /**
     * Oculta el teclado.
     */
    private fun hideKeyboard() {
        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(binding.etSearch.windowToken, 0)
        binding.etSearch.clearFocus()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.viewPager.adapter = null
        pagerAdapter = null
        _binding = null
    }
}