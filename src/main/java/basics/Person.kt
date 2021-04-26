package basics

interface Person {
	val name: String
	val age: Int
	val canBuyAlcohol: Boolean

	fun helloText(): String

	fun cheerText(person: Person): String
}

class Businessman(
	override val name: String,
	override val age: Int,
	override val canBuyAlcohol: Boolean = age >= 21
): Person {
	override fun helloText() = "Good morning"

	override fun cheerText(person: Person) = "Hello, my name is $name, nice to see you ${person.name}"
}

class Student(
	override val name: String,
	override val age: Int,
	override val canBuyAlcohol: Boolean = age >= 21
): Person {
	override fun helloText() = "Hi"

	override fun cheerText(person: Person) = "Hey ${person.name}, I am $name"
}

fun main(args: Array<String>) {
	val businessman: Person = TODO("Use Businessman constructor here once it is implemented")
	val student: Person = TODO("Use Student constructor here once it is implemented")

	println(businessman.helloText())
	println(student.helloText())

	println(businessman.cheerText(student))
	println(student.cheerText(businessman))

	fun sayIfCanBuyAlcohol(person: Person) {
		val modal = if (person.canBuyAlcohol) "can" else "can't"
		println("${person.name} $modal buy alcohol")
	}

	sayIfCanBuyAlcohol(businessman)
	sayIfCanBuyAlcohol(student)
}