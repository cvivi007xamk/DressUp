package com.example.pue

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.accessibility.AccessibilityNodeInfo
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.pue.data.DataSource


/**
 * Adapter for the [RecyclerView] in [MainActivity].
 */
class ClothingAdapter(private val context: Context?) :
    RecyclerView.Adapter<ClothingAdapter.ClothingViewHolder>() {

    private val dataset = DataSource.clothes

    //public var chosenClothes: MutableList<Cloth> = mutableListOf()
    private var chosenClothes = DataSource.chosenClothes

    /**
     * Provides a reference for the views needed to display items in your list.
     */
    class ClothingViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.cloth_image)
        val cardItem =
            view.findViewById<com.google.android.material.card.MaterialCardView>(R.id.card_item)
        val clothName = view.findViewById<TextView>(R.id.cloth_name)
        val cardBack = view.findViewById<androidx.constraintlayout.widget.ConstraintLayout>(R.id.view_card_back)

    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    /**
     * Creates new views with R.layout.item_view as its template
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClothingViewHolder {
        val layout = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_view, parent, false)
        // Setup custom accessibility delegate to set the text read
        layout.accessibilityDelegate = DressingAdapter
        return ClothingViewHolder(layout)
    }

    /**
     * Replaces the content of an existing view with new data
     */
    override fun onBindViewHolder(holder: ClothingViewHolder, position: Int) {
        //val resources = context?.resources

        holder.cardBack.visibility = View.GONE

        val clothItem = dataset[position]
        holder.clothName.text = clothItem.name
        holder.imageView.setImageResource(clothItem.imageResourceId)
        holder.cardItem.isChecked = clothItem.checked
        holder.cardItem.setOnClickListener {
            if (chosenClothes.contains(clothItem)) {
                //remove item from list and sort the list
                chosenClothes.remove(clothItem)
                chosenClothes.sortBy { it.order }
            }
            // add item to list and sort the list
            else chosenClothes.add(clothItem)
            chosenClothes.sortBy { it.order }

            //set the item checked (both the card and property of the item)
            holder.cardItem.setChecked(!holder.cardItem.isChecked)
            clothItem.checked = !clothItem.checked
            // val context = holder.view.context
            // val intent = Intent(context, DressingActivity::class.java)
            // intent.putExtra(DressingActivity.LETTER, holder.clothName.text.toString())
            // context.startActivity(intent)

        }
    }

    // Setup custom accessibility delegate to set the text read with
    // an accessibility service
    companion object Accessibility : View.AccessibilityDelegate() {
        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        override fun onInitializeAccessibilityNodeInfo(
            host: View?,
            info: AccessibilityNodeInfo?
        ) {
            super.onInitializeAccessibilityNodeInfo(host, info)
            // With `null` as the second argument to [AccessibilityAction], the
            // accessibility service announces "double tap to activate".
            // If a custom string is provided,
            // it announces "double tap to <custom string>".
            val customString = host?.context?.getString(R.string.valmis)
            val customClick =
                AccessibilityNodeInfo.AccessibilityAction(
                    AccessibilityNodeInfo.ACTION_CLICK,
                    customString
                )
            info?.addAction(customClick)
        }
    }
}