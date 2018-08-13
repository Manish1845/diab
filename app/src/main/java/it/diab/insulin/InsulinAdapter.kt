package it.diab.insulin

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import it.diab.R
import it.diab.db.entities.Insulin
import it.diab.insulin.editor.EditorActivity
import it.diab.ui.recyclerview.ViewHolderExt

class InsulinAdapter(private val mContext: Context) :
        PagedListAdapter<Insulin, InsulinAdapter.InsulinHolder>(CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InsulinHolder {
        return InsulinHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.item_insulin, parent, false))
    }

    override fun onBindViewHolder(holder: InsulinHolder, position: Int) {
        if (position == itemCount - 1) {
            holder.onBind(mContext, null)
        } else {
            val item = getItem(position)
            if (item == null) {
                holder.clear()
            } else {
                holder.onBind(mContext, item)
            }
        }
    }

    override fun getItemCount() = super.getItemCount() + 1

    class InsulinHolder(view: View): ViewHolderExt(view) {
        private val mTitle: TextView = view.findViewById(R.id.item_insulin_name)
        private val mIcon: ImageView = view.findViewById(R.id.item_insulin_icon)

        fun onBind(context: Context, insulin: Insulin?) {
            if (insulin == null) {
                bindAddView(context)
            } else {
                bindItemView(context, insulin)
            }
        }

        fun clear() {
            itemView.visibility = View.GONE
        }

        private fun bindAddView(context: Context) {
            mTitle.text = context.getString(R.string.insulin_item_add)
            mIcon.setImageResource(R.drawable.ic_add)
            itemView.setOnClickListener { _ ->
                context.startActivity(Intent(context, EditorActivity::class.java)) }
        }

        private fun bindItemView(context: Context, insulin: Insulin) {
            id = insulin.uid

            mTitle.text = insulin.name
            mIcon.setImageResource(insulin.timeFrame.icon)

            itemView.setOnClickListener { _ ->
                val intent = Intent(context, EditorActivity::class.java)
                intent.putExtra(EditorActivity.EXTRA_UID, insulin.uid)
                context.startActivity(intent)
            }
        }
    }

    companion object {
        private val CALLBACK = object : DiffUtil.ItemCallback<Insulin>() {
            override fun areContentsTheSame(oldItem: Insulin, newItem: Insulin) =
                    oldItem == newItem

            override fun areItemsTheSame(oldItem: Insulin, newItem: Insulin) =
                    oldItem.uid == newItem.uid
        }
    }
}
