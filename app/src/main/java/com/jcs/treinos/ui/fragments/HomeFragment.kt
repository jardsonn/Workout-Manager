package com.jcs.treinos.ui.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.jcs.treinos.R
import com.jcs.treinos.data.models.User
import com.jcs.treinos.data.models.Workout
import com.jcs.treinos.databinding.HomeFragmentBinding
import com.jcs.treinos.databinding.ProfileMenuLayoutBinding
import com.jcs.treinos.ui.AuthenticationActivity
import com.jcs.treinos.ui.adapter.WorkoutAdapter
import com.jcs.treinos.ui.component.CreateWorkoutDialog
import com.jcs.treinos.ui.viewmodel.AuthViewModel
import com.jcs.treinos.ui.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _binding: HomeFragmentBinding? = null
    private var profileBinding: ProfileMenuLayoutBinding? = null
    private val binding get() = _binding!!

    private val authViewModel: AuthViewModel by viewModels()
    private val viewModel: HomeViewModel by viewModels()

    private var user: User? = null

    private lateinit var workoutAdapter: WorkoutAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = HomeFragmentBinding.inflate(layoutInflater, null, false)
        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getWorkout()
        setupRecyclerView()
        setupListener()
        observerWorkouts()
        setVisibilityInfoEmpytList(user == null)
    }

    private fun setupRecyclerView() {
        workoutAdapter = WorkoutAdapter()
        binding.rvWorkout.run {
            adapter = workoutAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun setupListener() {
        binding.root.setOnRefreshListener {
            getWorkout()
        }

        binding.fabNewWorkout.setOnClickListener {
            if (user == null) {
                startActivity(Intent(requireContext(), AuthenticationActivity::class.java))
            } else {
                CreateWorkoutDialog.getInstance(viewModel).apply {

                    setOnDialogButtonsListener { saveState ->
                        when (saveState) {
                            is CreateWorkoutDialog.SaveState.Success -> {
                                getWorkout()
                                Toast.makeText(
                                    requireContext(),
                                    saveState.message,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            is CreateWorkoutDialog.SaveState.Error -> {
                                Toast.makeText(
                                    requireContext(),
                                    saveState.message,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                    }
                }.show(this@HomeFragment.parentFragmentManager, "createWorkout")

            }
        }
    }

    override fun onStart() {
        super.onStart()
        authObserver()
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        val menuItem = menu[0]
        profileBinding = ProfileMenuLayoutBinding.bind(menuItem.actionView)
        setAvatarIcon(user?.photoUrl, profileBinding)
        profileBinding!!.root.setOnClickListener {
            onOptionsItemSelected(menuItem)
        }
        super.onPrepareOptionsMenu(menu)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_signin -> {
                if (user == null) {
                    startActivity(Intent(requireContext(), AuthenticationActivity::class.java))
                } else {
                    authViewModel.signOut()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getWorkout() =
        viewModel.getWorkout()


    private fun observerWorkouts() {
        viewModel.workout.observe(viewLifecycleOwner, { workouts ->
            binding.root.isRefreshing = false
            workoutAdapter.submitList(workouts)
            setVisibilityInfoEmpytList(workouts.isEmpty())
        })
    }

    private fun setVisibilityInfoEmpytList(isShow: Boolean){
        if (isShow){
            binding.imgListEmpty.visibility = View.VISIBLE
            binding.textListEmpty.visibility = View.VISIBLE
        }else{
            binding.imgListEmpty.visibility = View.GONE
            binding.textListEmpty.visibility = View.GONE
        }
    }

    private fun authObserver() {
        lifecycleScope.launch(Dispatchers.Main) {
            authViewModel.getUser().collectLatest {
                user = it
                setAvatarIcon(user?.photoUrl, profileBinding)
            }
        }
    }

    private fun setAvatarIcon(photoUrl: Uri?, profileBinding: ProfileMenuLayoutBinding?) {
        if (profileBinding != null) {
            if (photoUrl != null) {
                Glide.with(requireContext())
                    .load(photoUrl)
                    .centerInside()
                    .placeholder(R.drawable.ic_account)
                    .into(profileBinding.avatarMenu)
            } else {
                profileBinding.avatarMenu.setImageResource(R.drawable.ic_account)
            }
        }

    }


    override fun onDestroyOptionsMenu() {
        super.onDestroyOptionsMenu()
        profileBinding = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}