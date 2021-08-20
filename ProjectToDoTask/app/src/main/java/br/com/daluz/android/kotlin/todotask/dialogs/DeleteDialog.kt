package br.com.daluz.android.kotlin.todotask.dialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import br.com.daluz.android.kotlin.todotask.R
import br.com.daluz.android.kotlin.todotask.models.Task

private const val ARG_PARAM_TASK_DELETE = "arg_param_task_delete"

class DeleteTaskDialog : DialogFragment() {

    private lateinit var mListener: OnDeleteTaskConfirmListener
    private var paramTask: Task? = null

    companion object {
        const val TAG = "DeleteDialog"

        @JvmStatic
        fun newInstance(task: Task) =
            DeleteTaskDialog().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_PARAM_TASK_DELETE, task)
                }
            }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnDeleteTaskConfirmListener) {
            mListener = context
        } else {
            throw RuntimeException("$context must implement OnDeleteTaskConfirmListener")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            paramTask = it.getParcelable(ARG_PARAM_TASK_DELETE)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.task_menu_task_delete_title))
            .setMessage(getString(R.string.task_menu_task_delete_message))
            .setPositiveButton(getString(R.string.command_ok)) { dialog, id ->
                if(paramTask != null){
                    mListener.onDeleteTaskConfirmListener(paramTask!!)
                }
                dialog.dismiss()
            }
            .setNegativeButton(getString(R.string.command_cancel)) { dialog, id ->
                dialog.cancel()
            }
            .create()

    interface OnDeleteTaskConfirmListener {
        fun onDeleteTaskConfirmListener(task: Task)
    }
}