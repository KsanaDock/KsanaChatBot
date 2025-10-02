package com.ksanaDock.model.dto;

import java.util.Map;
import lombok.Data;

@Data
public class PersonalityTestDTO {
    private Map<String, Integer> personalityScores;
} 