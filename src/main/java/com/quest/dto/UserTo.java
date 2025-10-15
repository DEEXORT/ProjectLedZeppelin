package com.quest.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class UserTo {
    Long id;
    String login;
    String password;
    Long characterId;
}
