package ru.mxk.game.fifteen.configuration;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@Validated
@ConfigurationProperties(prefix = "game")
public class GameOptions {
    @Min(1)
    @Max(30)
    @NotNull
    private Integer size;
}
