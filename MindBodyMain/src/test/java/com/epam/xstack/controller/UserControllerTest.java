package com.epam.xstack.controller;

import com.epam.xstack.service.UserService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Mock
    private UserService userService;


    @InjectMocks
    private UserController userController;

//    @Test
//    void login_Successful() {
//        UserDTO userDTO = mock(UserDTO.class); // Create a mock UserDTO
//        User mockUser = mock(User.class); // Create a mock User
//
//        when(userService.login(userDTO)).thenReturn(Optional.of(mockUser));
//
//        ResponseEntity<?> response = userController.login(any(HttpServletResponse.class), any(HttpServletRequest.class), userDTO);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals("Login successful.", response.getBody());
//
////        verify(userDetailsService).setCurrentUser(mockUser);
//    }
//
//    @Test
//    void login_UserNotFound() {
//        UserDTO userDTO = mock(UserDTO.class); // Create a mock UserDTO
//
//        when(userService.login(userDTO)).thenReturn(Optional.empty());
//
//        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> userController.login(any(HttpServletResponse.class), any(HttpServletRequest.class), userDTO));
//
//        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
//    }
//
//    @Test
//    void changePassword_Successful() {
//        PasswordChangeDTO passwordChangeDTO = new PasswordChangeDTO("user", "oldPassword", "newPassword");
//
//        when(userService.changePassword("user", "oldPassword", "newPassword")).thenReturn(true);
//
//        ResponseEntity<?> response = userController.changePassword(passwordChangeDTO);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals("Password changed successfully", response.getBody());
//    }
//
//    @Test
//    void changePassword_Failed() {
//        PasswordChangeDTO passwordChangeDTO = new PasswordChangeDTO("user", "oldPassword", "newPassword");
//
//        when(userService.changePassword("user", "oldPassword", "newPassword")).thenReturn(false);
//
//        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> userController.changePassword(passwordChangeDTO));
//
//        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
//    }

}
