import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class BinarySearchTreeTest {

    private val tree = BinarySearchTree<Int>()

    // 1. Создание и начальное состояние
    @Test
    fun `новое дерево имеет пустой корень`() {
        assertNull(tree.root)
    }


    // 2. Вставка (insert)
    @Test
    fun `вставка первого элемента создает корень`() {
        tree.insert(50)

        assertEquals(50, tree.root?.value)
        assertNull(tree.root?.left)
        assertNull(tree.root?.right)
    }

    @Test
    fun `вставка нескольких элементов формирует корректную структуру`() {
        // Ожидаемая структура:
        //      50
        //     /  \
        //   30    70
        //  /  \
        // 20  40
        tree.insert(50)
        tree.insert(30)
        tree.insert(70)
        tree.insert(20)
        tree.insert(40)

        assertEquals(50, tree.root?.value)
        assertEquals(30, tree.root?.left?.value)
        assertEquals(70, tree.root?.right?.value)
        assertEquals(20, tree.root?.left?.left?.value)
        assertEquals(40, tree.root?.left?.right?.value)
    }

    @Test
    fun `вставка дубликата игнорируется и не ломает структуру`() {
        tree.insert(50)
        tree.insert(50)
        tree.insert(50)

        assertEquals(50, tree.root?.value)
        assertNull(tree.root?.left)
        assertNull(tree.root?.right)
    }

    @Test
    fun `повторная вставка существующего элемента не увеличивает дерево`() {
        tree.insert(50)
        tree.insert(30)
        tree.insert(70)

        tree.insert(30)

        assertEquals(30, tree.root?.left?.value)
        assertNull(tree.root?.left?.left)
        assertNull(tree.root?.left?.right)
    }


    // 3. Поиск (search)
    @Test
    fun `поиск в пустом дереве возвращает false`() {
        assertFalse(tree.search(10))
    }

    @Test
    fun `поиск существующих элементов возвращает true`() {
        tree.insert(50)
        tree.insert(30)
        tree.insert(70)

        assertTrue(tree.search(50)) // корень
        assertTrue(tree.search(30)) // левый потомок
        assertTrue(tree.search(70)) // правый потомок
    }

    @Test
    fun `поиск несуществующих элементов возвращает false`() {
        tree.insert(50)
        tree.insert(30)
        tree.insert(70)

        assertFalse(tree.search(10))  // меньше минимума
        assertFalse(tree.search(90))  // больше максимума
        assertFalse(tree.search(40))  // между узлами
    }


    // 4. Удаление (delete)
    @Test
    fun `удаление из пустого дерева не вызывает ошибок`() {
        tree.delete(10)

        assertNull(tree.root)
    }

    @Test
    fun `удаление несуществующего элемента не меняет дерево`() {
        tree.insert(50)
        tree.insert(30)

        tree.delete(99)

        assertEquals(50, tree.root?.value)
        assertEquals(30, tree.root?.left?.value)
    }

    @Test
    fun `удаление листа (узла без потомков)`() {
        tree.insert(50)
        tree.insert(30) // лист

        tree.delete(30)

        assertEquals(50, tree.root?.value)
        assertNull(tree.root?.left)
    }

    @Test
    fun `удаление узла с одним левым потомком`() {
        tree.insert(50)
        tree.insert(30)
        tree.insert(20) // левый потомок узла 30

        tree.delete(30)

        assertEquals(50, tree.root?.value)
        assertEquals(20, tree.root?.left?.value)
        assertNull(tree.root?.left?.left)
    }

    @Test
    fun `удаление узла с одним правым потомком`() {
        tree.insert(50)
        tree.insert(30)
        tree.insert(40) // правый потомок узла 30

        tree.delete(30)

        assertEquals(50, tree.root?.value)
        assertEquals(40, tree.root?.left?.value)
        assertNull(tree.root?.left?.right)
    }

    @Test
    fun `удаление узла с двумя потомками (замена на минимум из правого поддерева)`() {
        // Структура:
        //      50
        //     /  \
        //   30    70
        //        /  \
        //       60   80
        tree.insert(50)
        tree.insert(30)
        tree.insert(70)
        tree.insert(60)
        tree.insert(80)

        // Удаляем корень (50), у которого два потомка
        // Минимум в правом поддереве (от 70) равен 60
        // Значение 50 должно замениться на 60, а старый узел 60 должен быть удален
        tree.delete(50)

        assertEquals(60, tree.root?.value)
        assertEquals(30, tree.root?.left?.value)
        assertEquals(70, tree.root?.right?.value)
        assertNull(tree.root?.right?.left) // старый узел 60 удален
        assertEquals(80, tree.root?.right?.right?.value)
    }

    @Test
    fun `удаление единственного элемента делает дерево пустым`() {
        tree.insert(42)

        tree.delete(42)

        assertNull(tree.root)
    }

    @Test
    fun `после удаления элемент перестаёт находиться в дереве`() {
        tree.insert(50)
        tree.insert(30)
        tree.insert(70)

        tree.delete(30)

        assertFalse(tree.search(30))
        assertTrue(tree.search(50))
        assertTrue(tree.search(70))
    }

    @Test
    fun `после удаления корня поиск старого корня возвращает false`() {
        tree.insert(50)
        tree.insert(30)
        tree.insert(70)

        tree.delete(50)

        assertFalse(tree.search(50))
        assertTrue(tree.search(30))
        assertTrue(tree.search(70))
    }

    @Test
    fun `после удаления всех элементов дерево становится пустым`() {
        tree.insert(50)
        tree.insert(30)
        tree.insert(70)

        tree.delete(30)
        tree.delete(70)
        tree.delete(50)

        assertNull(tree.root)
        assertFalse(tree.search(50))
        assertFalse(tree.search(30))
        assertFalse(tree.search(70))
    }

    @Test
    fun `после удаления можно снова вставить то же значение`() {
        tree.insert(50)

        tree.delete(50)
        tree.insert(50)

        assertTrue(tree.search(50))
        assertEquals(50, tree.root?.value)
    }


    // 5. Обходы (traversals)

    @Test
    fun `левый прямой обход посещает корень, левое поддерево и правое поддерево`() {
        //       1
        //      / \
        //     2   3
        //    / \   \
        //   4   5   6
        val tree = BinarySearchTree<Int>()
        tree.root = Node(
            1,
            left = Node(
                2,
                left = Node(4),
                right = Node(5)
            ),
            right = Node(
                3,
                right = Node(6)
            )
        )

        val result = mutableListOf<Int>()
        tree.leftPreOrder { result.add(it) }

        assertEquals(listOf(1, 2, 4, 5, 3, 6), result)
    }

    @Test
    fun `левый обратный обход посещает левое поддерево, правое поддерево и корень`() {
        //       1
        //      / \
        //     2   3
        //    / \   \
        //   4   5   6
        val tree = BinarySearchTree<Int>()
        tree.root = Node(
            1,
            left = Node(
                2,
                left = Node(4),
                right = Node(5)
            ),
            right = Node(
                3,
                right = Node(6)
            )
        )

        val result = mutableListOf<Int>()
        tree.leftPostOrder { result.add(it) }

        assertEquals(listOf(4, 5, 2, 6, 3, 1), result)
    }

    @Test
    fun `правый прямой обход посещает корень, правое поддерево и левое поддерево`() {
        //       1
        //      / \
        //     2   3
        //    / \   \
        //   4   5   6
        val tree = BinarySearchTree<Int>()
        tree.root = Node(
            1,
            left = Node(
                2,
                left = Node(4),
                right = Node(5)
            ),
            right = Node(
                3,
                right = Node(6)
            )
        )

        val result = mutableListOf<Int>()
        tree.rightPreOrder { result.add(it) }

        assertEquals(listOf(1, 3, 6, 2, 5, 4), result)
    }

    @Test
    fun `правый обратный обход посещает правое поддерево, левое поддерево и корень`() {
        //       1
        //      / \
        //     2   3
        //    / \   \
        //   4   5   6
        val tree = BinarySearchTree<Int>()
        tree.root = Node(
            1,
            left = Node(
                2,
                left = Node(4),
                right = Node(5)
            ),
            right = Node(
                3,
                right = Node(6)
            )
        )

        val result = mutableListOf<Int>()
        tree.rightPostOrder { result.add(it) }

        assertEquals(listOf(6, 3, 5, 4, 2, 1), result)
    }

    @Test
    fun `обход пустого дерева возвращает пустой список`() {
        val result = mutableListOf<Int>()
        tree.leftPreOrder { result.add(it) }

        assertTrue(result.isEmpty())
    }

    @Test
    fun `обходы пустого дерева не вызывают action`() {
        var callCount = 0

        tree.leftPreOrder { callCount++ }
        assertEquals(0, callCount)

        tree.leftPostOrder { callCount++ }
        assertEquals(0, callCount)

        tree.rightPreOrder { callCount++ }
        assertEquals(0, callCount)

        tree.rightPostOrder { callCount++ }
        assertEquals(0, callCount)
    }

    @Test
    fun `обход одного элемента возвращает только этот элемент`() {
        tree.insert(42)

        val preOrder = mutableListOf<Int>()
        tree.leftPreOrder { preOrder.add(it) }

        val postOrder = mutableListOf<Int>()
        tree.leftPostOrder { postOrder.add(it) }

        val rightPreOrder = mutableListOf<Int>()
        tree.rightPreOrder { rightPreOrder.add(it) }

        val rightPostOrder = mutableListOf<Int>()
        tree.rightPostOrder { rightPostOrder.add(it) }

        assertEquals(listOf(42), preOrder)
        assertEquals(listOf(42), postOrder)
        assertEquals(listOf(42), rightPreOrder)
        assertEquals(listOf(42), rightPostOrder)
    }

    @Test
    fun `левый прямой обход BST возвращает корень первым`() {
        tree.insert(50)
        tree.insert(30)
        tree.insert(70)
        tree.insert(20)
        tree.insert(40)

        val result = mutableListOf<Int>()
        tree.leftPreOrder { result.add(it) }

        assertEquals(50, result.first())
    }

    @Test
    fun `правый прямой обход BST возвращает корень первым`() {
        tree.insert(50)
        tree.insert(30)
        tree.insert(70)
        tree.insert(20)
        tree.insert(40)

        val result = mutableListOf<Int>()
        tree.rightPreOrder { result.add(it) }

        assertEquals(50, result.first())
    }

    @Test
    fun `левый обратный обход BST возвращает корень последним`() {
        tree.insert(50)
        tree.insert(30)
        tree.insert(70)
        tree.insert(20)
        tree.insert(40)

        val result = mutableListOf<Int>()
        tree.leftPostOrder { result.add(it) }

        assertEquals(50, result.last())
    }

    @Test
    fun `правый обратный обход BST возвращает корень последним`() {
        tree.insert(50)
        tree.insert(30)
        tree.insert(70)
        tree.insert(20)
        tree.insert(40)

        val result = mutableListOf<Int>()
        tree.rightPostOrder { result.add(it) }

        assertEquals(50, result.last())
    }

    @Test
    fun `все обходы посещают каждый элемент ровно один раз`() {
        tree.insert(50)
        tree.insert(30)
        tree.insert(70)
        tree.insert(20)
        tree.insert(40)

        val expected = listOf(50, 30, 70, 20, 40)

        val leftPreOrder = mutableListOf<Int>()
        tree.leftPreOrder { leftPreOrder.add(it) }

        val leftPostOrder = mutableListOf<Int>()
        tree.leftPostOrder { leftPostOrder.add(it) }

        val rightPreOrder = mutableListOf<Int>()
        tree.rightPreOrder { rightPreOrder.add(it) }

        val rightPostOrder = mutableListOf<Int>()
        tree.rightPostOrder { rightPostOrder.add(it) }

        assertEquals(expected.size, leftPreOrder.size)
        assertEquals(expected.size, leftPostOrder.size)
        assertEquals(expected.size, rightPreOrder.size)
        assertEquals(expected.size, rightPostOrder.size)

        assertEquals(expected.sorted(), leftPreOrder.sorted())
        assertEquals(expected.sorted(), leftPostOrder.sorted())
        assertEquals(expected.sorted(), rightPreOrder.sorted())
        assertEquals(expected.sorted(), rightPostOrder.sorted())
    }

    @Test
    fun `переданный action используется при обходе`() {
        tree.insert(10)
        tree.insert(5)
        tree.insert(15)

        val result = mutableListOf<String>()
        tree.leftPreOrder { result.add("значение: $it") }

        assertEquals(
            listOf("значение: 10", "значение: 5", "значение: 15"),
            result
        )
    }


    // 6. In-order обход BST

    @Test
    fun `in-order обход BST возвращает элементы по возрастанию`() {
        tree.insert(50)
        tree.insert(30)
        tree.insert(70)
        tree.insert(20)
        tree.insert(40)

        val result = mutableListOf<Int>()
        tree.inOrder { result.add(it) }

        assertEquals(listOf(20, 30, 40, 50, 70), result)
    }

    @Test
    fun `in-order обход пустого BST возвращает пустой список`() {
        val result = mutableListOf<Int>()
        tree.inOrder { result.add(it) }

        assertTrue(result.isEmpty())
    }
}