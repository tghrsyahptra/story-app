package com.tghrsyahptra.storyapp.ui.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.tghrsyahptra.storyapp.R

class PasswordEdText : AppCompatEditText {

    private lateinit var passwordIcon: Drawable
    private var passwordLength = 0

    constructor(context: Context) : super(context) {
        initialize()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initialize()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initialize()
    }

    private fun initialize() {
        passwordIcon = ContextCompat.getDrawable(context, R.drawable.ic_baseline_lock_24) as Drawable
        displayPasswordIcon()
        addPasswordTextWatcher()
    }

    private fun addPasswordTextWatcher() {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // No action needed here
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                passwordLength = s.length
                if (s.isNotEmpty() && passwordLength < 8) {
                    error = context.getString(R.string.password_short)
                }
            }

            override fun afterTextChanged(s: Editable?) {
                // No action needed here
            }
        })
    }

    private fun displayPasswordIcon() {
        setDrawableIcon(startOfTheText = passwordIcon)
    }

    private fun setDrawableIcon(
        startOfTheText: Drawable? = null,
        topOfTheText: Drawable? = null,
        endOfTheText: Drawable? = null,
        bottomOfTheText: Drawable? = null
    ) {
        setCompoundDrawablesWithIntrinsicBounds(startOfTheText, topOfTheText, endOfTheText, bottomOfTheText)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        applyTextStyles()
        applyPasswordTransformation()
    }

    private fun applyTextStyles() {
        context.apply {
            setTextColor(ContextCompat.getColor(this, R.color.black))
            hint = getString(R.string.hint_password)
            textSize = 16f
            setHintTextColor(ContextCompat.getColor(this, R.color.gray_800))
            background = ContextCompat.getDrawable(this, R.drawable.form_input)
        }
        maxLines = 1
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
    }

    private fun applyPasswordTransformation() {
        transformationMethod = PasswordTransformationMethod.getInstance()
    }
}