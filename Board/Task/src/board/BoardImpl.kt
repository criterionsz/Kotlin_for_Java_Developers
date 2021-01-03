package board

fun createSquareBoard(width: Int): SquareBoard = SquareBoardImpl(width)
fun <T> createGameBoard(width: Int): GameBoard<T> = GameBoardImpl(width, createSquareBoard(width))


class SquareBoardImpl(override val width: Int) : SquareBoard {
    private val cells: MutableList<Cell> = mutableListOf()

    init {
        for (i in 1..width) {
            for (j in 1..width) {
                cells.add(Cell(i, j))
            }
        }
    }

    override fun getCellOrNull(i: Int, j: Int): Cell? {
        if (i > width || j > width)
            return null
        return cells.find { cell -> cell.i == i && cell.j == j }
    }

    override fun getCell(i: Int, j: Int): Cell {
        if (i > width || j > width)
            throw IllegalArgumentException()
        return cells.find { cell -> cell.i == i && cell.j == j }!!
    }

    override fun getAllCells(): Collection<Cell> {
        return cells
    }

    override fun getRow(i: Int, jRange: IntProgression): List<Cell> {
        val lst = mutableListOf<Cell>()
        for (j in jRange) {
            if (getCellOrNull(i, j) == null)
                break
            lst.add(getCell(i, j))
        }
        return lst
    }

    override fun getColumn(iRange: IntProgression, j: Int): List<Cell> {
        val lst = mutableListOf<Cell>()
        for (i in iRange) {
            if (getCellOrNull(i, j) == null)
                break
            lst.add(getCell(i, j))
        }
        return lst
    }

    override fun Cell.getNeighbour(direction: Direction): Cell? {
        return when (direction) {
            Direction.UP -> {
                getCellOrNull(i - 1, j)
            }
            Direction.LEFT -> {
                getCellOrNull(i, j - 1)
            }
            Direction.DOWN -> {
                getCellOrNull(i + 1, j)
            }
            Direction.RIGHT -> {
                getCellOrNull(i, j + 1)
            }
        }
    }
}

class GameBoardImpl<T>(override val width: Int, private val board: SquareBoard) : SquareBoard by board, GameBoard<T> {
    private var map: MutableMap<Cell, T?> = board.getAllCells().associateWith { null }.toMutableMap()
    override fun set(cell: Cell, value: T?) {
        map[cell] = value
    }

    override fun filter(predicate: (T?) -> Boolean): Collection<Cell> =
            map.filter { predicate(it.value) }.keys


    override fun find(predicate: (T?) -> Boolean): Cell? =
            map.filter { predicate(it.value) }.keys.firstOrNull()


    override fun any(predicate: (T?) -> Boolean): Boolean =
            map.any { predicate(it.value) }

    override fun all(predicate: (T?) -> Boolean): Boolean =
            map.all { predicate(it.value) }

    override fun get(cell: Cell): T? = map[cell]

}

