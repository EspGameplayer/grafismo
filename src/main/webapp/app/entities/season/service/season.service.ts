import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISeason, getSeasonIdentifier } from '../season.model';

export type EntityResponseType = HttpResponse<ISeason>;
export type EntityArrayResponseType = HttpResponse<ISeason[]>;

@Injectable({ providedIn: 'root' })
export class SeasonService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/seasons');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(season: ISeason): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(season);
    return this.http
      .post<ISeason>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(season: ISeason): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(season);
    return this.http
      .put<ISeason>(`${this.resourceUrl}/${getSeasonIdentifier(season) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(season: ISeason): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(season);
    return this.http
      .patch<ISeason>(`${this.resourceUrl}/${getSeasonIdentifier(season) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ISeason>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ISeason[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addSeasonToCollectionIfMissing(seasonCollection: ISeason[], ...seasonsToCheck: (ISeason | null | undefined)[]): ISeason[] {
    const seasons: ISeason[] = seasonsToCheck.filter(isPresent);
    if (seasons.length > 0) {
      const seasonCollectionIdentifiers = seasonCollection.map(seasonItem => getSeasonIdentifier(seasonItem)!);
      const seasonsToAdd = seasons.filter(seasonItem => {
        const seasonIdentifier = getSeasonIdentifier(seasonItem);
        if (seasonIdentifier == null || seasonCollectionIdentifiers.includes(seasonIdentifier)) {
          return false;
        }
        seasonCollectionIdentifiers.push(seasonIdentifier);
        return true;
      });
      return [...seasonsToAdd, ...seasonCollection];
    }
    return seasonCollection;
  }

  protected convertDateFromClient(season: ISeason): ISeason {
    return Object.assign({}, season, {
      startDate: season.startDate?.isValid() ? season.startDate.format(DATE_FORMAT) : undefined,
      endDate: season.endDate?.isValid() ? season.endDate.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.startDate = res.body.startDate ? dayjs(res.body.startDate) : undefined;
      res.body.endDate = res.body.endDate ? dayjs(res.body.endDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((season: ISeason) => {
        season.startDate = season.startDate ? dayjs(season.startDate) : undefined;
        season.endDate = season.endDate ? dayjs(season.endDate) : undefined;
      });
    }
    return res;
  }
}
