import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IReferee, getRefereeIdentifier } from '../referee.model';

export type EntityResponseType = HttpResponse<IReferee>;
export type EntityArrayResponseType = HttpResponse<IReferee[]>;

@Injectable({ providedIn: 'root' })
export class RefereeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/referees');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(referee: IReferee): Observable<EntityResponseType> {
    return this.http.post<IReferee>(this.resourceUrl, referee, { observe: 'response' });
  }

  update(referee: IReferee): Observable<EntityResponseType> {
    return this.http.put<IReferee>(`${this.resourceUrl}/${getRefereeIdentifier(referee) as number}`, referee, { observe: 'response' });
  }

  partialUpdate(referee: IReferee): Observable<EntityResponseType> {
    return this.http.patch<IReferee>(`${this.resourceUrl}/${getRefereeIdentifier(referee) as number}`, referee, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IReferee>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IReferee[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addRefereeToCollectionIfMissing(refereeCollection: IReferee[], ...refereesToCheck: (IReferee | null | undefined)[]): IReferee[] {
    const referees: IReferee[] = refereesToCheck.filter(isPresent);
    if (referees.length > 0) {
      const refereeCollectionIdentifiers = refereeCollection.map(refereeItem => getRefereeIdentifier(refereeItem)!);
      const refereesToAdd = referees.filter(refereeItem => {
        const refereeIdentifier = getRefereeIdentifier(refereeItem);
        if (refereeIdentifier == null || refereeCollectionIdentifiers.includes(refereeIdentifier)) {
          return false;
        }
        refereeCollectionIdentifiers.push(refereeIdentifier);
        return true;
      });
      return [...refereesToAdd, ...refereeCollection];
    }
    return refereeCollection;
  }
}
