package ru.javawebinar.topjava.web.meal;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealWithExceed;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.util.MealsUtil;

import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;

import java.util.Collection;

@Controller
public class MealRestController {
    private MealService service;
    private final org.slf4j.Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    public MealRestController(MealService service) {
        this.service = service;
    }

    public MealService getService() {
        return service;
    }

    public MealWithExceed get(int id, int caloriesPerDay) {
        log.info("get {}", id);
        return getAll(caloriesPerDay).stream().filter(mwe -> mwe.getId() == id).findAny().orElse(null);
    }

    public MealWithExceed create(Meal meal, int caloriesPerDay) {
        log.info("create {}", meal);
        checkNew(meal);
        service.create(meal);
        return get(meal.getId(), caloriesPerDay);
    }

    public void delete(int id) {
        log.info("delete {}", id);
        service.delete(id);
    }

    public void update(Meal meal, int id) {
        log.info("update {} with id={}", meal, id);
        assureIdConsistent(meal, id);
        service.update(meal);
    }

    public Collection<MealWithExceed> getAll(int caloriesPerDay) {
        log.info("getAll");
        return MealsUtil.getWithExceeded(service.getAll(), caloriesPerDay);
    }
}