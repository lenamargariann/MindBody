package com.epam.xstack.dao;

import com.epam.xstack.model.TrainerWorkload;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TrainerWorkloadDao {
    private final MongoTemplate template;

    public TrainerWorkload get(String username) {
        Query findQuery = Query.query(Criteria.where("trainerUsername").is(username));
        return Optional
                .ofNullable(template.findOne(findQuery, TrainerWorkload.class))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Trainer workload data not found"));
    }

    public TrainerWorkload add(TrainerWorkload trainerWorkload) {
        try {
            TrainerWorkload existing = get(trainerWorkload.getTrainerUsername());
            String year = trainerWorkload.getYears().keySet().stream().toList().get(0);
            String month = trainerWorkload.getYears().get(year).keySet().stream().toList().get(0);
            int duration = trainerWorkload.getYears().get(year).get(month);
            update(new Update().inc("years.".concat(year).concat(".").concat(month), duration), trainerWorkload.getTrainerUsername());
            return existing;
        } catch (ResponseStatusException e) {
            return template.insert(trainerWorkload, "trainerWorkloads");
        }
    }

    public TrainerWorkload delete(TrainerWorkload trainerWorkload) {
        TrainerWorkload existing = get(trainerWorkload.getTrainerUsername());
        if (existing != null) {
            String year = trainerWorkload.getYears().keySet().stream().toList().get(0);
            String month = trainerWorkload.getYears().get(year).keySet().stream().toList().get(0);
            int duration = trainerWorkload.getYears().get(year).get(month);
            update(new Update().inc("years.".concat(year).concat(".").concat(month), -duration), trainerWorkload.getTrainerUsername());
            return existing;

        } else throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Trainer workload data not found");
    }

    public void update(Update update, String username) {
        Query findQuery = Query.query(Criteria.where("trainerUsername").is(username));
        if (template.updateFirst(findQuery, update, TrainerWorkload.class).getModifiedCount() == 0) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Trainer workload update failed");
        }
    }


}
