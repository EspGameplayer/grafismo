import { IPlayer } from 'app/entities/player/player.model';
import { ICompetitionPlayer } from 'app/entities/competition-player/competition-player.model';

export interface IPosition {
  id?: number;
  name?: string;
  abb?: string;
  parents?: IPosition[] | null;
  children?: IPosition[] | null;
  players?: IPlayer[] | null;
  competitionPlayers?: ICompetitionPlayer[] | null;
}

export class Position implements IPosition {
  constructor(
    public id?: number,
    public name?: string,
    public abb?: string,
    public parents?: IPosition[] | null,
    public children?: IPosition[] | null,
    public players?: IPlayer[] | null,
    public competitionPlayers?: ICompetitionPlayer[] | null
  ) {}
}

export function getPositionIdentifier(position: IPosition): number | undefined {
  return position.id;
}
