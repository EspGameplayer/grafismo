import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { GraphicElementPosComponent } from '../list/graphic-element-pos.component';
import { GraphicElementPosDetailComponent } from '../detail/graphic-element-pos-detail.component';
import { GraphicElementPosUpdateComponent } from '../update/graphic-element-pos-update.component';
import { GraphicElementPosRoutingResolveService } from './graphic-element-pos-routing-resolve.service';

const graphicElementPosRoute: Routes = [
  {
    path: '',
    component: GraphicElementPosComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: GraphicElementPosDetailComponent,
    resolve: {
      graphicElementPos: GraphicElementPosRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: GraphicElementPosUpdateComponent,
    resolve: {
      graphicElementPos: GraphicElementPosRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: GraphicElementPosUpdateComponent,
    resolve: {
      graphicElementPos: GraphicElementPosRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(graphicElementPosRoute)],
  exports: [RouterModule],
})
export class GraphicElementPosRoutingModule {}
