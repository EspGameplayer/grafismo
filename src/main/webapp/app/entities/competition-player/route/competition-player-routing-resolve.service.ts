import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICompetitionPlayer, CompetitionPlayer } from '../competition-player.model';
import { CompetitionPlayerService } from '../service/competition-player.service';

@Injectable({ providedIn: 'root' })
export class CompetitionPlayerRoutingResolveService implements Resolve<ICompetitionPlayer> {
  constructor(protected service: CompetitionPlayerService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICompetitionPlayer> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((competitionPlayer: HttpResponse<CompetitionPlayer>) => {
          if (competitionPlayer.body) {
            return of(competitionPlayer.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new CompetitionPlayer());
  }
}
