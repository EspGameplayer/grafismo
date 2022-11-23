import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CallupComponent } from './list/callup.component';
import { CallupDetailComponent } from './detail/callup-detail.component';
import { CallupUpdateComponent } from './update/callup-update.component';
import { CallupDeleteDialogComponent } from './delete/callup-delete-dialog.component';
import { CallupRoutingModule } from './route/callup-routing.module';

@NgModule({
  imports: [SharedModule, CallupRoutingModule],
  declarations: [CallupComponent, CallupDetailComponent, CallupUpdateComponent, CallupDeleteDialogComponent],
  entryComponents: [CallupDeleteDialogComponent],
})
export class CallupModule {}
