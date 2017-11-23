package ru.javawebinar.topjava.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.AuthorizedUser;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFoundWithId;

import java.util.Collection;

@Service
public class MealServiceImpl implements MealService {

    private MealRepository repository;

    @Autowired
    public MealServiceImpl(MealRepository repository) {
        this.repository = repository;
    }

    @Override
    public Meal create(Meal meal) {
        return repository.save(meal);
    }

    @Override
    public void delete(int id) throws NotFoundException {
        checkRights(id);
        checkNotFoundWithId(repository.delete(id), id);
    }

    @Override
    public Meal get(int id) throws NotFoundException {
        checkRights(id);
        return checkNotFoundWithId(repository.get(id), id);
    }

    @Override
    public void update(Meal meal) {
        checkRights(meal.getId());
        repository.save(meal);
    }

    @Override
    public Collection<Meal> getAll() {
        return repository.getAll();
    }

    @Override
    public MealRepository getRepository() {
        return repository;
    }

    private void checkRights(int id) throws NotFoundException {
        if (checkNotFoundWithId(repository.get(id), id).getUserId() != AuthorizedUser.id())
            throw new NotFoundException("Not your food, heck off");
    }
}