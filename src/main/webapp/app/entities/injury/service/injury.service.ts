import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IInjury, getInjuryIdentifier } from '../injury.model';

export type EntityResponseType = HttpResponse<IInjury>;
export type EntityArrayResponseType = HttpResponse<IInjury[]>;

@Injectable({ providedIn: 'root' })
export class InjuryService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/injuries');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(injury: IInjury): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(injury);
    return this.http
      .post<IInjury>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(injury: IInjury): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(injury);
    return this.http
      .put<IInjury>(`${this.resourceUrl}/${getInjuryIdentifier(injury) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(injury: IInjury): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(injury);
    return this.http
      .patch<IInjury>(`${this.resourceUrl}/${getInjuryIdentifier(injury) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IInjury>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IInjury[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addInjuryToCollectionIfMissing(injuryCollection: IInjury[], ...injuriesToCheck: (IInjury | null | undefined)[]): IInjury[] {
    const injuries: IInjury[] = injuriesToCheck.filter(isPresent);
    if (injuries.length > 0) {
      const injuryCollectionIdentifiers = injuryCollection.map(injuryItem => getInjuryIdentifier(injuryItem)!);
      const injuriesToAdd = injuries.filter(injuryItem => {
        const injuryIdentifier = getInjuryIdentifier(injuryItem);
        if (injuryIdentifier == null || injuryCollectionIdentifiers.includes(injuryIdentifier)) {
          return false;
        }
        injuryCollectionIdentifiers.push(injuryIdentifier);
        return true;
      });
      return [...injuriesToAdd, ...injuryCollection];
    }
    return injuryCollection;
  }

  protected convertDateFromClient(injury: IInjury): IInjury {
    return Object.assign({}, injury, {
      moment: injury.moment?.isValid() ? injury.moment.toJSON() : undefined,
      estReturnDate: injury.estReturnDate?.isValid() ? injury.estReturnDate.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.moment = res.body.moment ? dayjs(res.body.moment) : undefined;
      res.body.estReturnDate = res.body.estReturnDate ? dayjs(res.body.estReturnDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((injury: IInjury) => {
        injury.moment = injury.moment ? dayjs(injury.moment) : undefined;
        injury.estReturnDate = injury.estReturnDate ? dayjs(injury.estReturnDate) : undefined;
      });
    }
    return res;
  }
}
