package com.example.thrive.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.thrive.ui.BooksAdapter
import com.example.thrive.R
import com.example.thrive.databinding.MainFragmentBinding
import com.example.thrive.di.Injectable
import com.example.thrive.model.Book
import javax.inject.Inject

import com.example.thrive.api.Status.SUCCESS
import com.example.thrive.api.Status.LOADING
import com.example.thrive.api.Status.ERROR
import com.google.android.material.snackbar.Snackbar

class MainFragment: Fragment(), Injectable {

    @Inject
    lateinit var viewModel: MainViewModel

    private lateinit var adapter: BooksAdapter

    private lateinit var binding: MainFragmentBinding

    private lateinit var rootLayout: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.main_fragment,
            container,
            false
        )

        rootLayout = binding.root

        return rootLayout
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initView()
    }

    override fun onStart() {
        super.onStart()

        viewModel.getBooks()

        viewModel.booksResponse.observe(viewLifecycleOwner, Observer { response ->
            when (response.status) {
                SUCCESS -> {
                    response.data?.let { adapter.updateBookList(it) }
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

    private fun initView() {
        adapter = BooksAdapter(arrayListOf()) { book: Book -> goToBookDetail(book) }
        binding.recyclerView.adapter = adapter

        binding.floatingActionButton.setOnClickListener {
            goToCreateBook()
        }
    }

    private fun setProgressView() {
        binding.loadingProgressBarMainFrag.visibility = View.VISIBLE
    }

    private fun clearProgressView() {
        binding.loadingProgressBarMainFrag.visibility = View.GONE
    }

    private fun goToBookDetail(book: Book) {
        val bundle = bundleOf("bookId" to book.id)
        findNavController().navigate(R.id.action_mainFragment_to_detailFragment, bundle)
    }

    private fun goToCreateBook() {
        findNavController().navigate(R.id.action_mainFragment_to_createBookFragment)
    }
}