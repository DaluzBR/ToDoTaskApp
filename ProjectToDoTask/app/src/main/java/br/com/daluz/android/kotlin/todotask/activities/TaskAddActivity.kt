package br.com.daluz.android.kotlin.todotask.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import br.com.daluz.android.kotlin.todotask.R
import br.com.daluz.android.kotlin.todotask.dao.TaskDAO
import br.com.daluz.android.kotlin.todotask.databinding.ActivityTaskAddBinding
import br.com.daluz.android.kotlin.todotask.extensions.format
import br.com.daluz.android.kotlin.todotask.extensions.formatTime
import br.com.daluz.android.kotlin.todotask.extensions.text
import br.com.daluz.android.kotlin.todotask.models.Task
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.util.*

class TaskAddActivity : AppCompatActivity() {

    private lateinit var mBindViewById: ActivityTaskAddBinding
    private var mAction = MainActivity.TASK_RESULT_CREATE
    private var mDate = 0L
    private var mHour = 0
    private var mMinute = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_task_add)
        mBindViewById = ActivityTaskAddBinding.inflate(layoutInflater)
        setContentView(mBindViewById.root)

        val toolbar = mBindViewById.toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        if (intent.hasExtra(MainActivity.TAG_TASK_EDIT)) {
            mBindViewById.btnSave.text = resources.getString(R.string.task_add_edit)

            val taskId = intent.getIntExtra(MainActivity.TAG_TASK_EDIT, 0)
            if (taskId != 0) {
                TaskDAO.getItemById(taskId)?.let {
                    mBindViewById.tilTitle.text = it.title
                    mBindViewById.tilDate.text = it.date
                    mBindViewById.tilTime.text = it.time
                    mBindViewById.tilDescription.text = it.description
                    mAction = MainActivity.TASK_RESULT_EDIT
                }
            }
        }

        insertListener()
    }

    private fun insertListener() {
        mBindViewById.tilDate.editText?.setOnClickListener { getData() }

        mBindViewById.tilTime.editText?.setOnClickListener { getTime() }

        mBindViewById.btnSave.setOnClickListener { saveNewTask() }

        mBindViewById.btnCancel.setOnClickListener { onBackPressed() }
    }

    private fun getData() {
        val datePicker = MaterialDatePicker.Builder.datePicker().build()
        datePicker.addOnPositiveButtonClickListener {
            val timeZone = TimeZone.getDefault()
            val offSet = -1 * timeZone.getOffset(Date().time)
            mDate = it + offSet
            mBindViewById.tilDate.text = Date(mDate).format()
        }
        datePicker.show(supportFragmentManager, null)
    }

    private fun getTime() {
        val timePicker = MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_24H)
            .build()

        timePicker.addOnPositiveButtonClickListener {
            mHour = timePicker.hour
            mMinute = timePicker.minute
            mBindViewById.tilTime.text =
                "${mHour.formatTime()}:${mMinute.formatTime()}"
        }
        timePicker.show(supportFragmentManager, null)
    }

    private fun saveNewTask() {
        val task = Task(
            id = intent.getIntExtra(MainActivity.TAG_TASK_EDIT, 0),
            title = mBindViewById.tilTitle.text,
            date = mBindViewById.tilDate.text,
            time = mBindViewById.tilTime.text,
            description = mBindViewById.tilDescription.text
        )

        if (checkInputFieldRequired(task)) {
            if (mAction == MainActivity.TASK_RESULT_EDIT) {
                TaskDAO.update(task)
            } else {
                TaskDAO.insert(task)
            }

            setResult(mAction)
            finish()
        } else {
            Toast.makeText(
                this,
                resources.getText(R.string.task_add_field_non_filled_message),
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun checkInputFieldRequired(task: Task): Boolean {
        return (task.title.isNotEmpty() && task.title.isNotBlank()) &&
                (task.date.isNotEmpty() && task.date.isNotBlank()) &&
                (task.time.isNotEmpty() && task.time.isNotBlank())
    }

    // TODO: Evitar data anterior a data atual.
}