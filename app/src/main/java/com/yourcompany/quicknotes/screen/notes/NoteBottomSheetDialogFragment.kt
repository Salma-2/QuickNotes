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

import android.graphics.drawable.Icon
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.yourcompany.quicknotes.R
import com.yourcompany.quicknotes.ShortcutManagerWrapper
import com.yourcompany.quicknotes.databinding.BottomSheetDialogFragmentNoteBinding
import com.yourcompany.quicknotes.domain.Note
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.lang.NullPointerException

private const val KEY_NOTE = "note"

class NoteBottomSheetDialogFragment : BottomSheetDialogFragment() {

  companion object {

    val TAG = "NoteBottomSheetDialogFragment_TAG"

    fun newInstance(note: Note) = NoteBottomSheetDialogFragment().apply {
      arguments = Bundle().apply {
        putParcelable(KEY_NOTE, note)
      }
    }
  }

  lateinit var binding: BottomSheetDialogFragmentNoteBinding
  private val viewModel: NotesViewModel by sharedViewModel()
  private val shortcutManagerWrapper: ShortcutManagerWrapper by inject()

  private val note: Note by lazy {
    requireArguments().getParcelable(KEY_NOTE) as Note? ?: throw NullPointerException(
        "Note needs to be passed")
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View {
    binding = BottomSheetDialogFragmentNoteBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    val isShortcutCreated = shortcutManagerWrapper.isShortcutCreated(noteId = note.id)
    binding.setupManageShortcut(isShortcutCreated)
    binding.setupActions(isShortcutCreated)
  }

  private fun BottomSheetDialogFragmentNoteBinding.setupManageShortcut(isShortcutCreated: Boolean) {
    val iconRes = if (isShortcutCreated) R.drawable.ic_remove_shortcut else R.drawable.ic_add_shortcut
    manageShortcutIcon.setImageIcon(Icon.createWithResource(requireContext(), iconRes))

    val labelRes = if (isShortcutCreated) R.string.remove_shortcut else R.string.create_shortcut
    manageShortcutLabel.setText(labelRes)

    createPinnedShortcut.visibility = if (isShortcutCreated) View.VISIBLE else View.GONE
  }

  private fun BottomSheetDialogFragmentNoteBinding.setupActions(isShortcutCreated: Boolean) {
    delete.setOnClickListener { viewModel.doAction(BottomSheetAction.Delete(note = note)) }
    manageShortcut.setOnClickListener {
      viewModel.doAction(
          BottomSheetAction.ManageShortcut(note = note, isShortcutCreated = isShortcutCreated)
      )
    }
    createPinnedShortcut.setOnClickListener {
      viewModel.doAction(BottomSheetAction.CreatePinnedShortcut(note = note))
    }
  }
}

sealed class BottomSheetAction {

  data class ManageShortcut(val note: Note, val isShortcutCreated: Boolean) : BottomSheetAction()
  data class CreatePinnedShortcut(val note: Note) : BottomSheetAction()
  data class Delete(val note: Note) : BottomSheetAction()
}
