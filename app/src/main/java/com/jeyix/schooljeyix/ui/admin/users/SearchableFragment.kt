package com.jeyix.schooljeyix.ui.admin.users

/**
 * Una interfaz que los fragmentos dentro del ViewPager pueden implementar
 * para recibir consultas de búsqueda del UsersFragment padre.
 */
interface SearchableFragment {
    fun onSearchQuery(query: String)
}