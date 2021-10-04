package com.example.pue

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.content.Context
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.accessibility.AccessibilityNodeInfo
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.animation.doOnEnd
import androidx.recyclerview.widget.RecyclerView
import com.example.pue.data.DataSource
import java.util.*

/**
 * Adapter for the [RecyclerView] in [DressingActivity].
 */
class DressingAdapter(context: Context) :
    RecyclerView.Adapter<DressingAdapter.DressViewHolder>() {

    private var chosenClothes = DataSource.chosenClothes

    // Update the order of the list according to drag and drop
    fun moveItem(fromPosition: Int, toPosition: Int): Boolean {

        if (fromPosition < toPosition) {
            for (i in fromPosition until toPosition) {
                Collections.swap(chosenClothes, i, i + 1)
            }
        } else {
            for (i in fromPosition downTo toPosition + 1) {
                Collections.swap(chosenClothes, i, i - 1)
            }
        }
        notifyItemMoved(fromPosition, toPosition)
        return true
    }

    class DressViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val cardItem =
            view.findViewById<com.google.android.material.card.MaterialCardView>(R.id.card_item)
        val clothName = view.findViewById<TextView>(R.id.cloth_name)
        val imageView: ImageView = view.findViewById(R.id.cloth_image)
        val cardBackText = view.findViewById<TextView>(R.id.puettu_text)
        val thumbImageView: ImageView = view.findViewById(R.id.thumb_image)
        val cardFront = view.findViewById<androidx.constraintlayout.widget.ConstraintLayout>(R.id.view_card_front)
        val cardBack = view.findViewById<androidx.constraintlayout.widget.ConstraintLayout>(R.id.view_card_back)

    }

    override fun getItemCount(): Int = chosenClothes.size

    /**
     * Creates new views with R.layout.item_view as its template
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DressViewHolder {
        val layout = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_view, parent, false)
        // Setup custom accessibility delegate to set the text read
        layout.accessibilityDelegate = ClothingAdapter
        return DressViewHolder(layout)
    }

    /**
     * Replaces the content of an existing view with new data
     */
    override fun onBindViewHolder(holder: DressViewHolder, position: Int) {

        holder.cardBack.visibility = View.GONE

        val item = chosenClothes[position]
        // Needed to call startActivity
       val context = holder.view.context

        // Set the text of the WordViewHolder
        holder.clothName.text = item.name
        holder.imageView.setImageResource(item.imageResourceId)
        //holder.thumbImageView.setImageResource()
        holder.cardBackText.text = item.name + " puettu"

        // Flip the card (if already flipped, then flip back again)
        holder.cardItem.setOnClickListener {
            if (holder.cardBack.visibility == View.GONE) {
                flipCard(context, holder.cardBack, holder.cardFront)
            }
            else {
                flipCard(context, holder.cardFront, holder.cardBack)
            }
        }

// How to implicit intent to Google if wanted
        //holder.button.setOnClickListener {
        // val queryUrl: Uri = Uri.parse("${DressingActivity.SEARCH_PREFIX}${item}")
        // val intent = Intent(Intent.ACTION_VIEW, queryUrl)
        // context.startActivity(intent)

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
            val customString = host?.context?.getString(R.string.look_up_word)
            val customClick =
                AccessibilityNodeInfo.AccessibilityAction(
                    AccessibilityNodeInfo.ACTION_CLICK,
                    customString
                )
            info?.addAction(customClick)
        }
    }

    /**
     * Flips the card with flip animation
     */
    // Code from here https://medium.com/geekculture/how-to-add-card-flip-animation-in-the-android-app-3060afeadd45
    fun flipCard(context: Context, visibleView: View, inVisibleView: View) {
        try {
            //visibleView.setVisibility(View.VISIBLE)
            visibleView.visibility = View.VISIBLE
            val scale = context.resources.displayMetrics.density
            val cameraDist = 8000 * scale
            visibleView.cameraDistance = cameraDist
            inVisibleView.cameraDistance = cameraDist
            val flipOutAnimatorSet =
                AnimatorInflater.loadAnimator(
                    context,
                    R.animator.flip_out
                ) as AnimatorSet
            flipOutAnimatorSet.setTarget(inVisibleView)
            val flipInAnimatorSet =
                AnimatorInflater.loadAnimator(
                    context,
                    R.animator.flip_in
                ) as AnimatorSet
            flipInAnimatorSet.setTarget(visibleView)
            flipOutAnimatorSet.start()
            flipInAnimatorSet.start()
            flipInAnimatorSet.doOnEnd {
                inVisibleView.visibility = View.GONE
                //inVisibleView.setVisibility(View.GONE)
            }
        } catch (e: Exception) {
            Log.d("VIRHE!:", e.toString())        }
    }

    //=====================================

}