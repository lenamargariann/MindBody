package com.epam.xstack.service.impl;

import com.epam.xstack.dao.impl.TraineeDaoImpl;
import com.epam.xstack.model.Trainee;
import com.epam.xstack.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TraineeServiceImplTest {

    @InjectMocks
    private TraineeServiceImpl traineeService;

    @Mock
    private TraineeDaoImpl traineeDao;

    @Mock
    private UserServiceImpl userService;

//    @Test
//    public void testUpdate() {
//        Trainee trainee = new Trainee();
//
//        traineeService.update(trainee);
//
//        verify(traineeDao, times(1)).update(trainee);
//    }

    @Test
    public void testCreate() {
        LocalDate dateOfBirth = LocalDate.now();
        String address = "Some Address";
        String firstname = "John";
        String lastname = "Doe";

        when(userService.create(firstname, lastname, "gsdyfnhkmlA")).thenReturn(new User());
        when(traineeDao.create(any())).thenReturn(Optional.of(new Trainee()));

//        traineeService.create(dateOfBirth);

        verify(traineeDao, times(1)).create(any());
        verify(userService, times(1)).create(firstname, lastname, "fcgfvhjA `");
    }

//    @Test
//    public void testDeleteSuccess() {
//        Trainee trainee = new Trainee();
//
//        when(traineeDao.delete(trainee)).thenReturn(true);
//
//        traineeService.delete(trainee);
//
//        verify(traineeDao, times(1)).delete(trainee);
//    }
//
//    @Test
//    public void testDeleteFailure() {
//        Trainee trainee = new Trainee();
//
//        when(traineeDao.delete(trainee)).thenReturn(false);
//
//        traineeService.delete(trainee);
//
//        verify(traineeDao, times(1)).delete(trainee);
//    }
}
