import { ITeam } from 'app/entities/team/team.model';

export interface IStadium {
  id?: number;
  name?: string | null;
  graphicsName?: string;
  location?: string | null;
  capacity?: number | null;
  fieldLength?: number | null;
  fieldWidth?: number | null;
  teams?: ITeam[] | null;
}

export class Stadium implements IStadium {
  constructor(
    public id?: number,
    public name?: string | null,
    public graphicsName?: string,
    public location?: string | null,
    public capacity?: number | null,
    public fieldLength?: number | null,
    public fieldWidth?: number | null,
    public teams?: ITeam[] | null
  ) {}
}

export function getStadiumIdentifier(stadium: IStadium): number | undefined {
  return stadium.id;
}
