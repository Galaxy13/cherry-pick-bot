package com.galaxy13.cherrypickbot.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum CommandType {
    CHERRYPICK("cherry-pick"),
    MERGE("merge");

    private final String command;
}
