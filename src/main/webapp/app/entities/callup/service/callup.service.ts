import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICallup, getCallupIdentifier } from '../callup.model';

export type EntityResponseType = HttpResponse<ICallup>;
export type EntityArrayResponseType = HttpResponse<ICallup[]>;

@Injectable({ providedIn: 'root' })
export class CallupService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/callups');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(callup: ICallup): Observable<EntityResponseType> {
    return this.http.post<ICallup>(this.resourceUrl, callup, { observe: 'response' });
  }

  update(callup: ICallup): Observable<EntityResponseType> {
    return this.http.put<ICallup>(`${this.resourceUrl}/${getCallupIdentifier(callup) as number}`, callup, { observe: 'response' });
  }

  partialUpdate(callup: ICallup): Observable<EntityResponseType> {
    return this.http.patch<ICallup>(`${this.resourceUrl}/${getCallupIdentifier(callup) as number}`, callup, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICallup>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICallup[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addCallupToCollectionIfMissing(callupCollection: ICallup[], ...callupsToCheck: (ICallup | null | undefined)[]): ICallup[] {
    const callups: ICallup[] = callupsToCheck.filter(isPresent);
    if (callups.length > 0) {
      const callupCollectionIdentifiers = callupCollection.map(callupItem => getCallupIdentifier(callupItem)!);
      const callupsToAdd = callups.filter(callupItem => {
        const callupIdentifier = getCallupIdentifier(callupItem);
        if (callupIdentifier == null || callupCollectionIdentifiers.includes(callupIdentifier)) {
          return false;
        }
        callupCollectionIdentifiers.push(callupIdentifier);
        return true;
      });
      return [...callupsToAdd, ...callupCollection];
    }
    return callupCollection;
  }
}
