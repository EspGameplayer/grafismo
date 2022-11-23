import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITeamStaffMember, TeamStaffMember } from '../team-staff-member.model';
import { TeamStaffMemberService } from '../service/team-staff-member.service';

@Injectable({ providedIn: 'root' })
export class TeamStaffMemberRoutingResolveService implements Resolve<ITeamStaffMember> {
  constructor(protected service: TeamStaffMemberService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITeamStaffMember> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((teamStaffMember: HttpResponse<TeamStaffMember>) => {
          if (teamStaffMember.body) {
            return of(teamStaffMember.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new TeamStaffMember());
  }
}
