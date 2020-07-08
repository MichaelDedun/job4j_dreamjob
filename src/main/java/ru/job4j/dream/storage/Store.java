package ru.job4j.dream.storage;

import ru.job4j.dream.model.Post;

import java.time.LocalDate;
import java.time.Month;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Store {

    private static final Store INST = new Store();

    private Map<Integer, Post> posts = new ConcurrentHashMap<>();

    private Store() {
        posts.put(1, new Post(1, "Junior Java Job", "без опыта работы", LocalDate.now()));
        posts.put(2, new Post(2, "Middle Java Job", "опыт работы 2 года", LocalDate.of(2020, Month.APRIL,23)));
        posts.put(3, new Post(3, "Senior Java Job", "опыт работы 5 лет", LocalDate.of(2019, Month.DECEMBER, 22)));
    }

    public static Store instOf() {
        return INST;
    }

    public Collection<Post> findAll() {
        return posts.values();
    }

}
