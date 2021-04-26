package collections

fun List<Student>.makeBestStudentsList(): String = asSequence()
	.filter { it.result >= 50 }
	.filter { it.pointsInSemester > 15 }
	.sortedByDescending { it.result }
	.withIndex()
	.map { (index, student) ->
		StudentWithPrize(student.name, student.surname, when (index) {
			0 -> "$5000"
			1, 2, 3 -> "$3000"
			else -> "$1000"
		})
	}
	.sortedWith(compareBy({ it.surname }, { it.name }))
	.joinToString(separator = "\n") { (name, surname, cashPrize) ->
		"$name $surname, $cashPrize"
	}

private data class StudentWithPrize(
	val name: String,
	val surname: String,
	val cashPrize: String
)
