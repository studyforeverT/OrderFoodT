package com.nvc.orderfood.extension

import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.nvc.orderfood.R

@BindingAdapter("set_image_url")
fun setImage(img: ImageView, link: String) {
    Glide.with(img.context).load(link).into(img)
}

@BindingAdapter("set_bg_by_position")
fun setBackgroundByPosition(cardView: CardView, position: Int) {
    cardView.setCardBackgroundColor(
        ContextCompat.getColor(
            cardView.context,
            if (position % 2 == 1) R.color.orange else R.color.bg_cart
        )
    )
}

@BindingAdapter("set_text_color_by_position")
fun setTextColorByPosition(textView: TextView, position: Int) {
    textView.setTextColor(
        ContextCompat.getColor(
            textView.context,
            if (position % 2 == 1) R.color.white else R.color.black
        )
    )
}

@BindingAdapter("set_text_color_by_status")
fun setTextColorByStatus(textView: TextView, status: Int) {
    textView.setTextColor(
        ContextCompat.getColor(
            textView.context,
            when (status) {
                0 -> R.color.teal_700
                1 -> R.color.green
                else -> {
                    R.color.orange
                }
            }
        )
    )
}

