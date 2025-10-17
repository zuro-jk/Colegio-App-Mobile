package com.jeyix.schooljeyix.ui.admin.users

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.jeyix.schooljeyix.ui.admin.users.staff.AdminStaffListFragment
import com.jeyix.schooljeyix.ui.admin.users.parent.AdminParentListFragment
import com.jeyix.schooljeyix.ui.admin.users.student.AdminStudentListFragment

private const val NUM_TABS = 3

class UsersPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> AdminStudentListFragment()
            1 -> AdminParentListFragment()
            2 -> AdminStaffListFragment()
            else -> throw IllegalStateException("Posición de pestaña inválida")
        }
    }
}