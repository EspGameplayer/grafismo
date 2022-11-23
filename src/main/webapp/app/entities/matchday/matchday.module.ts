import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { MatchdayComponent } from './list/matchday.component';
import { MatchdayDetailComponent } from './detail/matchday-detail.component';
import { MatchdayUpdateComponent } from './update/matchday-update.component';
import { MatchdayDeleteDialogComponent } from './delete/matchday-delete-dialog.component';
import { MatchdayRoutingModule } from './route/matchday-routing.module';

@NgModule({
  imports: [SharedModule, MatchdayRoutingModule],
  declarations: [MatchdayComponent, MatchdayDetailComponent, MatchdayUpdateComponent, MatchdayDeleteDialogComponent],
  entryComponents: [MatchdayDeleteDialogComponent],
})
export class MatchdayModule {}
