import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { StadiumComponent } from '../list/stadium.component';
import { StadiumDetailComponent } from '../detail/stadium-detail.component';
import { StadiumUpdateComponent } from '../update/stadium-update.component';
import { StadiumRoutingResolveService } from './stadium-routing-resolve.service';

const stadiumRoute: Routes = [
  {
    path: '',
    component: StadiumComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: StadiumDetailComponent,
    resolve: {
      stadium: StadiumRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: StadiumUpdateComponent,
    resolve: {
      stadium: StadiumRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: StadiumUpdateComponent,
    resolve: {
      stadium: StadiumRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(stadiumRoute)],
  exports: [RouterModule],
})
export class StadiumRoutingModule {}
