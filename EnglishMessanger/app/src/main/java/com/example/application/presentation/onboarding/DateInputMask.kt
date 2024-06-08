package com.example.application.presentation.onboarding

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import java.util.*

class DateInputMask(private val input: EditText) : TextWatcher {

    private var current = ""
    private val ddmmyyyy = "DDMMYYYY"
    private val cal = Calendar.getInstance()

    override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {}

    override fun afterTextChanged(editable: Editable?) {
        if (editable.toString() != current) {
            var clean = editable.toString().replace("[^\\d.]|\\.".toRegex(), "")
            val cleanC = current.replace("[^\\d.]|\\.".toRegex(), "")

            val cl = clean.length
            var sel = cl
            for (i in 2..cl step 2) sel++
            if (clean == cleanC) sel--

            if (clean.length < 8) {
                clean += ddmmyyyy.substring(clean.length)
            } else {
                var day = clean.substring(0, 2).toInt()
                var mon = clean.substring(2, 4).toInt()
                var year = clean.substring(4, 8).toInt()

                if (mon > 12) mon = 12
                cal.set(Calendar.MONTH, mon - 1)
                year = if (year < 1900) 1900 else if (year > 2100) 2100 else year
                cal.set(Calendar.YEAR, year)
                day = if (day > cal.getActualMaximum(Calendar.DATE)) cal.getActualMaximum(Calendar.DATE) else day
                clean = String.format("%02d%02d%02d", day, mon, year)
            }

            clean = String.format("%s.%s.%s", clean.substring(0, 2),
                clean.substring(2, 4),
                clean.substring(4, 8))

            sel = if (sel < 0) 0 else sel
            current = clean
            input.removeTextChangedListener(this)
            input.setText(current)
            input.setSelection(if (sel < current.count()) sel else current.count())
            input.addTextChangedListener(this)
        }
    }
}
