package solutions.digamma.damas.jcr.user

import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import solutions.digamma.damas.common.InvalidArgumentException
import solutions.digamma.damas.common.NotFoundException
import solutions.digamma.damas.jcr.RepositoryTest
import solutions.digamma.damas.user.Group
import solutions.digamma.damas.user.GroupManager
import solutions.digamma.damas.user.User
import java.util.Arrays

class JcrUserManagerTest: RepositoryTest() {

    private val manager = inject(JcrUserManager::class.java)

    @Before
    fun setUp() {
        this.login()
    }

    @After
    fun tearDown() {
        this.logout()
    }

    @Test
    fun create() {
        val user = Mockito.mock(User::class.java)
        Mockito.`when`(user.login).thenReturn("tester")
        Mockito.`when`(user.firstName).thenReturn("Mister")
        Mockito.`when`(user.lastName).thenReturn("Tester")
        val entity = this.manager.create(user)
        assert(user.login == entity.login)
        assert(user.firstName == entity.firstName)
        assert(user.lastName == entity.lastName)
        this.manager.delete(entity.id)
    }

    @Test
    fun update() {
        val user = Mockito.mock(User::class.java)
        Mockito.`when`(user.login).thenReturn("tester")
        Mockito.`when`(user.firstName).thenReturn("Mister")
        Mockito.`when`(user.lastName).thenReturn("Tester")
        val id = this.manager.create(user).id
        Mockito.`when`(user.firstName).thenReturn("Master")
        Mockito.`when`(user.lastName).thenReturn("Taster")
        this.manager.update(id, user)
        val entity = this.manager.retrieve(id)
        assert(user.login == entity.login)
        assert(user.firstName == entity.firstName)
        assert(user.lastName == entity.lastName)
        this.manager.delete(id)
    }

    @Test
    fun updateGroups() {
        /* Set up groups */
        val gm = inject(GroupManager::class.java)
        val group = Mockito.mock(Group::class.java)
        val testers = "testers"
        val devops = "devops"
        Mockito.`when`(group.name).thenReturn(testers)
        gm.create(group)
        Mockito.`when`(group.name).thenReturn(devops)
        gm.create(group)

        /* Set up user */
        var user = Mockito.mock(User::class.java)
        Mockito.`when`(user.login).thenReturn("tester")
        val id = this.manager.create(user).id

        /* Add */
        user = Mockito.mock(User::class.java)
        Mockito.`when`(user.memberships).thenReturn(
                Arrays.asList(testers, devops)
        )
        this.manager.update(id, user)
        assert(this.manager.retrieve(id).memberships == user.memberships)

        /* Remove */
        user = Mockito.mock(User::class.java)
        Mockito.`when`(user.memberships).thenReturn(
                Arrays.asList(testers)
        )
        this.manager.update(id, user)
        assert(this.manager.retrieve(id).memberships == user.memberships)

        /* Add back */
        user = Mockito.mock(User::class.java)
        Mockito.`when`(user.memberships)
                .thenReturn(Arrays.asList(testers, devops))
        this.manager.update(id, user)
        assert(this.manager.retrieve(id).memberships == user.memberships)

        /* Tear down user */
        this.manager.delete(id)

        /* Tear down groups */
        gm.delete(testers)
        gm.delete(devops)
    }

    @Test
    fun retrieve() {
        val user = Mockito.mock(User::class.java)
        Mockito.`when`(user.login).thenReturn("tester")
        Mockito.`when`(user.firstName).thenReturn("Mister")
        Mockito.`when`(user.lastName).thenReturn("Tester")
        val id = this.manager.create(user).id
        val entity = this.manager.retrieve(id)
        assert(user.login == entity.login)
        assert(user.firstName == entity.firstName)
        assert(user.lastName == entity.lastName)
        this.manager.delete(id)
    }

    @Test
    fun delete() {
        val user = Mockito.mock(User::class.java)
        Mockito.`when`(user.login).thenReturn("tester")
        val id = this.manager.create(user).id
        this.manager.delete(id)
        try {
            this.manager.retrieve(id)
            assert(false)
        } catch (_: NotFoundException) {
            assert(true)
        }
    }

    @Test
    fun find() {
        val user = Mockito.mock(User::class.java)
        Mockito.`when`(user.login).thenReturn("tester")
        val id = this.manager.create(user).id
        this.commit()
        val page = this.manager.find()
        assert(page.total == 1)
        assert(page.size == 1)
        assert(page.objects.size == 1)
        val entity = page.objects.iterator().next()
        assert(user.login == entity.login)
        this.manager.delete(id)
    }

    @Test
    fun updatePassword() {
        val user = Mockito.mock(User::class.java)
        Mockito.`when`(user.login).thenReturn("tester")
        val id = this.manager.create(user).id
        this.manager.updatePassword(id, "P@55w0rd")
        try {
            /** Password too short */
            this.manager.updatePassword(id, "pass")
            assert(false)
        } catch (_: InvalidArgumentException) {
            assert(true)
        }
        this.manager.delete(id)
    }
}
