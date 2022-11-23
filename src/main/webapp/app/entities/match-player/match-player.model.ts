import { IPlayer } from 'app/entities/player/player.model';
import { IPosition } from 'app/entities/position/position.model';
import { IMatch } from 'app/entities/match/match.model';
import { ICallup } from 'app/entities/callup/callup.model';
import { ILineup } from 'app/entities/lineup/lineup.model';

export interface IMatchPlayer {
  id?: number;
  shirtNumber?: number | null;
  isWarned?: number | null;
  player?: IPlayer;
  position?: IPosition | null;
  motmMatch?: IMatch;
  captainCallup?: ICallup;
  callups?: ICallup[] | null;
  lineups?: ILineup[] | null;
}

export class MatchPlayer implements IMatchPlayer {
  constructor(
    public id?: number,
    public shirtNumber?: number | null,
    public isWarned?: number | null,
    public player?: IPlayer,
    public position?: IPosition | null,
    public motmMatch?: IMatch,
    public captainCallup?: ICallup,
    public callups?: ICallup[] | null,
    public lineups?: ILineup[] | null
  ) {}
}

export function getMatchPlayerIdentifier(matchPlayer: IMatchPlayer): number | undefined {
  return matchPlayer.id;
}
