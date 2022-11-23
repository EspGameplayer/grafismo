import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { LocalAssociationProvinceComponent } from '../list/local-association-province.component';
import { LocalAssociationProvinceDetailComponent } from '../detail/local-association-province-detail.component';
import { LocalAssociationProvinceUpdateComponent } from '../update/local-association-province-update.component';
import { LocalAssociationProvinceRoutingResolveService } from './local-association-province-routing-resolve.service';

const localAssociationProvinceRoute: Routes = [
  {
    path: '',
    component: LocalAssociationProvinceComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: LocalAssociationProvinceDetailComponent,
    resolve: {
      localAssociationProvince: LocalAssociationProvinceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: LocalAssociationProvinceUpdateComponent,
    resolve: {
      localAssociationProvince: LocalAssociationProvinceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: LocalAssociationProvinceUpdateComponent,
    resolve: {
      localAssociationProvince: LocalAssociationProvinceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(localAssociationProvinceRoute)],
  exports: [RouterModule],
})
export class LocalAssociationProvinceRoutingModule {}
