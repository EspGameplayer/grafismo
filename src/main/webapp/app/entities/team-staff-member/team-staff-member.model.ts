import { IPerson } from 'app/entities/person/person.model';
import { ITeam } from 'app/entities/team/team.model';
import { StaffMemberRole } from 'app/entities/enumerations/staff-member-role.model';

export interface ITeamStaffMember {
  id?: number;
  role?: StaffMemberRole | null;
  person?: IPerson;
  team?: ITeam | null;
}

export class TeamStaffMember implements ITeamStaffMember {
  constructor(public id?: number, public role?: StaffMemberRole | null, public person?: IPerson, public team?: ITeam | null) {}
}

export function getTeamStaffMemberIdentifier(teamStaffMember: ITeamStaffMember): number | undefined {
  return teamStaffMember.id;
}
