package com.jeyix.schooljeyix.ui.parent.dashboard

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.jeyix.schooljeyix.R
import com.jeyix.schooljeyix.data.local.datastore.UserPreferences
import dagger.hilt.android.AndroidEntryPoint
import jakarta.inject.Inject
import kotlinx.coroutines.launch
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop

@AndroidEntryPoint
class DashboardFragment : Fragment() {

    @Inject
    lateinit var userPreferences: UserPreferences

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tvWelcome = view.findViewById<TextView>(R.id.tvWelcome)
        val ivAvatar = view.findViewById<ImageView>(R.id.ivAvatar)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                userPreferences.user.collect { user ->
                    user?.let {
                        tvWelcome.text = "¡Hola, ${it.firstName ?: "Usuario"}!"

                        if (!it.profileImageUrl.isNullOrBlank()) {
                            Glide.with(requireContext())
                                .load(it.profileImageUrl)
                                .transform(CircleCrop())
                                .placeholder(R.drawable.ic_parent_avatar_24)
                                .error(R.drawable.ic_parent_avatar_24)
                                .into(ivAvatar)
                        } else {
                            ivAvatar.setImageResource(R.drawable.ic_parent_avatar_24)
                        }
                    }
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.parent_fragment_dashboard, container, false)


        return view
    }



}