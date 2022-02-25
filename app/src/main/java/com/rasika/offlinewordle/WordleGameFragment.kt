package com.rasika.offlinewordle

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.rasika.offlinewordle.databinding.FragmenntWordleGameBinding

private const val WORD_FILE ="words.txt"

class WordleGameFragment:Fragment() {
    private  var _binding: FragmenntWordleGameBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmenntWordleGameBinding.inflate(inflater, container, false)
        val view = binding.root
        val viewModel = ViewModelProvider(this).get(WordleGameViewModel::class.java)

        val myWordFile = requireActivity().assets.open(WORD_FILE)

        if (viewModel.selectedWord == "") {
            viewModel.selectWord(myWordFile)
        }

        // Shows grid
        showGrid(viewModel)

        binding.helpIcon.setOnClickListener {
            val helpFragment = HelpFragment()
            val fragmentManager = activity?.supportFragmentManager
            if (fragmentManager != null) {
                fragmentManager.beginTransaction()
                    .add(R.id.fragment_container, helpFragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .addToBackStack("_")
                    .commit()
            }
        }

        binding.fab.setOnClickListener {
            val wordsFile = requireActivity().assets.open(WORD_FILE)
            val userWordCaseSensitive = binding.textEntry.text.toString()
            val userWord = userWordCaseSensitive.lowercase()
            val correctList = viewModel.checkLetters(userWord.toCharArray())
            if (userWord.length != 5) {
                Toast.makeText(context, R.string.invalid_length, Toast.LENGTH_SHORT).show()
            }
            else if (!isAllChars(userWord)) {
                Toast.makeText(context, R.string.invalid_entry, Toast.LENGTH_SHORT).show()
            }
            else if (!viewModel.isWordValid(userWord, wordsFile)) {
                Toast.makeText(context, R.string.invalid_word, Toast.LENGTH_SHORT).show()
            }
            else {
                viewModel.saveLetters(userWord)
                if (checkForWin(correctList)) {
                    endGame(true, viewModel.selectedWord, viewModel)
                }
                updateGrid(viewModel.turn, correctList, userWord, viewModel)
                showGrid(viewModel)
                viewModel.incrementTurn()
                binding.textEntry.text.clear()
            }

            if (viewModel.turn > 6) {
                endGame(checkForWin(correctList), viewModel.selectedWord, viewModel)
            }
        }

        binding.resetButton.setOnClickListener {
            viewModel.reset(requireActivity().assets.open(WORD_FILE))
            resetViews()
        }


        return view
    }

    private fun endGame(isWon: Boolean, correctWord: String, viewModel: WordleGameViewModel) {
        binding.textEntry.visibility = View.GONE
        binding.winLoseText.visibility = View.VISIBLE
        binding.dictionaryLink.visibility = View.VISIBLE
        if (isWon) {
            val winText = getString(R.string.win_text)
            binding.winLoseText.text = winText
            binding.dictionaryLink.text = viewModel.linkToWord
        }
        else {
            val loseText = "${getString(R.string.lose_text)} $correctWord"
            binding.winLoseText.text = loseText
            binding.dictionaryLink.text = viewModel.linkToWord
        }
        binding.fab.visibility = View.GONE
        binding.resetButton.visibility = View.VISIBLE
    }

    private fun checkForWin(correctList: List<Int>): Boolean {
        if (correctList == listOf(2, 2, 2, 2, 2)) {
            return true
        }
        return false
    }

    private fun isAllChars(word: String) : Boolean {
        if (word.matches(Regex("[a-zA-Z]+"))) {
            return true
        }
        return false
    }

    // Colors the grid based on if letters are correct
    private fun updateGrid(turn: Int, correctList: List<Int>, userWord: String, viewModel: WordleGameViewModel) {
        val colorIdList = IntArray(5)
        for (i in 0 until correctList.size) {
            if (correctList[i] == 2) {
                colorIdList[i] = R.color.correct_green
            }
            else if (correctList[i] == 1) {
                colorIdList[i] = R.color.almost_correct_yellow
            }
            else {
                colorIdList[i] = R.color.incorrect_gray
            }
        }

        viewModel.saveGridColors(correctList, turn)


        val userWordAsList = mutableListOf<String>()
        for (i in 0 until userWord.length) {
            userWordAsList.add(i, userWord[i].toString())
        }
        viewModel.saveGridText(userWordAsList, turn)
    }

