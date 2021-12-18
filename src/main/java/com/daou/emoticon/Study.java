package com.daou.emoticon;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Study {

    private StudyStatus status;
    private int limit;
    private String name;

    public Study(int limit) {
        if (limit < 0) {
            throw new IllegalArgumentException("limit은 0보다 커야한다.");
        }
        this.limit = limit;
    }

    public Study(int limit, String name) {
        if (limit < 0) {
            throw new IllegalArgumentException("limit은 0보다 커야한다.");
        }
        this.limit = limit;
        this.name = name;
    }

}
