import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { SystemConfigurationComponent } from './list/system-configuration.component';
import { SystemConfigurationDetailComponent } from './detail/system-configuration-detail.component';
import { SystemConfigurationUpdateComponent } from './update/system-configuration-update.component';
import { SystemConfigurationDeleteDialogComponent } from './delete/system-configuration-delete-dialog.component';
import { SystemConfigurationRoutingModule } from './route/system-configuration-routing.module';

@NgModule({
  imports: [SharedModule, SystemConfigurationRoutingModule],
  declarations: [
    SystemConfigurationComponent,
    SystemConfigurationDetailComponent,
    SystemConfigurationUpdateComponent,
    SystemConfigurationDeleteDialogComponent,
  ],
  entryComponents: [SystemConfigurationDeleteDialogComponent],
})
export class SystemConfigurationModule {}
