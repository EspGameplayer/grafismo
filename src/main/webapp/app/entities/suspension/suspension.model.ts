import dayjs from 'dayjs/esm';
import { IPerson } from 'app/entities/person/person.model';
import { ICompetition } from 'app/entities/competition/competition.model';

export interface ISuspension {
  id?: number;
  matches?: number | null;
  moment?: dayjs.Dayjs | null;
  reason?: string | null;
  person?: IPerson;
  competition?: ICompetition;
}

export class Suspension implements ISuspension {
  constructor(
    public id?: number,
    public matches?: number | null,
    public moment?: dayjs.Dayjs | null,
    public reason?: string | null,
    public person?: IPerson,
    public competition?: ICompetition
  ) {}
}

export function getSuspensionIdentifier(suspension: ISuspension): number | undefined {
  return suspension.id;
}
