package org.example.springprojektzespolowy.repositories;

import org.example.springprojektzespolowy.models.User;
import org.example.springprojektzespolowy.repositories.userRepos.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("Should find all users with their groups")
    void findAllWithGroups() {
        // given
        User user1 = new User("uid1", "User1", "user1@example.com", "Country1", LocalDate.of(2002,2,2), 2L);
        User user2 = new User("uid2", "User2", "user2@example.com", "Country2", LocalDate.of(2002,2,2), 2L);

        entityManager.persist(user1);
        entityManager.persist(user2);
        entityManager.flush();

        // when
        List<User> users = userRepository.findAllWithGroups();

        // then
        assertThat(users).hasSize(2);
        assertThat(users.get(0).getUId()).isEqualTo("uid1");
        assertThat(users.get(1).getUId()).isEqualTo("uid2");
    }

    @Test
    @DisplayName("Should find user with groups by UId")
    void findUserWithGroupsByUId() {
        // given
        User user = new User("test-uid", "TestUser", "test@example.com", "TestCountry", LocalDate.of(2002,2,2), 2L);
        entityManager.persist(user);
        entityManager.flush();

        // when
        User foundUser = userRepository.findUserWithGroupsByUId("test-uid");

        // then
        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getUId()).isEqualTo("test-uid");
        assertThat(foundUser.getName()).isEqualTo("TestUser");
    }

    @Test
    @DisplayName("Should check if user exists by UId")
    void existsUserByUId() {
        // given
        User user = new User("exist-uid", "ExistUser", "exist@example.com", "ExistCountry", LocalDate.of(2002,2,2), 2L);
        entityManager.persist(user);
        entityManager.flush();

        // when
        boolean exists = userRepository.existsUserByUId("exist-uid");
        boolean notExists = userRepository.existsUserByUId("non-existent-uid");

        // then
        assertThat(exists).isTrue();
        assertThat(notExists).isFalse();
    }

    @Test
    @DisplayName("Should find user by UId")
    void findByUId() {
        // given
        User user = new User("find-uid", "FindUser", "find@example.com", "FindCountry", LocalDate.of(2002,2,2), 2L);
        entityManager.persist(user);
        entityManager.flush();

        // when
        User foundUser = userRepository.findByUId("find-uid");

        // then
        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getUId()).isEqualTo("find-uid");
        assertThat(foundUser.getName()).isEqualTo("FindUser");
    }

    @Test
    @DisplayName("Should delete user by UId")
    void deleteUserByUId() {
        // given
        User user = new User("delete-uid", "DeleteUser", "delete@example.com", "DeleteCountry", LocalDate.of(2002,2,2), 2L);
        entityManager.persist(user);
        entityManager.flush();

        assertThat(userRepository.existsUserByUId("delete-uid")).isTrue();

        // when
        userRepository.deleteUserByUId("delete-uid");
        entityManager.flush();

        // then
        assertThat(userRepository.existsUserByUId("delete-uid")).isFalse();
    }
}
