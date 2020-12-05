CREATE TABLE team
(
    id       UUID    NOT NULL,
    version  BIGINT  NOT NULL,
    name     VARCHAR NOT NULL,
    coach_id UUID    NOT NULL,
    PRIMARY KEY (id)
);

ALTER TABLE team
    ADD CONSTRAINT FK_team_to_user FOREIGN KEY (coach_id) REFERENCES tt_user;

CREATE TABLE team_player -- <.>
(
    id        UUID    NOT NULL,
    team_id   UUID    NOT NULL, -- <.>
    player_id UUID    NOT NULL, -- <.>
    position  VARCHAR NOT NULL, -- <.>
    PRIMARY KEY (id)
);

ALTER TABLE team_player
    ADD CONSTRAINT FK_team_player_to_team FOREIGN KEY (team_id) REFERENCES team; -- <.>
ALTER TABLE team_player
    ADD CONSTRAINT FK_team_player_to_user FOREIGN KEY (player_id) REFERENCES tt_user;
