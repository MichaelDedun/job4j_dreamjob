package ru.job4j.dream.storage;

import org.apache.commons.dbcp2.BasicDataSource;
import ru.job4j.dream.model.Candidate;
import ru.job4j.dream.model.Photo;
import ru.job4j.dream.model.Post;
import ru.job4j.dream.model.User;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

public class PsqlStore implements Store {
    private final BasicDataSource pool = new BasicDataSource();

    private PsqlStore() {
        Properties cfg = new Properties();
        try (BufferedReader io = new BufferedReader(
                new FileReader("db.properties")
        )) {
            cfg.load(io);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        try {
            Class.forName(cfg.getProperty("jdbc.driver"));
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        pool.setDriverClassName(cfg.getProperty("jdbc.driver"));
        pool.setUrl(cfg.getProperty("jdbc.url"));
        pool.setUsername(cfg.getProperty("jdbc.username"));
        pool.setPassword(cfg.getProperty("jdbc.password"));
        pool.setMinIdle(5);
        pool.setMaxIdle(10);
        pool.setMaxOpenPreparedStatements(100);
    }

    private static final class Lazy {
        private static final Store INST = new PsqlStore();
    }

    public static Store instOf() {
        return Lazy.INST;
    }

    @Override
    public Collection<Post> findAllPosts() {
        List<Post> posts = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("SELECT * FROM post")
        ) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    posts.add(new Post(
                                    it.getInt("id"),
                                    it.getString("name"),
                                    it.getString("description"),
                                    it.getDate("date").toLocalDate()
                            )
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return posts;
    }

    @Override
    public Collection<Candidate> findAllCandidates() {
        List<Candidate> candidates = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("SELECT * FROM candidate")
        ) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    candidates.add(new Candidate(
                                    it.getInt("id"),
                                    it.getString("name"),
                                    it.getLong("photo_id")
                            )
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return candidates;
    }

    @Override
    public void save(Post post) {
        if (post.getId() == 0) {
            createPost(post);
        } else {
            updatePost(post);
        }
    }

    @Override
    public void save(Candidate candidate) {
        if (candidate.getId() == 0) {
            createCandidate(candidate);
        } else {
            updateCandidate(candidate);
        }
    }

    @Override
    public Post findPostById(int id) {
        try (Connection cn = pool.getConnection(); final PreparedStatement statement = cn.prepareStatement("SELECT * FROM post WHERE id = ?")) {
            Post post = null;
            statement.setInt(1, id);
            try (ResultSet set = statement.executeQuery()) {
                if (set.next())
                    post = new Post(set.getInt("id"), set.getString("name"), set.getString("description"), set.getDate("date").toLocalDate());
            }
            return post;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Candidate findCandidateById(int id) {
        try (Connection cn = pool.getConnection(); final PreparedStatement statement = cn.prepareStatement("SELECT * FROM candidate WHERE id = ?")) {
            Candidate candidate = null;
            statement.setInt(1, id);
            try (ResultSet set = statement.executeQuery()) {
                if (set.next())
                    candidate = new Candidate(set.getInt("id"),
                            set.getString("name"),
                            set.getLong("photo_id"));
            }
            return candidate;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    private void updatePost(Post post) {
        try (Connection cn = pool.getConnection(); final PreparedStatement statement = cn.prepareStatement("UPDATE post SET name = ?, description = ?, date = ? WHERE id = ? ")) {
            statement.setString(1, post.getName());
            statement.setString(2, post.getDescription());
            statement.setDate(3, java.sql.Date.valueOf(post.getCreated()));
            statement.setInt(4, post.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateCandidate(Candidate candidate) {
        try (Connection cn = pool.getConnection(); final PreparedStatement statement = cn.prepareStatement("UPDATE candidate SET name = ? WHERE id = ? ")) {
            statement.setString(1, candidate.getName());
            statement.setInt(2, candidate.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Post createPost(Post post) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("INSERT INTO post(name,description, date) VALUES (?,?,?)", PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            ps.setString(1, post.getName());
            ps.setString(2, post.getDescription());
            ps.setDate(3, java.sql.Date.valueOf(post.getCreated()));
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    post.setId(id.getInt(1));
                    post.setName(id.getString(2));
                    post.setDescription(id.getString(3));
                    post.setCreated(id.getDate(4).toLocalDate());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return post;
    }

    private Candidate createCandidate(Candidate candidate) {
        try (Connection cn = pool.getConnection(); PreparedStatement psCand = cn.prepareStatement("INSERT INTO candidate(name, photo_id) VALUES (?, ?)", PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            psCand.setString(1, candidate.getName());
            psCand.setLong(2, candidate.getPhotoId());
            psCand.execute();
            try (ResultSet idCand = psCand.getGeneratedKeys()) {
                if (idCand.next()) {
                    candidate.setId(idCand.getInt(1));
                    candidate.setName(idCand.getString(2));
                    candidate.setPhotoId(idCand.getLong(3));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return candidate;
    }

    @Override
    public Photo savePhoto(Photo photo) {
        try (Connection cn = pool.getConnection(); PreparedStatement psPhoto = cn.prepareStatement("INSERT INTO photo(name) VALUES(?)", PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            psPhoto.setString(1, photo.getName());
            psPhoto.execute();
            try (ResultSet it = psPhoto.getGeneratedKeys()) {
                if (it.next()) {
                    photo.setId(it.getLong(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return photo;
    }

    public User createUser(User user) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("INSERT INTO user(name,email,password) VALUES (?,?,?)", PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());
            if (ps.execute()) {
                try (ResultSet set = ps.getGeneratedKeys()) {
                    user.setId(set.getLong(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    public User findById(Long id) {
        User user = null;
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("SELECT * FROM users WHERE id = ?")) {

            ps.setLong(1, id);
            try (ResultSet set = ps.executeQuery()) {
                if (set.next()) {
                    user = new User();
                    user.setId(set.getLong("id"));
                    user.setName(set.getString("name"));
                    user.setEmail(set.getString("email"));
                    user.setPassword(set.getString("password"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    @Override
    public Photo findPhotoById(Long id) {
        Photo result = null;
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(
                     "SELECT * FROM photo WHERE id = ?"
             )
        ) {
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                result = new Photo(
                        rs.getLong(1),
                        rs.getString("name")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}
