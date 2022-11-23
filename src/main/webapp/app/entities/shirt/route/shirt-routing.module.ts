import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ShirtComponent } from '../list/shirt.component';
import { ShirtDetailComponent } from '../detail/shirt-detail.component';
import { ShirtUpdateComponent } from '../update/shirt-update.component';
import { ShirtRoutingResolveService } from './shirt-routing-resolve.service';

const shirtRoute: Routes = [
  {
    path: '',
    component: ShirtComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ShirtDetailComponent,
    resolve: {
      shirt: ShirtRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ShirtUpdateComponent,
    resolve: {
      shirt: ShirtRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ShirtUpdateComponent,
    resolve: {
      shirt: ShirtRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(shirtRoute)],
  exports: [RouterModule],
})
export class ShirtRoutingModule {}
