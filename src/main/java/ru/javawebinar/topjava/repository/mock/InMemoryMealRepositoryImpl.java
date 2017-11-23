package ru.javawebinar.topjava.repository.mock;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.AuthorizedUser;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepositoryImpl implements MealRepository {
    private Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.MEALS.forEach(this::save);
    }

    @Override
    public Meal save(Meal meal) {
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            meal.setUserId(AuthorizedUser.id());
        }
        repository.put(meal.getId(), meal);
        return meal;
    }

    @Override
    public boolean delete(int id) {
        AtomicBoolean result = new AtomicBoolean(false);
        repository.computeIfPresent(id, (x, y) -> {
            result.set(true);
            return null;
        });
        return result.get();
    }

    @Override
    public Meal get(int id) {
        return repository.values().stream()
                .filter(meal -> meal.getUserId() == AuthorizedUser.id()
                        && meal.getId() == id).findAny().orElse(null);
    }

    @Override
    public Collection<Meal> getAll() {
        List<Meal> result = repository.values().stream()
                .filter(meal -> meal.getUserId() == AuthorizedUser.id())
                .collect(Collectors.toList());
        result.sort(Comparator.comparing(Meal::getDateTime).reversed());

        return result;
    }
}

