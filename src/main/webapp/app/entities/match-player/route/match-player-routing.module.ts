import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { MatchPlayerComponent } from '../list/match-player.component';
import { MatchPlayerDetailComponent } from '../detail/match-player-detail.component';
import { MatchPlayerUpdateComponent } from '../update/match-player-update.component';
import { MatchPlayerRoutingResolveService } from './match-player-routing-resolve.service';

const matchPlayerRoute: Routes = [
  {
    path: '',
    component: MatchPlayerComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: MatchPlayerDetailComponent,
    resolve: {
      matchPlayer: MatchPlayerRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: MatchPlayerUpdateComponent,
    resolve: {
      matchPlayer: MatchPlayerRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: MatchPlayerUpdateComponent,
    resolve: {
      matchPlayer: MatchPlayerRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(matchPlayerRoute)],
  exports: [RouterModule],
})
export class MatchPlayerRoutingModule {}
