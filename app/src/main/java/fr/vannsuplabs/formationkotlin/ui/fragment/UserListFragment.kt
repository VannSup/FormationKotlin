package fr.vannsuplabs.formationkotlin.ui.fragment

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import fr.vannsuplabs.formationkotlin.R
import fr.vannsuplabs.formationkotlin.data.model.User
import fr.vannsuplabs.formationkotlin.ui.activity.MainActivity
import fr.vannsuplabs.formationkotlin.ui.adapter.UserAdapter
import fr.vannsuplabs.formationkotlin.ui.holder.OnUserClickListener
import fr.vannsuplabs.formationkotlin.ui.viewModel.UserViewModel
import kotlinx.android.synthetic.main.fragment_user_list.view.*

class UserListFragment : Fragment(), OnUserClickListener {

    private lateinit var userViewModel: UserViewModel
    private lateinit var userAdapter: UserAdapter
    private lateinit var searchView: SearchView
    private var currentSearch = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.run {
            userViewModel = ViewModelProvider(this, UserViewModel).get()
        } ?: throw IllegalStateException("Invalid Activity")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_user_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        (activity as? MainActivity)?.supportActionBar?.apply {
            this.title = "Profils"
        }
        // We need to inject the OnUserClickListener in the constructor of the adapter
        userAdapter = UserAdapter(this)
        view.user_list_recycler_view.apply {
            adapter = userAdapter
        }
        userViewModel.usersPagedList.observe(viewLifecycleOwner) {
            userAdapter.submitList(it)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search, menu)
        val manager = activity?.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchItem = menu.findItem(R.id.search_item)
        val searchView = searchItem.actionView as SearchView
        this.searchView = searchView

        val searchIcon = searchView.findViewById(androidx.appcompat.R.id.search_button) as ImageView
        searchIcon.setImageDrawable(ContextCompat.getDrawable(requireActivity(), R.drawable.ic_search))

        val searchAutoComplete = searchView.findViewById(androidx.appcompat.R.id.search_src_text) as SearchView.SearchAutoComplete
        searchAutoComplete.setHintTextColor(ContextCompat.getColor(requireActivity(),R.color.color_on_surface))
        searchAutoComplete.setTextColor(ContextCompat.getColor(requireActivity(),R.color.color_on_surface))

        searchView.queryHint = "enter your search here"

        searchView.setSearchableInfo(manager.getSearchableInfo(activity?.componentName))

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    currentSearch = query
                    searchView.clearFocus()

                    userAdapter = UserAdapter(this@UserListFragment)
                    requireView().user_list_recycler_view.apply {
                        adapter = userAdapter
                    }
                    userViewModel.getUserSearch(query).observe(this@UserListFragment) {
                        userAdapter.submitList(it)
                    }

                    return true
                }
                return false
            }

            override fun onQueryTextChange(newSearch: String?): Boolean {
                return if (newSearch != null) {
                    currentSearch = newSearch
                    true
                } else
                    false
            }
        })

        searchView.setOnCloseListener {
            currentSearch = ""
            return@setOnCloseListener false
        }
    }

    // Implementation of OnUserClickListener
    override fun invoke(view: View, user: User) {
        findNavController()
            .navigate(
                R.id.userDetailFragment,
                bundleOf(UserDetailFragment.ARG_USER_ID_KEY to user.login),
                navOptions {
                    anim {

                    }
                }
        )
    }
}