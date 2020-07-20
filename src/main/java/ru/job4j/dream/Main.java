package ru.job4j.dream;

import ru.job4j.dream.model.Candidate;
import ru.job4j.dream.model.Post;
import ru.job4j.dream.storage.PsqlStore;
import ru.job4j.dream.storage.Store;

import java.time.LocalDate;

public class Main {

    public static void main(String[] args) {
        Store store = PsqlStore.instOf();
        store.save(new Post(0, "Java Job", "asdfasdf", LocalDate.now()));
        for (Post post : store.findAllPosts()) {
            System.out.println(post.getId() + " " + post.getName());
        }
        System.out.println(store.findPostById(1));
        store.save(new Candidate(0,"kek"));
        for (Candidate candidate : store.findAllCandidates()) {
            System.out.println(candidate.getId() + " " + candidate.getName());
        }
        System.out.println(store.findCandidateById(1));
    }

}
