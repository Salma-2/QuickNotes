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

package com.yourcompany.quicknotes

import android.content.Context
import androidx.core.content.pm.ShortcutInfoCompat
import com.yourcompany.quicknotes.domain.Note

interface ShortcutManagerWrapper {

  fun addNoteShortcut(note: Note)
  fun removeNoteShortcut(noteId: String)
  fun updateShortcut(note: Note)
  fun createPinnedShortcut(note: Note)
  fun isShortcutCreated(noteId: String): Boolean
}

class ShortcutManagerWrapperImpl(
    private val context: Context
) : ShortcutManagerWrapper {

  override fun addNoteShortcut(note: Note) {
    // TODO 4: Push Dynamic Shortcut
  }

  override fun removeNoteShortcut(noteId: String) {
    // TODO 7: Remove Dynamic Shortcut"
  }

  override fun updateShortcut(note: Note) {
    // TODO 5: Update Dynamic Shortcut
  }

  override fun createPinnedShortcut(note: Note) {
    // TODO 8: Create Pinned Shortcut
  }

  // TODO 6: Check if Dynamic Shortcut exists
  override fun isShortcutCreated(noteId: String): Boolean {
    return false
  }

  private fun createShortcutInfo(note: Note): ShortcutInfoCompat {
    TODO("3: Create shortcut that leads a user to the specific Note")
  }
}
