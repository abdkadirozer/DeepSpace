package com.example.server.model.projection;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ScoreBoardDto {
    private String username;
    private Integer point;
    private LocalDateTime date;


    public static final class ScoreBoardDtoBuilder {
        private String username;
        private Integer point;
        private LocalDateTime date;

        private ScoreBoardDtoBuilder() {
        }

        public static ScoreBoardDtoBuilder aScoreBoardDto() {
            return new ScoreBoardDtoBuilder();
        }

        public ScoreBoardDtoBuilder setUsername(String username) {
            this.username = username;
            return this;
        }

        public ScoreBoardDtoBuilder setPoint(Integer point) {
            this.point = point;
            return this;
        }

        public ScoreBoardDtoBuilder setDate(LocalDateTime date) {
            this.date = date;
            return this;
        }

        public ScoreBoardDto build() {
            ScoreBoardDto scoreBoardDto = new ScoreBoardDto();
            scoreBoardDto.setUsername(username);
            scoreBoardDto.setPoint(point);
            scoreBoardDto.setDate(date);
            return scoreBoardDto;
        }
    }
}
