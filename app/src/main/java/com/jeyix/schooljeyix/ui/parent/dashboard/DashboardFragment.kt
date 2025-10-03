package com.jeyix.schooljeyix.ui.parent.dashboard

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.android.material.card.MaterialCardView
import com.jeyix.schooljeyix.R


class DashboardFragment : Fragment() {

    private lateinit var btnStudents: MaterialCardView
    private lateinit var btnPayments: MaterialCardView
    private lateinit var btnNotifications: MaterialCardView
    private lateinit var btnProfile: MaterialCardView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.parent_fragment_dashboard, container, false)

        // Referencias a los botones
        btnStudents = view.findViewById(R.id.btnStudents)
        btnPayments = view.findViewById(R.id.btnPayments)
        btnNotifications = view.findViewById(R.id.btnNotifications)
        btnProfile = view.findViewById(R.id.btnProfile)

        // Configurar acciones
        setupActions()

        return view
    }

    private fun setupActions() {
        btnStudents.setOnClickListener {
            findNavController().navigate(R.id.nav_parent_students)
        }

        btnPayments.setOnClickListener {
            findNavController().navigate(R.id.nav_parent_payments)
        }

        btnNotifications.setOnClickListener {
            findNavController().navigate(R.id.nav_parent_notifications)
        }

        btnProfile.setOnClickListener {
            findNavController().navigate(R.id.nav_parent_profile)
        }
    }

}