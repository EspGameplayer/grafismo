import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IGraphicElementPos, getGraphicElementPosIdentifier } from '../graphic-element-pos.model';

export type EntityResponseType = HttpResponse<IGraphicElementPos>;
export type EntityArrayResponseType = HttpResponse<IGraphicElementPos[]>;

@Injectable({ providedIn: 'root' })
export class GraphicElementPosService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/graphic-element-pos');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(graphicElementPos: IGraphicElementPos): Observable<EntityResponseType> {
    return this.http.post<IGraphicElementPos>(this.resourceUrl, graphicElementPos, { observe: 'response' });
  }

  update(graphicElementPos: IGraphicElementPos): Observable<EntityResponseType> {
    return this.http.put<IGraphicElementPos>(
      `${this.resourceUrl}/${getGraphicElementPosIdentifier(graphicElementPos) as number}`,
      graphicElementPos,
      { observe: 'response' }
    );
  }

  partialUpdate(graphicElementPos: IGraphicElementPos): Observable<EntityResponseType> {
    return this.http.patch<IGraphicElementPos>(
      `${this.resourceUrl}/${getGraphicElementPosIdentifier(graphicElementPos) as number}`,
      graphicElementPos,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IGraphicElementPos>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IGraphicElementPos[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addGraphicElementPosToCollectionIfMissing(
    graphicElementPosCollection: IGraphicElementPos[],
    ...graphicElementPosToCheck: (IGraphicElementPos | null | undefined)[]
  ): IGraphicElementPos[] {
    const graphicElementPos: IGraphicElementPos[] = graphicElementPosToCheck.filter(isPresent);
    if (graphicElementPos.length > 0) {
      const graphicElementPosCollectionIdentifiers = graphicElementPosCollection.map(
        graphicElementPosItem => getGraphicElementPosIdentifier(graphicElementPosItem)!
      );
      const graphicElementPosToAdd = graphicElementPos.filter(graphicElementPosItem => {
        const graphicElementPosIdentifier = getGraphicElementPosIdentifier(graphicElementPosItem);
        if (graphicElementPosIdentifier == null || graphicElementPosCollectionIdentifiers.includes(graphicElementPosIdentifier)) {
          return false;
        }
        graphicElementPosCollectionIdentifiers.push(graphicElementPosIdentifier);
        return true;
      });
      return [...graphicElementPosToAdd, ...graphicElementPosCollection];
    }
    return graphicElementPosCollection;
  }
}
