class ArrBinaryTree<T: Comparable<T>> {

    private val elements = mutableListOf<T>()

    val size: Int
        get() = elements.size

    // в полном бинарном дереве
    // новый элемент всегда добавляется в конец массива
    fun add(value: T){
        elements.add(value)
    }

    // удаление элемента по значению
    fun remove(value: T): Boolean{

        val index = elements.indexOf(value)

        if (index == -1){
            return false
        }

        val lastIndex = elements.size - 1

        // если удаляемый элемент не является последним,
        // заменяем его последним элементом массива
        if (index != lastIndex){
            elements[index] = elements[lastIndex]
        }

        // Удаляем последний элемент
        // (который теперь продублирован или был целевым)
        elements.removeAt(lastIndex)
        return true

    }

    // возврат элемента по индексу
    fun get(index: Int): T? {
        if (index < 0 || index >= elements.size) {
            return null    // индекс выходит за границы массива
        }
        return elements[index]
    }

    // вспомогательный метод для просеивания элемента вниз
    // св-во максимальной кучи - родитель всегда больше своих потомков
    private fun siftDown(start: Int, end: Int){

        var root = start

        while (true){
            val leftChild = 2 * root + 1
            val rightChild = leftChild + 1
            var largest = root

            // если левый потомок сущесвтует и больше корня
            if (leftChild < end && elements[leftChild] > elements[largest]) {
                largest = leftChild
            }

            // если правый потомок сущетсвует и больше текущего наибольшего
            if (rightChild < end && elements[rightChild] > elements[largest]){
                largest = rightChild
            }

            // если больший элемент не является корнем меняей их местами
            if (largest != root){

                swap(root, largest)

                // продолжаем просеивание для нового положения элемента
                root = largest
            } else {
                break
            }

        }
    }

    // вспомогательный метод смены элементов местами
    private fun swap(i: Int, j: Int){
        if ( i == j ) return

        val temp = elements[i]
        elements[i] = elements[j]
        elements[j] = temp
    }

    // сортировка массива (турнирная сортировка)
    fun tournamentSort() {
        if (elements.size <= 1) return

        val n = elements.size

        for (i in n / 2 - 1 downTo 0) {
            siftDown(i, n)
        }

        // последовательно извлекаем каждый эдлемент из кучи
        for (i in n - 1 downTo 1) {

            // меняем местами корень (максимальнй элемент) с
            // последним элементом текущей кучи
            swap(0, i)

            // восстанавливаем св-во кучи для оставшейся части массива
            siftDown(0, i)
        }
    }

    // пирамидальная сортировка
    fun heapsort() {
        val n = elements.size

        if (n <= 1) return

        // построение максимальной кучи
        for (i in n / 2 - 1 downTo 0) {
            siftDown(i, n)
        }

        // извлечение максимального элемента из кучи
        for (i in n - 1 downTo 1) {
            swap(0, i)
            siftDown(0, i)
        }
    }
}