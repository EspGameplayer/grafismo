import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { LineupComponent } from './list/lineup.component';
import { LineupDetailComponent } from './detail/lineup-detail.component';
import { LineupUpdateComponent } from './update/lineup-update.component';
import { LineupDeleteDialogComponent } from './delete/lineup-delete-dialog.component';
import { LineupRoutingModule } from './route/lineup-routing.module';

@NgModule({
  imports: [SharedModule, LineupRoutingModule],
  declarations: [LineupComponent, LineupDetailComponent, LineupUpdateComponent, LineupDeleteDialogComponent],
  entryComponents: [LineupDeleteDialogComponent],
})
export class LineupModule {}
