package br.com.daluz.android.kotlin.todotask.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import br.com.daluz.android.kotlin.todotask.adapters.TaskRecyclerViewAdapter
import br.com.daluz.android.kotlin.todotask.dao.TaskDAO
import br.com.daluz.android.kotlin.todotask.databinding.ActivityMainBinding
import br.com.daluz.android.kotlin.todotask.dialogs.DeleteTaskDialog
import br.com.daluz.android.kotlin.todotask.models.Task

class MainActivity : AppCompatActivity(), DeleteTaskDialog.OnDeleteTaskConfirmListener {

    companion object {
        const val TAG_TASK_CREATE = "tag_task_create"
        const val TAG_TASK_EDIT = "tag_task_edit"
        const val TAG_TASK_DELETE = "tag_task_delete"
        const val TASK_RESULT_CREATE = 0
        const val TASK_RESULT_EDIT = 1
        const val TASK_RESULT_DELETE = 2
    }

    private lateinit var mBindViewById: ActivityMainBinding
    private val mTaskRecyclerViewAdapter by lazy { TaskRecyclerViewAdapter() }
    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if ((result.resultCode == TASK_RESULT_CREATE) ||
                (result.resultCode == TASK_RESULT_EDIT)
            ) {
                // Atualiza a lista.
                updateTaskList()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBindViewById = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBindViewById.root)

        val toolbar = mBindViewById.toolbar
        setSupportActionBar(toolbar)

        mBindViewById.rcvTasks.adapter = mTaskRecyclerViewAdapter
        updateTaskList()
        insertListeners()
    }

    private fun updateTaskList() {

        val list = TaskDAO.getList()
        if (list.isEmpty()) {
            mBindViewById.inlMainListEmpty.ctlMainListEmpty.visibility = View.VISIBLE
            mBindViewById.rcvTasks.visibility = View.GONE
        } else {
            mBindViewById.inlMainListEmpty.ctlMainListEmpty.visibility = View.GONE
            mBindViewById.rcvTasks.visibility = View.VISIBLE
        }
        mTaskRecyclerViewAdapter.submitList(list)
    }

    private fun insertListeners() {
        mBindViewById.fabAdd.setOnClickListener {
            /* Deprecated
             * startActivityForResult(Intent(this@MainActivity, TaskAddActivity::class.java),CREATE_NEW_TASK)
             */

            resultLauncher.launch(Intent(this@MainActivity, TaskAddActivity::class.java))
        }

        mTaskRecyclerViewAdapter.mListenerEdit = {
            val intent = Intent(this@MainActivity, TaskAddActivity::class.java)
            intent.putExtra(TAG_TASK_EDIT, it.id)
            resultLauncher.launch(intent)
        }

        mTaskRecyclerViewAdapter.mListenerDelete = {
            val dtd: DeleteTaskDialog = DeleteTaskDialog.newInstance(it)
            dtd.show(supportFragmentManager, DeleteTaskDialog.TAG)
        }
    }

    override fun onDeleteTaskConfirmListener(task: Task) {
        TaskDAO.deleteItem(task)
        updateTaskList()
    }
}
// TODO: Adicionar alarme para avisar sobre a ocorrência da tarefa.
// TODO: Adicionar persistência dos dados (SQLITE).
// TODO: Adicionar menu de configurações.