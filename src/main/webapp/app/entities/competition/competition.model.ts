import { ISponsor } from 'app/entities/sponsor/sponsor.model';
import { ITeam } from 'app/entities/team/team.model';
import { CompetitionType } from 'app/entities/enumerations/competition-type.model';

export interface ICompetition {
  id?: number;
  name?: string;
  graphicsName?: string;
  type?: CompetitionType;
  colour?: string | null;
  suspensionYcMatches?: number | null;
  sponsor?: ISponsor | null;
  motmSponsor?: ISponsor | null;
  parent?: ICompetition | null;
  teams?: ITeam[] | null;
  children?: ICompetition[] | null;
}

export class Competition implements ICompetition {
  constructor(
    public id?: number,
    public name?: string,
    public graphicsName?: string,
    public type?: CompetitionType,
    public colour?: string | null,
    public suspensionYcMatches?: number | null,
    public sponsor?: ISponsor | null,
    public motmSponsor?: ISponsor | null,
    public parent?: ICompetition | null,
    public teams?: ITeam[] | null,
    public children?: ICompetition[] | null
  ) {}
}

export function getCompetitionIdentifier(competition: ICompetition): number | undefined {
  return competition.id;
}
