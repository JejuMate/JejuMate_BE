package com.Jejumate.Jejumate_BE.domain.chatbot.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ChatbotAction {

    CHAT("chat"),
    CREATE_SCHEDULE("create_schedule"),
    SUGGEST_ALTERNATIVE("suggest_alternative"),
    UPDATE_SCHEDULE("update_schedule"),
    REMOVE_PLACE("remove_place");

    private final String value;

    //JSON -> Enum 변환 ex) create_schedule -> CREATE_SCHEDULE
    @JsonCreator
    public static ChatbotAction from(String value) {
        for (ChatbotAction action : ChatbotAction.values()) {
            if (action.getValue().equals(value)) {
                return action;
            }
        }
        return null;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}