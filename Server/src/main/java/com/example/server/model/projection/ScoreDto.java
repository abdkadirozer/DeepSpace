package com.example.server.model.projection;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ScoreDto {

    private Long id;
    private String username;
    private Integer point;
    private LocalDateTime date;

    public static final class ScoreDtoBuilder {
        Long id;
        String username;
        Integer point;
        LocalDateTime date;

        private ScoreDtoBuilder() {
        }

        public static ScoreDtoBuilder aScoreDto() {
            return new ScoreDtoBuilder();
        }

        public ScoreDtoBuilder setId(Long id) {
            this.id = id;
            return this;
        }

        public ScoreDtoBuilder setUsername(String username) {
            this.username = username;
            return this;
        }

        public ScoreDtoBuilder setPoint(Integer point) {
            this.point = point;
            return this;
        }

        public ScoreDtoBuilder setDate(LocalDateTime date) {
            this.date = date;
            return this;
        }

        public ScoreDto build() {
            ScoreDto scoreDto = new ScoreDto();
            scoreDto.setId(id);
            scoreDto.setUsername(username);
            scoreDto.setPoint(point);
            scoreDto.setDate(date);
            return scoreDto;
        }
    }
}
