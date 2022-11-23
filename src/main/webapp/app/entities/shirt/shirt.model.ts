import { ITeam } from 'app/entities/team/team.model';
import { ISeason } from 'app/entities/season/season.model';

export interface IShirt {
  id?: number;
  colour1?: string | null;
  colour2?: string | null;
  type?: number | null;
  team?: ITeam | null;
  season?: ISeason | null;
}

export class Shirt implements IShirt {
  constructor(
    public id?: number,
    public colour1?: string | null,
    public colour2?: string | null,
    public type?: number | null,
    public team?: ITeam | null,
    public season?: ISeason | null
  ) {}
}

export function getShirtIdentifier(shirt: IShirt): number | undefined {
  return shirt.id;
}
