package com.jeyix.schooljeyix.ui.admin.notifications

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.jeyix.schooljeyix.R
import com.jeyix.schooljeyix.databinding.FragmentAdminNotificationsBinding
import com.jeyix.schooljeyix.domain.model.Announcement
import com.jeyix.schooljeyix.domain.model.AnnouncementType
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotificationsFragment : Fragment() {

    private var _binding: FragmentAdminNotificationsBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: AnnouncementsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdminNotificationsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupFab()

        loadDummyData()
    }

    private fun setupRecyclerView() {
        adapter = AnnouncementsAdapter { announcement ->
            Toast.makeText(context, "Click en: ${announcement.title}", Toast.LENGTH_SHORT).show()
        }

        binding.rvAnnouncements.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@NotificationsFragment.adapter
        }
    }

    private fun setupFab() {
        binding.fabAddAnnouncement.setOnClickListener {
            findNavController().navigate(R.id.action_nav_admin_communications_to_createAnnouncementFragment)
        }
    }

    private fun loadDummyData() {
        val dummyList = listOf(
            Announcement(1, "Suspensión de Clases", "Debido al mantenimiento eléctrico, mañana no habrá clases.", "Todos", "Hace 10m",
                AnnouncementType.URGENT, true),
            Announcement(2, "Recordatorio de Pago", "La mensualidad de Noviembre vence pronto.", "Padres", "Hace 2h", AnnouncementType.PAYMENT, false),
            Announcement(3, "Feria de Ciencias", "Están invitados a la feria anual este sábado.", "Todos", "Ayer", AnnouncementType.INFO, false),
            Announcement(4, "Reunión de Profesores", "Reunión de coordinación en la sala de maestros.", "Profesores", "Hace 3 días", AnnouncementType.INFO, true)
        )
        adapter.submitList(dummyList)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}