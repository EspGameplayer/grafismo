import dayjs from 'dayjs/esm';
import { IMatchPlayer } from 'app/entities/match-player/match-player.model';
import { ICallup } from 'app/entities/callup/callup.model';
import { ITeam } from 'app/entities/team/team.model';
import { IStadium } from 'app/entities/stadium/stadium.model';
import { ITeamStaffMember } from 'app/entities/team-staff-member/team-staff-member.model';
import { IShirt } from 'app/entities/shirt/shirt.model';
import { IMatchday } from 'app/entities/matchday/matchday.model';
import { IReferee } from 'app/entities/referee/referee.model';

export interface IMatch {
  id?: number;
  moment?: dayjs.Dayjs | null;
  attendance?: number | null;
  motm?: IMatchPlayer | null;
  homeCallup?: ICallup | null;
  awayCallup?: ICallup | null;
  homeTeam?: ITeam;
  awayTeam?: ITeam;
  stadium?: IStadium | null;
  matchDelegate?: ITeamStaffMember | null;
  homeShirt?: IShirt | null;
  awayShirt?: IShirt | null;
  matchday?: IMatchday | null;
  referees?: IReferee[] | null;
}

export class Match implements IMatch {
  constructor(
    public id?: number,
    public moment?: dayjs.Dayjs | null,
    public attendance?: number | null,
    public motm?: IMatchPlayer | null,
    public homeCallup?: ICallup | null,
    public awayCallup?: ICallup | null,
    public homeTeam?: ITeam,
    public awayTeam?: ITeam,
    public stadium?: IStadium | null,
    public matchDelegate?: ITeamStaffMember | null,
    public homeShirt?: IShirt | null,
    public awayShirt?: IShirt | null,
    public matchday?: IMatchday | null,
    public referees?: IReferee[] | null
  ) {}
}

export function getMatchIdentifier(match: IMatch): number | undefined {
  return match.id;
}
