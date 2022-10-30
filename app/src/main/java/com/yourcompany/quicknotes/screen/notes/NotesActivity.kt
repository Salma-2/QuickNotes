/*
 * Copyright (c) 2022 Razeware LLC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * Notwithstanding the foregoing, you may not use, copy, modify, merge, publish,
 * distribute, sublicense, create a derivative work, and/or sell copies of the
 * Software in any work that is designed, intended, or marketed for pedagogical or
 * instructional purposes related to programming, coding, application development,
 * or information technology.  Permission for such use, copying, modification,
 * merger, publication, distribution, sublicensing, creation of derivative works,
 * or sale is expressly withheld.
 *
 * This project and source code may use libraries or frameworks that are
 * released under various Open-Source licenses. Use of those libraries and
 * frameworks are governed by their own individual licenses.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.yourcompany.quicknotes.screen.notes

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.yourcompany.quicknotes.R
import com.yourcompany.quicknotes.databinding.ActivityNotesBinding
import com.yourcompany.quicknotes.domain.Note
import com.yourcompany.quicknotes.screen.newnote.NoteActivity
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class NotesActivity : AppCompatActivity() {

  companion object {

    fun createIntent(context: Context) = Intent(context, NotesActivity::class.java)
  }

  private val viewModel: NotesViewModel by viewModel()
  private lateinit var binding: ActivityNotesBinding
  private val notesAdapter: NotesAdapter by lazy {
    NotesAdapter(
        this::showNote,
        this::showNoteOptions
    )
  }

  private fun showNote(note: Note) {
    startActivity(NoteActivity.createIntent(this@NotesActivity, note.id))
  }

  private fun showNoteOptions(note: Note) {
    NoteBottomSheetDialogFragment.newInstance(note).show(
        supportFragmentManager,
        NoteBottomSheetDialogFragment.TAG
    )
  }

  private fun showDeleteDialog(note: Note) {
    AlertDialog.Builder(this).apply {
      setTitle(getString(R.string.dialog_delete_title))
      setMessage(getString(R.string.dialog_delete_message))
      setPositiveButton(
          getString(R.string.dialog_delete_positive_button)
      ) { _, _ -> viewModel.deleteNote(note) }
      setNegativeButton(getString(R.string.dialog_delete_negative_button)) { _, _ -> }
      setOnDismissListener { viewModel.doAction(null) }
    }.show()
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityNotesBinding.inflate(LayoutInflater.from(this))
    setContentView(binding.root)
    binding.initList()
    binding.initFab()
    fetchData()
    subscribeToBottomSheetActions()
  }

  private fun subscribeToBottomSheetActions() {
    MainScope().launch {
      viewModel.bottomSheetActions.collect { state ->
        when (state) {
          is BottomSheetAction.Delete -> showDeleteDialog(note = state.note)
          is BottomSheetAction.ManageShortcut -> {
            if (state.isShortcutCreated) {
              viewModel.removeNoteShortcut(note = state.note)
            } else {
              viewModel.createNoteShortcut(note = state.note)
            }
          }
          is BottomSheetAction.CreatePinnedShortcut ->
            viewModel.createPinnedShortcut(note = state.note)
          null -> {
            // Do nothing
          }
        }
        closeBottomSheetDialog()
      }
    }
  }

  private fun closeBottomSheetDialog() {
    supportFragmentManager.findFragmentByTag(NoteBottomSheetDialogFragment.TAG)?.let {
      supportFragmentManager.beginTransaction().remove(it).commit()
    }
  }

  private fun ActivityNotesBinding.initList() {
    notes.adapter = notesAdapter
    notes.layoutManager = LinearLayoutManager(baseContext)
  }

  private fun ActivityNotesBinding.initFab() {
    newNote.setOnClickListener {
      startActivity(NoteActivity.createIntent(this@NotesActivity, null))
    }
  }

  private fun fetchData() {
    MainScope().launch {
      viewModel.notes.collect { notesAdapter.submitList(it) }
    }
  }
}
