package fr.vannsuplabs.formationkotlin.ui.adapter

import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import fr.vannsuplabs.formationkotlin.data.model.User
import fr.vannsuplabs.formationkotlin.ui.holder.OnUserClickListener
import fr.vannsuplabs.formationkotlin.ui.holder.UserViewHolder

class UserAdapter (
    private val onUserClickListener: OnUserClickListener
) : PagedListAdapter<User, UserViewHolder>(Companion) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        getItem(position)?.run { holder.bind(this, onUserClickListener) }
    }

    companion object : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem == newItem
        }
    }
}