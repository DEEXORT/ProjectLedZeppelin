package com.quest.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AchievementTo {
    Long id;
    String text;
}
