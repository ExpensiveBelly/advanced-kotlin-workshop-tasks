package basics

import assertThrows
import generics.Failure
import generics.Response
import generics.Success
import org.junit.Assert.assertEquals
import org.junit.Test

class APITest {

    @Test
    fun `When asked for a user, correct user is returned`() {
        val studentsRepo = FakeStudentRepository()
        val analyticsRepository = FakeAnalyticsRepository()
        val controller = StudentController(studentsRepo, analyticsRepository)

        assertEquals(StudentAPI("Alec", "Strong"), controller.getUser(1))
        assertEquals(StudentAPI("Jordan", "Peterson"), controller.getUser(2))
        assertEquals(StudentAPI("Joe", "Regan"), controller.getUser(3))
        assertEquals(StudentAPI("Adam", "Savage"), controller.getUser(4))
    }

    @Test
    fun `When asked for a user that does not exist, correct error is thrown`() {
        val studentsRepo = FakeStudentRepository()
        val analyticsRepository = FakeAnalyticsRepository()
        val controller = StudentController(studentsRepo, analyticsRepository)

        val err = assertThrows<ApiError>("No user with id 10") { controller.getUser(10) }
        assertEquals("Error code should be 400", 400, err.code)
        assertThrows<ApiError>("No user with id 0") { controller.getUser(0) }
    }

    @Test
    fun `When asked for a user, ananlytics counter is bumped`() {
        val studentsRepo = FakeStudentRepository()
        val analyticsRepository = FakeAnalyticsRepository()
        val controller = StudentController(studentsRepo, analyticsRepository)

        assertEquals(0, analyticsRepository.getStudentByIdCount(1))
        controller.getUser(1)
        assertEquals(1, analyticsRepository.getStudentByIdCount(1))
        controller.getUser(1)
        controller.getUser(1)
        assertEquals(3, analyticsRepository.getStudentByIdCount(1))

        assertEquals(0, analyticsRepository.getStudentByIdCount(4))
        controller.getUser(4)
        assertEquals(1, analyticsRepository.getStudentByIdCount(4))
        assertEquals(3, analyticsRepository.getStudentByIdCount(1))
    }

    @Test
    fun `When asked for users, all users are returned and they are sorted by surname`() {
        val studentsRepo = FakeStudentRepository()
        val analyticsRepository = FakeAnalyticsRepository()
        val controller = StudentController(studentsRepo, analyticsRepository)

        val ret = controller.getUsers()
        val expected = listOf(
            StudentAPI("Jordan", "Peterson"),
            StudentAPI("Joe", "Regan"),
            StudentAPI("Adam", "Savage"),
            StudentAPI("Alec", "Strong")
        )
        assertEquals(expected, ret)
    }

    class FakeStudentRepository: StudentRepository {
        private val students = listOf(
            StudentEntity(1, "Alec", "Strong"),
            StudentEntity(2, "Jordan", "Peterson"),
            StudentEntity(3, "Joe", "Regan"),
            StudentEntity(4, "Adam", "Savage")
        )

        override fun findStudent(id: Long): StudentEntity? =
            students.firstOrNull { it.id == id }

        override fun findStudentResult(id: Long): Response<StudentEntity, NotFoundException> {
            return students.firstOrNull { it.id == id }
                ?.let { Success(it) }
                ?: Failure(NotFoundException)
        }


        override fun getAllStudents(): List<StudentEntity> =
            students
    }

    class FakeAnalyticsRepository: AnalyticsRepository {
        // Maps id to count
        private val counter: MutableMap<Long, Int> = mutableMapOf(1L to 0, 2L to 3, 3L to 5, 4L to 0)

        override fun getStudentByIdCount(id: Long): Int = counter[id] ?: throw Error("No user with such ID")

        override fun setStudentByIdCount(id: Long, count: Int) {
            counter[id] = count
        }
    }
}