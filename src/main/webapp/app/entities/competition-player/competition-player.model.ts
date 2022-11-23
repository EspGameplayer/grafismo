import { IPlayer } from 'app/entities/player/player.model';
import { ICompetition } from 'app/entities/competition/competition.model';
import { IPosition } from 'app/entities/position/position.model';

export interface ICompetitionPlayer {
  id?: number;
  preferredShirtNumber?: number | null;
  player?: IPlayer;
  competition?: ICompetition;
  preferredPositions?: IPosition[] | null;
}

export class CompetitionPlayer implements ICompetitionPlayer {
  constructor(
    public id?: number,
    public preferredShirtNumber?: number | null,
    public player?: IPlayer,
    public competition?: ICompetition,
    public preferredPositions?: IPosition[] | null
  ) {}
}

export function getCompetitionPlayerIdentifier(competitionPlayer: ICompetitionPlayer): number | undefined {
  return competitionPlayer.id;
}
