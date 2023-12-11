package com.epam.xstack.service.impl;

import com.epam.xstack.dao.impl.TrainingTypeDaoImpl;
import com.epam.xstack.model.TrainingType;
import com.epam.xstack.service.TrainingTypeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class TrainingTypeServiceImpl implements TrainingTypeService {
    public final TrainingTypeDaoImpl trainingTypeDao;

    @Override
    public Optional<TrainingType> findByName(String trainingTypeName) {
        return trainingTypeDao.findByName(trainingTypeName);
    }

    @Override
    public List<TrainingType> list() {
        return trainingTypeDao.list();
    }
}
