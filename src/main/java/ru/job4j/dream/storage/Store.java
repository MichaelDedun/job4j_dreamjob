package ru.job4j.dream.storage;

import ru.job4j.dream.model.*;

import java.sql.SQLException;
import java.util.Collection;

public interface Store {

    Collection<Post> findAllPosts();

    Collection<Candidate> findAllCandidates();

    Collection<City> findAllCities();

    void save(Post post);

    void save(Candidate candidate);

    Post findPostById(int id);

    Candidate findCandidateById(int id);

    Photo findPhotoById(Long id);

    Photo savePhoto(Photo photo);

    User createUser(User user) throws SQLException;

    City findCityById(Integer id);

    User findUserById(Long id);

    User findUserByEmail(String email);

}
