import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CompetitionPlayerComponent } from '../list/competition-player.component';
import { CompetitionPlayerDetailComponent } from '../detail/competition-player-detail.component';
import { CompetitionPlayerUpdateComponent } from '../update/competition-player-update.component';
import { CompetitionPlayerRoutingResolveService } from './competition-player-routing-resolve.service';

const competitionPlayerRoute: Routes = [
  {
    path: '',
    component: CompetitionPlayerComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CompetitionPlayerDetailComponent,
    resolve: {
      competitionPlayer: CompetitionPlayerRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CompetitionPlayerUpdateComponent,
    resolve: {
      competitionPlayer: CompetitionPlayerRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CompetitionPlayerUpdateComponent,
    resolve: {
      competitionPlayer: CompetitionPlayerRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(competitionPlayerRoute)],
  exports: [RouterModule],
})
export class CompetitionPlayerRoutingModule {}
