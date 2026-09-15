class BinarySearchTree <T:Comparable<T>> {
    var root: Node<T> ?= null

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

    fun insert(value:T){
        root = insertRec(root, value)
    }

    private fun searchRec(current: Node<T>?, value: T): Boolean{

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

    fun search(value: T): Boolean{
        return searchRec(root, value)
    }

    private fun deletRec(current: Node<T>?, value: T): Node<T>?{

        if (current == null) return null

        when {

            value < current.value -> current.left = deletRec(current.left, value)
            value > current.value -> current.right = deletRec(current.right, value)

            else  -> {
                // узел найден
                if (current.left == null) return current.right
                if (current.right == null) return current.left

                // Если узел имеет двух потомков: находим наимменьший элемент в правом поддереве
                current.value = findMin(current.right!!)
                current.right = deletRec(current.right, current.value)
            }
        }
        return current
    }

    fun delete(value: T){
        root = deletRec(root, value)
    }

    private fun findMin(node: Node<T>): T{

        var current = node

        while (current.left != null){
            current = current.left!!
        }

        return current.value
    }
}