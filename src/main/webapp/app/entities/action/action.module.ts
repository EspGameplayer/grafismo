import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ActionComponent } from './list/action.component';
import { ActionDetailComponent } from './detail/action-detail.component';
import { ActionUpdateComponent } from './update/action-update.component';
import { ActionDeleteDialogComponent } from './delete/action-delete-dialog.component';
import { ActionRoutingModule } from './route/action-routing.module';

@NgModule({
  imports: [SharedModule, ActionRoutingModule],
  declarations: [ActionComponent, ActionDetailComponent, ActionUpdateComponent, ActionDeleteDialogComponent],
  entryComponents: [ActionDeleteDialogComponent],
})
export class ActionModule {}
