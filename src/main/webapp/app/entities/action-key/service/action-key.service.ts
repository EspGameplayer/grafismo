import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IActionKey, getActionKeyIdentifier } from '../action-key.model';

export type EntityResponseType = HttpResponse<IActionKey>;
export type EntityArrayResponseType = HttpResponse<IActionKey[]>;

@Injectable({ providedIn: 'root' })
export class ActionKeyService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/action-keys');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(actionKey: IActionKey): Observable<EntityResponseType> {
    return this.http.post<IActionKey>(this.resourceUrl, actionKey, { observe: 'response' });
  }

  update(actionKey: IActionKey): Observable<EntityResponseType> {
    return this.http.put<IActionKey>(`${this.resourceUrl}/${getActionKeyIdentifier(actionKey) as number}`, actionKey, {
      observe: 'response',
    });
  }

  partialUpdate(actionKey: IActionKey): Observable<EntityResponseType> {
    return this.http.patch<IActionKey>(`${this.resourceUrl}/${getActionKeyIdentifier(actionKey) as number}`, actionKey, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IActionKey>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IActionKey[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addActionKeyToCollectionIfMissing(
    actionKeyCollection: IActionKey[],
    ...actionKeysToCheck: (IActionKey | null | undefined)[]
  ): IActionKey[] {
    const actionKeys: IActionKey[] = actionKeysToCheck.filter(isPresent);
    if (actionKeys.length > 0) {
      const actionKeyCollectionIdentifiers = actionKeyCollection.map(actionKeyItem => getActionKeyIdentifier(actionKeyItem)!);
      const actionKeysToAdd = actionKeys.filter(actionKeyItem => {
        const actionKeyIdentifier = getActionKeyIdentifier(actionKeyItem);
        if (actionKeyIdentifier == null || actionKeyCollectionIdentifiers.includes(actionKeyIdentifier)) {
          return false;
        }
        actionKeyCollectionIdentifiers.push(actionKeyIdentifier);
        return true;
      });
      return [...actionKeysToAdd, ...actionKeyCollection];
    }
    return actionKeyCollection;
  }
}
