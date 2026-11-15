class BinarySearchTree <T:Comparable<T>> {

    var root: Node<T>? = null


    fun insert(value:T){
        root = insertRec(root, value)
    }

    // меньшие - нвлево
    // большее - направо
    // идёт рекурсивно, пока не наткнётся на null
    private fun insertRec(current: Node<T>?, value: T):Node<T>{

        // если текущий узел пустое место - создаём новый узел с нашим значением и возвращаем его
        if (current == null) return Node(value)

        when {

            // если новое значение значение меньеш текущего, рекурсивно вызываем нашу функцию для левого поддерева
            // и присваеваем результат в лево
            value < current.value -> current.left = insertRec(current.left, value)

            // если новое больше текущего, то присваиваем результат вправо
            value > current.value -> current.right = insertRec(current.right, value)

            else ->  return current
        }
        return current
    }

    fun search(value: T): Boolean{
        return searchRec(root, value)
    }

    private fun searchRec(current: Node<T>?, value: T): Boolean {

        // если элемента нету в дереве - то возвращаем false
        if (current == null) return false

        // если элемент совпадает с текущем узлом - возвращаем true
        if (value == current.value) return true

        // если значение меньше текущего, ищем в левом поддереве. Если больше — в правом
        return if (value < current.value)
            searchRec(current.left, value)
        else
            searchRec(current.right, value)
    }

    fun delete(value: T){
        root = deletRec(root, value)
    }

    private fun deletRec(node: Node<T>?, value: T): Node<T>?{

        if (node == null) return null

        when {

            // блок поиска, есть ли удаляемый элемент в дереве

            // если наше удаляемое значение меньше корня - то значит оно в левом поддерве
            value < node.value -> {
                node.left = deletRec(node.left, value)
            }  // рекрсивно - потому что само поддерево является бинарным деревом

           // если больше - значит в правом поддереве
            value > node.value -> {
                node.right = deletRec(node.right, value)
            }

            // тут мы уже нашли наш удаляемый элемент
            else  -> {

                // нет левого потомка - возвращаем правого
                if (node.left == null) return node.right

                // нет правого потомка - возвращаем левого
                if (node.right == null) return node.left

                // Есть два потомка.
                // Находим минимальное значение справа
                val minValue = findMin(node.right!!)

                // Заменяем значение удаляемого узла
                node.value = minValue

                // Удаляем старую копию минимального значения
                node.right = deletRec(node.right, minValue)
            }
        }

        // возвращаем наше дерево после удаления
        return node
    }

    // ищим самое маленькое число в правой части дерева
    private fun findMin(node: Node<T>): T{

        var current = node

        // пока слева есть элемент - идём дальше левее
        while (current.left != null){
            current = current.left!!
        }

        // когда слева элементов больше нет - мы нашли самое маленькое
        return current.value
    }

    // левый прямой обход
    // action - функция, который принимает тип T и ничего не возвращает
    // = :: println - если вызвали и не переделали никакого параметра, то атоматически используется простой println
    fun leftPreOrder(action: (T) -> Unit = ::println) {
        fun traverse(node: Node<T>?) {
            if (node == null) return

            action(node.value)
            traverse(node.left)
            traverse(node.right)
        }
        traverse(root)
    }

    // левый обратный обход
    fun leftPostOrder(action: (T) -> Unit = ::println) {
        fun traverse(node: Node<T>?) {
            if (node == null) return

            traverse(node.left)
            traverse(node.right)
            action(node.value)
        }
        traverse(root)
    }

    // правый прямой обход
    // action - функция, который принимает тип T и ничего не возвращает
    // = :: println - если вызвали и не переделали никакого параметра, то атоматически используется простой println
    fun rightPreOrder(action: (T) -> Unit = ::println) {
        fun traverse(node: Node<T>?) {
            if (node == null) return

            action(node.value)
            traverse(node.right)
            traverse(node.left)
        }
        traverse(root)
    }

    // праывй обратный обход
    fun rightPostOrder(action: (T) -> Unit = ::println) {
        fun traverse(node: Node<T>?) {
            if (node == null) return

            traverse(node.right)
            traverse(node.left)
            action(node.value)
        }
        traverse(root)
    }

    fun inOrder(action: (T) -> Unit = ::println) {
        fun traverse(node: Node<T>?) {
            if (node == null) return

            traverse(node.left)
            action(node.value)
            traverse(node.right)
        }

        traverse(root)
    }
}