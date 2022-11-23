import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISystemConfiguration, getSystemConfigurationIdentifier } from '../system-configuration.model';

export type EntityResponseType = HttpResponse<ISystemConfiguration>;
export type EntityArrayResponseType = HttpResponse<ISystemConfiguration[]>;

@Injectable({ providedIn: 'root' })
export class SystemConfigurationService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/system-configurations');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(systemConfiguration: ISystemConfiguration): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(systemConfiguration);
    return this.http
      .post<ISystemConfiguration>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(systemConfiguration: ISystemConfiguration): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(systemConfiguration);
    return this.http
      .put<ISystemConfiguration>(`${this.resourceUrl}/${getSystemConfigurationIdentifier(systemConfiguration) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(systemConfiguration: ISystemConfiguration): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(systemConfiguration);
    return this.http
      .patch<ISystemConfiguration>(`${this.resourceUrl}/${getSystemConfigurationIdentifier(systemConfiguration) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ISystemConfiguration>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ISystemConfiguration[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addSystemConfigurationToCollectionIfMissing(
    systemConfigurationCollection: ISystemConfiguration[],
    ...systemConfigurationsToCheck: (ISystemConfiguration | null | undefined)[]
  ): ISystemConfiguration[] {
    const systemConfigurations: ISystemConfiguration[] = systemConfigurationsToCheck.filter(isPresent);
    if (systemConfigurations.length > 0) {
      const systemConfigurationCollectionIdentifiers = systemConfigurationCollection.map(
        systemConfigurationItem => getSystemConfigurationIdentifier(systemConfigurationItem)!
      );
      const systemConfigurationsToAdd = systemConfigurations.filter(systemConfigurationItem => {
        const systemConfigurationIdentifier = getSystemConfigurationIdentifier(systemConfigurationItem);
        if (systemConfigurationIdentifier == null || systemConfigurationCollectionIdentifiers.includes(systemConfigurationIdentifier)) {
          return false;
        }
        systemConfigurationCollectionIdentifiers.push(systemConfigurationIdentifier);
        return true;
      });
      return [...systemConfigurationsToAdd, ...systemConfigurationCollection];
    }
    return systemConfigurationCollection;
  }

  protected convertDateFromClient(systemConfiguration: ISystemConfiguration): ISystemConfiguration {
    return Object.assign({}, systemConfiguration, {
      currentPeriodStartMoment: systemConfiguration.currentPeriodStartMoment?.isValid()
        ? systemConfiguration.currentPeriodStartMoment.toJSON()
        : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.currentPeriodStartMoment = res.body.currentPeriodStartMoment ? dayjs(res.body.currentPeriodStartMoment) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((systemConfiguration: ISystemConfiguration) => {
        systemConfiguration.currentPeriodStartMoment = systemConfiguration.currentPeriodStartMoment
          ? dayjs(systemConfiguration.currentPeriodStartMoment)
          : undefined;
      });
    }
    return res;
  }
}
