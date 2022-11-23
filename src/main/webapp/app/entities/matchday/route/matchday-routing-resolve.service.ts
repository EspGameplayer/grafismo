import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IMatchday, Matchday } from '../matchday.model';
import { MatchdayService } from '../service/matchday.service';

@Injectable({ providedIn: 'root' })
export class MatchdayRoutingResolveService implements Resolve<IMatchday> {
  constructor(protected service: MatchdayService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IMatchday> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((matchday: HttpResponse<Matchday>) => {
          if (matchday.body) {
            return of(matchday.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Matchday());
  }
}
