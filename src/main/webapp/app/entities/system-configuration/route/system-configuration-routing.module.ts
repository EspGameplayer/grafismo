import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SystemConfigurationComponent } from '../list/system-configuration.component';
import { SystemConfigurationDetailComponent } from '../detail/system-configuration-detail.component';
import { SystemConfigurationUpdateComponent } from '../update/system-configuration-update.component';
import { SystemConfigurationRoutingResolveService } from './system-configuration-routing-resolve.service';

const systemConfigurationRoute: Routes = [
  {
    path: '',
    component: SystemConfigurationComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SystemConfigurationDetailComponent,
    resolve: {
      systemConfiguration: SystemConfigurationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SystemConfigurationUpdateComponent,
    resolve: {
      systemConfiguration: SystemConfigurationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SystemConfigurationUpdateComponent,
    resolve: {
      systemConfiguration: SystemConfigurationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(systemConfigurationRoute)],
  exports: [RouterModule],
})
export class SystemConfigurationRoutingModule {}
