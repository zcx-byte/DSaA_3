import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class Node<T>(
    var value: T,
    var left: Node<T>? = null,
    var right: Node<T>? = null
)

class BynaryTreeTest {

    @Test
    fun `вставка первого элемента создает корень`() {
        val tree = BynaryTree<Int>()
        tree.insert(10)

        assertEquals(10, tree.root?.value)
        assertNull(tree.root?.left)
        assertNull(tree.root?.right)
    }

    @Test
    fun `вставка трех элементов заполняет корень и его непосредственных потомков`() {
        val tree = BynaryTree<Int>()
        tree.insert(10)
        tree.insert(20)
        tree.insert(30)

        assertEquals(10, tree.root?.value)
        assertEquals(20, tree.root?.left?.value)
        assertEquals(30, tree.root?.right?.value)
    }

    @Test
    fun `вставка четырех элементов корректно переходит на следующий уровень слева направо`() {
        val tree = BynaryTree<Int>()
        tree.insert(10)
        tree.insert(20)
        tree.insert(30)
        tree.insert(40)

        assertEquals(10, tree.root?.value)
        assertEquals(20, tree.root?.left?.value)
        assertEquals(30, tree.root?.right?.value)
        assertEquals(40, tree.root?.left?.left?.value)

        assertNull(tree.root?.left?.right)
        assertNull(tree.root?.right?.left)
        assertNull(tree.root?.right?.right)
    }

    @Test
    fun `удаление из пустого дерева не вызывает ошибок`() {
        val tree = BynaryTree<Int>()
        tree.delete(10)
        assertNull(tree.root)
    }

    @Test
    fun `удаление единственного элемента обнуляет корень`() {
        val tree = BynaryTree<Int>()
        tree.insert(10)
        tree.delete(10)
        assertNull(tree.root)
    }

    @Test
    fun `удаление узла заменяет его значением последнего узла в обходе в ширину`() {
        val tree = BynaryTree<Int>()
        tree.insert(10)
        tree.insert(20)
        tree.insert(30)
        tree.insert(40)

        tree.delete(20)

        assertEquals(10, tree.root?.value)
        assertEquals(40, tree.root?.left?.value)
        assertEquals(30, tree.root?.right?.value)
        assertNull(tree.root?.left?.left)
        assertNull(tree.root?.left?.right)
    }

    @Test
    fun `удаление несуществующего значения не изменяет дерево`() {
        val tree = BynaryTree<Int>()
        tree.insert(10)
        tree.insert(20)

        tree.delete(99)

        assertEquals(10, tree.root?.value)
        assertEquals(20, tree.root?.left?.value)
    }

    private fun createSampleTree(): BynaryTree<Int> {
        val tree = BynaryTree<Int>()
        tree.insert(1)
        tree.insert(2)
        tree.insert(3)
        tree.insert(4)
        tree.insert(5)
        tree.insert(6)
        return tree
    }

    @Test
    fun `левый прямой обход (Root, Left, Right)`() {
        val tree = createSampleTree()
        val result = tree.leftPreOrder()
        assertEquals(listOf(1, 2, 4, 5, 3, 6), result)
    }

    @Test
    fun `левый обратный обход (Left, Right, Root)`() {
        val tree = createSampleTree()
        val result = tree.leftPostOrder()
        assertEquals(listOf(4, 5, 2, 6, 3, 1), result)
    }

    @Test
    fun `правый прямой обход (Root, Right, Left)`() {
        val tree = createSampleTree()
        val result = tree.rightPreOrder()
        assertEquals(listOf(1, 3, 6, 2, 5, 4), result)
    }

    @Test
    fun `правый обратный обход (Right, Left, Root)`() {
        val tree = createSampleTree()
        val result = tree.rightPostOrder()
        assertEquals(listOf(6, 3, 5, 4, 2, 1), result)
    }

    @Test
    fun `обход в ширину (по уровням, слева направо)`() {
        val tree = createSampleTree()
        val result = tree.bfs()
        assertEquals(listOf(1, 2, 3, 4, 5, 6), result)
    }

    @Test
    fun `обратный обход в ширину (по уровням снизу вверх, справа налево)`() {
        val tree = createSampleTree()
        val result = tree.Revbfs()
        assertEquals(listOf(6, 5, 4, 3, 2, 1), result)
    }
}