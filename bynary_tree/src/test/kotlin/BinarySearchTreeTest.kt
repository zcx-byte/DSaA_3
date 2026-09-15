import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

// Класс узла, необходимый для работы вашего кода
class Node<T>(
    var value: T,
    var left: Node<T>? = null,
    var right: Node<T>? = null
)

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
}