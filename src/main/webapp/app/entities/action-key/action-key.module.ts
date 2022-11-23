import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ActionKeyComponent } from './list/action-key.component';
import { ActionKeyDetailComponent } from './detail/action-key-detail.component';
import { ActionKeyUpdateComponent } from './update/action-key-update.component';
import { ActionKeyDeleteDialogComponent } from './delete/action-key-delete-dialog.component';
import { ActionKeyRoutingModule } from './route/action-key-routing.module';

@NgModule({
  imports: [SharedModule, ActionKeyRoutingModule],
  declarations: [ActionKeyComponent, ActionKeyDetailComponent, ActionKeyUpdateComponent, ActionKeyDeleteDialogComponent],
  entryComponents: [ActionKeyDeleteDialogComponent],
})
export class ActionKeyModule {}
