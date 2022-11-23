import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { MatchdayComponent } from '../list/matchday.component';
import { MatchdayDetailComponent } from '../detail/matchday-detail.component';
import { MatchdayUpdateComponent } from '../update/matchday-update.component';
import { MatchdayRoutingResolveService } from './matchday-routing-resolve.service';

const matchdayRoute: Routes = [
  {
    path: '',
    component: MatchdayComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: MatchdayDetailComponent,
    resolve: {
      matchday: MatchdayRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: MatchdayUpdateComponent,
    resolve: {
      matchday: MatchdayRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: MatchdayUpdateComponent,
    resolve: {
      matchday: MatchdayRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(matchdayRoute)],
  exports: [RouterModule],
})
export class MatchdayRoutingModule {}
