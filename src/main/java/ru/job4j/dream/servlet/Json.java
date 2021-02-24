package ru.job4j.dream.servlet;

import java.util.Objects;

public class Json {
    private String answer;

    public Json(String answer) {
        this.answer = answer;
    }

    public String getAnswer() {
        return answer;
    }

    public Json setAnswer(String answer) {
        this.answer = answer;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Json json = (Json) o;
        return Objects.equals(answer, json.answer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(answer);
    }

    @Override
    public String toString() {
        return "Json{" +
                "answer='" + answer + '\'' +
                '}';
    }
}
