import { IMatchStats } from 'app/entities/match-stats/match-stats.model';
import { IPlayer } from 'app/entities/player/player.model';
import { ActionType } from 'app/entities/enumerations/action-type.model';

export interface IAction {
  id?: number;
  minute?: number | null;
  second?: number | null;
  period?: number | null;
  type?: ActionType;
  status?: number | null;
  matchStats?: IMatchStats | null;
  players?: IPlayer[] | null;
}

export class Action implements IAction {
  constructor(
    public id?: number,
    public minute?: number | null,
    public second?: number | null,
    public period?: number | null,
    public type?: ActionType,
    public status?: number | null,
    public matchStats?: IMatchStats | null,
    public players?: IPlayer[] | null
  ) {}
}

export function getActionIdentifier(action: IAction): number | undefined {
  return action.id;
}
