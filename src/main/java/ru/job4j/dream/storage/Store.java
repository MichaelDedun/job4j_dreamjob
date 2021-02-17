package ru.job4j.dream.storage;

import ru.job4j.dream.model.Candidate;
import ru.job4j.dream.model.Photo;
import ru.job4j.dream.model.Post;
import ru.job4j.dream.model.User;

import java.sql.SQLException;
import java.util.Collection;

public interface Store {

    Collection<Post> findAllPosts();

    Collection<Candidate> findAllCandidates();

    void save(Post post);

    void save(Candidate candidate);

    Post findPostById(int id);

    Candidate findCandidateById(int id);

    Photo findPhotoById(Long id);

    Photo savePhoto(Photo photo);

    User createUser(User user) throws SQLException;

    User findUserById(Long id);

    User findUserByEmail(String email);

}
