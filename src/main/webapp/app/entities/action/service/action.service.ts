import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAction, getActionIdentifier } from '../action.model';

export type EntityResponseType = HttpResponse<IAction>;
export type EntityArrayResponseType = HttpResponse<IAction[]>;

@Injectable({ providedIn: 'root' })
export class ActionService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/actions');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(action: IAction): Observable<EntityResponseType> {
    return this.http.post<IAction>(this.resourceUrl, action, { observe: 'response' });
  }

  update(action: IAction): Observable<EntityResponseType> {
    return this.http.put<IAction>(`${this.resourceUrl}/${getActionIdentifier(action) as number}`, action, { observe: 'response' });
  }

  partialUpdate(action: IAction): Observable<EntityResponseType> {
    return this.http.patch<IAction>(`${this.resourceUrl}/${getActionIdentifier(action) as number}`, action, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IAction>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAction[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addActionToCollectionIfMissing(actionCollection: IAction[], ...actionsToCheck: (IAction | null | undefined)[]): IAction[] {
    const actions: IAction[] = actionsToCheck.filter(isPresent);
    if (actions.length > 0) {
      const actionCollectionIdentifiers = actionCollection.map(actionItem => getActionIdentifier(actionItem)!);
      const actionsToAdd = actions.filter(actionItem => {
        const actionIdentifier = getActionIdentifier(actionItem);
        if (actionIdentifier == null || actionCollectionIdentifiers.includes(actionIdentifier)) {
          return false;
        }
        actionCollectionIdentifiers.push(actionIdentifier);
        return true;
      });
      return [...actionsToAdd, ...actionCollection];
    }
    return actionCollection;
  }
}
