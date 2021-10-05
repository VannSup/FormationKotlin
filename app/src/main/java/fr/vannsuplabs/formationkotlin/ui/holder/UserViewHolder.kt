package fr.vannsuplabs.formationkotlin.ui.holder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import fr.vannsuplabs.formationkotlin.R
import fr.vannsuplabs.formationkotlin.data.model.User
import kotlinx.android.synthetic.main.holder_user.view.*

/**
 * SAM (Single Abstract Method) to listen a click.
 *
 * This callback contains the view clicked, and the user attached to the view
 */
typealias OnUserClickListener = (view: View, user: User) -> Unit

class UserViewHolder private constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(model: User, onClick: OnUserClickListener) {
        itemView.apply {
            this.setOnClickListener { onClick(it, model) }
            this.holder_user_name.text = model.login
            this.holder_user_type.text = model.type
            this.holder_user_id.text = context.getString(R.string.user_id_holder_text, model.id)
            Glide.with(this)
                .load(model.avatar_url)
                .circleCrop()
                .into(this.holder_user_avatar)
        }
    }

    companion object {
        /**
         * Create a new Instance of [UserViewHolder]
         */
        fun create(parent: ViewGroup): UserViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(
                R.layout.holder_user,
                parent,
                false
            )
            return UserViewHolder(view)
        }
    }
}