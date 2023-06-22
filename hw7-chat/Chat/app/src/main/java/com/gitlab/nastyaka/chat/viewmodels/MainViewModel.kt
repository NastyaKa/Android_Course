package com.gitlab.nastyaka.chat.viewmodels

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import com.gitlab.nastyaka.chat.databinding.FragmentMainBinding
import com.gitlab.nastyaka.chat.repos.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject internal constructor(
    private val mainRepository: MainRepository
) : ViewModel() {
    fun msgs(name: String) = mainRepository.getMsgs(name)

    fun sendTxt(fragment: Fragment, binding: FragmentMainBinding, name: String) {
        closeKeyBoard(fragment)
        val textView = binding.textik
        val text = textView.text.toString()
        textView.text.clear()
        mainRepository.sendText(text, name)
    }

    fun sendPhoto(fragment: Fragment, selectedImageUri: Uri, name: String) {
        val selectedImageName: String
        fragment.requireContext().contentResolver.query(
            selectedImageUri, arrayOf(
                OpenableColumns.DISPLAY_NAME, OpenableColumns.SIZE
            ), null, null, null
        ).use { cursor ->
            cursor ?: return
            val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            cursor.moveToFirst()
            selectedImageName = cursor.getString(nameIndex)
        }

        val output = File(fragment.requireContext().filesDir, selectedImageName)
        try {
            val inputStream =
                fragment.requireContext().contentResolver.openInputStream(selectedImageUri)
                    ?: return
            val outputStream = FileOutputStream(output)
            var read: Int
            val bufferSize = 4096
            val buffers = ByteArray(bufferSize)
            while (inputStream.read(buffers).also { read = it } != -1) {
                outputStream.write(buffers, 0, read)
            }
            inputStream.close()
            outputStream.close()
        } catch (e: java.lang.Exception) {
            return
        }
        mainRepository.sendPhoto(output.path, name)
    }

    private fun closeKeyBoard(fragment: Fragment) {
        val view = fragment.requireActivity().currentFocus
        if (view != null) {
            val imm =
                fragment.requireActivity()
                    .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}