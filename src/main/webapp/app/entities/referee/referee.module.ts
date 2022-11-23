import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { RefereeComponent } from './list/referee.component';
import { RefereeDetailComponent } from './detail/referee-detail.component';
import { RefereeUpdateComponent } from './update/referee-update.component';
import { RefereeDeleteDialogComponent } from './delete/referee-delete-dialog.component';
import { RefereeRoutingModule } from './route/referee-routing.module';

@NgModule({
  imports: [SharedModule, RefereeRoutingModule],
  declarations: [RefereeComponent, RefereeDetailComponent, RefereeUpdateComponent, RefereeDeleteDialogComponent],
  entryComponents: [RefereeDeleteDialogComponent],
})
export class RefereeModule {}
