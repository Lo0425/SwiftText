package com.example.swifttext.ui.presentation.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.swifttext.R
import com.example.swifttext.data.model.Rules
import com.example.swifttext.data.model.User
import com.example.swifttext.databinding.ItemLayoutBinding
import com.example.swifttext.utils.Utils.update

class RulesAdapter(private var items: List<Rules>, val onClick: (item: Rules) -> Unit) :
    RecyclerView.Adapter<RulesAdapter.ItemRulesHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemRulesHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemLayoutBinding.inflate(layoutInflater, parent, false)
        return ItemRulesHolder(binding)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ItemRulesHolder, position: Int) {
        val item = items[position]
        holder.binding.run {
            tvTextIncludes.text = item.textIncludes
            tvReply.text = item.reply
            if(item.status){
                ivStatus.setImageResource(R.drawable.circle_true)
            }else{
                ivStatus.setImageResource(R.drawable.circle_false)
            }

            Log.d("debugging",item.username)
            cvChatItem.setOnClickListener {
                onClick(item)
            }
        }
    }

    fun setRules(items: List<Rules>) {
        this.items = items
        notifyDataSetChanged()
    }


    class ItemRulesHolder(val binding: ItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)

}



