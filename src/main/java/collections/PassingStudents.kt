package collections

fun List<Student>.makePassingStudentsListText(): String = asSequence()
	.filter { it.result >= 50 }
	.filter { it.pointsInSemester > 15 }
	.sortedWith(compareBy({ it.surname }, { it.name }))
	.joinToString(separator = "\n") { (name, surname, result, _) ->
		"$name $surname, $result"
	}