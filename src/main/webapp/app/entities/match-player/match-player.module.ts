import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { MatchPlayerComponent } from './list/match-player.component';
import { MatchPlayerDetailComponent } from './detail/match-player-detail.component';
import { MatchPlayerUpdateComponent } from './update/match-player-update.component';
import { MatchPlayerDeleteDialogComponent } from './delete/match-player-delete-dialog.component';
import { MatchPlayerRoutingModule } from './route/match-player-routing.module';

@NgModule({
  imports: [SharedModule, MatchPlayerRoutingModule],
  declarations: [MatchPlayerComponent, MatchPlayerDetailComponent, MatchPlayerUpdateComponent, MatchPlayerDeleteDialogComponent],
  entryComponents: [MatchPlayerDeleteDialogComponent],
})
export class MatchPlayerModule {}
