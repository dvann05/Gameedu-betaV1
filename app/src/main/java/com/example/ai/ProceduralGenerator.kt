package com.example.ai

import com.example.data.model.GameDifficulty
import kotlin.random.Random

data class MathQuestion(
    val level: Int,
    val questionText: String,
    val itemEmoji: String,
    val countItems: Int,
    val num1: Int,
    val num2: Int,
    val operator: String,
    val correctAnswer: Int,
    val choices: List<Int>
)

data class MazeCell(
    val row: Int,
    val col: Int,
    var topWall: Boolean = true,
    var rightWall: Boolean = true,
    var bottomWall: Boolean = true,
    var leftWall: Boolean = true,
    var visited: Boolean = false
)

data class MazeData(
    val rows: Int,
    val cols: Int,
    val grid: List<List<MazeCell>>,
    val startRow: Int = 0,
    val startCol: Int = 0,
    val goalRow: Int,
    val goalCol: Int
)

data class WordSortQuestion(
    val targetWord: String,
    val hintTranslation: String,
    val iconEmoji: String,
    val scrambledLetters: List<Char>
)

data class PatternQuestion(
    val sequenceEmojis: List<String>,
    val correctMissingEmoji: String,
    val choices: List<String>
)

object ProceduralGenerator {

    private val fruitEmojis = listOf("🍎", "🍌", "🍊", "🍇", "🍓", "🍉", "🍒", "🍍", "🍐", "🍑")
    private val animalEmojis = listOf("🐶", "🐱", "🦁", "🐯", "🐼", "🐨", "🐮", "🐷", "🐸", "🐵")
    private val shapeEmojis = listOf("🔴", "🔵", "🟡", "🟢", "🟣", "🟧", "⭐", "💎", "🌙", "☁️")

    private val commonWords = mapOf(
        "APPLE" to Pair("Apel", "🍎"),
        "BANANA" to Pair("Pisang", "🍌"),
        "CAT" to Pair("Kucing", "🐱"),
        "DOG" to Pair("Anjing", "🐶"),
        "FISH" to Pair("Ikan", "🐟"),
        "LION" to Pair("Singa", "🦁"),
        "STAR" to Pair("Bintang", "⭐"),
        "MOON" to Pair("Bulan", "🌙"),
        "SUN" to Pair("Matahari", "☀️"),
        "CAR" to Pair("Mobil", "🚗"),
        "BALL" to Pair("Bola", "⚽"),
        "BOOK" to Pair("Buku", "📚"),
        "TREE" to Pair("Pohon", "🌳"),
        "HOUSE" to Pair("Rumah", "🏠"),
        "BIRD" to Pair("Burung", "🦜")
    )

    // 1. Procedural Math Questions
    fun generateMathQuestion(level: Int, difficulty: GameDifficulty): MathQuestion {
        val rand = Random(level * 31L + System.currentTimeMillis() % 1000)
        val maxNum = (5 + (level / 2) * difficulty.multiplier.toInt()).coerceAtMost(99)
        val emoji = fruitEmojis.random(rand)

        val isAddition = rand.nextBoolean() || level < 10
        val n1 = rand.nextInt(1, maxNum.coerceAtLeast(5))
        val n2 = rand.nextInt(1, if (isAddition) maxNum else n1.coerceAtLeast(2))

        val (op, ans) = if (isAddition) {
            "+" to (n1 + n2)
        } else {
            "-" to (n1 - n2)
        }

        val questionText = "$n1 $op $n2 = ?"

        val distractors = mutableSetOf<Int>()
        while (distractors.size < 3) {
            val delta = rand.nextInt(-5, 6)
            val d = ans + delta
            if (d >= 0 && d != ans) {
                distractors.add(d)
            }
        }

        val choices = (distractors + ans).shuffled(rand)

        return MathQuestion(
            level = level,
            questionText = questionText,
            itemEmoji = emoji,
            countItems = if (ans in 1..20) ans else n1,
            num1 = n1,
            num2 = n2,
            operator = op,
            correctAnswer = ans,
            choices = choices
        )
    }

    // 2. Procedural Maze Generator (DFS Recursive Backtracker)
    fun generateMaze(level: Int): MazeData {
        val size = (4 + (level / 15)).coerceAtMost(12)
        val rows = size
        val cols = size

        val grid = List(rows) { r ->
            List(cols) { c ->
                MazeCell(r, c)
            }
        }

        val stack = mutableListOf<MazeCell>()
        val startCell = grid[0][0]
        startCell.visited = true
        stack.add(startCell)

        val rand = Random(level * 999L + 123)

        while (stack.isNotEmpty()) {
            val current = stack.last()
            val unvisitedNeighbors = getUnvisitedNeighbors(current, grid, rows, cols)

            if (unvisitedNeighbors.isNotEmpty()) {
                val next = unvisitedNeighbors.random(rand)
                removeWalls(current, next)
                next.visited = true
                stack.add(next)
            } else {
                stack.removeAt(stack.size - 1)
            }
        }

        return MazeData(
            rows = rows,
            cols = cols,
            grid = grid,
            startRow = 0,
            startCol = 0,
            goalRow = rows - 1,
            goalCol = cols - 1
        )
    }

    private fun getUnvisitedNeighbors(
        cell: MazeCell,
        grid: List<List<MazeCell>>,
        rows: Int,
        cols: Int
    ): List<MazeCell> {
        val list = mutableListOf<MazeCell>()
        val r = cell.row
        val c = cell.col

        if (r > 0 && !grid[r - 1][c].visited) list.add(grid[r - 1][c])
        if (r < rows - 1 && !grid[r + 1][c].visited) list.add(grid[r + 1][c])
        if (c > 0 && !grid[r][c - 1].visited) list.add(grid[r][c - 1])
        if (c < cols - 1 && !grid[r][c + 1].visited) list.add(grid[r][c + 1])

        return list
    }

    private fun removeWalls(a: MazeCell, b: MazeCell) {
        if (a.row == b.row) {
            if (a.col < b.col) {
                a.rightWall = false
                b.leftWall = false
            } else {
                a.leftWall = false
                b.rightWall = false
            }
        } else if (a.col == b.col) {
            if (a.row < b.row) {
                a.bottomWall = false
                b.topWall = false
            } else {
                a.topWall = false
                b.bottomWall = false
            }
        }
    }

    // 3. Procedural Word Sort Question
    fun generateWordSort(level: Int): WordSortQuestion {
        val rand = Random(level * 77L + System.currentTimeMillis() % 500)
        val entry = commonWords.entries.toList().random(rand)
        val word = entry.key
        val hint = entry.value.first
        val emoji = entry.value.second

        val scrambled = word.toCharArray().toList().shuffled(rand)
        return WordSortQuestion(
            targetWord = word,
            hintTranslation = hint,
            iconEmoji = emoji,
            scrambledLetters = scrambled
        )
    }

    // 4. Procedural Pattern Logic
    fun generatePatternQuestion(level: Int): PatternQuestion {
        val rand = Random(level * 43L)
        val set = shapeEmojis.shuffled(rand).take(2)
        val a = set[0]
        val b = set[1]

        // Pattern A-B-A-B-[?]
        val sequence = listOf(a, b, a, b)
        val correct = a

        val distractors = shapeEmojis.filter { it != correct }.shuffled(rand).take(3)
        val choices = (distractors + correct).shuffled(rand)

        return PatternQuestion(
            sequenceEmojis = sequence,
            correctMissingEmoji = correct,
            choices = choices
        )
    }
}
