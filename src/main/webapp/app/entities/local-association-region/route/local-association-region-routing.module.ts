import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { LocalAssociationRegionComponent } from '../list/local-association-region.component';
import { LocalAssociationRegionDetailComponent } from '../detail/local-association-region-detail.component';
import { LocalAssociationRegionUpdateComponent } from '../update/local-association-region-update.component';
import { LocalAssociationRegionRoutingResolveService } from './local-association-region-routing-resolve.service';

const localAssociationRegionRoute: Routes = [
  {
    path: '',
    component: LocalAssociationRegionComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: LocalAssociationRegionDetailComponent,
    resolve: {
      localAssociationRegion: LocalAssociationRegionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: LocalAssociationRegionUpdateComponent,
    resolve: {
      localAssociationRegion: LocalAssociationRegionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: LocalAssociationRegionUpdateComponent,
    resolve: {
      localAssociationRegion: LocalAssociationRegionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(localAssociationRegionRoute)],
  exports: [RouterModule],
})
export class LocalAssociationRegionRoutingModule {}
