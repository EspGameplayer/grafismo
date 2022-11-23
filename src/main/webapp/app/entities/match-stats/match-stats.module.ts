import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { MatchStatsComponent } from './list/match-stats.component';
import { MatchStatsDetailComponent } from './detail/match-stats-detail.component';
import { MatchStatsUpdateComponent } from './update/match-stats-update.component';
import { MatchStatsDeleteDialogComponent } from './delete/match-stats-delete-dialog.component';
import { MatchStatsRoutingModule } from './route/match-stats-routing.module';

@NgModule({
  imports: [SharedModule, MatchStatsRoutingModule],
  declarations: [MatchStatsComponent, MatchStatsDetailComponent, MatchStatsUpdateComponent, MatchStatsDeleteDialogComponent],
  entryComponents: [MatchStatsDeleteDialogComponent],
})
export class MatchStatsModule {}
