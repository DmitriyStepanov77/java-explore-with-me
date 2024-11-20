package ru.practicum.explore.model.enums;

public enum CommentState {
    PENDING,
    PUBLISHED,
    REJECTED,
    UNKNOWN;

    public static CommentState convert(String str) {
        for (CommentState eventState : CommentState.values()) {
            if (eventState.toString().equals(str)) {
                return eventState;
            }
        }
        return UNKNOWN;
    }
}
