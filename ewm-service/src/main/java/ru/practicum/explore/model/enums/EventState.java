package ru.practicum.explore.model.enums;

public enum EventState {
    PENDING,
    PUBLISHED,
    CANCELED,
    UNKNOWN;

    public static EventState convert(String str) {
        for (EventState eventState : EventState.values()) {
            if (eventState.toString().equals(str)) {
                return eventState;
            }
        }
        return UNKNOWN;
    }
}
