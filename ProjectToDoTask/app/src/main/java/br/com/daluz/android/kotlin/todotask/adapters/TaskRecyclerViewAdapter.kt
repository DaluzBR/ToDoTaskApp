package br.com.daluz.android.kotlin.todotask.adapters


import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import br.com.daluz.android.kotlin.todotask.R
import br.com.daluz.android.kotlin.todotask.databinding.AdapterMainTasksBinding
import br.com.daluz.android.kotlin.todotask.models.Task

class TaskRecyclerViewAdapter :
    ListAdapter<Task, TaskRecyclerViewAdapter.TaskViewHolder>(DiffCallback()) {

    var mListenerEdit: (Task) -> Unit = {}
    var mListenerDelete: (Task) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val findViewById = AdapterMainTasksBinding.inflate(inflater, parent, false)
        return TaskViewHolder(findViewById)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class TaskViewHolder(
        private val bindViewById: AdapterMainTasksBinding
    ) : RecyclerView.ViewHolder(bindViewById.root) {

        fun bind(task: Task) {
            bindViewById.txvTitle.text = task.title
            bindViewById.txvDate.text = "${task.date} ${task.time}"
            bindViewById.imvMore.setOnClickListener {
                openPopupMenu(task)
            }
        }

        private fun openPopupMenu(task: Task) {
            val imvMore = bindViewById.imvMore
            val popupMenu = PopupMenu(imvMore.context, imvMore)
            popupMenu.menuInflater.inflate(R.menu.menu_popup_more, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.action_task_edit -> mListenerEdit(task)
                    R.id.action_task_delete -> mListenerDelete(task)
                }
                return@setOnMenuItemClickListener true
            }
            popupMenu.show()
        }
    }

}

class DiffCallback : DiffUtil.ItemCallback<Task>() {
    override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
        return (oldItem.id == newItem.id)&&
                (oldItem.title == newItem.title)&&
                (oldItem.date == newItem.date)&&
                (oldItem.time == newItem.time)&&
                (oldItem.description == newItem.description)
    }

}
