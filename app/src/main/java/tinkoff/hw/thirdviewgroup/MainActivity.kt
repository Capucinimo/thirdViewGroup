package tinkoff.hw.thirdviewgroup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import com.google.android.material.chip.ChipDrawable
import com.google.android.material.chip.Chip

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val firstGroup = findViewById<MyViewGroup>(R.id.firstGroup)
        val secondGroup = findViewById<MyViewGroup>(R.id.secondGroup)

        if (savedInstanceState != null) {
            val chipsTextFirstGroup = savedInstanceState.getStringArrayList("chipsTextFirstGroup")!!
            val chipsTextSecondGroup = savedInstanceState.getStringArrayList("chipsTextSecondGroup")!!
            for (chipsText in chipsTextFirstGroup) {
                createChip(firstGroup, chipsText, secondGroup)
            }
            for (chipsText in chipsTextSecondGroup) {
                createChip(secondGroup, chipsText, firstGroup)
            }
        } else {
            for (i in 0..20) {
                createChip(firstGroup, randomWord(15), secondGroup)
            }
            for (i in 21..60) {
                createChip(secondGroup, randomWord(15), firstGroup)
            }
        }
    }

    private fun randomWord(maxLength:Int): String {
        val length = if (maxLength>36 || maxLength <1) 15 else maxLength
        return java.util.UUID.randomUUID().toString().substring((36 - length..35).random())
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val firstGroup = findViewById<MyViewGroup>(R.id.firstGroup)
        val secondGroup = findViewById<MyViewGroup>(R.id.secondGroup)
        saveChipsText(firstGroup,outState,"chipsTextFirstGroup")
        saveChipsText(secondGroup,outState,"chipsTextSecondGroup")
    }

    private fun saveChipsText(myViewGroup: MyViewGroup, outState: Bundle,key: String? ) {
        val chipsTextMyViewGroup: ArrayList<String> = ArrayList()
        for (i in 0 until myViewGroup.childCount) {
            val currentChip = myViewGroup.getChildAt(i) as Chip
            chipsTextMyViewGroup.add(currentChip.chipText.toString())
        }
        outState.putStringArrayList(key, chipsTextMyViewGroup)
    }

    private fun createChip(entryViewGroup: ViewGroup, text: String, newViewGroup: ViewGroup) {
        val chip = Chip(this)
        chip.setChipDrawable(ChipDrawable.createFromResource(this, R.xml.chip))
        //val chip = LayoutInflater.from(this).inflate(R.layout.chip, entryViewGroup, false)
        chip.chipText = text
        //chip.id = View.generateViewId()
        chip.setOnCloseIconClickListener {
            if (chip.parent == entryViewGroup) {
                entryViewGroup.removeView(chip)
                newViewGroup.addView(chip)
            } else if (chip.parent == newViewGroup) {
                newViewGroup.removeView(chip)
                entryViewGroup.addView(chip)
            }
        }
        entryViewGroup.addView(chip)
    }
}
