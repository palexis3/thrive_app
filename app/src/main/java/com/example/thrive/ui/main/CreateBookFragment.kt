package com.example.thrive.ui.main

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.thrive.R
import com.example.thrive.databinding.CreateBookFragmentBinding
import com.example.thrive.di.Injectable
import com.example.thrive.model.BookCreateRequest
import com.example.thrive.util.hideKeyboard
import javax.inject.Inject

/**
 * CreateBookFragment extends Textwatcher so that all the edit text that exist in the layout
 * can all emit there text changes and allow the submit button to easily be disabled/enabled based
 * on whether there's a non-empty string in each edit text.
 */
class CreateBookFragment: Fragment(), Injectable, TextWatcher {

    @Inject
    lateinit var viewModel: MainViewModel

    private lateinit var binding: CreateBookFragmentBinding

    private val editTextValidityMap = mutableMapOf<Int, Boolean>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.create_book_fragment,
            container,
            false
        )

        binding.author.addTextChangedListener(this)
        binding.categories.addTextChangedListener(this)
        binding.publisher.addTextChangedListener(this)
        binding.title.addTextChangedListener(this)

        editTextValidityMap[binding.author.id] = false
        editTextValidityMap[binding.categories.id] = false
        editTextValidityMap[binding.publisher.id] = false
        editTextValidityMap[binding.title.id] = false

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

       requireActivity().onBackPressedDispatcher?.addCallback(
            viewLifecycleOwner,
            object: OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().popBackStack(R.id.mainFragment, false)
                }
            })

        initView()
    }

    private fun initView() {
        binding.submit.isEnabled = false

        binding.submit.setOnClickListener {
            val title = binding.title.text.toString()
            val author = binding.author.text.toString()
            val categories = binding.categories.text.toString()
            val publisher = binding.publisher.text.toString()

            val bookCreateRequest = BookCreateRequest(title, author, categories, publisher)

            viewModel.createBook(bookCreateRequest)

            hideKeyboard()
            goToMain()
        }
    }

    private fun goToMain() {
        findNavController().navigate(R.id.action_createBookFragment_to_mainFragment)
    }

    /**
     * Using the hashcode of the all the editText that exist and update their
     * respective validity. If all the editText have non-empty strings, then
     * the submit button is enabled.
     */
    override fun afterTextChanged(editable: Editable?) {
        if (editable != null) {
            val isNotEmpty = editable.toString().isNotEmpty()
            when (editable.hashCode()) {
                binding.title.text.hashCode() -> {
                    editTextValidityMap[binding.title.id] = isNotEmpty
                }
                binding.author.text.hashCode() -> {
                    editTextValidityMap[binding.author.id] = isNotEmpty
                }
                binding.categories.text.hashCode() -> {
                    editTextValidityMap[binding.categories.id] = isNotEmpty
                }
                binding.publisher.text.hashCode() -> {
                    editTextValidityMap[binding.publisher.id] = isNotEmpty
                }
            }
            binding.submit.isEnabled = !editTextValidityMap.values.contains(false)
        }
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
}