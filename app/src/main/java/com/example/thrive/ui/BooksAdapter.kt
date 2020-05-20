package com.example.thrive.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.thrive.R
import com.example.thrive.model.Book
import kotlinx.android.synthetic.main.books_adapter_item_layout.view.*

class BooksAdapter(private val booksList: ArrayList<Book>, private val clickListener: (Book) -> Unit):
    ListAdapter<Book, BooksAdapter.BooksViewHolder>(BookDiffUtilCallBack()) {

    class BooksViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(book: Book, clickListener: (Book) -> Unit) {
            itemView.apply {
                bookAuthor.text = book.author
                bookTitle.text = book.title
                setOnClickListener { clickListener(book) }
            }
        }
    }

    override fun onBindViewHolder(holder: BooksViewHolder, position: Int) {
        holder.bind(booksList[position], clickListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BooksViewHolder =
        BooksViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.books_adapter_item_layout,
                parent,
                false
            )
        )

    override fun getItemCount(): Int = booksList.size

    fun updateBookList(booksList: List<Book>) {
        this.booksList.apply {
            clear()
            addAll(booksList)
        }
        notifyDataSetChanged()
    }
}

class BookDiffUtilCallBack : DiffUtil.ItemCallback<Book>() {
    override fun areContentsTheSame(oldItem: Book, newItem: Book): Boolean {
       return oldItem.author == newItem.author
               && oldItem.title == newItem.title
    }

    override fun areItemsTheSame(oldItem: Book, newItem: Book): Boolean {
        return oldItem == newItem
    }
}