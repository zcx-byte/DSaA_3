class BynaryTree <T:Comparable<T>> {

    var root: Node<T>? = null

    // вставка в бинарное дерево
    fun insert(value: T) {

        val newNode = Node(value)

        if (root == null) {
            root = newNode
            return
        }

        // создаётся очередь, в который помещаем корневой узел
        // очередь будет хранить узлы, которые ещё предстоит проверить на наличие свободных
        // дочерних позиций
        val queue = ArrayDeque<Node<T>>()
        queue.add(root!!)

        // выполняется до тех пор, пока в очереди есть узлы для проверки
        while (queue.isNotEmpty()) {

            // извлекаем первый узел из очереди
            val current = queue.removeFirst()

            // проверяется левый потомок
            if (current.left == null) {

                // если нет, то узел записывается туда
                current.left = newNode
                return      // прекращаем выполнение функции
            } else {

                //если существует - добавляем его в очередь
                queue.add(current.left!!)
            }

            // тоже самое с правым потомком
            if (current.right == null) {
                current.right = newNode
                return
            } else {
                queue.add(current.right!!)
            }
        }
    }

    fun delete(value: T) {
        if (root == null) {
            return
        }

        // Особый случай: удаляем единственный элемент (корень без потомков)
        if (root!!.left == null && root!!.right == null) {
            if (root!!.value == value) {
                root = null
            }
            return
        }

        // Используем BFS для поиска удаляемого узла и последнего узла
        val queue = ArrayDeque<Node<T>>()
        queue.add(root!!)

        //Эта переменная хранит ссылку на узел, значение которого совпадает с удаляемым.
        var nodeToDelete: Node<T>? = null

        //Эта переменная хранит ссылку на последний узел в дереве — самый правый узел на самом нижнем уровне.
        var lastNode: Node<T>? = null

        //Эта переменная хранит ссылку на родителя последнего узла.
        var parentOfLast: Node<T>? = null

        while (queue.isNotEmpty()) {
            val current = queue.removeFirst()

            lastNode = current

            // Проверяем, является ли текущий узел удаляемым
            if (current.value == value) {
                nodeToDelete = current
            }

            // Добавляем потомков в очередь и запоминаем родителя последнего узла
            if (current.left != null) {
                parentOfLast = current
                queue.add(current.left!!)
            }
            if (current.right != null) {
                parentOfLast = current
                queue.add(current.right!!)
            }
        }

        // Если удаляемый элемент не найден, ничего не делаем
        if (nodeToDelete == null) {
            return
        }

        // Копируем значение последнего узла в удаляемый узел
        nodeToDelete.value = lastNode!!.value

        // Удаляем последний узел из дерева
        if (parentOfLast != null) {
            if (parentOfLast.right == lastNode) {
                parentOfLast.right = null
            } else {
                parentOfLast.left = null
            }
        }
    }

    // левый прямой обход
    private fun leftPreOrderRec(node: Node<T>?, result: MutableList<T>){
        if (node == null) return

        result.add(node.value)
        leftPreOrderRec(node.left, result) // левое поддерево
        leftPreOrderRec(node.right, result) // правое поддерево
    }


    fun leftPreOrder(): List<T> {
        val res = mutableListOf<T>()
        leftPreOrderRec(root, res)
        return res
    }

    // левый обратный обход
    private fun leftPostOrderRec(node: Node<T>?, result: MutableList<T>){
        if (node == null) return

        leftPostOrderRec(node.left, result) // левое поддерево
        leftPostOrderRec(node.right, result) // правое поддерево
        result.add(node.value)
    }

    fun leftPostOrder(): List<T> {
        val res = mutableListOf<T>()
        leftPostOrderRec(root, res)
        return res
    }

    // правый прямой обход
    private fun rightPreOrderRec(node: Node<T>?, result: MutableList<T>){
        if (node == null) return

        result.add(node.value)
        rightPreOrderRec(node.right, result)
        rightPreOrderRec(node.left, result)
    }

    fun rightPreOrder(): List<T>{
        val res = mutableListOf<T>()

        rightPreOrderRec(root, res)
        return res
    }

    // правый обратный обход
    private fun rightPostOrderRecursive(node: Node<T>?, result: MutableList<T>){
        if (node == null) return

        rightPostOrderRecursive(node.right, result)
        rightPostOrderRecursive(node.left, result)
        result.add(node.value)
    }

    fun rightPostOrder(): List<T>{
        val res =mutableListOf<T>()
        rightPostOrderRecursive(root, res)
        return res
    }

    // обход в ширину
    fun bfs(): List<T>{
        val result = mutableListOf<T>()

        if (root == null) return result

        val queue = ArrayDeque<Node<T>>()
        queue.add(root!!)

        while (queue.isNotEmpty()){
            val current = queue.removeFirst()
            result.add(current.value)

            if (current.left != null){
                queue.add(current.left!!)
            }

            if (current.right != null) {
                queue.add(current.right!!)
            }
        }
        return result
    }

    // обратный обход в ширину
    // обход дерева по уровням, но в противоположном направлении: снизу вверх, справа налево
    fun Revbfs(): List<T>{

        val result = mutableListOf<T>()

        if (root == null) return result

        val queue = ArrayDeque<Node<T>>()
        queue.add(root!!)

        // список для хранения уровней (каждый урорвень - отдельный список)
        val level = mutableListOf<MutableList<T>>()

        while (queue.isNotEmpty()){
            val levelSize = queue.size
            val currentLevel = mutableListOf<T>()

            // обычный оюход в ширину, но с разделением на уровни

            for (i in 0 until levelSize){
                val current = queue.removeFirst()
                currentLevel.add(current.value)


                if (current.left != null){
                    queue.add(current.left!!)
                }

                if (current.right != null) {
                    queue.add(current.right!!)
                }
            }

            level.add(currentLevel)
        }

        // обход уровней в обратном порядке (снизу вверх)
        // проходит по списку уровней в обратном порядке - от последнего (листья) к первому (корень).
        for (i in level.size - 1 downTo 0){

            // обход узлов внутри уровня в обратном порядке (справа налево)
            for (j in level[i].size - 1 downTo 0){
                result.add(level[i][j])
            }
        }
        return result
    }


}
