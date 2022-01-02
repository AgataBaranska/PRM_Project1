package com.example.financialmanager


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class RecyclerAdapter(
    private val transactionList: List<Transaction>,
    private val listener: OnItemClickListener,
    private val listenerLong: OnItemLongClickListener
) :
    RecyclerView.Adapter<RecyclerAdapter.RecyclerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.transaction, parent, false)
        return RecyclerViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        val currentItem = transactionList[position]
        holder.category.text = currentItem.category
        holder.place.text = currentItem.place
        holder.amount.text = "${currentItem.amount.toString()} ${Constants.CURRENCY}"
        holder.date.text = currentItem.date
        holder.img.setImageResource(currentItem.image)
    }

    override fun getItemCount(): Int {
        return transactionList.size
    }

    inner class RecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener, View.OnLongClickListener {
        //cashed view references
        val category: TextView = itemView.findViewById(R.id.tvTransactionCategory)
        val place: TextView = itemView.findViewById(R.id.tvTransactionPlace)
        val date: TextView = itemView.findViewById((R.id.tvDate))
        val amount: TextView = itemView.findViewById((R.id.tvAmount))
        val img: ImageView = itemView.findViewById(R.id.ivTransaction)

        init {
            itemView.setOnClickListener(this)
            itemView.setOnLongClickListener(this)
        }

        override fun onClick(v: View?) {
            val position: Int = bindingAdapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position)
            }
        }

        override fun onLongClick(v: View?): Boolean {
            val position: Int = bindingAdapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listenerLong.onItemLongClicked(position)
            }
            return true
        }
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    interface OnItemLongClickListener {
        fun onItemLongClicked(position: Int): Boolean
    }
}

