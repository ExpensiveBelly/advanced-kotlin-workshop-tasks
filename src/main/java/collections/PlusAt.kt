package collections

fun <T> List<T>.plusAt(index: Int, element: T): List<T> =
	takeIf { index in (0..size) }?.toMutableList()?.apply { add(index, element) } ?: throw IllegalArgumentException()