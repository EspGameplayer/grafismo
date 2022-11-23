import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAssociation, getAssociationIdentifier } from '../association.model';

export type EntityResponseType = HttpResponse<IAssociation>;
export type EntityArrayResponseType = HttpResponse<IAssociation[]>;

@Injectable({ providedIn: 'root' })
export class AssociationService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/associations');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(association: IAssociation): Observable<EntityResponseType> {
    return this.http.post<IAssociation>(this.resourceUrl, association, { observe: 'response' });
  }

  update(association: IAssociation): Observable<EntityResponseType> {
    return this.http.put<IAssociation>(`${this.resourceUrl}/${getAssociationIdentifier(association) as number}`, association, {
      observe: 'response',
    });
  }

  partialUpdate(association: IAssociation): Observable<EntityResponseType> {
    return this.http.patch<IAssociation>(`${this.resourceUrl}/${getAssociationIdentifier(association) as number}`, association, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IAssociation>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAssociation[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addAssociationToCollectionIfMissing(
    associationCollection: IAssociation[],
    ...associationsToCheck: (IAssociation | null | undefined)[]
  ): IAssociation[] {
    const associations: IAssociation[] = associationsToCheck.filter(isPresent);
    if (associations.length > 0) {
      const associationCollectionIdentifiers = associationCollection.map(associationItem => getAssociationIdentifier(associationItem)!);
      const associationsToAdd = associations.filter(associationItem => {
        const associationIdentifier = getAssociationIdentifier(associationItem);
        if (associationIdentifier == null || associationCollectionIdentifiers.includes(associationIdentifier)) {
          return false;
        }
        associationCollectionIdentifiers.push(associationIdentifier);
        return true;
      });
      return [...associationsToAdd, ...associationCollection];
    }
    return associationCollection;
  }
}
