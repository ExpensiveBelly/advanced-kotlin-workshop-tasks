package collections

// TODO: Quick sort should take first element (pivot), then split rest to bigger then pivot and smaller and finally return
// first smaller sorted, then pivot and finally bigger sorted
fun <T: Comparable<T>> List<T>.quickSort(): List<T> {
	val pivot = firstOrNull() ?: return emptyList()
	val (smaller, greater) = minus(pivot).partition { it < pivot }
	return smaller.quickSort() + pivot + greater.quickSort()
}