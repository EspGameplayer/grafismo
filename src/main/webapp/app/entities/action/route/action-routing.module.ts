import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ActionComponent } from '../list/action.component';
import { ActionDetailComponent } from '../detail/action-detail.component';
import { ActionUpdateComponent } from '../update/action-update.component';
import { ActionRoutingResolveService } from './action-routing-resolve.service';

const actionRoute: Routes = [
  {
    path: '',
    component: ActionComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ActionDetailComponent,
    resolve: {
      action: ActionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ActionUpdateComponent,
    resolve: {
      action: ActionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ActionUpdateComponent,
    resolve: {
      action: ActionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(actionRoute)],
  exports: [RouterModule],
})
export class ActionRoutingModule {}
