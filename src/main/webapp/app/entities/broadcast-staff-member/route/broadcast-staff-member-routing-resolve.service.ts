import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IBroadcastStaffMember, BroadcastStaffMember } from '../broadcast-staff-member.model';
import { BroadcastStaffMemberService } from '../service/broadcast-staff-member.service';

@Injectable({ providedIn: 'root' })
export class BroadcastStaffMemberRoutingResolveService implements Resolve<IBroadcastStaffMember> {
  constructor(protected service: BroadcastStaffMemberService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IBroadcastStaffMember> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((broadcastStaffMember: HttpResponse<BroadcastStaffMember>) => {
          if (broadcastStaffMember.body) {
            return of(broadcastStaffMember.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new BroadcastStaffMember());
  }
}
