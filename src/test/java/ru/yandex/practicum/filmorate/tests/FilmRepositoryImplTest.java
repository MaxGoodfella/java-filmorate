package ru.yandex.practicum.filmorate.tests;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.impl.FilmRepositoryImpl;
import ru.yandex.practicum.filmorate.repository.impl.GenreRepositoryImpl;
import ru.yandex.practicum.filmorate.repository.impl.RatingRepositoryImpl;
import ru.yandex.practicum.filmorate.repository.impl.UserRepositoryImpl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmRepositoryImplTest {

    private final JdbcTemplate jdbcTemplate;

    private FilmRepositoryImpl filmRepositoryImpl;

    private static RatingRepositoryImpl ratingRepositoryImpl;


    @BeforeEach
    public void setUp() {
        filmRepositoryImpl = new FilmRepositoryImpl(jdbcTemplate);
        ratingRepositoryImpl = new RatingRepositoryImpl(jdbcTemplate);
    }

    @AfterEach
    public void tearDown() {
        jdbcTemplate.execute("DELETE FROM FILMS");
    }


    @Test
    public void testFindFilmByExistingId() {
        Film newFilm = new Film("Name", "Description",
                LocalDate.of(1990, 12, 12), 100, 0);
        Film savedFilm = filmRepositoryImpl.save(newFilm);

        assertDoesNotThrow(() -> filmRepositoryImpl.findById(savedFilm.getId()));

        assertThat(savedFilm)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(newFilm);
    }

    @Test
    public void testFindFilmByNotExistingId_shouldThrowEmptyResultDataAccessException() {
        Film newFilm = new Film("Name", "Description",
                LocalDate.of(1990, 12, 12), 100, 0);
        filmRepositoryImpl.save(newFilm);

        assertThrows(EmptyResultDataAccessException.class, () -> filmRepositoryImpl.findById(2));
    }

    @Test
    public void testFindFilmByExistingName() {
        Film newFilm = new Film("Name", "Description",
                LocalDate.of(1990, 12, 12), 100, 0);
        Film savedFilm = filmRepositoryImpl.save(newFilm);

        assertDoesNotThrow(() -> filmRepositoryImpl.findByName(savedFilm.getName()));

        assertThat(savedFilm)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(newFilm);
    }

    @Test
    public void testFindFilmByNotExistingName_shouldThrowEmptyResultDataAccessException() {
        Film newFilm = new Film("Name", "Description",
                LocalDate.of(1990, 12, 12), 100, 0);
        filmRepositoryImpl.save(newFilm);


        assertThrows(EmptyResultDataAccessException.class, () -> filmRepositoryImpl.findByName("Description2"));
    }

    @Test
    public void testFindFilmIdByExistingName() {
        Film newFilm = new Film("Name", "Description",
                LocalDate.of(1990, 12, 12), 100, 0);

        Film savedFilm = filmRepositoryImpl.save(newFilm);

        assertDoesNotThrow(() -> filmRepositoryImpl.findIdByName(savedFilm.getName()));

        assertThat(savedFilm)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(newFilm);
    }

    @Test
    public void testFindFilmIdByNotExistingName_shouldReturnNull() {
        Film newFilm = new Film("Name", "Description",
                LocalDate.of(1990, 12, 12), 100, 0);
        filmRepositoryImpl.save(newFilm);

        assertNull(filmRepositoryImpl.findIdByName("Description2"));
    }

    @Test
    public void testFindByNameDescriptionReleaseDateAndDuration() {
        Film newFilm = new Film("Name", "Description",
                LocalDate.of(1990, 12, 12), 100, 0);

        Film savedFilm = filmRepositoryImpl.save(newFilm);

        assertDoesNotThrow(() -> filmRepositoryImpl.findByNameDescriptionReleaseDateAndDuration
                (savedFilm.getName(), savedFilm.getDescription(), savedFilm.getReleaseDate(), savedFilm.getDuration()));

        assertThat(savedFilm)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(newFilm);
    }

    @Test
    public void testFindByNameDescriptionReleaseDateAndDuration_shouldReturnNull() {
        Film newFilm = new Film("Name", "Description",
                LocalDate.of(1990, 12, 12), 100, 0);
        filmRepositoryImpl.save(newFilm);

        assertNull(filmRepositoryImpl.findByNameDescriptionReleaseDateAndDuration
                ("Name2", "Description2", LocalDate.of(1991, 12, 12),
                        1001));
    }

    @Test
    public void testFindAll() {
        Film newFilm1 = new Film("Name1", "Description1",
                LocalDate.of(1990, 12, 12), 100, 0);
        Film newFilm2 = new Film("Name2", "Description2",
                LocalDate.of(1990, 12, 12), 100, 0);
        List<Film> newFilms = new ArrayList<>();
        newFilms.add(newFilm1);
        newFilms.add(newFilm2);

        filmRepositoryImpl.save(newFilm1);
        filmRepositoryImpl.save(newFilm2);

        List<Film> savedFilms = filmRepositoryImpl.findAll();

        assertThat(savedFilms)
                .isNotNull()
                .usingRecursiveComparison()
                .ignoringFields("rating")
                .isEqualTo(newFilms);
    }


    @Test
    public void testSave() {
        Film newFilm = new Film("Name", "Description",
                LocalDate.of(1990, 12, 12), 100, 0);

        assertDoesNotThrow(() -> filmRepositoryImpl.save(newFilm));
    }

    @Test
    public void testSaveMany() {
        Film newFilm1 = new Film("Name1", "Description2",
                LocalDate.of(1990, 12, 12), 100, 0);
        Film newFilm2 = new Film("Name2", "Description2",
                LocalDate.of(1990, 12, 12), 100, 0);
        List<Film> newFilms = new ArrayList<>();
        newFilms.add(newFilm1);
        newFilms.add(newFilm2);

        assertDoesNotThrow(() -> filmRepositoryImpl.saveMany(newFilms));
    }


    @Test
    public void testUpdate() {
        Film newFilm = new Film("Name1", "Description1",
                LocalDate.of(1990, 12, 12), 100, 0);
        filmRepositoryImpl.save(newFilm);

        Film updatedFilm = new Film(newFilm.getId(), "Name2", "Description2",
                LocalDate.of(1990, 12, 12), 100, 0);

        assertDoesNotThrow(() -> filmRepositoryImpl.update(updatedFilm));

        Film finalFilm = filmRepositoryImpl.findById(newFilm.getId());

        Assertions.assertEquals("Name2", finalFilm.getName(), "Название не совпадает");
        Assertions.assertEquals("Description2", finalFilm.getDescription(), "Описание не совпадает");
        Assertions.assertEquals(LocalDate.of(1990, 12, 12), finalFilm.getReleaseDate(),
                "Дата выхода не совпадает");
        Assertions.assertEquals(100, finalFilm.getDuration(), "Продолжительность не совпадает");
        Assertions.assertEquals(0, finalFilm.getPopularity(), "Популярность не совпадает");
    }


    @Test
    public void testDeleteByExistingId() {
        Film newFilm = new Film("Name", "Description",
                LocalDate.of(1990, 12, 12), 100, 0);

        Film savedFilm = filmRepositoryImpl.save(newFilm);

        assertDoesNotThrow(() -> filmRepositoryImpl.deleteById(savedFilm.getId()));
        assertThrows(EmptyResultDataAccessException.class, () -> filmRepositoryImpl.findById(1));
    }

    @Test
    public void testDeleteByNotExistingId() {
        Film newFilm = new Film("Name", "Description",
                LocalDate.of(1990, 12, 12), 100, 0);
        filmRepositoryImpl.save(newFilm);

        boolean deletionResult = filmRepositoryImpl.deleteById(2);

        assertFalse(deletionResult);
    }

    @Test
    public void testDeleteAll() {
        Film newFilm1 = new Film("Name1", "Description2",
                LocalDate.of(1990, 12, 12), 100, 0);
        Film newFilm2 = new Film("Name2", "Description2",
                LocalDate.of(1990, 12, 12), 100, 0);
        Film newFilm3 = new Film("Name3", "Description3",
                LocalDate.of(1990, 12, 12), 100, 0);
        filmRepositoryImpl.save(newFilm1);
        filmRepositoryImpl.save(newFilm2);
        filmRepositoryImpl.save(newFilm3);

        assertDoesNotThrow(() -> filmRepositoryImpl.deleteAll());
    }


    @Test
    public void testAddLike() {

        UserRepositoryImpl userRepositoryImpl = new UserRepositoryImpl(jdbcTemplate);

        User newUser1 = new User(1, "user1", "user1@gmail.com", "User1 Name",
                LocalDate.of(1990, 1, 1));
        User newUser2 = new User(2, "user2", "user2@gmail.com", "User2 Name",
                LocalDate.of(1990, 1, 1));

        User savedUser1 = userRepositoryImpl.save(newUser1);
        User savedUser2 = userRepositoryImpl.save(newUser2);


        Film newFilm1 = new Film("Name1", "Description2",
                LocalDate.of(1990, 12, 12), 100, 0);
        Film newFilm2 = new Film("Name2", "Description2",
                LocalDate.of(1990, 12, 12), 100, 0);

        Film savedFilm1 = filmRepositoryImpl.save(newFilm1);
        Film savedFilm2 = filmRepositoryImpl.save(newFilm2);

        assertEquals(Integer.valueOf(0), savedFilm1.getPopularity());
        assertEquals(Integer.valueOf(0), savedFilm2.getPopularity());


        filmRepositoryImpl.addLike(savedFilm1.getId(), savedUser1.getId());
        filmRepositoryImpl.addLike(savedFilm1.getId(), savedUser2.getId());
        filmRepositoryImpl.addLike(savedFilm2.getId(), savedUser2.getId());

        assertEquals(2, filmRepositoryImpl.findFansIds(savedFilm1.getId()).size());
        assertTrue(filmRepositoryImpl.findFansIds(savedFilm1.getId()).contains(savedUser1.getId()));
        assertTrue(filmRepositoryImpl.findFansIds(savedFilm1.getId()).contains(savedUser2.getId()));
        assertEquals(1, filmRepositoryImpl.findFansIds(savedFilm2.getId()).size());
        assertTrue(filmRepositoryImpl.findFansIds(savedFilm2.getId()).contains(savedUser2.getId()));

    }

    @Test
    public void testRemoveLike() {

        UserRepositoryImpl userRepositoryImpl = new UserRepositoryImpl(jdbcTemplate);

        User newUser1 = new User(1, "user1", "user1@gmail.com", "User1 Name",
                LocalDate.of(1990, 1, 1));
        User newUser2 = new User(2, "user2", "user2@gmail.com", "User2 Name",
                LocalDate.of(1990, 1, 1));

        User savedUser1 = userRepositoryImpl.save(newUser1);
        User savedUser2 = userRepositoryImpl.save(newUser2);

        Film newFilm1 = new Film("Name1", "Description2",
                LocalDate.of(1990, 12, 12), 100, 0);
        Film newFilm2 = new Film("Name2", "Description2",
                LocalDate.of(1990, 12, 12), 100, 0);

        Film savedFilm1 = filmRepositoryImpl.save(newFilm1);
        Film savedFilm2 = filmRepositoryImpl.save(newFilm2);


        filmRepositoryImpl.addLike(savedFilm1.getId(), savedUser1.getId());
        filmRepositoryImpl.addLike(savedFilm1.getId(), savedUser2.getId());
        filmRepositoryImpl.addLike(savedFilm2.getId(), savedUser2.getId());

        assertEquals(2, filmRepositoryImpl.findFansIds(savedFilm1.getId()).size());
        assertTrue(filmRepositoryImpl.findFansIds(savedFilm1.getId()).contains(savedUser1.getId()));
        assertTrue(filmRepositoryImpl.findFansIds(savedFilm1.getId()).contains(savedUser2.getId()));
        assertEquals(1, filmRepositoryImpl.findFansIds(savedFilm2.getId()).size());
        assertTrue(filmRepositoryImpl.findFansIds(savedFilm2.getId()).contains(savedUser2.getId()));


        filmRepositoryImpl.removeLike(savedFilm1.getId(), savedUser2.getId());
        filmRepositoryImpl.removeLike(savedFilm2.getId(), savedUser2.getId());

        assertEquals(1, filmRepositoryImpl.findFansIds(savedFilm1.getId()).size());
        assertFalse(filmRepositoryImpl.findFansIds(savedFilm1.getId()).contains(savedUser2.getId()));
        assertEquals(0, filmRepositoryImpl.findFansIds(savedFilm2.getId()).size());
        assertFalse(filmRepositoryImpl.findFansIds(savedFilm2.getId()).contains(savedUser2.getId()));

    }

    @Test
    public void testGetTopByLikes() {

        UserRepositoryImpl userRepositoryImpl = new UserRepositoryImpl(jdbcTemplate);

        User newUser1 = new User(1, "user1", "user1@gmail.com", "User1 Name",
                LocalDate.of(1990, 1, 1));
        User newUser2 = new User(2, "user2", "user2@gmail.com", "User2 Name",
                LocalDate.of(1990, 1, 1));
        User newUser3 = new User(3, "user3", "user3@gmail.com", "User3 Name",
                LocalDate.of(1990, 1, 1));
        User newUser4 = new User(4, "user4", "user4@gmail.com", "User4 Name",
                LocalDate.of(1990, 1, 1));

        User savedUser1 = userRepositoryImpl.save(newUser1);
        User savedUser2 = userRepositoryImpl.save(newUser2);
        User savedUser3 = userRepositoryImpl.save(newUser3);
        User savedUser4 = userRepositoryImpl.save(newUser4);

        Film newFilm1 = new Film("Name1", "Description2",
                LocalDate.of(1990, 12, 12), 100, 0);
        Film newFilm2 = new Film("Name2", "Description2",
                LocalDate.of(1990, 12, 12), 100, 0);
        Film newFilm3 = new Film("Name3", "Description3",
                LocalDate.of(1990, 12, 12), 100, 0);
        Film newFilm4 = new Film("Name4", "Description4",
                LocalDate.of(1990, 12, 12), 100, 0);

        Film savedFilm1 = filmRepositoryImpl.save(newFilm1);
        Film savedFilm2 = filmRepositoryImpl.save(newFilm2);
        Film savedFilm3 = filmRepositoryImpl.save(newFilm3);
        Film savedFilm4 = filmRepositoryImpl.save(newFilm4);


        filmRepositoryImpl.addLike(savedFilm1.getId(), savedUser1.getId());
        filmRepositoryImpl.addLike(savedFilm2.getId(), savedUser1.getId());
        filmRepositoryImpl.addLike(savedFilm2.getId(), savedUser2.getId());
        filmRepositoryImpl.addLike(savedFilm3.getId(), savedUser1.getId());
        filmRepositoryImpl.addLike(savedFilm3.getId(), savedUser2.getId());
        filmRepositoryImpl.addLike(savedFilm3.getId(), savedUser3.getId());
        filmRepositoryImpl.addLike(savedFilm4.getId(), savedUser1.getId());
        filmRepositoryImpl.addLike(savedFilm4.getId(), savedUser2.getId());
        filmRepositoryImpl.addLike(savedFilm4.getId(), savedUser3.getId());
        filmRepositoryImpl.addLike(savedFilm4.getId(), savedUser4.getId());


        List<Film> top = filmRepositoryImpl.getTopByLikes(3);

        assertEquals(3, top.size());

        Film filmTop1 = top.get(0);
        assertEquals(savedFilm4.getId(), filmTop1.getId());
        assertEquals(savedFilm4.getName(), filmTop1.getName());
        assertEquals(savedFilm4.getDescription(), filmTop1.getDescription());
        assertEquals(Integer.valueOf(4), filmTop1.getPopularity());

        Film filmTop2 = top.get(1);
        assertEquals(savedFilm3.getId(), filmTop2.getId());
        assertEquals(savedFilm3.getName(), filmTop2.getName());
        assertEquals(savedFilm3.getDescription(), filmTop2.getDescription());
        assertEquals(Integer.valueOf(3), filmTop2.getPopularity());

        Film filmTop3 = top.get(2);
        assertEquals(savedFilm2.getId(), filmTop3.getId());
        assertEquals(savedFilm2.getName(), filmTop3.getName());
        assertEquals(savedFilm2.getDescription(), filmTop3.getDescription());
        assertEquals(Integer.valueOf(2), filmTop3.getPopularity());

    }

    @Test
    public void testFindFilmFansIds() {

        UserRepositoryImpl userRepositoryImpl = new UserRepositoryImpl(jdbcTemplate);

        User newUser1 = new User(1, "user1", "user1@gmail.com", "User1 Name",
                LocalDate.of(1990, 1, 1));
        User newUser2 = new User(2, "user2", "user2@gmail.com", "User2 Name",
                LocalDate.of(1990, 1, 1));
        User newUser3 = new User(3, "user3", "user3@gmail.com", "User3 Name",
                LocalDate.of(1990, 1, 1));
        User newUser4 = new User(4, "user4", "user4@gmail.com", "User4 Name",
                LocalDate.of(1990, 1, 1));

        User savedUser1 = userRepositoryImpl.save(newUser1);
        User savedUser2 = userRepositoryImpl.save(newUser2);
        User savedUser3 = userRepositoryImpl.save(newUser3);
        User savedUser4 = userRepositoryImpl.save(newUser4);

        Film newFilm1 = new Film("Name1", "Description2",
                LocalDate.of(1990, 12, 12), 100, 0);
        Film newFilm2 = new Film("Name2", "Description2",
                LocalDate.of(1990, 12, 12), 100, 0);
        Film newFilm3 = new Film("Name3", "Description3",
                LocalDate.of(1990, 12, 12), 100, 0);
        Film newFilm4 = new Film("Name4", "Description4",
                LocalDate.of(1990, 12, 12), 100, 0);

        Film savedFilm1 = filmRepositoryImpl.save(newFilm1);
        Film savedFilm2 = filmRepositoryImpl.save(newFilm2);
        Film savedFilm3 = filmRepositoryImpl.save(newFilm3);
        Film savedFilm4 = filmRepositoryImpl.save(newFilm4);


        filmRepositoryImpl.addLike(savedFilm1.getId(), savedUser1.getId());
        filmRepositoryImpl.addLike(savedFilm2.getId(), savedUser1.getId());
        filmRepositoryImpl.addLike(savedFilm2.getId(), savedUser2.getId());
        filmRepositoryImpl.addLike(savedFilm3.getId(), savedUser1.getId());
        filmRepositoryImpl.addLike(savedFilm3.getId(), savedUser2.getId());
        filmRepositoryImpl.addLike(savedFilm3.getId(), savedUser3.getId());
        filmRepositoryImpl.addLike(savedFilm4.getId(), savedUser1.getId());
        filmRepositoryImpl.addLike(savedFilm4.getId(), savedUser2.getId());
        filmRepositoryImpl.addLike(savedFilm4.getId(), savedUser3.getId());
        filmRepositoryImpl.addLike(savedFilm4.getId(), savedUser4.getId());


        List<Integer> film4fansIds = filmRepositoryImpl.findFansIds(savedFilm4.getId());

        assertEquals(4, film4fansIds.size());
        assertTrue(film4fansIds.contains(savedUser1.getId()));
        assertTrue(film4fansIds.contains(savedUser2.getId()));
        assertTrue(film4fansIds.contains(savedUser3.getId()));
        assertTrue(film4fansIds.contains(savedUser4.getId()));

    }

    @Test
    public void testAddFilmGenres() {

        GenreRepositoryImpl genreRepositoryImpl = new GenreRepositoryImpl(jdbcTemplate);

        Genre newGenre1 = new Genre(1, "Comedy");
        Genre newGenre2 = new Genre(2, "Drama");
        Genre newGenre3 = new Genre(3, "Thriller");

        Genre savedGenre1 = genreRepositoryImpl.save(newGenre1);
        Genre savedGenre2 = genreRepositoryImpl.save(newGenre2);
        Genre savedGenre3 = genreRepositoryImpl.save(newGenre3);

        List<Integer> film1genresIds = new ArrayList<>();
        film1genresIds.add(savedGenre1.getId());
        film1genresIds.add(savedGenre2.getId());
        film1genresIds.add(savedGenre3.getId());

        List<Integer> film2genresIds = new ArrayList<>();
        film2genresIds.add(savedGenre1.getId());
        film2genresIds.add(savedGenre2.getId());

        Film newFilm1 = new Film("Name1", "Description2",
                LocalDate.of(1990, 12, 12), 100, 0);
        Film newFilm2 = new Film("Name2", "Description2",
                LocalDate.of(1990, 12, 12), 100, 0);

        Film savedFilm1 = filmRepositoryImpl.save(newFilm1);
        Film savedFilm2 = filmRepositoryImpl.save(newFilm2);


        filmRepositoryImpl.addGenres(savedFilm1.getId(), film1genresIds);
        filmRepositoryImpl.addGenres(savedFilm2.getId(), film2genresIds);

        assertEquals(3, filmRepositoryImpl.findGenresNames(savedFilm1.getId()).size());
        assertTrue(filmRepositoryImpl.findGenresNames(savedFilm1.getId()).contains(savedGenre1.getName()));
        assertTrue(filmRepositoryImpl.findGenresNames(savedFilm1.getId()).contains(savedGenre2.getName()));
        assertTrue(filmRepositoryImpl.findGenresNames(savedFilm1.getId()).contains(savedGenre3.getName()));
        assertEquals(2, filmRepositoryImpl.findGenresNames(savedFilm2.getId()).size());
        assertTrue(filmRepositoryImpl.findGenresNames(savedFilm2.getId()).contains(savedGenre1.getName()));
        assertTrue(filmRepositoryImpl.findGenresNames(savedFilm2.getId()).contains(savedGenre2.getName()));

    }

    @Test
    public void testRemoveFilmGenres() {

        GenreRepositoryImpl genreRepositoryImpl = new GenreRepositoryImpl(jdbcTemplate);

        Genre newGenre1 = new Genre(1, "Comedy");
        Genre newGenre2 = new Genre(2, "Drama");
        Genre newGenre3 = new Genre(3, "Thriller");

        Genre savedGenre1 = genreRepositoryImpl.save(newGenre1);
        Genre savedGenre2 = genreRepositoryImpl.save(newGenre2);
        Genre savedGenre3 = genreRepositoryImpl.save(newGenre3);

        List<Integer> film1genresIds = new ArrayList<>();
        film1genresIds.add(savedGenre1.getId());
        film1genresIds.add(savedGenre2.getId());
        film1genresIds.add(savedGenre3.getId());

        List<Integer> film2genresIds = new ArrayList<>();
        film2genresIds.add(savedGenre1.getId());
        film2genresIds.add(savedGenre2.getId());

        Film newFilm1 = new Film("Name1", "Description2",
                LocalDate.of(1990, 12, 12), 100, 0);
        Film newFilm2 = new Film("Name2", "Description2",
                LocalDate.of(1990, 12, 12), 100, 0);

        Film savedFilm1 = filmRepositoryImpl.save(newFilm1);
        Film savedFilm2 = filmRepositoryImpl.save(newFilm2);


        filmRepositoryImpl.addGenres(savedFilm1.getId(), film1genresIds);
        filmRepositoryImpl.addGenres(savedFilm2.getId(), film2genresIds);

        assertEquals(3, filmRepositoryImpl.findGenresNames(savedFilm1.getId()).size());
        assertTrue(filmRepositoryImpl.findGenresNames(savedFilm1.getId()).contains(savedGenre1.getName()));
        assertTrue(filmRepositoryImpl.findGenresNames(savedFilm1.getId()).contains(savedGenre2.getName()));
        assertTrue(filmRepositoryImpl.findGenresNames(savedFilm1.getId()).contains(savedGenre3.getName()));
        assertEquals(2, filmRepositoryImpl.findGenresNames(savedFilm2.getId()).size());
        assertTrue(filmRepositoryImpl.findGenresNames(savedFilm2.getId()).contains(savedGenre1.getName()));
        assertTrue(filmRepositoryImpl.findGenresNames(savedFilm2.getId()).contains(savedGenre2.getName()));

        filmRepositoryImpl.removeGenre(savedFilm1.getId(), savedGenre2.getName());
        filmRepositoryImpl.removeGenre(savedFilm2.getId(), savedGenre2.getName());

        assertEquals(2, filmRepositoryImpl.findGenresNames(savedFilm1.getId()).size());
        assertTrue(filmRepositoryImpl.findGenresNames(savedFilm1.getId()).contains(savedGenre1.getName()));
        assertTrue(filmRepositoryImpl.findGenresNames(savedFilm1.getId()).contains(savedGenre3.getName()));
        assertEquals(1, filmRepositoryImpl.findGenresNames(savedFilm2.getId()).size());
        assertTrue(filmRepositoryImpl.findGenresNames(savedFilm2.getId()).contains(savedGenre1.getName()));

    }

}