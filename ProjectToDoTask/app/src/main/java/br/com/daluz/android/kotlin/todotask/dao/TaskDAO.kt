package br.com.daluz.android.kotlin.todotask.dao

import br.com.daluz.android.kotlin.todotask.models.Task

// TODO: DATABASE IN SQLITE (CRUD)

object TaskDAO {

    private val list = ArrayList<Task>()

    fun getList() = list.toList()

    fun getItemById(id: Int): Task? = list.find { it.id == id }

    fun deleteItem(task: Task) {
        list.remove(task)
    }

//    fun deleteItemById(id: Int): Boolean {
//        return if(getItemById(id) == null) {
//            false
//        } else{
//            list.removeAt(id)
//            true
//        }
//    }

    fun insert(task: Task) {
        list.add(task.copy(id = list.size + 1))
    }

    fun update(task: Task) {
        val index = list.indexOf(task)
         list[index] = task
    }

  //  fun clearAll() = list.clear()
}