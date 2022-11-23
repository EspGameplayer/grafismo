import { IPerson } from 'app/entities/person/person.model';
import { ITeam } from 'app/entities/team/team.model';
import { IPosition } from 'app/entities/position/position.model';
import { IAction } from 'app/entities/action/action.model';
import { Foot } from 'app/entities/enumerations/foot.model';

export interface IPlayer {
  id?: number;
  strongerFoot?: Foot | null;
  height?: number | null;
  weight?: number | null;
  photoContentType?: string | null;
  photo?: string | null;
  person?: IPerson;
  team?: ITeam | null;
  positions?: IPosition[] | null;
  actions?: IAction[] | null;
}

export class Player implements IPlayer {
  constructor(
    public id?: number,
    public strongerFoot?: Foot | null,
    public height?: number | null,
    public weight?: number | null,
    public photoContentType?: string | null,
    public photo?: string | null,
    public person?: IPerson,
    public team?: ITeam | null,
    public positions?: IPosition[] | null,
    public actions?: IAction[] | null
  ) {}
}

export function getPlayerIdentifier(player: IPlayer): number | undefined {
  return player.id;
}
