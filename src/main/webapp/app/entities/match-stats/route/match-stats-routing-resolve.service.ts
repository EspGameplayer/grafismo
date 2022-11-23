import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IMatchStats, MatchStats } from '../match-stats.model';
import { MatchStatsService } from '../service/match-stats.service';

@Injectable({ providedIn: 'root' })
export class MatchStatsRoutingResolveService implements Resolve<IMatchStats> {
  constructor(protected service: MatchStatsService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IMatchStats> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((matchStats: HttpResponse<MatchStats>) => {
          if (matchStats.body) {
            return of(matchStats.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new MatchStats());
  }
}
