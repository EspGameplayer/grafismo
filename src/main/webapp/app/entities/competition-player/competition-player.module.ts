import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CompetitionPlayerComponent } from './list/competition-player.component';
import { CompetitionPlayerDetailComponent } from './detail/competition-player-detail.component';
import { CompetitionPlayerUpdateComponent } from './update/competition-player-update.component';
import { CompetitionPlayerDeleteDialogComponent } from './delete/competition-player-delete-dialog.component';
import { CompetitionPlayerRoutingModule } from './route/competition-player-routing.module';

@NgModule({
  imports: [SharedModule, CompetitionPlayerRoutingModule],
  declarations: [
    CompetitionPlayerComponent,
    CompetitionPlayerDetailComponent,
    CompetitionPlayerUpdateComponent,
    CompetitionPlayerDeleteDialogComponent,
  ],
  entryComponents: [CompetitionPlayerDeleteDialogComponent],
})
export class CompetitionPlayerModule {}
