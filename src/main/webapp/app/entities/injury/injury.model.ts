import dayjs from 'dayjs/esm';
import { IPlayer } from 'app/entities/player/player.model';

export interface IInjury {
  id?: number;
  moment?: dayjs.Dayjs | null;
  estReturnDate?: dayjs.Dayjs | null;
  reason?: string | null;
  player?: IPlayer;
}

export class Injury implements IInjury {
  constructor(
    public id?: number,
    public moment?: dayjs.Dayjs | null,
    public estReturnDate?: dayjs.Dayjs | null,
    public reason?: string | null,
    public player?: IPlayer
  ) {}
}

export function getInjuryIdentifier(injury: IInjury): number | undefined {
  return injury.id;
}
