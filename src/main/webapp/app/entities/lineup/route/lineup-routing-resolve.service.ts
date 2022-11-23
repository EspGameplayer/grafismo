import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ILineup, Lineup } from '../lineup.model';
import { LineupService } from '../service/lineup.service';

@Injectable({ providedIn: 'root' })
export class LineupRoutingResolveService implements Resolve<ILineup> {
  constructor(protected service: LineupService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ILineup> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((lineup: HttpResponse<Lineup>) => {
          if (lineup.body) {
            return of(lineup.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Lineup());
  }
}
