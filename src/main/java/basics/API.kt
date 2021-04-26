package basics

import generics.Failure
import generics.Response
import generics.Success

class StudentController(
    val studentRepository: StudentRepository,
    val analyticsRepository: AnalyticsRepository
) {

    @GetMapping("/student/{id}")
    fun getUser(@PathVariable id: Long): StudentAPI = when (val response = studentRepository.findStudentResult(id)) {
        is Failure -> throw ApiError(code = 400, message = "No user with id $id")
        is Success -> {
            analyticsRepository.setStudentByIdCount(id, analyticsRepository.getStudentByIdCount(id) + 1)
            response.value.toApi()
        }
    }

    @GetMapping("/student")
    fun getUsers(): List<StudentAPI> = studentRepository.getAllStudents().map { it.toApi() }.sortedBy { it.surname }

    private fun StudentEntity.toApi() = StudentAPI(name = firstName, surname = lastName)
}

data class StudentAPI(
    val name: String,
    val surname: String
)

@Entity
data class StudentEntity(
    @Id @GeneratedValue
    val id: Long = -1,
    val firstName: String,
    val lastName: String
)

interface StudentRepository {

    fun findStudent(id: Long): StudentEntity?
    fun findStudentResult(id: Long): Response<StudentEntity, NotFoundException>
    fun getAllStudents(): List<StudentEntity>
}

object NotFoundException : Throwable()

interface AnalyticsRepository {

    fun getStudentByIdCount(id: Long): Int
    fun setStudentByIdCount(id: Long, count: Int)
}

data class ApiError(val code: Int, override val message: String) : Throwable(message)

annotation class Entity
annotation class Id
annotation class GeneratedValue
annotation class GetMapping(val path: String)
annotation class PathVariable