package com.rasika.offlinewordle

import androidx.lifecycle.ViewModel
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.util.*
import kotlin.math.abs

private const val WORDS_IN_LIST = 6234
class WordleGameViewModel : ViewModel() {
    var selectedWord = ""
    var turn = 1
    val gridColors = IntArray(30) { R.color.undecided_gray}
    val gridText = Array<String>(30) { ""}
    var correctLetterString = ""
    val correctLetterList = mutableListOf<Char>()
    var incorrectLetterString = ""
    private val incorrectLetterList = mutableListOf<Char>()
    var linkToWord = ""


    fun saveLetters(userWord: String) {
        for (i in 0 until userWord.length) {
            if (!correctLetterList.contains(userWord[i]) && selectedWord.contains(userWord[i])) {
                correctLetterList.add(userWord[i])
                correctLetterString += "${userWord[i]} "
            }
            if (!incorrectLetterList.contains(userWord[i]) && !selectedWord.contains(userWord[i])) {
                incorrectLetterList.add(userWord[i])
                incorrectLetterString += "${userWord[i]} "
            }
        }
    }

    fun saveGridColors(correctList: List<Int>, turn: Int) {
        for (i in 0 until correctList.size) {
            if (correctList[i] == 0) {
                gridColors[i + ((turn - 1) * 5)] = R.color.incorrect_gray
            }
            else if (correctList[i] == 1) {
                gridColors[i + ((turn - 1) * 5)] = R.color.almost_correct_yellow
            }
            else {
                gridColors[i + ((turn - 1) * 5)] = R.color.correct_green
            }
        }
    }

    fun saveGridText(textList: List<String>, turn: Int) {
        for (i in 0 until textList.size) {
            gridText[i + ((turn - 1) * 5)] = textList[i]
        }
    }


    // Sets selectedWord to random word
    fun selectWord(file: InputStream) {
        val scanner = Scanner(file)
        val randomNum = abs(Random().nextInt() % WORDS_IN_LIST) + 1
        for (i in 0 until randomNum) {
            scanner.next()
        }
        val word = scanner.next()
        selectedWord = word
    }

    // Checks if word is in word list
    fun isWordValid(userWord: String, wordList: InputStream): Boolean {
        val reader = BufferedReader(InputStreamReader(wordList))
        var word: String? = reader.readLine()
        while (word != null) {
            if (word == userWord) {
                return true
            }
            word = reader.readLine()
        }
        return false
    }

    fun incrementTurn() {
        turn++
    }

    // Returns list, 0 = incorrect letter, 1 = correct but wrong location, 2 = correct letter and location
    fun checkLetters(userLetterList: CharArray): List<Int> {
        val correctLetters = mutableListOf<Int>()
        for (i in 0 until selectedWord.length) {
            if (userLetterList[i] == selectedWord[i]) {
                correctLetters.add(i, 2)
            }
            else if (isLetterInWord(userLetterList[i], selectedWord)) {
                correctLetters.add(i, 1)
            }
            else {
                correctLetters.add(i, 0)
            }
        }
        return correctLetters
    }

    fun reset(file: InputStream) {
        selectWord(file)
        turn = 1
        for (i in 0 until gridColors.size) {
            gridColors[i] = R.color.undecided_gray
        }
        for (i in 0 until gridText.size) {
            gridText[i] = ""
        }
        correctLetterString = ""
        incorrectLetterString = ""
        correctLetterList.clear()
        incorrectLetterList.clear()
    }


    private fun isLetterInWord(char: Char, word: String): Boolean {
        for (i in 0 until word.length) {
            if (char == word[i]) {
                return true
            }
        }
        return false
    }
}