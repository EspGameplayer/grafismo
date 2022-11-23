import { ICompetition } from 'app/entities/competition/competition.model';

export interface IMatchday {
  id?: number;
  name?: string;
  graphicsName?: string | null;
  number?: number | null;
  competition?: ICompetition | null;
}

export class Matchday implements IMatchday {
  constructor(
    public id?: number,
    public name?: string,
    public graphicsName?: string | null,
    public number?: number | null,
    public competition?: ICompetition | null
  ) {}
}

export function getMatchdayIdentifier(matchday: IMatchday): number | undefined {
  return matchday.id;
}
