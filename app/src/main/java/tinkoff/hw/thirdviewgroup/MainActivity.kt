package tinkoff.hw.thirdviewgroup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import com.google.android.material.chip.ChipDrawable
import com.google.android.material.chip.Chip
import android.util.SparseArray
import android.os.Parcelable





class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val firstGroup = findViewById<MyViewGroup>(R.id.firstGroup)
        val secondGroup = findViewById<MyViewGroup>(R.id.secondGroup)
        for (i in 0..20){
            createChip(firstGroup, "Chip$i",secondGroup)
        }
        for (i in 21..31){
            createChip(secondGroup, "Chip$i",firstGroup)
        }
        /*val chip = createChip(firstGroup, "textnew",secondGroup)
        firstGroup.addView(chip)
        val chip2 = createChip(firstGroup, "chip2",secondGroup)
        firstGroup.addView(chip2)*/
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

    }



    private fun createChip(entryViewGroup: ViewGroup, text: String, newViewGroup: ViewGroup) {
        val chip = Chip(this)
        chip.setChipDrawable(ChipDrawable.createFromResource(this, R.xml.chip))

        chip.chipText = text
        chip.id = View.generateViewId()
        chip.setOnCloseIconClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                if (chip.parent == entryViewGroup) {
                    entryViewGroup.removeView(chip)
                    newViewGroup.addView(chip)
                }
                else if (chip.parent == newViewGroup) {
                    newViewGroup.removeView(chip)
                    entryViewGroup.addView(chip)
                }
            }
        })
        entryViewGroup.addView(chip)
    }
}
