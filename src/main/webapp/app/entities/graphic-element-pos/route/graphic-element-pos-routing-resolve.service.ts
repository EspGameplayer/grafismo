import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IGraphicElementPos, GraphicElementPos } from '../graphic-element-pos.model';
import { GraphicElementPosService } from '../service/graphic-element-pos.service';

@Injectable({ providedIn: 'root' })
export class GraphicElementPosRoutingResolveService implements Resolve<IGraphicElementPos> {
  constructor(protected service: GraphicElementPosService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IGraphicElementPos> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((graphicElementPos: HttpResponse<GraphicElementPos>) => {
          if (graphicElementPos.body) {
            return of(graphicElementPos.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new GraphicElementPos());
  }
}
