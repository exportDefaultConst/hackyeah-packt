package org.example.springprojektzespolowy.services;

import org.example.springprojektzespolowy.dto.mappers.UserDtoMapper;
import org.example.springprojektzespolowy.dto.userDto.UserDto;
import org.example.springprojektzespolowy.models.User;
import org.example.springprojektzespolowy.repositories.userRepos.UserRepository;
import org.example.springprojektzespolowy.services.userServices.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {


    @Mock
    private UserRepository userRepository;

    @Mock
    private UserDtoMapper userDtoMapper;

    @Mock
    private GroupService groupService;

    @InjectMocks
    private UserService userService;

    private User testUser;
    private UserDto testUserDto;

    @BeforeEach
    void setUp(){
        testUser = new User("b8aY27S9hgfYpizkjfoAlxoKR8G2","Wiktor","wiktor@wp.pl","poland", LocalDate.of(2002,2,2), 2L);
        testUserDto = new UserDto(1L,"b8aY27S9hgfYpizkjfoAlxoKR8G2","Wiktor","wiktor@wp.pl","poland", LocalDate.of(2000,10,21),1L);
    }


    @Test
    @DisplayName("Zwraca UserDto przez UId")
    void getUserByUId(){
        when(userRepository.findByUId("b8aY27S9hgfYpizkjfoAlxoKR8G2")).thenReturn(testUser);

        UserDto userDto = userService.getUserDtoByUId("b8aY27S9hgfYpizkjfoAlxoKR8G2");

        assertThat(userDto).isEqualTo(testUserDto);
        verify(userRepository).findByUId("b8aY27S9hgfYpizkjfoAlxoKR8G2");

    }

}
