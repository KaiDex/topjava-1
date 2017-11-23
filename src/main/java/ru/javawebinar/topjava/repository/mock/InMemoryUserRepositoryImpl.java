package ru.javawebinar.topjava.repository.mock;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryUserRepositoryImpl implements UserRepository {
    private Map<Integer, User> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    @Override
    public User save(User user) {
        if (user.isNew())
            user.setId(counter.incrementAndGet());
        repository.put(user.getId(), user);

        return user;
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
    public User get(int id) {
        return repository.get(id);
    }

    @Override
    @SuppressWarnings("all")
    public User getByEmail(String email) {
        return repository.values().stream().filter(user ->
                user.getEmail().equals(email))
                .findAny().orElse(null);
    }

    @Override
    public List<User> getAll() {
        return repository.entrySet().stream()
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }
}
