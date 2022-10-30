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

package com.yourcompany.quicknotes.screen.newnote

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.yourcompany.quicknotes.R
import com.yourcompany.quicknotes.databinding.ActivityNoteBinding
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

private const val KEY_NOTE_ID = "note_id"

class NoteActivity : AppCompatActivity() {

  private val viewModel: NoteViewModel by viewModel()
  private lateinit var binding: ActivityNoteBinding

  companion object {

    fun createIntent(context: Context, noteId: String?) = Intent(context,
        NoteActivity::class.java).apply {
      putExtra(KEY_NOTE_ID, noteId)
    }
  }

  private val noteId: String? by lazy { intent.getStringExtra(KEY_NOTE_ID) }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityNoteBinding.inflate(LayoutInflater.from(this))
    setContentView(binding.root)
    binding.setupToolbar()
    binding.setupInput()
    setupSavingResultAction()
    binding.getNote()
  }

  private fun ActivityNoteBinding.getNote() {
    noteId?.let { id ->
      val note = viewModel.getNote(id)
      titleInput.setText(note.title)
      contentInput.setText(note.content)
    }
  }

  private fun setupSavingResultAction() {
    MainScope().launch {
      viewModel.noteSavingResult.collect {
        if (it) {
          finish()
        } else {
          Toast.makeText(this@NoteActivity, "Something went wrong", Toast.LENGTH_SHORT).show()
        }
      }
    }
  }

  private fun ActivityNoteBinding.setupToolbar() {
    setSupportActionBar(toolbar)
    supportActionBar?.apply {
      setDisplayHomeAsUpEnabled(true)
      setDisplayShowHomeEnabled(true)
      title = ""
      toolbar.setNavigationIcon(R.drawable.ic_back)
      toolbar.setNavigationOnClickListener { finish() }
    }

    submitNote.text = getString(
        if (noteId == null) R.string.save_button_text else R.string.update_button_text
    )

    submitNote.setOnClickListener {
      noteId?.let {
        viewModel.updateNote(
            noteId = it,
            title = titleInput.text.toString().trim(),
            content = contentInput.text.toString().trim()
        )
      } ?: viewModel.createNote(
          title = titleInput.text.toString().trim(),
          content = contentInput.text.toString().trim()
      )
    }
  }

  private fun ActivityNoteBinding.setupInput() {
    titleInput.addTextChangedListener(object : TextWatcher {

      override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        // No implementation
      }

      override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        // No implementation
      }

      override fun afterTextChanged(text: Editable) {
        submitNote.isEnabled = text.isNotEmpty() && contentInput.text?.length != 0
      }
    })
    contentInput.addTextChangedListener(object : TextWatcher {

      override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        // No implementation
      }

      override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        // No implementation
      }

      override fun afterTextChanged(text: Editable) {
        submitNote.isEnabled = text.isNotEmpty() && titleInput.text?.length != 0
      }
    })
  }
}
