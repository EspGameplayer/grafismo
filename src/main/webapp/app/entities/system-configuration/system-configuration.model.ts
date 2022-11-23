import dayjs from 'dayjs/esm';
import { ISeason } from 'app/entities/season/season.model';
import { ISponsor } from 'app/entities/sponsor/sponsor.model';

export interface ISystemConfiguration {
  id?: number;
  currentPeriodStartMoment?: dayjs.Dayjs | null;
  currentSeason?: ISeason | null;
  defaultSponsorLogo?: ISponsor | null;
  defaultMotmSponsorLogo?: ISponsor | null;
}

export class SystemConfiguration implements ISystemConfiguration {
  constructor(
    public id?: number,
    public currentPeriodStartMoment?: dayjs.Dayjs | null,
    public currentSeason?: ISeason | null,
    public defaultSponsorLogo?: ISponsor | null,
    public defaultMotmSponsorLogo?: ISponsor | null
  ) {}
}

export function getSystemConfigurationIdentifier(systemConfiguration: ISystemConfiguration): number | undefined {
  return systemConfiguration.id;
}
