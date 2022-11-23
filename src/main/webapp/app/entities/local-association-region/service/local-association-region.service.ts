import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ILocalAssociationRegion, getLocalAssociationRegionIdentifier } from '../local-association-region.model';

export type EntityResponseType = HttpResponse<ILocalAssociationRegion>;
export type EntityArrayResponseType = HttpResponse<ILocalAssociationRegion[]>;

@Injectable({ providedIn: 'root' })
export class LocalAssociationRegionService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/local-association-regions');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(localAssociationRegion: ILocalAssociationRegion): Observable<EntityResponseType> {
    return this.http.post<ILocalAssociationRegion>(this.resourceUrl, localAssociationRegion, { observe: 'response' });
  }

  update(localAssociationRegion: ILocalAssociationRegion): Observable<EntityResponseType> {
    return this.http.put<ILocalAssociationRegion>(
      `${this.resourceUrl}/${getLocalAssociationRegionIdentifier(localAssociationRegion) as number}`,
      localAssociationRegion,
      { observe: 'response' }
    );
  }

  partialUpdate(localAssociationRegion: ILocalAssociationRegion): Observable<EntityResponseType> {
    return this.http.patch<ILocalAssociationRegion>(
      `${this.resourceUrl}/${getLocalAssociationRegionIdentifier(localAssociationRegion) as number}`,
      localAssociationRegion,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ILocalAssociationRegion>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ILocalAssociationRegion[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addLocalAssociationRegionToCollectionIfMissing(
    localAssociationRegionCollection: ILocalAssociationRegion[],
    ...localAssociationRegionsToCheck: (ILocalAssociationRegion | null | undefined)[]
  ): ILocalAssociationRegion[] {
    const localAssociationRegions: ILocalAssociationRegion[] = localAssociationRegionsToCheck.filter(isPresent);
    if (localAssociationRegions.length > 0) {
      const localAssociationRegionCollectionIdentifiers = localAssociationRegionCollection.map(
        localAssociationRegionItem => getLocalAssociationRegionIdentifier(localAssociationRegionItem)!
      );
      const localAssociationRegionsToAdd = localAssociationRegions.filter(localAssociationRegionItem => {
        const localAssociationRegionIdentifier = getLocalAssociationRegionIdentifier(localAssociationRegionItem);
        if (
          localAssociationRegionIdentifier == null ||
          localAssociationRegionCollectionIdentifiers.includes(localAssociationRegionIdentifier)
        ) {
          return false;
        }
        localAssociationRegionCollectionIdentifiers.push(localAssociationRegionIdentifier);
        return true;
      });
      return [...localAssociationRegionsToAdd, ...localAssociationRegionCollection];
    }
    return localAssociationRegionCollection;
  }
}
