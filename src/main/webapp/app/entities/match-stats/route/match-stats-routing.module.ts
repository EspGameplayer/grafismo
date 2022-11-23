import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { MatchStatsComponent } from '../list/match-stats.component';
import { MatchStatsDetailComponent } from '../detail/match-stats-detail.component';
import { MatchStatsUpdateComponent } from '../update/match-stats-update.component';
import { MatchStatsRoutingResolveService } from './match-stats-routing-resolve.service';

const matchStatsRoute: Routes = [
  {
    path: '',
    component: MatchStatsComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: MatchStatsDetailComponent,
    resolve: {
      matchStats: MatchStatsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: MatchStatsUpdateComponent,
    resolve: {
      matchStats: MatchStatsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: MatchStatsUpdateComponent,
    resolve: {
      matchStats: MatchStatsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(matchStatsRoute)],
  exports: [RouterModule],
})
export class MatchStatsRoutingModule {}