    private fun showGrid(viewModel: WordleGameViewModel) {
        binding.a1.setBackgroundResource(viewModel.gridColors[0])
        binding.a2.setBackgroundResource(viewModel.gridColors[1])
        binding.a3.setBackgroundResource(viewModel.gridColors[2])
        binding.a4.setBackgroundResource(viewModel.gridColors[3])
        binding.a5.setBackgroundResource(viewModel.gridColors[4])
        binding.b1.setBackgroundResource(viewModel.gridColors[5])
        binding.b2.setBackgroundResource(viewModel.gridColors[6])
        binding.b3.setBackgroundResource(viewModel.gridColors[7])
        binding.b4.setBackgroundResource(viewModel.gridColors[8])
        binding.b5.setBackgroundResource(viewModel.gridColors[9])
        binding.c1.setBackgroundResource(viewModel.gridColors[10])
        binding.c2.setBackgroundResource(viewModel.gridColors[11])
        binding.c3.setBackgroundResource(viewModel.gridColors[12])
        binding.c4.setBackgroundResource(viewModel.gridColors[13])
        binding.c5.setBackgroundResource(viewModel.gridColors[14])
        binding.d1.setBackgroundResource(viewModel.gridColors[15])
        binding.d2.setBackgroundResource(viewModel.gridColors[16])
        binding.d3.setBackgroundResource(viewModel.gridColors[17])
        binding.d4.setBackgroundResource(viewModel.gridColors[18])
        binding.d5.setBackgroundResource(viewModel.gridColors[19])
        binding.e1.setBackgroundResource(viewModel.gridColors[20])
        binding.e2.setBackgroundResource(viewModel.gridColors[21])
        binding.e3.setBackgroundResource(viewModel.gridColors[22])
        binding.e4.setBackgroundResource(viewModel.gridColors[23])
        binding.e5.setBackgroundResource(viewModel.gridColors[24])
        binding.f1.setBackgroundResource(viewModel.gridColors[25])
        binding.f2.setBackgroundResource(viewModel.gridColors[26])
        binding.f3.setBackgroundResource(viewModel.gridColors[27])
        binding.f4.setBackgroundResource(viewModel.gridColors[28])
        binding.f5.setBackgroundResource(viewModel.gridColors[29])

        binding.a1.text = viewModel.gridText[0]
        binding.a2.text = viewModel.gridText[1]
        binding.a3.text = viewModel.gridText[2]
        binding.a4.text = viewModel.gridText[3]
        binding.a5.text = viewModel.gridText[4]
        binding.b1.text = viewModel.gridText[5]
        binding.b2.text = viewModel.gridText[6]
        binding.b3.text = viewModel.gridText[7]
        binding.b4.text = viewModel.gridText[8]
        binding.b5.text = viewModel.gridText[9]
        binding.c1.text = viewModel.gridText[10]
        binding.c2.text = viewModel.gridText[11]
        binding.c3.text = viewModel.gridText[12]
        binding.c4.text = viewModel.gridText[13]
        binding.c5.text = viewModel.gridText[14]
        binding.d1.text = viewModel.gridText[15]
        binding.d2.text = viewModel.gridText[16]
        binding.d3.text = viewModel.gridText[17]
        binding.d4.text = viewModel.gridText[18]
        binding.d5.text = viewModel.gridText[19]
        binding.e1.text = viewModel.gridText[20]
        binding.e2.text = viewModel.gridText[21]
        binding.e3.text = viewModel.gridText[22]
        binding.e4.text = viewModel.gridText[23]
        binding.e5.text = viewModel.gridText[24]
        binding.f1.text = viewModel.gridText[25]
        binding.f2.text = viewModel.gridText[26]
        binding.f3.text = viewModel.gridText[27]
        binding.f4.text = viewModel.gridText[28]
        binding.f5.text = viewModel.gridText[29]


    }

    private fun resetViews() {
        binding.a1.text = ""
        binding.a2.text = ""
        binding.a3.text = ""
        binding.a4.text = ""
        binding.a5.text = ""
        binding.b1.text = ""
        binding.b2.text = ""
        binding.b3.text = ""
        binding.b4.text = ""
        binding.b5.text = ""
        binding.c1.text = ""
        binding.c2.text = ""
        binding.c3.text = ""
        binding.c4.text = ""
        binding.c5.text = ""
        binding.d1.text = ""
        binding.d2.text = ""
        binding.d3.text = ""
        binding.d4.text = ""
        binding.d5.text = ""
        binding.e1.text = ""
        binding.e2.text = ""
        binding.e3.text = ""
        binding.e4.text = ""
        binding.e5.text = ""
        binding.f1.text = ""
        binding.f2.text = ""
        binding.f3.text = ""
        binding.f4.text = ""
        binding.f5.text = ""
        val bgRes = R.color.undecided_gray
        binding.a1.setBackgroundResource(bgRes)
        binding.a2.setBackgroundResource(bgRes)
        binding.a3.setBackgroundResource(bgRes)
        binding.a4.setBackgroundResource(bgRes)
        binding.a5.setBackgroundResource(bgRes)
        binding.b1.setBackgroundResource(bgRes)
        binding.b2.setBackgroundResource(bgRes)
        binding.b3.setBackgroundResource(bgRes)
        binding.b4.setBackgroundResource(bgRes)
        binding.b5.setBackgroundResource(bgRes)
        binding.c1.setBackgroundResource(bgRes)
        binding.c2.setBackgroundResource(bgRes)
        binding.c3.setBackgroundResource(bgRes)
        binding.c4.setBackgroundResource(bgRes)
        binding.c5.setBackgroundResource(bgRes)
        binding.d1.setBackgroundResource(bgRes)
        binding.d2.setBackgroundResource(bgRes)
        binding.d3.setBackgroundResource(bgRes)
        binding.d4.setBackgroundResource(bgRes)
        binding.d5.setBackgroundResource(bgRes)
        binding.e1.setBackgroundResource(bgRes)
        binding.e2.setBackgroundResource(bgRes)
        binding.e3.setBackgroundResource(bgRes)
        binding.e4.setBackgroundResource(bgRes)
        binding.e5.setBackgroundResource(bgRes)
        binding.f1.setBackgroundResource(bgRes)
        binding.f2.setBackgroundResource(bgRes)
        binding.f3.setBackgroundResource(bgRes)
        binding.f4.setBackgroundResource(bgRes)
        binding.f5.setBackgroundResource(bgRes)

        binding.resetButton.visibility = View.INVISIBLE
        binding.winLoseText.visibility = View.INVISIBLE
        binding.textEntry.visibility = View.VISIBLE
        binding.fab.visibility = View.VISIBLE
        binding.dictionaryLink.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}