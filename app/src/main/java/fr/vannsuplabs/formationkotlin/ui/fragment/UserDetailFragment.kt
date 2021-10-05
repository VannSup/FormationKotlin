package fr.vannsuplabs.formationkotlin.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import com.bumptech.glide.Glide
import fr.vannsuplabs.formationkotlin.R
import fr.vannsuplabs.formationkotlin.ui.activity.MainActivity
import fr.vannsuplabs.formationkotlin.ui.viewModel.UserViewModel
import fr.vannsuplabs.formationkotlin.utils.DateConverter
import kotlinx.android.synthetic.main.fragment_user_detail.view.*

class UserDetailFragment : Fragment() {

    private lateinit var userViewModel: UserViewModel
    private var userLogin: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.run {
            userViewModel = ViewModelProvider(this, UserViewModel).get()
        } ?: throw IllegalStateException("Invalid Activity")
        userLogin =
            arguments?.getString(ARG_USER_ID_KEY)
                ?: throw java.lang.IllegalStateException("No ID found")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_user_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userLogin?.let {
            userViewModel.getUserByLogin(it) {
                (activity as? MainActivity)?.supportActionBar?.apply {
                    this.title = it.name
                }
                view.apply {
                    this.user_details_login.text = it.login
                    this.user_details_id.text = context.getString(R.string.user_id_holder_text, it.id)
                    this.user_details_created_date.text = DateConverter.convertDateToString(it.created_at)
                    this.user_details_public_repos.text =
                        this.context.resources.getQuantityString(
                            R.plurals.public_repos_number,
                            it.public_repos?:0,
                            it.public_repos
                        )
                    this.user_details_followers.text =
                        this.context.resources.getQuantityString(
                            R.plurals.followers_number,
                            it.followers?:0,
                            it.followers
                        )
                    this.user_details_html_url.text = it.url
                    this.user_details_location.text = it.location?:"Unknown"
                    this.user_details_name.text = it.name?:"Unknown"
                    Glide.with(this)
                        .load(it.avatar_url)
                        .circleCrop()
                        .into(this.user_details_image_view)
                }
            }
        }
    }

    companion object {
        const val ARG_USER_ID_KEY = "arg_user_id_key"
    }
}