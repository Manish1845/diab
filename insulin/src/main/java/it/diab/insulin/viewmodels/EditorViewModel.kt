/*
 * Copyright (c) 2019 Bevilacqua Joey
 *
 * Licensed under the GNU GPLv3 license
 *
 * The text of the license can be found in the LICENSE file
 * or at https://www.gnu.org/licenses/gpl.txt
 */
package it.diab.insulin.viewmodels

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import it.diab.data.entities.Insulin
import it.diab.data.extensions.toTimeFrame
import it.diab.data.repositories.InsulinRepository
import it.diab.insulin.components.status.EditableOutStatus
import kotlinx.coroutines.launch

class EditorViewModel internal constructor(
    private val repo: InsulinRepository
) : ViewModel() {

    private lateinit var insulin: Insulin

    fun setInsulin(uid: Long, block: (Insulin) -> Unit) {
        viewModelScope.launch {
            runSetInsulin(uid)
            block(insulin)
        }
    }

    fun delete() {
        viewModelScope.launch {
            runDelete()
        }
    }

    fun save(status: EditableOutStatus) {
        viewModelScope.launch {
            runSave(status)
        }
    }

    @VisibleForTesting
    suspend fun runSetInsulin(uid: Long) {
        insulin = repo.getById(uid)
    }

    @VisibleForTesting
    suspend fun runDelete() {
        repo.delete(insulin)
    }

    @VisibleForTesting
    suspend fun runSave(status: EditableOutStatus) {
        insulin.apply {
            name = status.name
            timeFrame = (status.timeFrameIndex - 1).toTimeFrame()
            hasHalfUnits = status.hasHalfUnits
            isBasal = status.isBasal
        }

        repo.insert(insulin)
    }
}