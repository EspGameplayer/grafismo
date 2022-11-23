import { IMatch } from 'app/entities/match/match.model';
import { IAction } from 'app/entities/action/action.model';

export interface IMatchStats {
  id?: number;
  homePossessionTime?: number | null;
  awayPossessionTime?: number | null;
  inContestPossessionTime?: number | null;
  match?: IMatch;
  actions?: IAction[] | null;
}

export class MatchStats implements IMatchStats {
  constructor(
    public id?: number,
    public homePossessionTime?: number | null,
    public awayPossessionTime?: number | null,
    public inContestPossessionTime?: number | null,
    public match?: IMatch,
    public actions?: IAction[] | null
  ) {}
}

export function getMatchStatsIdentifier(matchStats: IMatchStats): number | undefined {
  return matchStats.id;
}
