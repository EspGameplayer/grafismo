import { IPerson } from 'app/entities/person/person.model';

export interface IBroadcastStaffMember {
  id?: number;
  person?: IPerson;
}

export class BroadcastStaffMember implements IBroadcastStaffMember {
  constructor(public id?: number, public person?: IPerson) {}
}

export function getBroadcastStaffMemberIdentifier(broadcastStaffMember: IBroadcastStaffMember): number | undefined {
  return broadcastStaffMember.id;
}
