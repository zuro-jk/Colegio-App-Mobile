package com.jeyix.schooljeyix.ui.admin.dashboard

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.jeyix.schooljeyix.R
import com.jeyix.schooljeyix.databinding.FragmentAdminDashboardBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

@AndroidEntryPoint
class DashboardFragment : Fragment() {

    private var _binding: FragmentAdminDashboardBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DashboardViewModel by viewModels()
    private lateinit var activityAdapter: RecentActivityAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdminDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupHeader()
        setupChartConfig()
        setupRecyclerView()
        setupButtons()
        setupObservers()
    }


    private fun setupButtons() {
        binding.btnAddStudent.setOnClickListener {
            findNavController().navigate(R.id.action_dashboard_to_studentForm)
        }

        binding.btnAddPayment.setOnClickListener {
            findNavController().navigate(R.id.action_dashboard_to_createEnrollment)
        }

        binding.btnSendNotice.setOnClickListener {
            findNavController().navigate(R.id.action_dashboard_to_createAnnouncement)
        }

        binding.btnViewAllActivity.setOnClickListener {
            findNavController().navigate(R.id.nav_admin_finance)
        }

        binding.tvTitle.setOnClickListener {
            viewModel.loadDashboardData()
            Toast.makeText(context, "Actualizando datos...", Toast.LENGTH_SHORT).show()
        }
    }


    private fun setupHeader() {
        val sdf = SimpleDateFormat("EEEE, d MMMM", Locale("es", "ES"))
        val currentDate = sdf.format(Date())
        binding.tvSubtitle.text = "Última actualización: $currentDate"
    }

    private fun setupRecyclerView() {
        activityAdapter = RecentActivityAdapter()
        binding.rvRecentActivity.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = activityAdapter
        }
    }

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collectLatest { state ->

                binding.tvKpiStudents.text = state.studentCount.toString()
                binding.tvKpiIncome.text = "S/ ${state.totalIncome}"
                binding.tvKpiAlerts.text = state.pendingAlerts.toString()

                if (state.pendingAlerts > 0) {
                    binding.tvKpiAlertsLabel.text = "Requieren Atención"
                    binding.tvKpiAlertsLabel.setTextColor(ContextCompat.getColor(requireContext(), com.google.android.material.R.color.design_default_color_error))
                } else {
                    binding.tvKpiAlertsLabel.text = "Todo en orden"
                    binding.tvKpiAlertsLabel.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.darker_gray))
                }

                if (state.chartData.isNotEmpty()) {
                    updateChartData(state.chartData)
                }

                activityAdapter.submitList(state.recentActivity)

                if (!state.error.isNullOrEmpty()) {
                    Toast.makeText(context, state.error, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun setupChartConfig() {
        val barChart = binding.barChart
        barChart.description.isEnabled = false
        barChart.legend.isEnabled = false
        barChart.axisRight.isEnabled = false
        barChart.axisLeft.axisMinimum = 0f

        val xAxis = barChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(false)
        xAxis.valueFormatter = IndexAxisValueFormatter(listOf("Lun", "Mar", "Mié", "Jue", "Vie"))
    }

    private fun updateChartData(dataList: List<Float>) {
        val entries = ArrayList<BarEntry>()
        dataList.forEachIndexed { index, value ->
            entries.add(BarEntry(index.toFloat(), value))
        }

        val color = try {
            ContextCompat.getColor(requireContext(), R.color.accent_teal)
        } catch (e: Exception) {
            Color.BLUE
        }

        val dataSet = BarDataSet(entries, "Ingresos")
        dataSet.color = color
        dataSet.valueTextColor = Color.BLACK
        dataSet.valueTextSize = 12f

        val barData = BarData(dataSet)
        binding.barChart.data = barData
        binding.barChart.invalidate()
        binding.barChart.animateY(1000)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}