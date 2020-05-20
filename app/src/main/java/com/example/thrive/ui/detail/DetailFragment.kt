package com.example.thrive.ui.detail

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.thrive.R
import com.example.thrive.databinding.DetailFragmentBinding
import com.example.thrive.di.Injectable
import com.example.thrive.model.Book
import com.example.thrive.model.BookUpdateRequest
import javax.inject.Inject

import com.example.thrive.api.Status.SUCCESS
import com.example.thrive.api.Status.LOADING
import com.example.thrive.api.Status.ERROR
import com.google.android.material.snackbar.Snackbar

class DetailFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModel: DetailViewModel

    private lateinit var binding: DetailFragmentBinding

    private lateinit var rootLayout: View

    private lateinit var bookId: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        activity?.onBackPressedDispatcher?.addCallback(
            viewLifecycleOwner,
            object: OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().navigate(R.id.action_detailFragment_to_mainFragment)
                }
            })

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.detail_fragment,
            container,
            false
        )

        bookId = arguments?.getString("bookId").toString()

        rootLayout = binding.root

        return rootLayout
    }

    override fun onStart() {
        super.onStart()

        viewModel.getBook(bookId)

        viewModel.bookResponse.observe(viewLifecycleOwner, Observer { response ->
            when (response.status) {
                SUCCESS -> {
                    response.data?.let { applyBook(it) }
                    clearProgressView()
                }
                LOADING -> {
                    setProgressView()
                }
                ERROR -> {
                    clearProgressView()
                    Snackbar.make(rootLayout, R.string.snackbar_api_error, Snackbar.LENGTH_SHORT)
                }
            }
        })
    }

    private fun applyBook(book: Book) {
        binding.bookTitle.text = book.title
        binding.bookAuthor.text = "Written by: ${book.author}"
        binding.bookPublisher.text = "Publisher: ${book.publisher}"
        binding.bookCategories.text = "Categories: ${book.categories}"
        binding.bookLastCheckout.text = "${book.lastCheckedOutBy} @ ${book.lastCheckedOut}"
        binding.checkoutButton.setOnClickListener {
            enterLastCheckout()
        }
    }

    private fun enterLastCheckout() {
        val builder = activity?.let { AlertDialog.Builder(it) }
        val input = EditText(activity)

        builder?.setView(input)
        builder?.setTitle(R.string.enter_name_label)
        builder?.setPositiveButton(R.string.submit_button_label) { _, _ ->
            // It's okay to pass in input string to our view model here, since positive button
            // is only enabled when someone has entered some text for their name
            val bookRequest = BookUpdateRequest(input.text.toString())
            viewModel.updateBook(bookId, bookRequest)
        }
        builder?.setNegativeButton(R.string.cancel_button_label) {dialog, _ ->
            dialog.cancel()
        }

        val dialog = builder?.create()!!
        dialog.show()

        dialog?.getButton(AlertDialog.BUTTON_POSITIVE)?.isEnabled = false

        input.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun afterTextChanged(s: Editable?) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val str = s.toString()
                dialog?.getButton(AlertDialog.BUTTON_POSITIVE)?.isEnabled =
                    str.isNotEmpty()
            }
        })
    }

    private fun setProgressView() {
        binding.loadingProgressBarDetailFrag.visibility = View.VISIBLE
    }

    private fun clearProgressView() {
        binding.loadingProgressBarDetailFrag.visibility = View.GONE
    }
}
