package io.getarrays.userservice.consumer.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Player {
    private Long id;
    private Integer x;
    private Integer y;
    private Set<Character> steps;
}
