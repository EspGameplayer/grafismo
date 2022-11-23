import { IMatchPlayer } from 'app/entities/match-player/match-player.model';
import { ITeamStaffMember } from 'app/entities/team-staff-member/team-staff-member.model';
import { IMatch } from 'app/entities/match/match.model';

export interface ICallup {
  id?: number;
  captain?: IMatchPlayer | null;
  dt?: ITeamStaffMember | null;
  dt2?: ITeamStaffMember | null;
  teamDelegate?: ITeamStaffMember | null;
  players?: IMatchPlayer[] | null;
  homeMatch?: IMatch;
  awayMatch?: IMatch;
}

export class Callup implements ICallup {
  constructor(
    public id?: number,
    public captain?: IMatchPlayer | null,
    public dt?: ITeamStaffMember | null,
    public dt2?: ITeamStaffMember | null,
    public teamDelegate?: ITeamStaffMember | null,
    public players?: IMatchPlayer[] | null,
    public homeMatch?: IMatch,
    public awayMatch?: IMatch
  ) {}
}

export function getCallupIdentifier(callup: ICallup): number | undefined {
  return callup.id;
}
