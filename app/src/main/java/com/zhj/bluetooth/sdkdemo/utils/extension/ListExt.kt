package com.zhj.bluetooth.sdkdemo.utils.extension

inline fun <reified T> List<T>.updateOne(finder: (T) -> Boolean, action: T.() -> Unit) = indexOfFirst(finder).let { index ->
    val newValue = this[index].deepCopy().apply { action(this) }
    if (index >= 0) copy(index, newValue) else this
}

inline fun <reified T> List<T>.updateAll(finder: (T) -> Boolean, action: T.() -> Unit) =
    this.map {
        if(finder(it)) {
            it.deepCopy().apply { action(this) }
        } else {
            it
        }
    }

inline fun <reified T> List<T>.removeOne(finder: (T) -> Boolean) = indexOfFirst(finder).let { index ->
    this.toMutableList().apply { removeAt(index) }.toList()
}

inline fun <reified T> List<T>.removeAll(finder: (T) -> Boolean) =
    this.filter {
        !finder(it)
    }

inline fun <reified T> List<T>.addFirst(obj: T) = this.toMutableList().apply { add(0, obj) }.toList()
inline fun <reified T> List<T>.addLast(obj: T) = this.toMutableList().apply { add(obj) }.toList()

fun <T> List<T>.copy(i: Int, value: T): List<T> = toMutableList().apply { set(i, value) }

inline fun <T, E> Iterable<T>.convert(action: (T) -> E): MutableList<E> {
    val list: MutableList<E> = mutableListOf()
    for (element in this) list.add(action(element))
    return list
}
