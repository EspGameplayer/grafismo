import { ICallup } from 'app/entities/callup/callup.model';
import { IFormation } from 'app/entities/formation/formation.model';
import { IMatchPlayer } from 'app/entities/match-player/match-player.model';

export interface ILineup {
  id?: number;
  callup?: ICallup;
  formation?: IFormation | null;
  players?: IMatchPlayer[] | null;
}

export class Lineup implements ILineup {
  constructor(public id?: number, public callup?: ICallup, public formation?: IFormation | null, public players?: IMatchPlayer[] | null) {}
}

export function getLineupIdentifier(lineup: ILineup): number | undefined {
  return lineup.id;
}
