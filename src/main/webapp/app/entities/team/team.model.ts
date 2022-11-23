import { IFormation } from 'app/entities/formation/formation.model';
import { IStadium } from 'app/entities/stadium/stadium.model';
import { ICompetition } from 'app/entities/competition/competition.model';

export interface ITeam {
  id?: number;
  name?: string;
  graphicsName?: string;
  abb?: string;
  badgeContentType?: string | null;
  badge?: string | null;
  monocBadgeContentType?: string | null;
  monocBadge?: string | null;
  preferredFormation?: IFormation | null;
  stadiums?: IStadium[] | null;
  competitions?: ICompetition[] | null;
}

export class Team implements ITeam {
  constructor(
    public id?: number,
    public name?: string,
    public graphicsName?: string,
    public abb?: string,
    public badgeContentType?: string | null,
    public badge?: string | null,
    public monocBadgeContentType?: string | null,
    public monocBadge?: string | null,
    public preferredFormation?: IFormation | null,
    public stadiums?: IStadium[] | null,
    public competitions?: ICompetition[] | null
  ) {}
}

export function getTeamIdentifier(team: ITeam): number | undefined {
  return team.id;
}
